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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body class="app-layout theme-kitchen">
    <div class="container" style="max-width: 1200px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 32px;">
            <div style="display: flex; flex-direction: column; gap: 8px;">
                <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Active Service</div>
                <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">配膳管理パネル</h1>
            </div>
            <a href="${pageContext.request.contextPath}/Logout" class="btn btn-outline" style="border-radius: 12px; font-size: 0.85rem; opacity: 0.6;">Logout System</a>
        </header>

        <div class="monitor-grid" style="gap: 24px; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));">
            <c:forEach var="item" items="${readyItems}">
                <div class="table-card" style="padding: 32px; border: none; background: #1e293b; box-shadow: var(--shadow-xl); text-align: left;">
                    <div style="font-size: 0.8rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.1em; margin-bottom: 4px;">Deliver To</div>
                    <div class="table-id" style="font-size: 4rem; line-height: 1; margin-bottom: 24px; color: #f8fafc; letter-spacing: -0.05em;"><c:out value="${item.tableName}" /></div>
                    
                    <div class="card-content" style="margin-bottom: 32px; background: rgba(0,0,0,0.2); padding: 16px; border-radius: 16px;">
                        <div style="font-size: 1.4rem; font-weight: 800; margin-bottom: 8px; color: #f1f5f9;"><c:out value="${item.productName}" /></div>
                        <div style="font-size: 1.25rem; color: #fbbf24; font-weight: 900;">数量: ${item.quantity} PCs</div>
                    </div>

                    <form action="Home" method="post">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="serve">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <button type="submit" class="btn btn-success" style="width: 100%; height: 56px; border-radius: 16px; font-weight: 800; background: #10b981; box-shadow: 0 4px 14px rgba(16,185,129,0.3);">
                            <span>✅</span> 配膳完了
                        </button>
                    </form>
                </div>
            </c:forEach>

            <c:if test="${empty readyItems}">
                <div class="placeholder-view" style="grid-column: 1 / -1; padding-top: 100px;">
                    <div class="placeholder-icon" style="font-size: 6rem; opacity: 0.1;">🍽️</div>
                    <h2 style="font-size: 1.5rem; font-weight: 800; color: #94a3b8;">現在、配膳待ちの料理はありません</h2>
                    <p style="opacity: 0.5; margin-bottom: 32px;">調理が完了するとここにカードが表示されます。</p>
                    <button onclick="location.reload()" class="btn btn-outline" style="min-width: 240px; border-radius: 14px;">
                        🔄 画面を更新して確認
                    </button>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
