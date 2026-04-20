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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body class="app-layout theme-kitchen">
    <!-- 左サイドバー: メニュー管理（品切れ操作） -->
    <div class="sidebar" style="width: 300px; border-right: 1px solid rgba(255,255,255,0.05); background: #020617;">
        <div class="sidebar-header" style="background: #1e293b; padding: 32px 24px; border-bottom: 1px solid rgba(255,255,255,0.05);">
            <div style="font-size: 0.7rem; text-transform: uppercase; letter-spacing: 0.15em; color: var(--accent); font-weight: 800; margin-bottom: 4px;">Kitchen Operations</div>
            <div style="font-size: 1.25rem; font-weight: 900;">品切れ管理</div>
        </div>
        <div class="sidebar-content" style="padding: 12px 0;">
            <c:forEach var="p" items="${allProducts}">
                <div class="sidebar-item" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding: 16px 24px;">
                    <div style="display: flex; flex-direction: column; gap: 12px;">
                        <div class="text-truncate" style="font-weight: 600; color: #f1f5f9; font-size: 0.95rem;" title="<c:out value='${p.name}' />">
                            <c:out value="${p.name}" />
                        </div>
                        <form action="Home" method="post" style="margin: 0; text-align: right;">
                            <input type="hidden" name="csrf_token" value="${csrf_token}">
                            <input type="hidden" name="action" value="toggle_availability">
                            <input type="hidden" name="productId" value="${p.id}">
                            <input type="hidden" name="currentAvailable" value="${p.available}">
                            <button type="submit" class="badge ${p.available ? 'badge-success' : 'badge-danger'}" style="border:none; cursor:pointer; padding: 6px 16px; border-radius: 100px; font-weight: 800; font-size: 0.75rem;">
                                ${p.available ? '● 販売中' : '✖ 売切'}
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="sidebar-footer" style="padding: 24px; text-align: center;">
            <a href="${pageContext.request.contextPath}/Logout" class="link-back" style="font-size: 0.85rem; opacity: 0.6;">Logout System</a>
        </div>
    </div>

    <!-- メインコンテンツ: 注文一覧 -->
    <div class="main-content" style="padding: 48px; min-height: 100vh;">
        <header class="page-header" style="margin-bottom: 48px; flex-direction: column; align-items: flex-start; gap: 8px; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 32px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Prep Queue</div>
            <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">調理待ち注文一覧</h1>
            <div style="font-size: 0.9rem; color: var(--text-sub); margin-top: 8px;">
                ※調理完了後にボタンを押してフロアへ通知してください
            </div>
        </header>

        <div class="monitor-grid" style="gap: 24px; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));">
            <c:forEach var="item" items="${activeItems}">
                <div class="table-card" style="padding: 32px; border: none; background: #1e293b; box-shadow: var(--shadow-xl); text-align: left; transition: transform 0.2s ease;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 16px;">
                        <span style="font-size: 1.5rem; font-weight: 900; color: var(--accent);"><c:out value="${item.tableName}" /></span>
                        <span class="badge" style="background: rgba(255,255,255,0.05); color: #94a3b8; font-size: 0.75rem;">
                            <fmt:formatDate value="${item.orderedAt}" pattern="HH:mm" /> Order
                        </span>
                    </div>
                    <div class="card-content" style="margin-bottom: 32px;">
                        <div style="font-size: 1.75rem; font-weight: 800; line-height: 1.2; margin-bottom: 16px; color: #f8fafc; letter-spacing: -0.02em;">
                            <c:out value="${item.productName}" />
                        </div>
                        <div style="font-size: 1.4rem; color: #fbbf24; font-weight: 900; background: rgba(251, 191, 36, 0.05); padding: 12px 16px; border-radius: 12px; display: inline-block;">
                            数量: ${item.quantity}
                        </div>
                    </div>
                    
                    <form action="Home" method="post" style="margin-top: auto;">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="complete">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <button type="submit" class="btn btn-success" style="width: 100%; height: 60px; font-size: 1.1rem; font-weight: 800; border-radius: 16px; justify-content: center; background: #10b981; box-shadow: 0 4px 14px 0 rgba(16, 185, 129, 0.3);">
                            調理完了
                        </button>
                    </form>
                </div>
            </c:forEach>
            <c:if test="${empty activeItems}">
                <div class="placeholder-view" style="grid-column: 1 / -1; padding-top: 100px;">
                    <div class="placeholder-icon" style="font-size: 6rem; opacity: 0.1;">👨‍🍳</div>
                    <h2 style="font-size: 1.5rem; font-weight: 800; color: #94a3b8; letter-spacing: 0.05em;">調理待ちの注文はありません</h2>
                    <p style="opacity: 0.5;">新しい注文が入るとここに表示されます。</p>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
