<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注文履歴 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .history-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 30px;
            background: var(--bg-card);
            border-radius: 20px;
            box-shadow: 0 15px 40px rgba(0,0,0,0.1);
        }
        .history-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 2px solid var(--bg-body);
            padding-bottom: 20px;
        }
        .history-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }
        .history-table th, .history-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid var(--border);
        }
        .history-table th {
            background: var(--bg-body);
            color: var(--text-sub);
            font-size: 0.85rem;
            text-transform: uppercase;
        }
        .status-badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
        }
        .status-cooking { background: #fff3cd; color: #856404; }
        .status-ready { background: #d4edda; color: #155724; }
        .status-served { background: #e2e3e5; color: #383d41; }

        .history-footer {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            padding-top: 20px;
        }
        .total-amount-box {
            text-align: right;
        }
        .total-label { font-size: 1rem; color: var(--text-sub); }
        .total-value { font-size: 2.5rem; font-weight: bold; color: var(--primary); }

        .btn-return {
            padding: 15px 30px;
            background: var(--primary);
            color: white;
            text-decoration: none;
            border-radius: 12px;
            font-weight: bold;
            transition: background 0.2s;
        }
        .btn-return:hover { background: var(--primary-light); }
    </style>
</head>
<body class="bg-body">
    <div class="history-container">
        <div class="history-header">
            <h1>注文履歴 (座席: ${history.tableName})</h1>
            <a href="Menu" class="btn-return">メニューに戻る</a>
        </div>

        <table class="history-table">
            <thead>
                <tr>
                    <th>商品名</th>
                    <th>単価</th>
                    <th>数量</th>
                    <th>小計</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${history.items}">
                    <tr>
                        <td style="font-weight: bold; word-break: break-all;"><c:out value="${item.productName}" /></td>
                        <td>¥<fmt:formatNumber value="${item.unitPrice}" /></td>
                        <td>${item.quantity}</td>
                        <td style="font-weight: bold;">¥<fmt:formatNumber value="${item.quantity * item.unitPrice}" /></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty history.items}">
                    <tr>
                        <td colspan="4" style="text-align: center; padding: 50px; color: #999;">
                            まだ注文履歴はありません。
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <div class="history-footer">
            <p style="color: #888; font-size: 0.9rem;">※お会計の際はレジにて座席名をお伝えください。</p>
            <div class="total-amount-box">
                <div class="total-label">現時点の合計</div>
                <div class="total-value">¥<fmt:formatNumber value="${history.totalAmount}" /></div>
            </div>
        </div>
    </div>
</body>
</html>
