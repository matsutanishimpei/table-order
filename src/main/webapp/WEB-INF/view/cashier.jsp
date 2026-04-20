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
    <style>
        body { display: flex; height: 100vh; overflow: hidden; }

        /* 左側：座席一覧 */
        .table-selector {
            width: 300px; background: var(--bg-card); border-right: 1px solid var(--border);
            display: flex; flex-direction: column; flex-shrink: 0;
        }

        .header-box { padding: 20px; background: var(--primary); color: white; font-weight: bold; font-size: 1.2rem; }
        .table-list { flex-grow: 1; overflow-y: auto; }

        .table-btn {
            display: block; width: 100%; padding: 20px; text-align: left; border: none; background: none;
            border-bottom: 1px solid var(--border); cursor: pointer; font-size: 1.1rem;
            text-decoration: none; color: var(--text-main); transition: background 0.2s;
        }

        .table-btn:hover { background: var(--bg-body); }
        .table-btn.active { background: var(--success); color: white; border-bottom: none; }

        /* 右側：精算詳細 */
        .checkout-detail {
            flex-grow: 1; padding: 40px; overflow-y: auto; display: flex;
            flex-direction: column; align-items: center; background-color: var(--bg-body);
        }

        .summary-card {
            background: var(--bg-card); width: 100%; max-width: 600px;
            border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); padding: 30px;
        }

        .summary-header { border-bottom: 2px solid var(--bg-body); padding-bottom: 20px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; }
        .item-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px dashed var(--border); }

        .total-row {
            margin-top: 30px; padding-top: 20px; border-top: 2px solid var(--primary);
            display: flex; justify-content: space-between;
            font-size: 2rem; font-weight: bold; color: var(--primary);
        }

        .checkout-btn {
            margin-top: 40px; width: 100%; padding: 20px; background: var(--success);
            color: white; border: none; border-radius: 8px; font-size: 1.5rem; font-weight: bold;
            cursor: pointer; box-shadow: 0 4px 15px rgba(39, 174, 96, 0.3);
        }

        .placeholder { color: var(--text-sub); text-align: center; margin-top: 100px; }
        .logout-panel { padding: 20px; border-top: 1px solid var(--border); }
    </style>
</head>
<body>
    <!-- サイドバー：座席選択 -->
    <div class="table-selector">
        <div class="header-box">精算待ちテーブル</div>
        <div class="table-list">
            <c:forEach var="t" items="${unsettledTables}">
                <a href="Home?tableId=${t.tableId}" class="table-btn ${t.tableId == selectedSummary.tableId ? 'active' : ''}">
                    <c:out value="${t.tableName}" />
                </a>
            </c:forEach>
            <c:if test="${empty unsettledTables}">
                <p style="padding: 20px; color: #999; text-align: center;">現在、会計待ちのテーブルはありません。</p>
            </c:if>
        </div>
        <div class="logout-panel">
            <a href="${pageContext.request.contextPath}/Logout" style="color: #666; text-decoration: none; font-size: 0.9rem;">ログアウト</a>
        </div>
    </div>

    <!-- メイン：レジ詳細 -->
    <div class="checkout-detail">
        <c:choose>
            <c:when test="${not empty selectedSummary}">
                <div class="summary-card">
                    <div class="summary-header">
                        <h2><c:out value="${selectedSummary.tableName}" /> 様</h2>
                        <span style="color: #7f8c8d;">注文数: ${selectedSummary.items.size()}点</span>
                    </div>

                    <div class="items-list">
                        <c:forEach var="item" items="${selectedSummary.items}">
                            <div class="item-row">
                                <div class="word-break" style="max-width: 300px;">
                                    <span style="font-weight: bold;"><c:out value="${item.productName}" /></span>
                                    <span style="color: #7f8c8d; margin-left: 10px;">@¥<fmt:formatNumber value="${item.unitPrice}" /></span>
                                </div>
                                <div>
                                    <span style="margin-right: 20px;">× ${item.quantity}</span>
                                    <span style="font-weight: bold;">¥<fmt:formatNumber value="${item.subtotal}" /></span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="total-row">
                        <span>合計金額</span>
                        <span>¥<fmt:formatNumber value="${selectedSummary.totalAmount}" /></span>
                    </div>

                    <form action="Home" method="post">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="checkout">
                        <input type="hidden" name="tableId" value="${selectedSummary.tableId}">
                        <button type="submit" class="checkout-btn" onclick="return confirm('精算を完了しますか？');">
                            精算を完了する
                        </button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="placeholder">
                    <div style="font-size: 5rem; margin-bottom: 20px;">🏧</div>
                    <h2>精算するテーブルを左から選択してください</h2>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
