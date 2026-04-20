<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン | テーブルオーダーシステム</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-layout">
    <div class="glass-card">
        <h1 class="auth-title">TABLE ORDER</h1>
        <p class="auth-subtitle">システムにログインしてください</p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger" style="margin-bottom: 32px; padding: 12px; border-radius: 16px; font-size: 0.9rem; background: rgba(239, 68, 68, 0.15); border: 1px solid rgba(239, 68, 68, 0.3); color: #fca5a5;">
                <span>⚠️ ${error}</span>
            </div>
        </c:if>

        <form action="Login" method="post">
            <div class="form-group">
                <label for="id" class="form-label">ユーザーID</label>
                <input type="text" id="id" name="id" class="form-control" required placeholder="IDを入力" autofocus>
            </div>
            <div class="form-group" style="margin-top: 24px;">
                <label for="pw" class="form-label">パスワード</label>
                <input type="password" id="pw" name="pw" class="form-control" required placeholder="••••••••">
            </div>
            <button type="submit" class="btn btn-primary btn-block" style="margin-top: 40px; padding: 16px; border-radius: 16px;">
                ログイン
            </button>
        </form>
    </div>
</body>
</html>
