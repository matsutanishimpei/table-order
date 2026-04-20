<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>売上管理 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styl    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container">
        <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back">← 管理メニューへ戻る</a>
        
        <header class="page-header">
            <h1>売上管理ダッシュボード</h1>
        </header>

        <div class="dashboard-grid">
            <div class="stat-card">
                <div class="stat-label">累計売上高</div>
                <div class="stat-value">¥<fmt:formatNumber value="${totalSales}" /></div>
            </div>
            <div class="stat-card">
                <div class="stat-label">直近の日次売上平均</div>
                <div class="stat-value">
                    <c:set var="sum" value="0" />
                    <c:forEach var="d" items="${dailySales}">
                        <c:set var="sum" value="${sum + d.amount}" />
                    </c:forEach>
                    <fmt:formatNumber value="${dailySales.size() > 0 ? sum / dailySales.size() : 0}" pattern="¥#,###" />
                </div>
            </div>
        </div>

        <div class="dashboard-grid" style="grid-template-columns: 1fr 1fr;">
            <!-- 日次売上トレンド -->
            <section class="card">
                <h2>日次売上トレンド (直近7日間)</h2>
                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>日付</th>
                            <th>売上高</th>
                            <th>件数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${dailySales}">
                            <tr>
                                <td><fmt:formatDate value="${d.salesDate}" pattern="yyyy/MM/dd" /></td>
                                <td>¥<fmt:formatNumber value="${d.amount}" /></td>
                                <td>${d.orderCount} 件</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty dailySales}">
                            <tr><td colspan="3" style="text-align: center; color: #999; padding: 40px;">データがありません</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>

            <!-- 商品別ランキング -->
            <section class="card">
                <h2>商品別売上ランキング</h2>
                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>順位</th>
                            <th>商品名</th>
                            <th>数量</th>
                            <th>売上合計</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${productRanking}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td><strong><c:out value="${p.productName}" /></strong></td>
                                <td>${p.totalQuantity} 個</td>
                                <td>¥<fmt:formatNumber value="${p.totalAmount}" /></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty productRanking}">
                            <tr><td colspan="4" style="text-align: center; color: #999; padding: 40px;">データがありません</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </div>
</body>
</html>
