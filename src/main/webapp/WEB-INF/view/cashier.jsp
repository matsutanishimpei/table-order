<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>レジ精算システム - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="app-layout">
    <!-- サイドバー：座席選択 -->
    <div class="sidebar">
        <div class="sidebar-header">精算待ちテーブル</div>
        <div class="sidebar-content">
            <c:forEach var="t" items="${unsettledTables}">
                <a href="Home?tableId=${t.tableId}" class="sidebar-item ${t.tableId == selectedSummary.tableId ? 'active' : ''}">
                    <c:out value="${t.tableName}" />
                </a>
            </c:forEach>
            <c:if test="${empty unsettledTables}">
                <div style="padding: 40px 20px; color: #999; text-align: center; font-size: 0.9rem;">
                    現在、会計待ちのテーブルはありません。
                </div>
            </c:if>
        </div>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/Logout" class="link-back" style="font-size: 0.9rem;">ログアウト</a>
        </div>
    </div>

    <!-- メイン：レジ詳細 -->
    <div class="main-content">
        <c:choose>
            <c:when test="${not empty selectedSummary}">
                <div class="card" style="max-width: 700px;">
                    <div style="display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 24px; border-bottom: 2px solid var(--bg-body); padding-bottom: 16px;">
                        <h2 style="border:none; margin:0; padding:0;"><c:out value="${selectedSummary.tableName}" /> 様</h2>
                        <span style="color: var(--text-sub); font-size: 0.9rem;">注文数: ${selectedSummary.items.size()}点</span>
                    </div>

                    <div class="items-list">
                        <c:forEach var="item" items="${selectedSummary.items}">
                            <div style="display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px dashed var(--border);">
                                <div class="word-break" style="max-width: 350px;">
                                    <span style="font-weight: bold;"><c:out value="${item.productName}" /></span>
                                    <span style="color: var(--text-sub); margin-left: 10px; font-size: 0.85rem;">@¥<fmt:formatNumber value="${item.unitPrice}" /></span>
                                </div>
                                <div style="text-align: right;">
                                    <span style="margin-right: 15px;">× ${item.quantity}</span>
                                    <span style="font-weight: bold; min-width: 80px; display: inline-block;">¥<fmt:formatNumber value="${item.subtotal}" /></span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div style="margin-top: 32px; padding-top: 24px; border-top: 3px solid var(--primary); display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 1.2rem; font-weight: bold;">合計金額</span>
                        <span style="font-size: 2.2rem; font-weight: 900; color: var(--accent);">¥<fmt:formatNumber value="${selectedSummary.totalAmount}" /></span>
                    </div>

                    <form action="Home" method="post" style="margin-top: 40px;">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="checkout">
                        <input type="hidden" name="tableId" value="${selectedSummary.tableId}">
                        <button type="submit" class="order-btn" onclick="return confirm('精算を完了しますか？');">
                            精算を完了する
                        </button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="placeholder-view">
                    <div class="placeholder-icon">🏧</div>
                    <h2>精算するテーブルを左から選択してください</h2>
                    <p>会計待ちのテーブルが選択されると、注文詳細が表示されます。</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
