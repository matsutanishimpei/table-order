<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>売上管理 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .container { max-width: 1000px; padding-bottom: 50px; }
        
        .sales-header {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .stat-card {
            background: var(--bg-card);
            padding: 25px;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.05);
            text-align: center;
        }

        .stat-label { color: var(--text-sub); font-size: 0.9rem; margin-bottom: 10px; font-weight: bold; }
        .stat-value { font-size: 2rem; font-weight: bold; color: var(--accent); }
        .stat-unit { font-size: 1rem; margin-left: 4px; color: var(--text-sub); }

        .dashboard-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
        }

        @media (max-width: 768px) {
            .dashboard-grid { grid-template-columns: 1fr; }
        }

        section h2 { margin-bottom: 20px; font-size: 1.25rem; border-left: 4px solid var(--accent); padding-left: 10px; }

        table { width: 100%; border-collapse: collapse; background: var(--bg-card); border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid var(--border); }
        th { background: var(--primary); color: white; font-weight: normal; }
        tr:last-child td { border-bottom: none; }
        tr:hover { background: #fcfcfc; }

        .back-link { display: inline-block; margin-bottom: 20px; text-decoration: none; color: var(--primary); font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <a href="${pageContext.request.contextPath}/Admin/Home" class="back-link">← 管理メニューへ戻る</a>
        
        <header class="page-header">
            <h1>売上管理ダッシュボード</h1>
        </header>

        <div class="sales-header">
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
                    ¥<fmt:formatNumber value="${dailySales.size() > 0 ? sum / dailySales.size() : 0}" pattern="#,###" />
                </div>
            </div>
        </div>

        <div class="dashboard-grid">
            <!-- 日次売上トレンド -->
            <section>
                <h2>日次売上トレンド (直近7日間)</h2>
                <table>
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
                            <tr><td colspan="3" style="text-align: center; color: #999;">データがありません</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>

            <!-- 商品別ランキング -->
            <section>
                <h2>商品別売上ランキング</h2>
                <table>
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
                                <td><strong>${p.productName}</strong></td>
                                <td>${p.totalQuantity} 個</td>
                                <td>¥<fmt:formatNumber value="${p.totalAmount}" /></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty productRanking}">
                            <tr><td colspan="4" style="text-align: center; color: #999;">データがありません</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </div>
</body>
</html>
