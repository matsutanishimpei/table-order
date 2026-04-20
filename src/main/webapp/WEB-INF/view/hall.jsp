<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>配膳管理パネル - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container">
        <header class="page-header">
            <h1>配膳管理パネル</h1>
            <a href="${pageContext.request.contextPath}/Logout" class="link-back">ログアウト</a>
        </header>

        <div class="monitor-grid">
            <c:forEach var="item" items="${readyItems}">
                <div class="table-card status-ready">
                    <div style="font-size: 0.9rem; color: var(--text-sub); font-weight: bold; margin-bottom: 8px;">DELIVER TO</div>
                    <div class="table-id"><c:out value="${item.tableName}" /></div>
                    
                    <div class="card-content" style="margin-bottom: 20px;">
                        <div style="font-size: 1.4rem; font-weight: bold; margin-bottom: 8px;"><c:out value="${item.productName}" /></div>
                        <div style="font-size: 1.2rem; color: var(--accent); font-weight: bold;">数量: ${item.quantity}</div>
                    </div>

                    <form action="Home" method="post">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="serve">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <button type="submit" class="btn btn-success btn-block">配膳完了</button>
                    </form>
                </div>
            </c:forEach>

            <c:if test="${empty readyItems}">
                <div class="card" style="grid-column: 1 / -1; text-align: center; padding: 60px 20px;">
                    <h2 style="border: none; padding: 0;">待機中の料理はありません</h2>
                    <p style="color: #888; margin-bottom: 24px;">注文が調理完了になるとここに表示されます。</p>
                    <button onclick="location.reload()" class="btn btn-outline">
                        最新の情報に更新
                    </button>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
