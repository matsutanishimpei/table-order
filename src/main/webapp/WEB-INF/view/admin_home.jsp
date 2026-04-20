<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理者メニュー - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
</head>
<body>
    <div class="container">
        <header class="page-header">
            <h1>管理者メインメニュー</h1>
        </header>
        
        <div class="nav-grid">
            <!-- カテゴリー管理 -->
            <a href="${pageContext.request.contextPath}/Admin/Categories" class="nav-card">
                <div class="nav-icon">🏷️</div>
                <div class="nav-title">カテゴリー管理</div>
                <div class="nav-desc">ジャンルの追加・編集</div>
            </a>

            <!-- 商品管理 -->
            <a href="${pageContext.request.contextPath}/Admin/Products" class="nav-card">
                <div class="nav-icon">🍴</div>
                <div class="nav-title">商品管理</div>
                <div class="nav-desc">メニューの登録・編集・販売停止</div>
            </a>

            <!-- 注文確認 -->
            <a href="${pageContext.request.contextPath}/Admin/OrderMonitor" class="nav-card">
                <div class="nav-icon">📋</div>
                <div class="nav-title">受注状況</div>
                <div class="nav-desc">フロア全体の稼働状況を監視します</div>
            </a>

            <!-- 売上統計 -->
            <a href="${pageContext.request.contextPath}/Admin/Sales" class="nav-card">
                <div class="nav-icon">📈</div>
                <div class="nav-title">売上管理</div>
                <div class="nav-desc">日次・月次の売上を集計します</div>
            </a>
        </div>

        <div style="margin-top: 40px; text-align: center;">
            <a href="${pageContext.request.contextPath}/Logout" class="btn btn-danger">ログアウトして戻る</a>
        </div>
    </div>
</body>
</html>
