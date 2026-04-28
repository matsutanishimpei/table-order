package controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.AppConstants;

/**
 * すべてのサーブレットの基底クラスです。
 * 共通の例外ハンドリングやユーティリティメソッドを提供します。
 */
@Slf4j
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * リクエスト処理をインターセプトし、共通の例外ハンドリングを行います。
     * 実行中に未捕捉の例外が発生した場合はログに記録し、共通エラー画面へ遷移させます。
     *
     * @param req HTTPリクエスト
     * @param resp HTTPレスポンス
     * @throws ServletException サーブレット例外が発生した場合
     * @throws IOException 入出力例外が発生した場合
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (Exception e) {
            // 例外をログに出力
            log.error("想定外のエラーが発生しました: {}", this.getClass().getName(), e);
            
            // レスポンスが既にコミットされているか確認
            if (!resp.isCommitted()) {
                req.setAttribute(AppConstants.ATTR_ERROR_MESSAGE, "サーバー内部でエラーが発生しました。時間をおいて再度お試しください。");
                req.setAttribute(AppConstants.ATTR_EXCEPTION, e);
                req.getRequestDispatcher(AppConstants.VIEW_ERROR).forward(req, resp);
            } else {
                // 既に書き込みが始まっている場合は、標準のエラー処理に任せるか、単にスローする
                throw new ServletException(e);
            }
        }
    }
}
