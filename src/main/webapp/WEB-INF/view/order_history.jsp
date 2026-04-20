<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注文履歴・お会計確認 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container" style="max-width: 900px; padding: 40px 20px;">
        <header class="page-header">
            <h1>注文履歴 (座席: ${history.tableName})</h1>
            <a href="Menu" class="link-back">メニューに戻る</a>
        </header>

        <div class="card">
            <h2>注文明細</h2>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>商品名</th>
                        <th style="width: 100px; text-align: right;">単価</th>
                        <th style="width: 80px; text-align: center;">数量</th>
                        <th style="width: 120px; text-align: right;">小計</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${history.items}">
                        <tr>
                            <td style="font-weight: bold;"><c:out value="${item.productName}" /></td>
                            <td style="text-align: right;">¥<fmt:formatNumber value="${item.unitPrice}" /></td>
                            <td style="text-align: center;">${item.quantity}</td>
                            <td style="text-align: right; font-weight: bold;">¥<fmt:formatNumber value="${item.quantity * item.unitPrice}" /></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty history.items}">
                        <tr>
                            <td colspan="4" style="text-align: center; padding: 60px; color: var(--text-sub);">
                                まだ注文履歴はありません。
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${not empty history.items}">
            <div class="card" style="margin-top: 40px; background: var(--primary); color: white; border-top: none;">
                <div style="display: flex; justify-content: space-between; align-items: center;">
                    <div style="text-align: left;">
                        <span style="font-size: 1.1rem; opacity: 0.9;">現時点のお支払い合計（税込）</span>
                        <p style="margin: 8px 0 0; font-size: 0.85rem; opacity: 0.7;">
                            ※お帰りの際にレジにて座席名をお伝えください。
                        </p>
                    </div>
                    <span style="font-size: 2.8rem; font-weight: 900; color: var(--accent);">¥<fmt:formatNumber value="${history.totalAmount}" /></span>
                </div>
            </div>
        </c:if>

        <div style="text-align: center; margin-top: 40px;">
            <a href="Menu" class="btn btn-outline" style="min-width: 250px;">お食事を続ける</a>
        </div>
    </div>
</body>
</html>
