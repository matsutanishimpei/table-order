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
    <div class="container" style="max-width: 800px;">
        <header class="page-header">
            <h1>商品詳細の編集</h1>
            <a href="Products" class="link-back">← 一覧へ戻る</a>
        </header>

        <c:if test="${param.msg == 'invalid'}">
            <div class="alert alert-danger">入力内容が正しくありません。値を確認してください。</div>
        </c:if>
        <c:if test="${param.msg == 'toolong'}">
            <div class="alert alert-danger">商品名が長すぎます（最大100文字）。</div>
        </c:if>

        <div class="card">
            <h2>商品情報の修正</h2>
            <form action="Products" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="id" value="${product.id}">
                
                <div class="form-group">
                    <label class="form-label">商品名</label>
                    <input type="text" name="name" class="form-control" required 
                           value="<c:out value='${product.name}' />" maxlength="100">
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 20px;">
                    <div class="form-group">
                        <label class="form-label">カテゴリー</label>
                        <select name="categoryId" class="form-control" required>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}" ${product.categoryId == cat.id ? 'selected' : ''}>
                                    <c:out value="${cat.name}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">価格（税抜）</label>
                        <input type="number" name="price" class="form-control" required min="0" 
                               value="${product.price}">
                    </div>
                </div>

                <div class="form-group" style="margin-top: 20px;">
                    <label class="form-label">商品説明</label>
                    <textarea name="description" class="form-control" rows="4" 
                              placeholder="詳細画面に表示される説明文を入力してください。"><c:out value="${product.description}" /></textarea>
                </div>

                <div class="form-group" style="margin-top: 20px;">
                    <label class="form-label">アレルギー情報</label>
                    <input type="text" name="allergyInfo" class="form-control" 
                           value="<c:out value='${product.allergyInfo}' />" placeholder="例：小麦、乳、卵">
                </div>

                <div style="margin-top: 20px; padding: 15px; background: var(--bg-body); border-radius: 8px;">
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 10px;">
                        <input type="checkbox" name="isAvailable" ${product.available ? 'checked' : ''} style="width: 20px; height: 20px;">
                        <span style="font-weight: bold;">販売中として公開する</span>
                    </label>
                    <p style="margin: 5px 0 0 30px; font-size: 0.85rem; color: var(--text-sub);">
                        チェックを外すと、お客様のメニュー画面には表示されなくなります（品切れ等）。
                    </p>
                </div>

                <div style="margin-top: 40px; display: flex; gap: 15px;">
                    <button type="submit" class="btn btn-primary" style="flex: 2; padding: 15px;">変更内容を保存する</button>
                    <a href="Products" class="btn btn-outline" style="flex: 1; padding: 15px;">キャンセル</a>
                </div>
            </form>
        </div>

        <div style="margin-top: 20px; text-align: center; font-size: 0.9rem; color: var(--text-sub);">
            商品ID: ${product.id}
        </div>
    </div>
</body>
</html>
