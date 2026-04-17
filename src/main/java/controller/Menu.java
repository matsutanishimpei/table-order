package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * メニュー画面を表示するサーブレットクラスです。
 */
@WebServlet("/Menu")
public class Menu extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションの確認（ログインしていない場合はログイン画面に弾く）
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        // メニュー画面にフォワード
        request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
    }
}
