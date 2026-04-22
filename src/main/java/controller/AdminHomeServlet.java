package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 管理者のホーム画面を表示するサーブレットです。
 */
@WebServlet("/Admin/Home")
public class AdminHomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 管理者用メニュー画面へ遷移
        request.getRequestDispatcher("/WEB-INF/view/admin_home.jsp").forward(request, response);
    }
}
