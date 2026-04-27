package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import service.CategoryService;
import service.impl.CategoryServiceImpl;
import service.ProductService;
import service.impl.ProductServiceImpl;

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
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageStorageProvider imageStorageProvider;

    public ProductAdminServlet() {
        this(new ProductServiceImpl(), new CategoryServiceImpl(), CloudinaryUtil.getInstance());
    }

    public ProductAdminServlet(ProductService productService, CategoryService categoryService, ImageStorageProvider imageStorageProvider) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageStorageProvider = imageStorageProvider;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        // 基本情報の更新
        p.setName(request.getParameter("name"));
        p.setCategoryId(ValidationUtil.parseIntSafe(request.getParameter("categoryId"), 0));
        p.setPrice(ValidationUtil.parseIntSafe(request.getParameter("price"), 0));
        p.setDescription(request.getParameter("description"));
        p.setAllergyInfo(request.getParameter("allergyInfo"));
        p.setAvailable(request.getParameter("isAvailable") != null);

        // バリデーション実行
        ValidationResult vr = validateProduct(p, request.getPart("imageFile"), !isUpdate);
        if (vr.isInvalid()) {
            request.setAttribute(AppConstants.ATTR_ERROR, vr.message());
            request.setAttribute(AppConstants.ATTR_PRODUCT, p);
            request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categoryService.findAll());
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT).forward(request, response);
            return;
        }

        // 画像のアップロード処理
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            // アップロード前に古い識別子を保持
            String oldImageId = p.getImagePath();
            
            // ImageStorageProviderを使用してアップロード
            String imageId = imageStorageProvider.upload(filePart);
            if (imageId != null) {
                p.setImagePath(imageId);
                
                // 新しい画像のアップロードに成功した場合、古い画像を削除
                if (oldImageId != null && !oldImageId.isEmpty()) {
                    imageStorageProvider.delete(oldImageId);
                }
            } else {
                request.setAttribute(AppConstants.ATTR_ERROR, "画像のアップロードに失敗しました。");
                request.setAttribute(AppConstants.ATTR_PRODUCT, p);
                request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categoryService.findAll());
                request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT).forward(request, response);
                return;
            }
        }

        boolean success;
        if (isUpdate) {
            success = productService.update(p);
        } else {
            success = productService.insert(p);
        }

        if (success) {
            response.sendRedirect(AppConstants.REDIRECT_ADMIN_PRODUCT + "?msg=success");
        } else {
            request.setAttribute(AppConstants.ATTR_ERROR, "データベースの更新に失敗しました。");
            request.setAttribute(AppConstants.ATTR_PRODUCT, p);
            request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categoryService.findAll());
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT).forward(request, response);
        }
    }

    private ValidationResult validateProduct(Product p, Part filePart, boolean imageRequired) {
        ValidationResult res = ValidationUtil.validateRequired(p.getName(), "商品名");
        if (res.isInvalid()) return res;

        res = ValidationUtil.validateMaxLength(p.getName(), AppConstants.MAX_PRODUCT_NAME_LENGTH, "商品名");
        if (res.isInvalid()) return res;

        res = ValidationUtil.validatePositive(p.getCategoryId(), "カテゴリ");
        if (res.isInvalid()) return res;

        res = ValidationUtil.validatePositive(p.getPrice(), "価格");
        if (res.isInvalid()) return res;

        if (filePart != null && filePart.getSize() > 0) {
            res = ValidationUtil.validateImage(filePart);
            if (res.isInvalid()) return res;
        } else if (imageRequired) {
            return ValidationResult.failure("画像は必須です。");
        }

        return ValidationResult.success();
    }
}
