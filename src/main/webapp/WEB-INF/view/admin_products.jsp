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
                        <label for="name">商品名</label>
                        <input type="text" id="name" name="name" required placeholder="例：カルビ焼肉">
                    </div>
                    <div class="form-group">
                        <label for="categoryId">カテゴリー <a href="${pageContext.request.contextPath}/Admin/Categories" style="font-size: 0.8rem; font-weight: normal; margin-left: 5px;">[新規追加]</a></label>
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
                        <th>ID</th>
                        <th>商品名</th>
                        <th>カテゴリ</th>
                        <th>価格</th>
                        <th>状態</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${productList}">
                        <tr>
                            <td>${p.id}</td>
                            <td><strong><c:out value="${p.name}" /></strong></td>
                            <td>
                                <c:forEach var="cat" items="${categoryList}">
                                    <c:if test="${p.categoryId == cat.id}"><c:out value="${cat.name}" /></c:if>
                                </c:forEach>
                            </td>
                            <td>¥<fmt:formatNumber value="${p.price}" /></td>
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
