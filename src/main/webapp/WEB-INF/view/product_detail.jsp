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
    <div class="container" style="max-width: 720px; padding: 64px 24px;">
        <a href="Menu?categoryId=${selectedCategoryId}" class="link-back" style="font-weight: 600; letter-spacing: 0.05em;">← メニューへ戻る</a>

        <header class="detail-header" style="margin-top: 48px; border-bottom: 2px solid var(--border); padding-bottom: 40px; margin-bottom: 48px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em; margin-bottom: 12px;">Product Overview</div>
            <h1 class="detail-title product-title" style="font-size: 2.8rem; font-weight: 900; letter-spacing: -0.03em; line-height: 1.1; margin-bottom: 24px;">
                <c:out value="${product.name}" />
            </h1>
            <div class="detail-price" style="font-size: 2.2rem; font-weight: 800; letter-spacing: -0.02em;">
                <span style="font-size: 1.2rem; font-weight: 600; opacity: 0.6; margin-right: 4px;">¥</span><fmt:formatNumber value="${product.price}" />
                <span style="font-size: 0.9rem; font-weight: 500; opacity: 0.5; margin-left: 8px;">(tax excl.)</span>
            </div>
        </header>

        <div class="card" style="border: none; box-shadow: var(--shadow-xl); margin-bottom: 40px;">
            <h2 style="font-size: 1.1rem; text-transform: uppercase; color: var(--text-sub); border-left: none; padding-left: 0; letter-spacing: 0.1em; margin-bottom: 16px;">DESCRIPTION</h2>
            <div class="word-break" style="line-height: 1.8; color: var(--text-main); font-size: 1.15rem; font-weight: 400;">
                <c:choose>
                    <c:when test="${not empty product.description}">
                        <c:out value="${product.description}" />
                    </c:when>
                    <c:otherwise>
                        <span style="color: var(--text-sub); font-style: italic; font-weight: 300;">商品説明の内容を現在準備しております。少々お待ちください。</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card" style="border: none; box-shadow: var(--shadow-lg); background: #fdfaf6; border: 1px solid #f3ebdf;">
            <h2 style="font-size: 1rem; color: #b45309; border-left: none; padding-left: 0; letter-spacing: 0.1em; margin-bottom: 12px;">ALLERGY INFO</h2>
            <div style="color: #92400e; font-size: 1rem;">
                <c:choose>
                    <c:when test="${not empty product.allergyInfo}">
                        <span style="font-weight: 700; background: #fef3c7; padding: 2px 8px; border-radius: 4px; margin-right: 8px;">特定原材料等</span>
                        <span class="word-break" style="font-weight: 500;"><c:out value="${product.allergyInfo}" /></span>
                    </c:when>
                    <c:otherwise>
                        <span style="color: #d97706; opacity: 0.6;">この商品に該当するアレルギー情報は登録されていません。</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div style="margin-top: 64px; border-top: 1px solid var(--border); padding-top: 48px;">
            <form action="Cart" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                
                <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 32px;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="quantity" class="form-label" style="font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-sub);">Quantity</label>
                        <div style="display: flex; gap: 16px; align-items: center; margin-top: 8px;">
                            <input type="number" id="quantity" name="quantity" class="form-control" 
                                   value="1" min="1" max="20" style="width: 120px; height: 56px; text-align: center; font-size: 1.4rem; font-weight: 800; border-radius: 16px;">
                        </div>
                    </div>
                    <div style="text-align: right;">
                        <div style="font-size: 0.85rem; color: var(--text-sub); margin-bottom: 4px;">Subtotal (estimate)</div>
                        <div style="font-size: 1.75rem; font-weight: 900; color: var(--primary);">
                            <span style="font-size: 1rem; opacity: 0.5;">¥</span><fmt:formatNumber value="${product.price}" />
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${product.available}">
                        <button type="submit" class="order-btn" style="height: 72px; font-size: 1.3rem;">
                            <span>カートに追加する</span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="order-btn" disabled style="height: 72px;">
                            <span>現在品切れ中です</span>
                        </button>
                    </c:otherwise>
                </c:choose>
            </form>
        </div>

        <div style="text-align: center; margin-top: 48px;">
            <a href="Menu?categoryId=${selectedCategoryId}" class="btn btn-outline" style="min-width: 240px; height: 56px; border-radius: 16px; font-size: 0.95rem; color: var(--text-sub);">メニューへ戻る（キャンセル）</a>
        </div>
    </div>
</body>
</html>
