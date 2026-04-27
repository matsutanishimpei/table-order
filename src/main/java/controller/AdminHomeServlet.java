package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.AppConstants;

/**
 * 管理者のホーム画面を表示するサーブレットです。
 */
@WebServlet("/Admin/Home")
public class AdminHomeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 管理者用メニュー画面へ遷移
        request.getRequestDispatcher(AppConstants.VIEW_ADMIN_HOME).forward(request, response);
    }
}
