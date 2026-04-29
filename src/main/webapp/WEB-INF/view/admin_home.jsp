<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>管理センター | テーブルオーダーシステム</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[1200px] mx-auto px-12 py-20">
        <header class="mb-20 flex justify-between items-end">
            <div class="space-y-4">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-primary-600 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Administrative Nexus</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950">
                    管理メニュー<span class="text-primary-600">.</span>
                </h1>
                <p class="text-slate-500 font-medium italic opacity-60">店舗運営における全機能の統合管理コンソール</p>
            </div>
            
            <form action="${pageContext.request.contextPath}/Logout" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <button type="submit" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-xs font-black text-slate-400 hover:text-red-500 hover:border-red-100 hover:bg-red-50 transition-all uppercase tracking-widest">
                    ログアウト
                </button>
            </form>
        </header>

        <main class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
            <!-- フロア監視 -->
            <a href="Monitor" class="premium-card p-10 group no-underline transition-all hover:border-primary-500/50 hover:-translate-y-2 hover:shadow-2xl">
                <div class="w-14 h-14 rounded-2xl bg-primary-50 flex items-center justify-center text-2xl mb-8 group-hover:bg-primary-600 group-hover:text-white transition-all">📡</div>
                <h2 class="text-2xl font-black text-slate-900 mb-2">フロア監視</h2>
                <p class="text-xs font-bold text-slate-400 leading-relaxed uppercase tracking-wider mb-8">Real-time status tracking</p>
                <div class="text-[10px] font-black text-primary-600 uppercase tracking-widest flex items-center gap-2 italic">
                    モニターを開く <span class="group-hover:translate-x-1 transition-transform">→</span>
                </div>
            </a>

            <!-- 商品管理 -->
            <a href="Product" class="premium-card p-10 group no-underline transition-all hover:border-emerald-500/50 hover:-translate-y-2 hover:shadow-2xl">
                <div class="w-14 h-14 rounded-2xl bg-emerald-50 flex items-center justify-center text-2xl mb-8 group-hover:bg-emerald-500 group-hover:text-white transition-all">🍲</div>
                <h2 class="text-2xl font-black text-slate-900 mb-2">商品管理</h2>
                <p class="text-xs font-bold text-slate-400 leading-relaxed uppercase tracking-wider mb-8">Asset and inventory master</p>
                <div class="text-[10px] font-black text-emerald-600 uppercase tracking-widest flex items-center gap-2 italic">
                    一覧を表示 <span class="group-hover:translate-x-1 transition-transform">→</span>
                </div>
            </a>

            <!-- カテゴリー管理 -->
            <a href="Category" class="premium-card p-10 group no-underline transition-all hover:border-accent-500/50 hover:-translate-y-2 hover:shadow-2xl">
                <div class="w-14 h-14 rounded-2xl bg-accent-50 flex items-center justify-center text-2xl mb-8 group-hover:bg-accent-600 group-hover:text-white transition-all">🏷️</div>
                <h2 class="text-2xl font-black text-slate-900 mb-2">カテゴリー管理</h2>
                <p class="text-xs font-bold text-slate-400 leading-relaxed uppercase tracking-wider mb-8">Taxonomy and classification</p>
                <div class="text-[10px] font-black text-accent-600 uppercase tracking-widest flex items-center gap-2 italic">
                    設定を編集 <span class="group-hover:translate-x-1 transition-transform">→</span>
                </div>
            </a>

            <!-- 売上管理 -->
            <a href="Sales" class="premium-card p-10 group no-underline transition-all hover:border-amber-500/50 hover:-translate-y-2 hover:shadow-2xl lg:col-span-1">
                <div class="w-14 h-14 rounded-2xl bg-amber-50 flex items-center justify-center text-2xl mb-8 group-hover:bg-amber-500 group-hover:text-white transition-all">📈</div>
                <h2 class="text-2xl font-black text-slate-900 mb-2">売上分析</h2>
                <p class="text-xs font-bold text-slate-400 leading-relaxed uppercase tracking-wider mb-8">Financial performance data</p>
                <div class="text-[10px] font-black text-amber-600 uppercase tracking-widest flex items-center gap-2 italic">
                    レポートを表示 <span class="group-hover:translate-x-1 transition-transform">→</span>
                </div>
            </a>

            <!-- ユーザー管理 -->
            <a href="User" class="premium-card p-10 group no-underline transition-all hover:border-indigo-500/50 hover:-translate-y-2 hover:shadow-2xl">
                <div class="w-14 h-14 rounded-2xl bg-indigo-50 flex items-center justify-center text-2xl mb-8 group-hover:bg-indigo-600 group-hover:text-white transition-all">👥</div>
                <h2 class="text-2xl font-black text-slate-900 mb-2">ユーザー管理</h2>
                <p class="text-xs font-bold text-slate-400 leading-relaxed uppercase tracking-wider mb-8">Access and identity control</p>
                <div class="text-[10px] font-black text-indigo-600 uppercase tracking-widest flex items-center gap-2 italic">
                    設定を編集 <span class="group-hover:translate-x-1 transition-transform">→</span>
                </div>
            </a>

            <!-- スタッフ用画面へのリンク -->
            <div class="lg:col-span-3 grid grid-cols-1 md:grid-cols-3 gap-8">
                <a href="${pageContext.request.contextPath}/Kitchen/Home" class="bg-slate-900 text-white p-10 rounded-4xl group no-underline transition-all hover:bg-slate-950 flex flex-col justify-between">
                    <div class="text-[10px] font-black text-slate-500 uppercase tracking-[0.4em] mb-4 italic">Staff View</div>
                    <div class="space-y-2">
                        <h3 class="text-xl font-black tracking-tight">キッチンモニター</h3>
                        <p class="text-[10px] font-bold text-slate-600 uppercase tracking-widest">Kitchen Prep Terminal</p>
                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/Hall/Home" class="bg-slate-900 text-white p-10 rounded-4xl group no-underline transition-all hover:bg-slate-950 flex flex-col justify-between">
                    <div class="text-[10px] font-black text-slate-500 uppercase tracking-[0.4em] mb-4 italic">Staff View</div>
                    <div class="space-y-2">
                        <h3 class="text-xl font-black tracking-tight">配膳モニター</h3>
                        <p class="text-[10px] font-bold text-slate-600 uppercase tracking-widest">Hall Service Hub</p>
                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/Cashier/Home" class="bg-slate-900 text-white p-10 rounded-4xl group no-underline transition-all hover:bg-slate-950 flex flex-col justify-between">
                    <div class="text-[10px] font-black text-slate-500 uppercase tracking-[0.4em] mb-4 italic">Staff View</div>
                    <div class="space-y-2">
                        <h3 class="text-xl font-black tracking-tight">会計モニター</h3>
                        <p class="text-[10px] font-bold text-slate-600 uppercase tracking-widest">Cashier Dashboard</p>
                    </div>
                </a>
            </div>
        </main>
    </div>
</body>
</html>
