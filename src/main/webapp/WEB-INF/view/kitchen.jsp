<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>キッチン管理パネル - Table Order</title>
    <title>キッチン管理パネル - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body { 
            background-color: var(--bg-dark); 
            color: var(--text-white);
            display: flex;
            height: 100vh;
            overflow: hidden;
            margin: 0;
        }
        
        .sidebar {
            width: var(--sidebar-w);
            background-color: #0f3460;
            border-right: 1px solid rgba(255,255,255,0.1);
            display: flex;
            flex-direction: column;
            height: 100vh;
            flex-shrink: 0;
        }

        .sidebar-header {
            padding: 20px;
            font-size: 1.2rem;
            font-weight: bold;
            background: rgba(0,0,0,0.2);
            flex-shrink: 0;
        }

        .product-list { 
            padding: 10px; 
            overflow-y: auto; 
            flex-grow: 1; 
        }

        .product-item {
            display: flex; justify-content: space-between; align-items: center;
            padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.05); font-size: 0.9rem;
        }

        .main-area {
            flex-grow: 1;
            height: 100vh;
            overflow-y: auto;
            padding: 30px;
        }

        .order-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
        }

        .order-card {
            background: var(--bg-card);
            border-left: 5px solid var(--accent);
            color: var(--text-white);
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.3);
        }

        .table-name { font-size: 1.4rem; font-weight: bold; color: var(--accent); }
        .product-name { font-size: 1.3rem; margin-bottom: 10px; }
        .quantity { font-size: 1.1rem; color: var(--warning); font-weight: bold; }
        
        .btn-toggle { padding: 5px 10px; border-radius: 4px; border: none; cursor: pointer; font-size: 0.8rem; font-weight: bold; }
        .status-available { background: var(--success); color: white; }
        .status-soldout { background: var(--danger); color: white; }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 1px solid rgba(255,255,255,0.1);
            padding-bottom: 15px;
        }

        .order-info {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }

        .order-time { font-size: 0.85rem; color: #888; }

        .btn-complete {
            margin-top: 20px;
            width: 100%;
            background: var(--success);
            color: white;
            border: none;
            padding: 15px;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.2s;
        }
    </style>
</head>
<body>
    <!-- 左サイドバー: メニュー管理（品切れ操作） -->
    <div class="sidebar">
        <div class="sidebar-header">
            品切れ管理
        </div>
        <div class="product-list">
            <c:forEach var="p" items="${allProducts}">
                <div class="product-item">
                    <span>${p.name}</span>
                    <form action="Home" method="post" style="margin: 0;">
                        <input type="hidden" name="action" value="toggle_availability">
                        <input type="hidden" name="productId" value="${p.id}">
                        <input type="hidden" name="currentAvailable" value="${p.available}">
                        <button type="submit" class="btn-toggle ${p.available ? 'status-available' : 'status-soldout'}">
                            ${p.available ? '販売中' : '売切'}
                        </button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <div style="padding: 20px; text-align: center;">
            <a href="${pageContext.request.contextPath}/Login" class="logout-btn">ログアウト</a>
        </div>
    </div>

    <!-- メインコンテンツ: 注文一覧 -->
    <div class="main-area">
        <div class="header">
            <h1>調理待ち注文</h1>
            <div style="font-size: 0.9rem; color: #888;">
                自動更新はありません（操作後に更新されます）
            </div>
        </div>

        <div class="order-grid">
            <c:forEach var="item" items="${activeItems}">
                <div class="order-card">
                    <div class="order-info">
                        <span class="table-name">${item.tableName}</span>
                        <span class="order-time">
                            <fmt:formatDate value="${item.orderedAt}" pattern="HH:mm" />
                        </span>
                    </div>
                    <div class="product-name">
                        ${item.productName}
                    </div>
                    <div class="quantity">
                        数量: ${item.quantity}
                    </div>
                    
                    <form action="Home" method="post">
                        <input type="hidden" name="action" value="complete">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <button type="submit" class="btn-complete">調理完了</button>
                    </form>
                </div>
            </c:forEach>
            <c:if test="${empty activeItems}">
                <div class="no-orders">
                    現在、調理待ちの注文はありません
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
