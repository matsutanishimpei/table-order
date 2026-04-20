<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>カテゴリー管理（管理者） - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container">
        <header class="page-header">
            <h1>カテゴリー管理</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back">← 管理メニューへ戻る</a>
        </header>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success">カテゴリーを追加しました。</div>
        </c:if>
        <c:if test="${param.msg == 'empty'}">
            <div class="alert alert-danger">カテゴリー名を入力してください。</div>
        </c:if>
        <c:if test="${param.msg == 'toolong'}">
            <div class="alert alert-danger">カテゴリー名が長すぎます（最大50文字以内で入力してください）。</div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger">処理中にエラーが発生しました。</div>
        </c:if>

        <!-- 登録フォーム -->
        <div class="card">
            <h2>新規カテゴリー追加</h2>
            <form action="Categories" method="post" class="form-inline">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <div class="flex-grow-1">
                    <input type="text" name="name" class="form-control" placeholder="例：サイドメニュー（最大50文字）" required autofocus maxlength="50">
                </div>
                <button type="submit" class="btn btn-primary">カテゴリーを追加</button>
            </form>
        </div>

        <!-- 一覧表示 -->
        <div class="card">
            <h2>カテゴリー一覧</h2>
            <table class="admin-table">
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
                            <td><strong><c:out value="${cat.name}" /></strong></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty categoryList}">
                        <tr>
                            <td colspan="2" style="text-align: center; color: #999; padding: 40px;">カテゴリーがありません。</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
