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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container" style="max-width: 1200px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px; flex-direction: column; align-items: flex-start; gap: 8px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Financial Performance</div>
            <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">売上管理ダッシュボード</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back" style="margin-top: 16px;">← 管理メニューへ戻る</a>
        </header>

        <div class="dashboard-grid" style="margin-bottom: 48px; gap: 32px;">
            <div class="stat-card" style="border: none; box-shadow: var(--shadow-xl);">
                <div class="stat-label" style="text-transform: uppercase; letter-spacing: 0.05em;">Total Sales / 累計売上高</div>
                <div class="stat-value" style="font-size: 3rem; color: var(--accent);"><span style="font-size: 1.5rem; opacity: 0.5; margin-right: 4px;">¥</span><fmt:formatNumber value="${totalSales}" /></div>
            </div>
            <div class="stat-card" style="border: none; box-shadow: var(--shadow-xl);">
                <div class="stat-label" style="text-transform: uppercase; letter-spacing: 0.05em;">Efficiency / 直近平均売上</div>
                <div class="stat-value" style="font-size: 3rem;">
                    <span style="font-size: 1.5rem; opacity: 0.5; margin-right: 4px;">¥</span>
                    <c:set var="sum" value="0" />
                    <c:forEach var="d" items="${dailySales}">
                        <c:set var="sum" value="${sum + d.amount}" />
                    </c:forEach>
                    <fmt:formatNumber value="${dailySales.size() > 0 ? sum / dailySales.size() : 0}" pattern="#,###" />
                </div>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 40px;">
            <!-- 日次売上トレンド -->
            <section class="card" style="padding: 0; overflow: hidden; border: none; box-shadow: var(--shadow-lg);">
                <div style="padding: 24px 32px; background: #f8fafc; border-bottom: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center;">
                    <h2 style="margin: 0; font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; border: none; padding: 0;">日次売上トレンド (直近7日間)</h2>
                </div>
                <table class="admin-table" style="box-shadow: none; border-radius: 0;">
                    <thead>
                        <tr>
                            <th style="background: transparent; border-bottom: 2px solid var(--border);">日付</th>
                            <th style="text-align: right; background: transparent; border-bottom: 2px solid var(--border);">売上高</th>
                            <th style="text-align: center; background: transparent; border-bottom: 2px solid var(--border);">件数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${dailySales}">
                            <tr>
                                <td style="font-family: monospace; font-weight: 600; color: var(--text-main);"><fmt:formatDate value="${d.salesDate}" pattern="yyyy/MM/dd" /></td>
                                <td style="text-align: right; font-weight: 700; color: var(--primary);">¥<fmt:formatNumber value="${d.amount}" /></td>
                                <td style="text-align: center;"><span class="badge badge-info">${d.orderCount} 件</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty dailySales}">
                            <tr><td colspan="3" style="text-align: center; color: var(--text-sub); padding: 80px 40px;">データが入っていません</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>

            <!-- 商品別ランキング -->
            <section class="card" style="padding: 0; overflow: hidden; border: none; box-shadow: var(--shadow-lg);">
                <div style="padding: 24px 32px; background: #f8fafc; border-bottom: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center;">
                    <h2 style="margin: 0; font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; border: none; padding: 0;">商品別売上ランキング</h2>
                </div>
                <table class="admin-table" style="box-shadow: none; border-radius: 0;">
                    <thead>
                        <tr>
                            <th style="width: 70px; text-align: center; background: transparent; border-bottom: 2px solid var(--border);">順位</th>
                            <th style="background: transparent; border-bottom: 2px solid var(--border);">商品名</th>
                            <th style="width: 100px; text-align: center; background: transparent; border-bottom: 2px solid var(--border);">数量</th>
                            <th style="width: 140px; text-align: right; background: transparent; border-bottom: 2px solid var(--border);">売上合計</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${productRanking}" varStatus="status">
                            <tr>
                                <td style="text-align: center;">
                                    <span style="font-size: 1.25rem; font-weight: 900; color: ${status.index < 3 ? 'var(--accent)' : 'var(--text-sub)'}; opacity: ${1 - (status.index * 0.15)}">
                                        ${status.index + 1}
                                    </span>
                                </td>
                                <td><strong style="color: var(--primary);"><c:out value="${p.productName}" /></strong></td>
                                <td style="text-align: center; font-weight: 500;">${p.totalQuantity} <small style="opacity: 0.5;">個</small></td>
                                <td style="text-align: right; font-weight: 700; color: var(--primary);">¥<fmt:formatNumber value="${p.totalAmount}" /></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty productRanking}">
                            <tr><td colspan="4" style="text-align: center; color: var(--text-sub); padding: 80px 40px;">データが入っていません</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </div>
</body>
</html>
