<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品注文 - Table Order</title>
    <title>商品注文 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body { display: flex; height: 100vh; overflow: hidden; }

        /* サイドバー（カテゴリ一覧） */
        .sidebar { width: 200px; background-color: var(--primary); color: white; flex-shrink: 0; }
        .category-item { padding: 15px 20px; cursor: pointer; text-decoration: none; color: #bdc3c7; display: block; border-bottom: 1px solid rgba(255,255,255,0.05); }
        .category-item:hover { background-color: var(--primary-light); color: white; }
        .category-item.active { background-color: var(--accent); color: white; font-weight: bold; }

        /* メインコンテンツ（商品一覧） */
        .main-content { flex-grow: 1; padding: 20px; overflow-y: auto; background-color: var(--bg-body); }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; }
        .product-card { background: var(--bg-card); padding: 15px; border-radius: 12px; }
        .product-name { font-size: 1.1rem; font-weight: bold; margin-bottom: 5px; }
        .product-price { color: var(--accent); font-weight: bold; margin-bottom: 15px; }

        /* カート（右側） */
        .cart-panel { width: 320px; background: var(--bg-card); border-left: 1px solid var(--border); display: flex; flex-direction: column; flex-shrink: 0; }
        .cart-header { padding: 20px; border-bottom: 1px solid var(--border); font-weight: bold; font-size: 1.2rem; background: var(--primary); color: white; }
        .cart-items { flex-grow: 1; overflow-y: auto; padding: 10px; }
        .cart-item { display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px dotted var(--border); font-size: 0.9rem; }
        .cart-footer { padding: 20px; background: var(--bg-body); border-top: 1px solid var(--border); }
        .total-price { display: flex; justify-content: space-between; font-size: 1.3rem; font-weight: bold; margin-bottom: 15px; }
    </style>
</head>
<body>
    <!-- サイドバー: カテゴリ選択 -->
    <div class="sidebar">
        <div style="padding: 20px; font-weight: bold; font-size: 1.2rem; border-bottom: 1px solid #3e5871; margin-bottom: 10px;">
            TABLE: ${sessionScope.user.id}
        </div>
        <c:forEach var="cat" items="${categories}">
            <a href="Menu?categoryId=${cat.id}" class="category-item ${selectedCategoryId == cat.id ? 'active' : ''}">
                ${cat.name}
            </a>
        </c:forEach>
    </div>

    <!-- メインコンテンツ: 商品表示 -->
    <div class="main-content">
        <div class="header">
            <h1>商品メニュー</h1>
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
        </div>

        <div class="product-grid">
            <c:forEach var="p" items="${products}">
                <div class="product-card">
                    <div class="product-name">${p.name}</div>
                    <div class="product-price">¥<fmt:formatNumber value="${p.price}" /></div>
                    <form action="Cart" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${p.id}">
                        <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                        <button type="submit" class="add-btn">カートに追加</button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- カートパネル -->
    <div class="cart-panel">
        <div class="cart-header">現在の注文カート</div>
        
        <div class="cart-items">
            <c:choose>
                <c:when test="${not empty sessionScope.cart}">
                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${sessionScope.cart}">
                        <div class="cart-item">
                            <div>
                                <strong>${item.name}</strong><br>
                                ¥<fmt:formatNumber value="${item.unitPrice}" /> × ${item.quantity}
                            </div>
                            <div style="text-align: right;">
                                ¥<fmt:formatNumber value="${item.subtotal}" /><br>
                                <form action="Cart" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                                    <button type="submit" style="border:none; background:none; color:red; cursor:pointer; font-size: 0.8rem;">[削除]</button>
                                </form>
                            </div>
                        </div>
                        <c:set var="total" value="${total + item.subtotal}" />
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="padding: 20px; color: #999; text-align: center;">カートは空です。</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="cart-footer">
            <div class="total-price">
                <span>合計</span>
                <span>¥<fmt:formatNumber value="${total != null ? total : 0}" /></span>
            </div>
            <form action="Order" method="post">
                <button type="submit" class="order-btn" ${empty sessionScope.cart ? 'disabled' : ''}>
                    注文を確定する
                </button>
            </form>
        </div>
    </div>
</body>
</html>
