<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メインメニュー</title>
<style>
    body { font-family: sans-serif; padding: 20px; }
    .menu-container { max-width: 600px; margin: 0 auto; }
</style>
</head>
<body>
    <div class="menu-container">
        <h1>メインメニュー</h1>
        <p>ログイン成功しました。</p>
        <p>ようこそ、<strong>${sessionScope.user.id}</strong> さん！</p>
        <p>ロール権限: ${sessionScope.user.role}</p>
        
        <hr>
        <!-- 今後の開発でここにメニュー項目を追加する -->
        <ul>
            <li><a href="#">注文する（準備中）</a></li>
            <li><a href="#">履歴を見る（準備中）</a></li>
        </ul>
        
        <!-- ログアウト用の仮リンク -->
        <p><a href="Login">ログイン画面へ戻る（※機能確認用）</a></p>
    </div>
</body>
</html>
