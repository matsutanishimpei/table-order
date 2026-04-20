<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>配膳管理パネル - Table Order</title>
    <title>配膳管理パネル - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .ready-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .ready-card {
            background: var(--bg-card);
            padding: 24px;
            border-top: 6px solid var(--success);
            color: var(--text-main);
        }

        .table-info { font-size: 0.9rem; color: var(--text-sub); font-weight: bold; margin-bottom: 10px; }
        .table-name { font-size: 2rem; margin-bottom: 15px; border-bottom: 2px solid var(--bg-body); padding-bottom: 10px; }
        .product-name { font-size: 1.4rem; font-weight: bold; margin-bottom: 5px; }
        .quantity { font-size: 1.2rem; color: var(--accent); font-weight: bold; }
        
        .no-items {
            text-align: center; padding: 80px 20px; background: var(--bg-card); border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05); grid-column: 1 / -1;
        }
    </style>
</head>
<body>
    <header>
        <h1>配膳管理パネル</h1>
        <a href="${pageContext.request.contextPath}/Login" class="logout-link">ログアウト</a>
    </header>

    <div class="container">
        <div class="ready-grid">
            <c:forEach var="item" items="${readyItems}">
                <div class="ready-card">
                    <div class="table-info">DELIVER TO</div>
                    <div class="table-name">${item.tableName}</div>
                    
                    <div class="product-info">
                        <div class="product-name">${item.productName}</div>
                        <div class="quantity">数量: ${item.quantity}</div>
                    </div>

                    <form action="Home" method="post">
                        <input type="hidden" name="action" value="serve">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <button type="submit" class="btn-serve">配膳完了</button>
                    </form>
                </div>
            </c:forEach>

            <c:if test="${empty readyItems}">
                <div class="no-items">
                    <h2>待機中の料理はありません</h2>
                    <p style="color: #888;">注文が調理完了になるとここに表示されます。</p>
                    <button onclick="location.reload()" style="background: none; border: 1px solid #ccc; padding: 10px 20px; border-radius: 4px; cursor: pointer; color: #666; margin-top: 20px;">
                        最新の情報に更新
                    </button>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
