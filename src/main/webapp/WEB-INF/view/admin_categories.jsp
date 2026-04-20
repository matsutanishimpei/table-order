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
    <div class="container" style="max-width: 900px; padding: 64px 24px;">
        <header class="page-header" style="margin-bottom: 48px; flex-direction: column; align-items: flex-start; gap: 8px;">
            <div style="font-size: 0.85rem; color: var(--accent); font-weight: 800; text-transform: uppercase; letter-spacing: 0.15em;">Classification Settings</div>
            <h1 style="font-size: 2.2rem; font-weight: 900; letter-spacing: -0.02em;">カテゴリー管理</h1>
            <a href="${pageContext.request.contextPath}/Admin/Home" class="link-back" style="margin-top: 16px;">← 管理メニューへ戻る</a>
        </header>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success" style="border-radius: 16px;">カテゴリーを正常に追加しました。</div>
        </c:if>
        <c:if test="${param.msg == 'empty'}">
            <div class="alert alert-danger" style="border-radius: 16px;">カテゴリー名を入力してください。</div>
        </c:if>
        <c:if test="${param.msg == 'toolong'}">
            <div class="alert alert-danger" style="border-radius: 16px;">カテゴリー名が長すぎます（最大50文字以内）。</div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger" style="border-radius: 16px;">処理中にエラーが発生しました。</div>
        </c:if>

        <div style="display: grid; grid-template-columns: 1fr; gap: 32px;">
            <!-- 登録フォーム -->
            <div class="card" style="box-shadow: var(--shadow-xl); border: none;">
                <h2 style="font-size: 1.1rem; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-sub); border: none; padding: 0; margin-bottom: 24px;">新規カテゴリー追加</h2>
                <form action="Categories" method="post" class="form-inline" style="gap: 16px;">
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    <div class="flex-grow-1">
                        <input type="text" name="name" class="form-control" style="height: 56px; border-radius: 14px;" placeholder="例：サイドメニュー（最大50文字）" required autofocus maxlength="50">
                    </div>
                    <button type="submit" class="btn btn-primary" style="height: 56px; padding: 0 32px; border-radius: 14px;">カテゴリーを追加</button>
                </form>
            </div>

            <!-- 一覧表示 -->
            <div class="card" style="padding: 0; overflow: hidden; border: none; box-shadow: var(--shadow-lg);">
                <div style="padding: 24px 32px; background: #f8fafc; border-bottom: 1px solid var(--border);">
                    <h2 style="margin: 0; font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; border: none; padding: 0;">登録済み一覧</h2>
                </div>
                <table class="admin-table" style="box-shadow: none; border-radius: 0;">
                    <thead>
                        <tr>
                            <th style="width: 120px; background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">ID</th>
                            <th style="background: transparent; color: var(--text-sub); border-bottom: 2px solid var(--border);">カテゴリー名</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cat" items="${categoryList}">
                            <tr>
                                <td style="color: var(--text-sub); font-family: monospace; font-size: 0.95rem;">#${cat.id}</td>
                                <td style="font-weight: 700; color: var(--primary); font-size: 1.1rem;">
                                    <c:out value="${cat.name}" />
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty categoryList}">
                            <tr>
                                <td colspan="2" style="text-align: center; padding: 100px 40px;">
                                    <div style="font-size: 3rem; margin-bottom: 16px; opacity: 0.1;">🏷️</div>
                                    <div style="color: var(--text-sub); font-weight: 500;">まだカテゴリーが登録されていません。</div>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
