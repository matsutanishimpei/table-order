<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品編集 - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container" style="max-width: 900px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px; flex-direction: column; align-items: flex-start; gap: 8px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Editing Record #${product.id}</div>
            <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">商品詳細の編集</h1>
            <a href="Products" class="link-back" style="margin-top: 16px;">← 商品一覧へ戻る</a>
        </header>

        <div style="display: flex; flex-direction: column; gap: 16px; margin-bottom: 32px;">
            <c:if test="${param.msg == 'invalid'}">
                <div class="alert alert-danger" style="border-radius: 16px; margin: 0;">入力内容に不備があります。数値を再確認してください。</div>
            </c:if>
            <c:if test="${param.msg == 'toolong'}">
                <div class="alert alert-danger" style="border-radius: 16px; margin: 0;">商品名が長すぎます（最大100文字）。</div>
            </c:if>
        </div>

        <div class="card" style="box-shadow: var(--shadow-xl); border: none; padding: 48px;">
            <h2 style="font-size: 1.15rem; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-sub); border: none; padding: 0; margin-bottom: 40px;">商品情報の修正</h2>
            <form action="Products" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="id" value="${product.id}">
                
                <div class="form-group">
                    <label class="form-label">商品名</label>
                    <input type="text" name="name" class="form-control" style="height: 56px;" required 
                           value="<c:out value='${product.name}' />" placeholder="例：特選カルビ焼肉" maxlength="100">
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-top: 32px;">
                    <div class="form-group">
                        <label class="form-label">カテゴリー</label>
                        <select name="categoryId" class="form-control" style="height: 56px;" required>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}" ${product.categoryId == cat.id ? 'selected' : ''}>
                                    <c:out value="${cat.name}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">価格（税抜）</label>
                        <input type="number" name="price" class="form-control" style="height: 56px;" required min="0" 
                               value="${product.price}">
                    </div>
                </div>

                <div class="form-group" style="margin-top: 32px;">
                    <label class="form-label">商品説明</label>
                    <textarea name="description" class="form-control" rows="4" style="border-radius: 16px;"
                               placeholder="詳細画面に表示される魅力的な説明文を入力してください。"><c:out value="${product.description}" /></textarea>
                </div>

                <div class="form-group" style="margin-top: 32px;">
                    <label class="form-label">アレルギー情報</label>
                    <input type="text" name="allergyInfo" class="form-control" style="height: 56px;"
                           value="<c:out value='${product.allergyInfo}' />" placeholder="例：小麦、乳、卵">
                </div>

                <div style="margin-top: 40px; padding: 24px; background: var(--bg-body); border-radius: 16px; border: 1px solid var(--border);">
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 12px; margin-bottom: 8px;">
                        <input type="checkbox" name="isAvailable" ${product.available ? 'checked' : ''} style="width: 22px; height: 22px;">
                        <span style="font-weight: 700; color: var(--primary);">この商品を販売中として公開する</span>
                    </label>
                    <p style="margin: 0 0 0 34px; font-size: 0.85rem; color: var(--text-sub); line-height: 1.6;">
                        チェックを解除すると即座にメニューから非表示（売切状態）になります。
                    </p>
                </div>

                <div style="margin-top: 56px; display: flex; gap: 16px;">
                    <button type="submit" class="btn btn-primary" style="flex: 2; height: 64px; font-size: 1.1rem; border-radius: 18px; font-weight: 800;">
                        変更内容を正式に保存
                    </button>
                    <a href="Products" class="btn btn-outline" style="flex: 1; height: 64px; line-height: 2.2; border-radius: 18px; font-weight: 700;">キャンセル</a>
                </div>
            </form>
        </div>

        <div style="margin-top: 32px; text-align: center; font-family: monospace; font-size: 0.85rem; color: var(--text-sub); opacity: 0.5;">
            SYSTEM_RECORD_ID: ${product.id}
        </div>
    </div>
</body>
</html>
