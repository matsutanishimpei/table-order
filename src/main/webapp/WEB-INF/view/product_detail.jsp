<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - 商品詳細 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container" style="max-width: 800px; padding: 40px 20px;">
        <a href="Menu?categoryId=${selectedCategoryId}" class="link-back">← メニューへ戻る</a>

        <header class="detail-header" style="margin-top: 24px;">
            <p class="badge badge-info" style="margin-bottom: 12px;">PRODUCT DETAIL</p>
            <h1 class="detail-title product-title"><c:out value="${product.name}" /></h1>
            <div class="detail-price" style="margin-top: 15px;">¥<fmt:formatNumber value="${product.price}" /></div>
        </header>

        <div class="card">
            <h2>商品説明</h2>
            <div class="word-break" style="line-height: 1.8; color: var(--text-body); font-size: 1.1rem;">
                <c:choose>
                    <c:when test="${not empty product.description}">
                        <c:out value="${product.description}" />
                    </c:when>
                    <c:otherwise>
                        <span style="color: var(--text-sub); font-style: italic;">商品説明は準備中です。</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card">
            <h2>アレルギー情報</h2>
            <div style="padding: 12px; background: #fff8f0; border-radius: 8px; border: 1px solid #ffe8cc; color: var(--text-main);">
                <c:choose>
                    <c:when test="${not empty product.allergyInfo}">
                        <strong style="color: #e67e22;">特定原材料等：</strong>
                        <span class="word-break"><c:out value="${product.allergyInfo}" /></span>
                    </c:when>
                    <c:otherwise>
                        <span style="color: var(--text-sub);">アレルギー情報はありません。</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card" style="margin-top: 40px; border-top: 4px solid var(--success);">
            <form action="Cart" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                
                <div class="form-group">
                    <label for="quantity" class="form-label" style="font-size: 1.1rem;">注文数量</label>
                    <div style="display: flex; gap: 15px; align-items: center;">
                        <input type="number" id="quantity" name="quantity" class="form-control" 
                               value="1" min="1" max="10" style="width: 100px; text-align: center; font-size: 1.2rem; font-weight: bold;">
                        <span style="font-weight: bold; color: var(--text-sub);">点</span>
                    </div>
                </div>

                <div style="margin-top: 40px;">
                    <c:choose>
                        <c:when test="${product.available}">
                            <button type="submit" class="order-btn">
                                カートに追加して一覧へ
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="order-btn" disabled>
                                現在品切れ中です
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </form>
        </div>

        <div style="text-align: center; margin-top: 30px;">
            <a href="Menu?categoryId=${selectedCategoryId}" class="btn btn-outline" style="min-width: 200px;">キャンセルして戻る</a>
        </div>
    </div>
</body>
</html>
