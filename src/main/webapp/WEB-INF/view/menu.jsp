<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品注文 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body class="app-layout">
    <!-- サイドバー: カテゴリ選択 -->
    <div class="sidebar" style="width: 260px;">
        <div class="sidebar-header" style="background: var(--primary); padding: 32px 24px; border-bottom: 1px solid rgba(255,255,255,0.05);">
            <div style="font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.1em; opacity: 0.7; margin-bottom: 4px;">Current Status</div>
            <div style="font-size: 1.25rem; font-weight: 800;">TABLE: ${sessionScope.user.id}</div>
        </div>
        <div class="sidebar-content">
            <c:forEach var="cat" items="${categories}">
                <a href="Menu?categoryId=${cat.id}" class="sidebar-item ${selectedCategoryId == cat.id ? 'active' : ''}">
                    <c:out value="${cat.name}" />
                </a>
            </c:forEach>
        </div>
        <div class="sidebar-footer">
            <a href="OrderHistory" class="btn btn-outline btn-block" style="border-radius: 12px; font-size: 0.85rem;">
                <span>⏱</span> 注文履歴を表示
            </a>
        </div>
    </div>

    <!-- メインコンテンツ: 商品表示 -->
    <div class="main-content" style="padding: 40px 48px;">
        <header class="page-header" style="margin-bottom: 48px;">
            <div style="display: flex; flex-direction: column;">
                <span style="font-size: 0.85rem; color: var(--accent); font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; margin-bottom: 4px;">Selections</span>
                <h1>商品メニュー</h1>
            </div>
            <div style="font-size: 0.9rem; color: var(--text-sub);">
                ${products.size()} items available
            </div>
        </header>

        <c:if test="${not empty message}"><div class="alert alert-success">${message}</div></c:if>
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

        <div class="dashboard-grid" style="grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));">
            <c:forEach var="p" items="${products}">
                <a href="ProductDetail?productId=${p.id}&categoryId=${selectedCategoryId}" class="product-card">
                    <div class="product-item-name text-clamp-2" title="<c:out value='${p.name}' />">
                        <c:out value="${p.name}" />
                    </div>
                    <div class="product-item-price">¥<fmt:formatNumber value="${p.price}" /></div>
                    <div style="margin-top: 16px; font-size: 0.8rem; color: var(--text-sub); display: flex; align-items: center; gap: 4px;">
                        <span>Details</span>
                        <span style="font-size: 1rem;">→</span>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>

    <!-- カートパネル (右側) -->
    <div class="side-panel" style="width: 360px;">
        <div class="panel-header" style="padding: 32px 24px; background: var(--bg-dark); color: white;">注文カート</div>
        
        <div class="panel-content" style="padding: 24px;">
            <c:choose>
                <c:when test="${not empty sessionScope.cart}">
                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${sessionScope.cart}">
                        <div class="cart-item-row" style="padding: 20px 0;">
                            <div style="flex: 1; padding-right: 12px;">
                                <div class="text-truncate" style="font-weight: 700; color: var(--primary); font-size: 1rem; margin-bottom: 4px;" title="<c:out value='${item.name}' />">
                                    <c:out value="${item.name}" />
                                </div>
                                <div style="font-size: 0.85rem; color: var(--text-sub);">
                                    ¥<fmt:formatNumber value="${item.unitPrice}" /> × ${item.quantity}
                                </div>
                            </div>
                            <div style="text-align: right; min-width: 80px;">
                                <div style="font-weight: 800; color: var(--text-main);">¥<fmt:formatNumber value="${item.subtotal}" /></div>
                                <form action="Cart" method="post" style="margin-top: 8px;">
                                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                                    <button type="submit" style="border:none; background:none; color:var(--text-sub); cursor:pointer; font-size: 0.75rem; padding:0; text-decoration: underline;">削除する</button>
                                </form>
                            </div>
                        </div>
                        <c:set var="total" value="${total + item.subtotal}" />
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="placeholder-view" style="padding-top: 100px;">
                        <div class="placeholder-icon" style="font-size: 4rem; opacity: 0.2;">🛒</div>
                        <p style="font-weight: 600; letter-spacing: 0.05em;">カートは空です</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="panel-footer" style="padding: 32px 24px; background: rgba(0,0,0,0.02);">
            <div class="cart-total-display" style="margin-bottom: 32px; border-top: 2px solid var(--border); padding-top: 24px;">
                <span style="font-size: 1rem; opacity: 0.6; font-weight: 600;">TOTAL</span>
                <span style="font-size: 2rem; letter-spacing: -0.02em;">¥<fmt:formatNumber value="${total != null ? total : 0}" /></span>
            </div>
            <form action="Order" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <button type="submit" class="order-btn" ${empty sessionScope.cart ? 'disabled' : ''} style="height: 64px;">
                    注文を確定する
                </button>
            </form>
        </div>
    </div>
</body>
</html>
