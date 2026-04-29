package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import service.CategoryService;
import service.ProductService;
import service.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import util.AppConstants;
import util.CloudinaryUtil;
import util.ImageStorageProvider;
import util.ValidationUtil;

import util.ValidationResult;

/**
 * 管理者用の商品管理を制御するサーブレットです。
 */
@WebServlet("/Admin/Product")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 15   // 15MB
)
public class ProductAdminServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final ProductService productService;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final CategoryService categoryService;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final ImageStorageProvider imageStorageProvider;

    public ProductAdminServlet() {
        this(ServiceFactory.getProductService(), ServiceFactory.getCategoryService(), CloudinaryUtil.getInstance());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ProductAdminServlet(ProductService productService, CategoryService categoryService,
            ImageStorageProvider imageStorageProvider) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageStorageProvider = imageStorageProvider;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 権限チェック：管理者権限がない場合は 403 エラー
        model.User user = (model.User) request.getSession().getAttribute(AppConstants.ATTR_USER);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        
        if ("edit".equals(action) || "add".equals(action)) {
            Product product;
            if ("edit".equals(action)) {
                int id = ValidationUtil.parseIntSafe(request.getParameter("id"), -1);
                product = productService.findById(id).orElse(null);
            } else {
                product = new Product(); // 新規登録用（ID=0）
            }

            if (product != null) {
                request.setAttribute(AppConstants.ATTR_PRODUCT, product);
                request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categoryService.findAll());
                request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT).forward(request, response);
                return;
            }
        }

        List<Product> products = productService.findAll();
        List<Category> categories = categoryService.findAll();

        request.setAttribute(AppConstants.ATTR_PRODUCT_LIST, products);
        request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categories);

        request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCTS).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 権限チェック：管理者権限がない場合は 403 エラー
        model.User user = (model.User) request.getSession().getAttribute(AppConstants.ATTR_USER);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int id = ValidationUtil.parseIntSafe(request.getParameter("id"), 0);
        Product p;
        boolean isUpdate = (id > 0);
        
        if (isUpdate) {
            Optional<Product> productOpt = productService.findById(id);
            if (productOpt.isEmpty()) {
                response.sendRedirect(AppConstants.REDIRECT_ADMIN_PRODUCT);
                return;
            }
            p = productOpt.get();
        } else {
            p = new Product();
        }

        // リクエストパラメータの取得
        String name = request.getParameter("name");
        int categoryId = ValidationUtil.parseIntSafe(request.getParameter("categoryId"), 0);
        int price = ValidationUtil.parseIntSafe(request.getParameter("price"), 0);
        String description = request.getParameter("description");
        String allergyInfo = request.getParameter("allergyInfo");
        boolean isAvailable = request.getParameter("isAvailable") != null;

        // 一時的な Product オブジェクトの作成（バリデーション用）
        // id と imagePath は既存のものを引き継ぐ
        p = new Product(
            id,
            categoryId,
            name,
            price,
            description,
            allergyInfo,
            p.imagePath(),
            isAvailable
        );

        try {
            // 1. 画像検証（サーブレット固有の責務）
            Part filePart = request.getPart("imageFile");
            validateImage(filePart, !isUpdate);

            // 2. 画像のアップロード処理
            if (filePart != null && filePart.getSize() > 0) {
                String oldImageId = p.imagePath();
                String imageId = imageStorageProvider.upload(filePart);
                if (imageId != null) {
                    // imagePath を更新した新しい Product インスタンスを生成
                    p = new Product(p.id(), p.categoryId(), p.name(), p.price(), p.description(),
                            p.allergyInfo(), imageId, p.isAvailable());
                    if (oldImageId != null && !oldImageId.isEmpty()) {
                        imageStorageProvider.delete(oldImageId);
                    }
                } else {
                    throw new exception.BusinessException("画像のアップロードに失敗しました。");
                }
            }

            // 3. サービス呼び出し（内部で業務バリデーションが実行される）
            if (isUpdate) {
                productService.update(p, user.id());
            } else {
                productService.insert(p, user.id());
            }

            response.sendRedirect(AppConstants.REDIRECT_ADMIN_PRODUCT + "?msg=success");

        } catch (exception.BusinessException e) {
            request.setAttribute(AppConstants.ATTR_ERROR, e.getMessage());
            request.setAttribute(AppConstants.ATTR_PRODUCT, p);
            request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categoryService.findAll());
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT).forward(request, response);
        }
    }

    private void validateImage(Part filePart, boolean imageRequired) {
        if (filePart != null && filePart.getSize() > 0) {
            ValidationResult res = ValidationUtil.validateImage(filePart);
            if (res.isInvalid()) {
                throw new exception.BusinessException(res.message());
            }
        } else if (imageRequired) {
            throw new exception.BusinessException("画像は必須です。");
        }
    }

}
