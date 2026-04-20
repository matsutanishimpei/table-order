<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品管理（管理者） - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container" style="max-width: 1100px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px; flex-direction: column; align-items: flex-start; gap: 8px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Inventory Management</div>
            <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">商品管理システム</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back" style="margin-top: 16px;">← 管理メニューへ戻る</a>
        </header>

        <div style="display: flex; flex-direction: column; gap: 16px; margin-bottom: 32px;">
            <c:if test="${param.msg == 'success'}">
                <div class="alert alert-success" style="border-radius: 16px; margin: 0;">新規商品を正式に登録しました。</div>
            </c:if>
            <c:if test="${param.msg == 'invalid'}">
                <div class="alert alert-danger" style="border-radius: 16px; margin: 0;">入力内容に不備があります。値を確認してください。</div>
            </c:if>
            <c:if test="${param.msg == 'toolong'}">
                <div class="alert alert-danger" style="border-radius: 16px; margin: 0;">商品名が長すぎます（最大100文字）。</div>
            </c:if>
            <c:if test="${param.msg == 'error'}">
                <div class="alert alert-danger" style="border-radius: 16px; margin: 0;">サーバーエラーが発生しました。時間を置いて再度お試しください。</div>
            </c:if>
            <c:if test="${param.msg == 'updatesuccess'}">
                <div class="alert alert-success" style="border-radius: 16px; margin: 0;">商品情報を正常に更新しました。</div>
            </c:if>
            <c:if test="${param.msg == 'notfound'}">
                <div class="alert alert-danger" style="border-radius: 16px; margin: 0;">指定された商品データが存在しません。</div>
            </c:if>
        </div>

        <!-- 新規登録フォーム -->
        <div class="card" style="box-shadow: var(--shadow-xl); border: none; margin-bottom: 48px;">
            <h2 style="font-size: 1.15rem; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-sub); border: none; padding: 0; margin-bottom: 32px;">新規商品登録</h2>
            <form action="Products" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <div style="display: grid; grid-template-columns: 2fr 1fr 1fr; gap: 24px;">
                    <div class="form-group">
                        <label class="form-label">商品名</label>
                        <input type="text" name="name" class="form-control" style="height: 52px;" required placeholder="例：特選カルビ焼肉" maxlength="100">
                    </div>
                    <div class="form-group">
                        <label class="form-label">カテゴリー</label>
                        <select name="categoryId" class="form-control" style="height: 52px;" required>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}"><c:out value="${cat.name}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">価格（税抜）</label>
                        <input type="number" name="price" class="form-control" style="height: 52px;" required min="0" placeholder="0">
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 24px; margin-top: 24px;">
                    <div class="form-group">
                        <label class="form-label">商品説明</label>
                        <textarea name="description" class="form-control" rows="3" style="border-radius: 16px;" placeholder="顧客用詳細画面に表示される魅力的な説明文を入力してください。"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-label">アレルギー情報</label>
                        <input type="text" name="allergyInfo" class="form-control" style="height: 52px;" placeholder="例：小麦、乳、卵">
                    </div>
                </div>
                <div style="margin-top: 24px; padding: 16px; background: var(--bg-body); border-radius: 12px; border: 1px solid var(--border);">
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 12px;">
                        <input type="checkbox" name="isAvailable" checked style="width: 20px; height: 20px;"> 
                        <span style="font-weight: 600; color: var(--primary);">この商品を即時販売開始する</span>
                    </label>
                </div>
                <div style="margin-top: 40px; display: flex; justify-content: flex-end;">
                    <button type="submit" class="btn btn-primary" style="padding: 16px 48px; border-radius: 16px; font-weight: 700;">
                        <span>🛒</span> 新規登録を実行
                    </button>
                </div>
            </form>
        </div>

        <!-- 商品一覧 -->
        <div class="card" style="padding: 0; overflow: hidden; border: none; box-shadow: var(--shadow-xl);">
            <div style="padding: 24px 32px; background: #f8fafc; border-bottom: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center;">
                    <h2 style="margin: 0; font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; border: none; padding: 0;">登録済み商品一覧</h2>
                    <span style="font-size: 0.85rem; color: var(--text-sub); font-weight: 600;">Total: ${productList.size()} items</span>
            </div>
            <table class="admin-table" style="box-shadow: none; border-radius: 0;">
                <thead>
                    <tr>
                        <th style="width: 70px; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">ID</th>
                        <th style="width: 220px; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">商品名</th>
                        <th style="width: 140px; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">カテゴリ</th>
                        <th style="width: 120px; text-align: right; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">価格</th>
                        <th style="background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">説明 / アレルギー</th>
                        <th style="width: 100px; text-align: center; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">状態</th>
                        <th style="width: 100px; text-align: center; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${productList}">
                        <tr>
                            <td style="color: var(--text-sub); font-family: monospace;">#${p.id}</td>
                            <td style="font-weight: 700; color: var(--primary);">
                                <c:out value="${p.name}" />
                            </td>
                            <td>
                                <c:forEach var="cat" items="${categoryList}">
                                    <c:if test="${p.categoryId == cat.id}">
                                        <span class="badge" style="background: rgba(99, 102, 241, 0.05); color: var(--accent); border: 1px solid rgba(99, 102, 241, 0.1);"><c:out value="${cat.name}" /></span>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td style="text-align: right; font-weight: 800; color: var(--primary);">¥<fmt:formatNumber value="${p.price}" /></td>
                            <td>
                                <div style="font-size: 0.85rem; line-height: 1.5;">
                                    <div class="text-clamp-2" style="color: var(--text-main); margin-bottom: 4px;"><c:out value="${p.description}" /></div>
                                    <div style="color: var(--accent); font-weight: 600; font-size: 0.75rem;">[ALLERGY: <c:out value="${empty p.allergyInfo ? 'NONE' : p.allergyInfo}" />]</div>
                                </div>
                            </td>
                            <td style="text-align: center;">
                                <span class="badge ${p.available ? 'badge-success' : 'badge-danger'}">
                                    ${p.available ? '販売中' : '売切'}
                                </span>
                            </td>
                            <td style="text-align: center;">
                                <a href="Products?action=edit&id=${p.id}" class="btn btn-outline" style="padding: 8px 16px; font-size: 0.75rem; border-radius: 10px; font-weight: 700;">編集</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
