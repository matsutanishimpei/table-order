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
    <div class="sidebar" style="width: 220px;">
        <div class="sidebar-header" style="font-size: 1.1rem; padding: 20px;">
            TABLE: ${sessionScope.user.id}
        </div>
        <div class="sidebar-content">
            <c:forEach var="cat" items="${categories}">
                <a href="Menu?categoryId=${cat.id}" class="sidebar-item ${selectedCategoryId == cat.id ? 'active' : ''}">
                    <c:out value="${cat.name}" />
                </a>
            </c:forEach>
        </div>
        <div class="sidebar-footer">
            <a href="OrderHistory" class="btn btn-outline btn-block" style="font-size: 0.9rem;">注文履歴を表示</a>
        </div>
    </div>

    <!-- メインコンテンツ: 商品表示 -->
    <div class="main-content">
        <header class="page-header">
            <h1>商品メニュー</h1>
        </header>

        <c:if test="${not empty message}"><div class="alert alert-success">${message}</div></c:if>
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

        <div class="monitor-grid" style="grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));">
            <c:forEach var="p" items="${products}">
                <a href="ProductDetail?productId=${p.id}&categoryId=${selectedCategoryId}" class="product-card">
                    <div class="product-item-name text-clamp-2" title="<c:out value='${p.name}' />">
                        <c:out value="${p.name}" />
                    </div>
                    <div class="product-item-price">¥<fmt:formatNumber value="${p.price}" /></div>
                </a>
            </c:forEach>
        </div>
    </div>

    <!-- カートパネル (右側) -->
    <div class="side-panel">
        <div class="panel-header">注文カート</div>
        
        <div class="panel-content">
            <c:choose>
                <c:when test="${not empty sessionScope.cart}">
                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${sessionScope.cart}">
                        <div class="cart-item-row">
                            <div class="text-truncate" style="max-width: 180px;">
                                <strong title="<c:out value='${item.name}' />"><c:out value="${item.name}" /></strong><br>
                                <span style="font-size: 0.85rem; color: var(--text-sub);">
                                    ¥<fmt:formatNumber value="${item.unitPrice}" /> × ${item.quantity}
                                </span>
                            </div>
                            <div style="text-align: right;">
                                <div style="font-weight: bold;">¥<fmt:formatNumber value="${item.subtotal}" /></div>
                                <form action="Cart" method="post" style="margin-top: 4px;">
                                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                                    <button type="submit" style="border:none; background:none; color:var(--danger); cursor:pointer; font-size: 0.8rem; padding:0;">[削除]</button>
                                </form>
                            </div>
                        </div>
                        <c:set var="total" value="${total + item.subtotal}" />
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="placeholder-view" style="padding-top: 60px;">
                        <div class="placeholder-icon" style="font-size: 3rem;">🛒</div>
                        <p>カートは空です</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="panel-footer">
            <div class="cart-total-display">
                <span>合計</span>
                <span>¥<fmt:formatNumber value="${total != null ? total : 0}" /></span>
            </div>
            <form action="Order" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <button type="submit" class="order-btn" ${empty sessionScope.cart ? 'disabled' : ''}>
                    注文を確定する
                </button>
            </form>
        </div>
    </div>
</body>
</html>
