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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body class="app-layout theme-kitchen">
    <!-- サイドバー：座席選択 -->
    <div class="sidebar" style="width: 320px; border-right: 1px solid rgba(255,255,255,0.05); background: #020617;">
        <div class="sidebar-header" style="background: #1e293b; padding: 32px 24px; border-bottom: 1px solid rgba(255,255,255,0.05);">
            <div style="font-size: 0.7rem; text-transform: uppercase; letter-spacing: 0.15em; color: var(--accent); font-weight: 800; margin-bottom: 4px;">Point of Sale</div>
            <div style="font-size: 1.25rem; font-weight: 900;">精算待ちテーブル</div>
        </div>
        <div class="sidebar-content" style="padding: 12px 0;">
            <c:forEach var="t" items="${unsettledTables}">
                <a href="Home?tableId=${t.tableId}" class="sidebar-item ${t.tableId == selectedSummary.tableId ? 'active' : ''}" style="padding: 20px 24px; display: flex; align-items: center; justify-content: space-between;">
                    <span style="font-weight: 700; font-size: 1.1rem;"><c:out value="${t.tableName}" /></span>
                    <span style="font-size: 0.75rem; opacity: 0.5;">READY TO PAY</span>
                </a>
            </c:forEach>
            <c:if test="${empty unsettledTables}">
                <div style="padding: 64px 24px; color: #64748b; text-align: center; font-size: 0.9rem; line-height: 1.6;">
                    <div style="font-size: 2.5rem; margin-bottom: 16px; opacity: 0.2;">🌙</div>
                    現在、会計待ちのテーブルはありません。
                </div>
            </c:if>
        </div>
        <div class="sidebar-footer" style="padding: 24px; text-align: center;">
            <a href="${pageContext.request.contextPath}/Logout" class="link-back" style="font-size: 0.85rem; opacity: 0.6;">Logout System</a>
        </div>
    </div>

    <!-- メイン：レジ詳細 -->
    <div class="main-content" style="padding: 48px; min-height: 100vh;">
        <c:choose>
            <c:when test="${not empty selectedSummary}">
                <div class="card" style="max-width: 800px; padding: 48px; border: none; background: #1e293b; box-shadow: var(--shadow-xl); border-radius: 32px;">
                    <header style="display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 48px; border-bottom: 2px solid rgba(255,255,255,0.05); padding-bottom: 32px;">
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <div style="font-size: 0.75rem; color: var(--accent); font-weight: 800; text-transform: uppercase;">Selected Table</div>
                            <h2 style="border:none; margin:0; padding:0; font-size: 2.2rem; font-weight: 900;"><c:out value="${selectedSummary.tableName}" /> 様</h2>
                        </div>
                        <span class="badge" style="background: rgba(255,255,255,0.05); color: #94a3b8; padding: 8px 16px; border-radius: 12px; font-weight: 700;">
                            ${selectedSummary.items.size()} items ordered
                        </span>
                    </header>

                    <div class="items-list" style="margin-bottom: 48px;">
                        <c:forEach var="item" items="${selectedSummary.items}">
                            <div style="display: flex; justify-content: space-between; align-items: center; padding: 18px 0; border-bottom: 1px dashed rgba(255,255,255,0.05);">
                                <div class="word-break" style="flex: 1; padding-right: 24px;">
                                    <div style="font-weight: 700; color: #f1f5f9; font-size: 1.1rem; margin-bottom: 4px;"><c:out value="${item.productName}" /></div>
                                    <div style="color: #94a3b8; font-size: 0.85rem; font-family: monospace;">@¥<fmt:formatNumber value="${item.unitPrice}" /></div>
                                </div>
                                <div style="text-align: right; display: flex; align-items: center; gap: 32px;">
                                    <div style="color: #94a3b8; font-weight: 600; font-size: 1rem; min-width: 60px;">× ${item.quantity}</div>
                                    <div style="font-weight: 800; min-width: 120px; color: #f8fafc; font-size: 1.25rem; font-family: monospace;">¥<fmt:formatNumber value="${item.subtotal}" /></div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div style="margin-top: 48px; padding: 40px; border-radius: 24px; background: rgba(0,0,0,0.2); display: flex; justify-content: space-between; align-items: center; border: 1px solid rgba(255,255,255,0.05);">
                        <div style="font-size: 1rem; font-weight: 800; color: var(--accent); text-transform: uppercase; letter-spacing: 0.1em;">Final Total</div>
                        <div style="font-size: 3.2rem; font-weight: 900; color: white; letter-spacing: -0.02em; font-family: monospace;">
                            <span style="font-size: 1.5rem; opacity: 0.5; margin-right: 4px;">¥</span><fmt:formatNumber value="${selectedSummary.totalAmount}" />
                        </div>
                    </div>

                    <form action="Home" method="post" style="margin-top: 56px;">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="checkout">
                        <input type="hidden" name="tableId" value="${selectedSummary.tableId}">
                        <button type="submit" class="order-btn" style="height: 72px; font-size: 1.3rem; border-radius: 20px;" onclick="return confirm('精算を完了し、テーブルを空けますか？');">
                            <span>🏧</span> 精算を完了し、閉じる
                        </button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="placeholder-view" style="padding-top: 120px;">
                    <div class="placeholder-icon" style="font-size: 6rem; opacity: 0.1;">🏧</div>
                    <h2 style="font-size: 1.75rem; font-weight: 800; color: #94a3b8; letter-spacing: 0.05em; margin-bottom: 16px;">精算するテーブルを選択してください</h2>
                    <p style="opacity: 0.5; max-width: 600px; margin: 0 auto; line-height: 1.8;">左側のリストから会計待ちの座席を選択すると、注文詳細と合計金額が表示されます。内容を確認し、精算処理を完了させてください。</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
