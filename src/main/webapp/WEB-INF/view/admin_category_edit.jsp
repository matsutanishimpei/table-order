<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>カテゴリー編集 | 管理センター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[800px] mx-auto px-12 py-20">
        <nav class="mb-12">
            <a href="Category" class="inline-flex items-center gap-2 text-xs font-black text-slate-400 hover:text-emerald-600 transition-colors no-underline uppercase tracking-[0.2em]">
                <span class="text-lg">←</span> カテゴリー一覧に戻る
            </a>
        </nav>

        <header class="mb-16">
            <div class="flex items-center gap-3 mb-4">
                <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full"></span>
                <span class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none italic">Taxonomy Editor</span>
            </div>
            <h1 class="text-6xl font-black tracking-tighter text-slate-950 leading-tight">
                カテゴリーの編集<span class="text-emerald-600">.</span>
            </h1>
            <p class="text-slate-500 font-medium italic opacity-60">カテゴリー名称の変更</p>
        </header>

        <c:if test="${param.msg == 'error'}">
            <div class="mb-10 p-5 rounded-2xl bg-red-50 border border-red-100 text-red-600 flex items-center gap-4 animate-fadeIn">
                <span class="text-lg">⚠️</span>
                <span class="text-xs font-bold uppercase tracking-wide">更新中にエラーが発生しました。入力内容を確認してください。</span>
            </div>
        </c:if>

        <main class="premium-card p-16 shadow-2xl border-none bg-white relative overflow-hidden">
            <div class="absolute -top-10 -right-10 text-[10rem] font-black text-slate-50 italic opacity-5 pointer-events-none select-none italic">
                #${category.id}
            </div>

            <form action="Category" method="post" class="relative z-10 space-y-12">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${category.id}">
                
                <div class="space-y-4">
                    <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">カテゴリー名</label>
                    <input type="text" name="name" required placeholder="カテゴリー名称" maxlength="50"
                           value="<c:out value='${category.name}' />"
                           class="w-full px-8 py-6 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-xl font-black shadow-inner">
                </div>

                <footer class="pt-12 border-t border-slate-50 flex gap-6">
                    <button type="submit" class="btn-primary bg-emerald-600 hover:bg-emerald-700 flex-grow py-6 text-base tracking-[0.4em] shadow-2xl shadow-emerald-600/20">
                        変更を保存する
                    </button>
                    <a href="Category" class="inline-flex items-center justify-center px-10 py-6 bg-slate-100 text-slate-400 text-xs font-black rounded-2xl hover:bg-red-50 hover:text-red-500 transition-all uppercase tracking-widest no-underline">
                        キャンセル
                    </a>
                </footer>
            </form>
        </main>
    </div>

    <style>
        .animate-fadeIn { animation: fadeIn 0.5s ease-out; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
