<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理者メニュー - Table Order</title>
    <style>
    <title>管理者メニュー - Table Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 20px;
            margin-top: 40px;
        }

        .menu-card {
            background: var(--bg-card);
            padding: 40px 20px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
            text-decoration: none;
            color: var(--text-main);
            transition: all 0.3s ease;
            display: flex;
            flex-direction: column;
            align-items: center;
            border: 1px solid transparent;
        }

        .menu-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            border-color: var(--accent);
        }

        .menu-icon { font-size: 3rem; margin-bottom: 15px; }
        .menu-title { font-size: 1.25rem; font-weight: bold; margin-bottom: 10px; }
        .menu-desc { font-size: 0.9rem; color: var(--text-sub); }

        .logout-link {
            margin-top: 40px;
            display: inline-block;
            color: var(--danger);
            text-decoration: none;
            font-weight: bold;
        }
    </style>
    </style>
</head>
<body>
    <div class="container">
        <h1>管理者メインメニュー</h1>
        
        <div class="menu-grid">
            <!-- カテゴリー管理 -->
            <a href="${pageContext.request.contextPath}/Admin/Categories" class="menu-card">
                <div class="menu-icon">🏷️</div>
                <div class="menu-title">カテゴリー管理</div>
                <div class="menu-desc">ジャンルの追加・編集</div>
            </a>

            <!-- 商品管理 -->
            <a href="${pageContext.request.contextPath}/Admin/Products" class="menu-card">
                <div class="menu-icon">🍴</div>
                <div class="menu-title">商品管理</div>
                <div class="menu-desc">メニューの登録・編集・販売停止</div>
            </a>

            <!-- 注文確認 -->
            <a href="${pageContext.request.contextPath}/Admin/OrderMonitor" class="menu-card">
                <div class="menu-icon">📋</div>
                <div class="menu-title">受注状況</div>
                <div class="menu-desc">フロア全体の稼働状況を監視します</div>
            </a>

            <!-- 売上統計 -->
            <a href="${pageContext.request.contextPath}/Admin/Sales" class="menu-card">
                <div class="menu-icon">📈</div>
                <div class="menu-title">売上管理</div>
                <div class="menu-desc">日次・月次の売上を集計します</div>
            </a>
        </div>

        <a href="${pageContext.request.contextPath}/Login" class="logout-link">ログアウトして戻る</a>
    </div>
</body>
</html>
