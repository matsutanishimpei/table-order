<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>キッチン管理パネル - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="app-layout theme-kitchen">
    <!-- 左サイドバー: メニュー管理（品切れ操作） -->
    <div class="sidebar">
        <div class="sidebar-header">
            品切れ管理
        </div>
        <div class="sidebar-content">
            <c:forEach var="p" items="${allProducts}">
                <div class="sidebar-item" style="display: flex; justify-content: space-between; align-items: center; padding: 12px 20px;">
                    <span class="text-truncate" style="max-width: 140px;" title="<c:out value='${p.name}' />">
                        <c:out value="${p.name}" />
                    </span>
                    <form action="Home" method="post" style="margin: 0;">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="toggle_availability">
                        <input type="hidden" name="productId" value="${p.id}">
                        <input type="hidden" name="currentAvailable" value="${p.available}">
                        <button type="submit" class="badge ${p.available ? 'badge-success' : 'badge-danger'}" style="border:none; cursor:pointer;">
                            ${p.available ? '販売中' : '売切'}
                        </button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <div class="sidebar-footer" style="text-align: center;">
            <a href="${pageContext.request.contextPath}/Logout" class="link-back" style="font-size: 0.9rem;">ログアウト</a>
        </div>
    </div>

    <!-- メインコンテンツ: 注文一覧 -->
    <div class="main-content" style="align-items: stretch; background-color: var(--bg-body);">
        <header class="page-header">
            <h1>調理待ち注文</h1>
            <div style="font-size: 0.9rem; color: var(--text-sub);">
                自動更新はありません（操作後に更新されます）
            </div>
        </header>

        <div class="monitor-grid">
            <c:forEach var="item" items="${activeItems}">
                <div class="table-card status-ready">
                    <div style="display: flex; justify-content: space-between; margin-bottom: 12px;">
                        <span class="table-id" style="font-size: 1.5rem; color: var(--accent);"><c:out value="${item.tableName}" /></span>
                        <span style="font-size: 0.85rem; color: var(--text-sub);">
                            <fmt:formatDate value="${item.orderedAt}" pattern="HH:mm" />
                        </span>
                    </div>
                    <div class="card-content" style="margin-bottom: 24px;">
                        <div style="font-size: 1.4rem; font-weight: bold; margin-bottom: 10px; color: white;">
                            <c:out value="${item.productName}" />
                        </div>
                        <div style="font-size: 1.2rem; color: var(--warning); font-weight: bold;">
                            数量: ${item.quantity}
                        </div>
                    </div>
                    
                    <form action="Home" method="post" style="margin-top: auto;">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="complete">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <button type="submit" class="btn btn-success btn-block" style="padding: 16px;">調理完了</button>
                    </form>
                </div>
            </c:forEach>
            <c:if test="${empty activeItems}">
                <div class="placeholder-view" style="grid-column: 1 / -1;">
                    <div class="placeholder-icon">👨‍🍳</div>
                    <h2>現在、調理待ちの注文はありません</h2>
                    <p>新しい注文が入るのを待っています。フロアの状況を確認してください。</p>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
