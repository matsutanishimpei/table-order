package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * すべてのサーブレットの基底クラスです。
 * 共通の例外ハンドリングやユーティリティメソッドを提供します。
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(BaseServlet.class.getName());

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (Exception e) {
            // 例外をログに出力
            logger.log(Level.SEVERE, "想定外のエラーが発生しました: " + this.getClass().getName(), e);
            
            // レスポンスが既にコミットされているか確認
            if (!resp.isCommitted()) {
                req.setAttribute("errorMessage", "サーバー内部でエラーが発生しました。時間をおいて再度お試しください。");
                req.setAttribute("exception", e);
                req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
            } else {
                // 既に書き込みが始まっている場合は、標準のエラー処理に任せるか、単にスローする
                throw new ServletException(e);
            }
        }
    }
}
