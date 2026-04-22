package controller;

import java.io.IOException;
import java.util.List;

import database.CategoryDAO;
import database.impl.CategoryDAOImpl;
import database.ProductDAO;
import database.impl.ProductDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;

/**
 * 管理者用の商品管理を制御するサーブレットです。
 */
@WebServlet("/Admin/Product")
public class ProductAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productDAO.findAll();
        List<Category> categories = categoryDAO.findAll();

        request.setAttribute("productList", products);
        request.setAttribute("categoryList", categories);

        request.getRequestDispatcher("/WEB-INF/view/admin_products.jsp").forward(request, response);
    }
}
