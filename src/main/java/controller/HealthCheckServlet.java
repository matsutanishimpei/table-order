package controller;

import database.DBManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * システムの稼働状態を確認するためのヘルスチェックエンドポイントです。
 * DB接続の可否を確認し、正常であれば 200 OK、異常があれば 503 Service Unavailable を返します。
 */
@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        boolean dbOk = checkDatabaseConnection();

        try (PrintWriter out = resp.getWriter()) {
            if (dbOk) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"status\": \"UP\", \"database\": \"OK\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                out.print("{\"status\": \"DOWN\", \"database\": \"ERROR\"}");
                logger.error("Health check failed: Database connection is not available.");
            }
        }
    }

    /**
     * 実際にDB接続を取得して、接続が可能かチェックします。
     */
    private boolean checkDatabaseConnection() {
        try (Connection conn = DBManager.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
