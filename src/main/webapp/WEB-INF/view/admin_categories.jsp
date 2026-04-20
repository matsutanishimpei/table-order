<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>カテゴリー管理（管理者） - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-group { display: flex; gap: 10px; margin-bottom: 20px; }
        input[type="text"] { flex-grow: 1; padding: 12px; border: 1px solid var(--border); border-radius: 6px; font-size: 1rem; }
        
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid var(--border); }
        th { background: var(--primary); color: white; }
        
        .back-link { text-decoration: none; color: var(--accent); font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>カテゴリー管理</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="back-link">← 管理メニューへ戻る</a>
        </header>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success">カテゴリーを追加しました。</div>
        </c:if>
        <c:if test="${param.msg == 'empty'}">
            <div class="alert alert-danger">カテゴリー名を入力してください。</div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger">処理中にエラーが発生しました。</div>
        </c:if>

        <!-- 登録フォーム -->
        <div class="card">
            <h2>新規カテゴリー追加</h2>
            <form action="Categories" method="post" class="form-group">
                <input type="text" name="name" placeholder="例：サイドメニュー" required autofocus>
                <button type="submit" class="submit-btn">追加</button>
            </form>
        </div>

        <!-- 一覧表示 -->
        <div class="card">
            <h2>カテゴリー一覧</h2>
            <table>
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>カテゴリー名</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cat" items="${categoryList}">
                        <tr>
                            <td>${cat.id}</td>
                            <td><strong>${cat.name}</strong></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty categoryList}">
                        <tr>
                            <td colspan="2" style="text-align: center; color: #999;">カテゴリーがありません。</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
