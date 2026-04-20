<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 30秒ごとに自動更新 -->
    <meta http-equiv="refresh" content="30">
    <title>受注状況監視 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .monitor-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .table-card {
            background: var(--bg-card);
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            border-top: 8px solid #ccc;
            transition: transform 0.2s;
            position: relative;
        }

        /* ステータス別の枠端色 */
        .table-card.status-idle { border-top-color: #94a3b8; }      /* 待機中: Gray */
        .table-card.status-cooking { border-top-color: #f1c40f; }   /* 調理中: Yellow */
        .table-card.status-ready { border-top-color: #e67e22; }     /* 配膳待ち: Orange */
        .table-card.status-eating { border-top-color: #27ae60; }    /* 食事中: Green */

        .table-id { font-size: 2rem; font-weight: bold; margin-bottom: 5px; color: var(--text-main); }
        .table-status-label {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: bold;
            margin-bottom: 15px;
            color: white;
        }

        .status-idle .table-status-label { background: #94a3b8; }
        .status-cooking .table-status-label { background: #f1c40f; color: #000; }
        .status-ready .table-status-label { background: #e67e22; }
        .status-eating .table-status-label { background: #27ae60; }

        .item-info { font-size: 0.9rem; color: var(--text-sub); line-height: 1.6; }
        .item-count { font-weight: bold; color: var(--text-main); }
        .total-price { font-size: 1.1rem; font-weight: bold; color: var(--accent); margin-top: 10px; }
        
        .last-order { font-size: 0.75rem; color: #999; margin-top: 15px; border-top: 1px dotted #eee; padding-top: 10px; }

        .back-link { display: inline-block; margin-bottom: 20px; text-decoration: none; color: var(--primary); font-weight: bold; }
        
        .refresh-info { font-size: 0.8rem; color: #999; text-align: right; margin-bottom: 10px; }
    </style>
</head>
<body>
    <div class="container">
        <a href="${pageContext.request.contextPath}/Admin/Home" class="back-link">← 管理メニューへ戻る</a>
        
        <header class="page-header">
            <h1>受注状況・フロア監視</h1>
            <div class="refresh-info">自動更新: 30秒ごと</div>
        </header>

        <div class="monitor-grid">
            <c:forEach var="t" items="${tableList}">
                <div class="table-card status-${t.statusCode}">
                    <div class="table-id">${t.tableName}</div>
                    <div class="table-status-label">${t.statusLabel}</div>
                    
                    <div class="item-info">
                        注文アイテム：<span class="item-count">${t.orderCount}</span> 点<br>
                        <div class="total-price">
                            ¥<fmt:formatNumber value="${t.totalAmount}" />
                        </div>
                    </div>

                    <div class="last-order">
                        最終注文: <c:choose>
                            <c:when test="${not empty t.lastOrderTime}">
                                <fmt:formatDate value="${t.lastOrderTime}" pattern="HH:mm" />
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>
