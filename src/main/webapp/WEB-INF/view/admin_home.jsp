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
    <div class="container" style="max-width: 1000px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 64px; flex-direction: column; align-items: flex-start; gap: 8px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Control Center</div>
            <h1 style="font-size: 2.5rem; font-weight: 900; letter-spacing: -0.02em;">管理者メインメニュー</h1>
        </header>
        
        <div class="nav-grid" style="gap: 24px; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));">
            <!-- カテゴリー管理 -->
            <a href="${pageContext.request.contextPath}/Admin/Categories" class="nav-card" style="padding: 48px 32px;">
                <div class="nav-icon" style="font-size: 3.5rem; margin-bottom: 24px;">🏷️</div>
                <div class="nav-title" style="font-size: 1.25rem;">カテゴリー管理</div>
                <div class="nav-desc" style="opacity: 0.7;">ジャンルの追加・編集</div>
            </a>

            <!-- 商品管理 -->
            <a href="${pageContext.request.contextPath}/Admin/Products" class="nav-card" style="padding: 48px 32px;">
                <div class="nav-icon" style="font-size: 3.5rem; margin-bottom: 24px;">🍴</div>
                <div class="nav-title" style="font-size: 1.25rem;">商品管理</div>
                <div class="nav-desc" style="opacity: 0.7;">メニューの登録・編集</div>
            </a>

            <!-- 注文確認 -->
            <a href="${pageContext.request.contextPath}/Admin/OrderMonitor" class="nav-card" style="padding: 48px 32px;">
                <div class="nav-icon" style="font-size: 3.5rem; margin-bottom: 24px;">📋</div>
                <div class="nav-title" style="font-size: 1.25rem;">受注状況</div>
                <div class="nav-desc" style="opacity: 0.7;">フロア全体の稼働状況監視</div>
            </a>

            <!-- 売上統計 -->
            <a href="${pageContext.request.contextPath}/Admin/Sales" class="nav-card" style="padding: 48px 32px;">
                <div class="nav-icon" style="font-size: 3.5rem; margin-bottom: 24px;">📈</div>
                <div class="nav-title" style="font-size: 1.25rem;">売上管理</div>
                <div class="nav-desc" style="opacity: 0.7;">日次・月次の売上累計</div>
            </a>
        </div>

        <div style="margin-top: 80px; text-align: center; border-top: 1px solid var(--border); padding-top: 48px;">
            <a href="${pageContext.request.contextPath}/Logout" class="btn btn-danger" style="padding: 16px 40px; border-radius: 16px; font-weight: 700;">
                <span>🚪</span> ログアウトして戻る
            </a>
            <p style="margin-top: 16px; font-size: 0.85rem; color: var(--text-sub);">ログイン中のユーザー: ${sessionScope.user.id}</p>
        </div>
    </div>
</body>
</html>
