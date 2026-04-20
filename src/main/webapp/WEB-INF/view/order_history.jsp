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
    <div class="container" style="max-width: 800px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px; flex-direction: column; align-items: flex-start; gap: 8px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Transaction Summary</div>
            <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">注文履歴 (座席: ${history.tableName})</h1>
        </header>

        <div class="card" style="padding: 0; overflow: hidden; border: none; box-shadow: var(--shadow-xl);">
            <div style="padding: 24px 32px; background: #f8fafc; border-bottom: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center;">
                <h2 style="margin: 0; font-size: 1.1rem; text-transform: uppercase; letter-spacing: 0.05em; border: none; padding: 0;">注文明細</h2>
                <span class="badge badge-info" style="font-size: 0.75rem;">${history.items.size()} Items</span>
            </div>
            <table class="admin-table" style="box-shadow: none; border-radius: 0;">
                <thead>
                    <tr>
                        <th style="background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">商品名</th>
                        <th style="width: 120px; text-align: right; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">単価</th>
                        <th style="width: 80px; text-align: center; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">数量</th>
                        <th style="width: 140px; text-align: right; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">小計</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${history.items}">
                        <tr>
                            <td style="font-weight: 600; color: var(--primary);"><c:out value="${item.productName}" /></td>
                            <td style="text-align: right; color: var(--text-sub); font-size: 0.95rem;">¥<fmt:formatNumber value="${item.unitPrice}" /></td>
                            <td style="text-align: center; font-weight: 500;">${item.quantity}</td>
                            <td style="text-align: right; font-weight: 700; color: var(--primary); font-size: 1.05rem;">¥<fmt:formatNumber value="${item.quantity * item.unitPrice}" /></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty history.items}">
                        <tr>
                            <td colspan="4" style="text-align: center; padding: 100px 40px;">
                                <div style="font-size: 3rem; margin-bottom: 16px; opacity: 0.1;">📝</div>
                                <div style="color: var(--text-sub); font-weight: 500;">まだご注文の履歴はございません。</div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${not empty history.items}">
            <div class="card" style="margin-top: 48px; border: none; background: var(--bg-dark); color: white; padding: 48px; box-shadow: var(--shadow-xl); overflow: hidden; position: relative;">
                <div style="position: absolute; top: -20px; right: -20px; font-size: 12rem; opacity: 0.05; pointer-events: none;">¥</div>
                <div style="display: flex; justify-content: space-between; align-items: center; position: relative; z-index: 1;">
                    <div style="text-align: left;">
                        <span style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Current Total</span>
                        <h3 style="margin: 8px 0 0; font-size: 1.1rem; font-weight: 400; opacity: 0.8;">現時点のお支払い合計（税込想定）</h3>
                    </div>
                    <div style="text-align: right;">
                        <span style="font-size: 3.2rem; font-weight: 900; color: white; letter-spacing: -0.02em;">¥<fmt:formatNumber value="${history.totalAmount}" /></span>
                    </div>
                </div>
            </div>
            <div style="margin-top: 16px; text-align: right; color: var(--text-sub); font-size: 0.85rem; padding-right: 12px;">
                ※正確な合計額は精算時にレジにてご確認ください。
            </div>
        </c:if>

        <div style="text-align: center; margin-top: 64px; display: flex; flex-direction: column; align-items: center; gap: 24px;">
            <a href="Menu" class="btn btn-primary" style="min-width: 320px; height: 64px; border-radius: 20px; font-size: 1.1rem;">お食事を続ける</a>
            <a href="Menu" class="link-back" style="font-size: 0.9rem;">トップメニューへ戻る</a>
        </div>
    </div>
</body>
</html>
