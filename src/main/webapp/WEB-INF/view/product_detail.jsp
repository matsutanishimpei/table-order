控制<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - 商品詳細</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <style>
        .detail-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 40px;
            background: var(--bg-card);
            border-radius: 24px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        .detail-header {
            border-bottom: 2px solid var(--bg-body);
            padding-bottom: 30px;
            margin-bottom: 30px;
        }
        .product-title {
            font-size: 2.5rem;
            font-weight: bold;
            color: var(--primary);
            line-height: 1.3;
            margin-bottom: 10px;
            word-break: break-all;
        }
        .product-price-large {
            font-size: 2rem;
            color: var(--accent);
            font-weight: bold;
        }
        .info-section {
            margin-bottom: 40px;
            line-height: 1.8;
            color: var(--text-main);
        }
        .section-label {
            display: block;
            font-size: 0.9rem;
            color: var(--text-sub);
            margin-bottom: 8px;
            font-weight: bold;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .btn-group {
            display: flex;
            gap: 20px;
            margin-top: 50px;
        }
        .btn-large {
            flex-grow: 1;
            padding: 20px;
            font-size: 1.3rem;
            border-radius: 12px;
            border: none;
            cursor: pointer;
            font-weight: bold;
            text-align: center;
            text-decoration: none;
            transition: all 0.2s;
        }
        .btn-cart-large { background: var(--success); color: white; box-shadow: 0 10px 20px rgba(39, 174, 96, 0.3); }
        .btn-back { background: #eee; color: #666; }
        .btn-large:hover { transform: translateY(-2px); filter: brightness(1.1); }
    </style>
</head>
<body class="bg-body">
    <div class="detail-container">
        <div class="detail-header">
            <span class="section-label">Product Detail</span>
            <h1 class="product-title"><c:out value="${product.name}" /></h1>
            <div class="product-price-large">¥<fmt:formatNumber value="${product.price}" /></div>
        </div>

        <div class="info-section">
            <span class="section-label">Description</span>
            <p>
                <c:choose>
                    <c:when test="${not empty product.description}">
                        <c:out value="${product.description}" />
                    </c:when>
                    <c:otherwise>
                        <span style="color: #999; font-style: italic;">商品説明は準備中です。</span>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="info-section">
            <span class="section-label">Allergy Information</span>
            <p>
                <c:choose>
                    <c:when test="${not empty product.allergyInfo}">
                        <c:out value="${product.allergyInfo}" />
                    </c:when>
                    <c:otherwise>
                        <span style="color: #999;">情報なし</span>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="btn-group">
            <a href="Menu?categoryId=${selectedCategoryId}" class="btn-large btn-back">戻る</a>
            <form action="Cart" method="post" style="flex-grow: 2;">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                <button type="submit" class="btn-large btn-cart-large" style="width: 100%;">カートに追加して一覧へ</button>
            </form>
        </div>
    </div>
</body>
</html>
