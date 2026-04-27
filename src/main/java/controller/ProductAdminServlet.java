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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import util.CloudinaryUtil;
import util.ImageStorageProvider;
import util.ValidationUtil;

/**
 * 管理者用の商品管理を制御するサーブレットです。
 */
@WebServlet("/Admin/Product")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15   // 15MB
)
public class ProductAdminServlet extends HttpServlet {
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
                request.setAttribute("product", product);
                request.setAttribute("categoryList", categoryService.findAll());
                request.getRequestDispatcher("/WEB-INF/view/admin_product_edit.jsp").forward(request, response);
                return;
            }
        }

        List<Product> products = productService.findAll();
        List<Category> categories = categoryService.findAll();

        request.setAttribute("productList", products);
        request.setAttribute("categoryList", categories);

        request.getRequestDispatcher("/WEB-INF/view/admin_products.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = ValidationUtil.parseIntSafe(request.getParameter("id"), 0);
        Product p;
        boolean isUpdate = (id > 0);
        
        if (isUpdate) {
            Optional<Product> productOpt = productService.findById(id);
            if (productOpt.isEmpty()) {
                response.sendRedirect("Product");
                return;
            }
            p = productOpt.get();
        } else {
            p = new Product();
        }

        // 基本情報の更新
        p.setName(request.getParameter("name"));
        p.setCategoryId(ValidationUtil.parseIntSafe(request.getParameter("categoryId"), p.getCategoryId()));
        p.setPrice(ValidationUtil.parseIntSafe(request.getParameter("price"), p.getPrice()));
        p.setDescription(request.getParameter("description"));
        p.setAllergyInfo(request.getParameter("allergyInfo"));
        p.setAvailable(request.getParameter("isAvailable") != null);

        // 画像のアップロード処理 (バリデーション、エラーハンドリング、旧画像削除付き)
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            if (ValidationUtil.isValidImage(filePart)) {
                // アップロード前に古い識別子を保持
                String oldImageId = p.getImagePath();
                
                // ImageStorageProviderを使用してアップロード
                String imageId = imageStorageProvider.upload(filePart);
                if (imageId != null) {
                    p.setImagePath(imageId);
                    
                    // 新しい画像のアップロードに成功した場合、古い画像を削除（孤立防止）
                    if (oldImageId != null && !oldImageId.isEmpty()) {
                        imageStorageProvider.delete(oldImageId);
                    }
                } else {
                    // アップロード失敗時
                    response.sendRedirect("Product?action=edit&id=" + id + "&msg=upload_failed");
                    return;
                }
            } else {
                // 不正な形式の場合
                response.sendRedirect("Product?action=edit&id=" + id + "&msg=invalid_format");
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
            response.sendRedirect("Product?msg=success");
        } else {
            String redirectUrl = "Product?action=" + (isUpdate ? "edit" : "add") + (isUpdate ? "&id=" + id : "") + "&msg=error";
            response.sendRedirect(redirectUrl);
        }
    }
}
