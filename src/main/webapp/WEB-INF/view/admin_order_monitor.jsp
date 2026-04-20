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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container">
        <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back">← 管理メニューへ戻る</a>
        
        <header class="page-header">
            <h1>受注状況・フロア監視</h1>
            <div style="font-size: 0.8rem; color: #999;">自動更新: 30秒ごと</div>
        </header>

        <div class="monitor-grid">
            <c:forEach var="t" items="${tableList}">
                <div class="table-card status-${t.statusCode}">
                    <div class="table-id"><c:out value="${t.tableName}" /></div>
                    <div class="table-status-badge"><c:out value="${t.statusLabel}" /></div>
                    
                    <div class="card-content">
                        <div style="font-size: 0.9rem; color: var(--text-sub); margin-bottom: 8px;">
                            注文アイテム：<span style="font-weight: bold; color: var(--text-main);">${t.orderCount}</span> 点
                        </div>
                        <div style="font-size: 1.2rem; font-weight: 800; color: var(--accent);">
                            ¥<fmt:formatNumber value="${t.totalAmount}" />
                        </div>
                    </div>

                    <div style="font-size: 0.75rem; color: #999; margin-top: 15px; border-top: 1px dashed var(--border); padding-top: 10px;">
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
