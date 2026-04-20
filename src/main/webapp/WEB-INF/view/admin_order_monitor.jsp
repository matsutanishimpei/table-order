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
    <div class="container" style="max-width: 1400px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px;">
            <div style="display: flex; flex-direction: column; gap: 8px;">
                <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Real-time Monitoring</div>
                <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">受注状況・フロア監視</h1>
            </div>
            <div style="text-align: right;">
                <div class="badge badge-info" style="font-size: 0.75rem; letter-spacing: 0.05em; padding: 8px 16px; border-radius: 12px; background: rgba(99,102,241,0.1); color: var(--accent);">
                    🔄 自動更新: 30秒ごと
                </div>
            </div>
        </header>

        <div class="monitor-grid" style="gap: 24px;">
            <c:forEach var="t" items="${tableList}">
                <div class="table-card" style="position: relative; overflow: hidden; border: none; box-shadow: var(--shadow-xl); padding: 48px 32px; transition: transform 0.3s ease;">
                    <!-- 状態に応じた背景アクセント -->
                    <div style="position: absolute; top: 0; left: 0; width: 100%; height: 6px; background: ${t.statusCode == '0' ? '#10b981' : 'var(--accent)'};"></div>
                    
                    <div class="table-id" style="margin-bottom: 16px; font-size: 4.5rem; letter-spacing: -0.05em; line-height: 1;"><c:out value="${t.tableName}" /></div>
                    
                    <div style="margin-bottom: 24px;">
                        <span class="badge ${t.statusCode == '0' ? 'badge-success' : 'badge-info'}" style="padding: 6px 20px; font-size: 0.85rem; border-radius: 100px;">
                            <c:out value="${t.statusLabel}" />
                        </span>
                    </div>
                    
                    <div class="card-content" style="background: var(--bg-body); padding: 20px; border-radius: 20px; margin-bottom: 24px;">
                        <div style="font-size: 0.85rem; color: var(--text-sub); margin-bottom: 8px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">
                            Items Ordered
                        </div>
                        <div style="display: flex; align-items: baseline; justify-content: center; gap: 4px;">
                            <span style="font-size: 2rem; font-weight: 900; color: var(--primary);">${t.orderCount}</span>
                            <span style="font-size: 0.9rem; font-weight: 600; color: var(--text-sub);">PCS</span>
                        </div>
                        <div style="margin-top: 16px; font-size: 1.5rem; font-weight: 800; color: var(--accent); letter-spacing: -0.02em;">
                            <span style="font-size: 0.9rem; opacity: 0.6; margin-right: 2px;">¥</span><fmt:formatNumber value="${t.totalAmount}" />
                        </div>
                    </div>

                    <div style="font-size: 0.8rem; color: var(--text-sub); font-weight: 500; display: flex; align-items: center; justify-content: center; gap: 6px;">
                        <span>Last Order:</span>
                        <span style="color: var(--text-main); font-weight: 700;">
                            <c:choose>
                                <c:when test="${not empty t.lastOrderTime}">
                                    <fmt:formatDate value="${t.lastOrderTime}" pattern="HH:mm" />
                                </c:when>
                                <c:otherwise>--:--</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>
