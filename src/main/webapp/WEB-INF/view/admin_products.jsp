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
    <style>
        .form-group { margin-bottom: 15px; display: flex; flex-direction: column; }
        label { font-weight: bold; margin-bottom: 5px; }
        input[type="text"], input[type="number"], select {
            padding: 10px; border: 1px solid var(--border); border-radius: 6px; font-size: 1rem;
        }
        
        table { width: 100%; border-collapse: collapse; background: var(--bg-card); border-radius: 8px; overflow: hidden; margin-top: 20px; }
        th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid var(--border); }
        th { background-color: var(--primary); color: white; }
        tr:hover { background-color: var(--bg-body); }

        .status-badge { padding: 4px 8px; border-radius: 4px; font-size: 0.85rem; font-weight: bold; }
        .status-on { background: #d4edda; color: #155724; }
        .status-off { background: #f8d7da; color: #721c24; }
        
        .back-link { text-decoration: none; color: var(--accent); font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <header class="page-header">
            <h1>商品管理システム</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="back-link">← 管理メニューへ戻る</a>
        </header>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success">商品を登録しました。</div>
        </c:if>
        <c:if test="${param.msg == 'invalid'}">
            <div class="alert alert-danger">入力内容が正しくありません（名前、価格、カテゴリーを確認してください）。</div>
        </c:if>
        <c:if test="${param.msg == 'toolong'}">
            <div class="alert alert-danger">商品名が長すぎます（最大100文字以内で入力してください）。</div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger">サーバーエラーにより登録に失敗しました。</div>
        </c:if>

        <!-- 新規登録フォーム -->
        <div class="card">
            <h2>新規商品登録</h2>
            <form action="Products" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 20px;">
                    <div class="form-group">
                        <label for="name">商品名 <span style="font-size: 0.8rem; font-weight: normal; color: var(--text-sub);">(最大100文字)</span></label>
                        <input type="text" id="name" name="name" required placeholder="例：カルビ焼肉" maxlength="100">
                    </div>
                    <div class="form-group">
                        <label for="categoryId">カテゴリー</label>
                        <select id="categoryId" name="categoryId" required>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}"><c:out value="${cat.name}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="price">価格（税抜）</label>
                        <input type="number" id="price" name="price" required min="0" placeholder="0">
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-top: 15px;">
                    <div class="form-group">
                        <label for="description">商品説明</label>
                        <textarea id="description" name="description" rows="2" placeholder="詳細画面に表示される説明文を入力してください。" style="padding:10px; border-radius:6px; border:1px solid var(--border); font-family:inherit;"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="allergyInfo">アレルギー情報</label>
                        <input type="text" id="allergyInfo" name="allergyInfo" placeholder="例：小麦、乳、卵">
                    </div>
                </div>
                <div style="margin-top: 10px;">
                    <label>
                        <input type="checkbox" name="isAvailable" checked> 販売中として登録する
                    </label>
                </div>
                <button type="submit" class="submit-btn">新規登録する</button>
            </form>
        </div>

        <!-- 商品一覧 -->
        <div class="card">
            <h2>登録済み商品一覧</h2>
            <table>
                <thead>
                    <tr>
                        <th style="width: 50px;">ID</th>
                        <th style="width: 150px;">商品名</th>
                        <th style="width: 100px;">カテゴリ</th>
                        <th style="width: 80px;">価格</th>
                        <th>説明 / アレルギー</th>
                        <th style="width: 80px;">状態</th>
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
                                <div style="font-size: 0.85rem; color: #555;">
                                    <c:out value="${p.description}" /><br>
                                    <small style="color: var(--accent);">[アレルギー: <c:out value="${empty p.allergyInfo ? 'なし' : p.allergyInfo}" />]</small>
                                </div>
                            </td>
                            <td>
                                <span class="status-badge ${p.available ? 'status-on' : 'status-off'}">
                                    ${p.available ? '販売中' : '売切'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
