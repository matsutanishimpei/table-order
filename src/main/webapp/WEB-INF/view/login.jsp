<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>ログイン | テーブルオーダーシステム</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-950 flex items-center justify-center min-h-screen p-6 relative overflow-hidden">
    <!-- 装飾用背景 -->
    <div class="absolute -top-24 -right-24 w-96 h-96 bg-primary-600/10 rounded-full blur-[120px] animate-pulse"></div>
    <div class="absolute -bottom-24 -left-24 w-96 h-96 bg-accent-600/10 rounded-full blur-[120px] animate-pulse" style="animation-delay: -2s;"></div>
    
    <main class="w-full max-w-[480px] relative z-10">
        <div class="glass-panel p-16 border-white/5 bg-white/5 backdrop-blur-2xl">
            <header class="text-center mb-16">
                <div class="inline-block px-4 py-2 rounded-full bg-primary-500/10 border border-primary-500/20 text-primary-400 text-[10px] font-black tracking-[0.3em] uppercase mb-6">
                    Establish Connection
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-white mb-4">
                    WELCOME<span class="text-primary-500">.</span>
                </h1>
                <p class="text-slate-400 font-medium tracking-wide">システムにログインして開始</p>
            </header>

            <c:if test="${not empty error}">
                <div class="mb-10 p-5 rounded-2xl bg-red-500/10 border border-red-500/20 flex items-center gap-4 animate-fadeIn">
                    <span class="text-xl">⚠️</span>
                    <span class="text-xs font-bold text-red-300 tracking-wide">${error}</span>
                </div>
            </c:if>

            <form action="Login" method="post" class="space-y-10">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <div class="space-y-4">
                    <label for="id" class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest ml-1">ユーザーID</label>
                    <input type="text" id="id" name="id" required placeholder="User ID" autofocus
                        class="w-full px-8 py-5 bg-white/5 border border-white/10 rounded-2xl text-white placeholder-slate-600 focus:outline-none focus:border-primary-500 focus:bg-white/10 transition-all font-bold text-lg">
                </div>
                <div class="space-y-4">
                    <label for="pw" class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest ml-1">パスワード</label>
                    <input type="password" id="pw" name="pw" required placeholder="••••••••"
                        class="w-full px-8 py-5 bg-white/5 border border-white/10 rounded-2xl text-white placeholder-slate-600 focus:outline-none focus:border-primary-500 focus:bg-white/10 transition-all font-bold text-lg">
                </div>
                
                <button type="submit" class="w-full btn-primary py-6 text-base shadow-2xl shadow-primary-500/40 uppercase tracking-[0.2em]">
                    ログイン
                </button>
            </form>
        </div>
        
        <footer class="mt-16 text-center opacity-40">
            <p class="text-[10px] font-bold text-slate-500 uppercase tracking-[0.4em]">© 2024 Table Order System Architects</p>
        </footer>
    </main>

    <style>
        .animate-fadeIn { animation: fadeIn 0.8s cubic-bezier(0.16, 1, 0.3, 1); }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
