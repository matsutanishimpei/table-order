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
    <div class="container">
        <header class="page-header">
            <h1>商品管理システム</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back">← 管理メニューへ戻る</a>
        </header>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success">商品を登録しました。</div>
        </c:if>
        <c:if test="${param.msg == 'invalid'}">
            <div class="alert alert-danger">入力内容が正しくありません。</div>
        </c:if>
        <c:if test="${param.msg == 'toolong'}">
            <div class="alert alert-danger">商品名が長すぎます（最大100文字）。</div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger">サーバーエラーにより登録に失敗しました。</div>
        </c:if>

        <c:if test="${param.msg == 'updatesuccess'}">
            <div class="alert alert-success">商品情報を更新しました。</div>
        </c:if>
        <c:if test="${param.msg == 'notfound'}">
            <div class="alert alert-danger">指定された商品が見つかりませんでした。</div>
        </c:if>

        <!-- 新規登録フォーム -->
        <div class="card">
            <h2>新規商品登録</h2>
            <form action="Products" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 20px;">
                    <div class="form-group">
                        <label class="form-label">商品名</label>
                        <input type="text" name="name" class="form-control" required placeholder="例：カルビ焼肉" maxlength="100">
                    </div>
                    <div class="form-group">
                        <label class="form-label">カテゴリー</label>
                        <select name="categoryId" class="form-control" required>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}"><c:out value="${cat.name}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">価格（税抜）</label>
                        <input type="number" name="price" class="form-control" required min="0" placeholder="0">
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-top: 15px;">
                    <div class="form-group">
                        <label class="form-label">商品説明</label>
                        <textarea name="description" class="form-control" rows="2" placeholder="詳細画面に表示される説明文を入力してください。"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-label">アレルギー情報</label>
                        <input type="text" name="allergyInfo" class="form-control" placeholder="例：小麦、乳、卵">
                    </div>
                </div>
                <div style="margin-top: 10px;">
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 8px;">
                        <input type="checkbox" name="isAvailable" checked style="width: 18px; height: 18px;"> 
                        <span>販売中として登録する</span>
                    </label>
                </div>
                <button type="submit" class="btn btn-primary" style="margin-top: 20px;">新規登録する</button>
            </form>
        </div>

        <!-- 商品一覧 -->
        <div class="card">
            <h2>登録済み商品一覧</h2>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th style="width: 50px;">ID</th>
                        <th style="width: 180px;">商品名</th>
                        <th style="width: 110px;">カテゴリ</th>
                        <th style="width: 100px;">価格</th>
                        <th>説明 / アレルギー</th>
                        <th style="width: 80px;">状態</th>
                        <th style="width: 80px;">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${productList}">
                        <tr>
                            <td>${p.id}</td>
                            <td>
                                <strong title="<c:out value='${p.name}' />"><c:out value="${p.name}" /></strong>
                            </td>
                            <td>
                                <c:forEach var="cat" items="${categoryList}">
                                    <c:if test="${p.categoryId == cat.id}"><span style="font-size: 0.9rem;"><c:out value="${cat.name}" /></span></c:if>
                                </c:forEach>
                            </td>
                            <td>¥<fmt:formatNumber value="${p.price}" /></td>
                            <td>
                                <div style="font-size: 0.85rem; color: var(--text-main);">
                                    <c:out value="${p.description}" /><br>
                                    <small style="color: var(--accent);">[アレルギー: <c:out value="${empty p.allergyInfo ? 'なし' : p.allergyInfo}" />]</small>
                                </div>
                            </td>
                            <td>
                                <span class="badge ${p.available ? 'badge-success' : 'badge-danger'}">
                                    ${p.available ? '販売中' : '売切'}
                                </span>
                            </td>
                            <td>
                                <a href="Products?action=edit&id=${p.id}" class="btn btn-outline" style="padding: 5px 10px; font-size: 0.8rem;">編集</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
