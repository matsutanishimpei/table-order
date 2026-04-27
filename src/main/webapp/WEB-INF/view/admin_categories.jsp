<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>カテゴリー管理 | 管理センター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[1200px] mx-auto px-12 py-20">
        <header class="mb-16 flex justify-between items-end">
            <div class="space-y-4">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Taxonomy System</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950">
                    カテゴリー管理<span class="text-emerald-600">.</span>
                </h1>
                <p class="text-slate-500 font-medium italic opacity-60">商品分類の登録および系統の管理</p>
            </div>
            
            <a href="Home" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-xs font-black text-slate-400 hover:text-slate-950 transition-all uppercase tracking-widest no-underline">
                管理ホームへ戻る
            </a>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="mb-12 animate-fadeIn p-5 rounded-2xl bg-emerald-50 border border-emerald-100 text-emerald-600 flex items-center gap-4 text-xs font-bold uppercase tracking-wide">
                <c:choose>
                    <c:when test="${param.msg == 'success'}"><span>✅</span> カテゴリーが正常に保存されました。</c:when>
                    <c:otherwise><span>⚠️</span> 処理中にエラーが発生しました。</c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <div class="grid grid-cols-1 gap-12">
            <!-- 新規登録エリア: Products には無いが Admin の一貫性を重視 -->
            <section class="premium-card p-10 bg-white border border-slate-100 shadow-xl rounded-3xl">
                <header class="mb-8">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] mb-2 font-mono italic">Quick Register</h2>
                    <h3 class="text-2xl font-black text-slate-900 tracking-tighter">新規カテゴリー追加</h3>
                </header>
                <form action="Category" method="post" class="flex flex-col md:flex-row gap-6 items-end">
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    <div class="flex-1 space-y-3 w-full">
                        <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">カテゴリー名</label>
                        <input type="text" name="name" required placeholder="例: おつまみ、ドリンクなど" maxlength="50"
                            class="w-full px-6 py-4 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-lg font-black shadow-inner">
                    </div>
                    <button type="submit" class="px-10 py-5 bg-emerald-600 text-white rounded-2xl font-bold transition-all hover:bg-emerald-700 active:scale-95 shadow-lg shadow-emerald-600/20 text-xs tracking-widest uppercase">
                        追加を実行
                    </button>
                </form>
            </section>

            <!-- 一覧エリア: admin_products.jsp の構造を完全踏襲 -->
            <main class="premium-card bg-white overflow-hidden shadow-2xl border-none">
                <header class="data-header bg-slate-50/50">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none">Registered Categories</h2>
                    <div class="flex items-center gap-4">
                        <span class="text-[10px] font-black uppercase text-slate-400 border border-slate-200 px-3 py-1 rounded-full">${categoryList.size()} <span class="opacity-40">Groups</span></span>
                    </div>
                </header>

                <div class="overflow-x-auto">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="text-[10px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-50">
                                <th class="px-10 py-6 w-32">ID</th>
                                <th class="px-10 py-6">カテゴリー名</th>
                                <th class="px-10 py-6 text-right w-40">属性</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-50">
                            <c:forEach var="cat" items="${categoryList}">
                                <tr class="group hover:bg-slate-50/50 transition-colors">
                                    <td class="px-10 py-8">
                                        <div class="text-[11px] font-black text-slate-300 font-mono italic">#${cat.id}</div>
                                    </td>
                                    <td class="px-10 py-8">
                                        <div class="text-lg font-black text-slate-900 leading-tight group-hover:text-emerald-600 transition-colors">
                                            <c:out value="${cat.name}" />
                                        </div>
                                    </td>
                                    <td class="px-10 py-8 text-right">
                                        <div class="flex items-center justify-end gap-2">
                                            <span class="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full bg-emerald-50 text-emerald-600 text-[9px] font-black uppercase tracking-widest border border-emerald-100 italic">
                                                <span class="w-1 h-1 bg-emerald-500 rounded-full"></span> Internal Node
                                            </span>
                                            <a href="Category?action=edit&id=${cat.id}" class="inline-flex items-center justify-center p-2.5 rounded-xl bg-slate-50 text-slate-400 hover:text-emerald-600 hover:bg-emerald-50 transition-all no-underline border border-transparent hover:border-emerald-100" title="編集">
                                                <span class="text-base">⚙️</span>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty categoryList}">
                                <tr>
                                    <td colspan="3" class="px-10 py-32 text-center">
                                        <p class="text-xs font-black text-slate-300 uppercase tracking-[0.5em] italic leading-relaxed">No taxonomy assets detected.</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>

    <style>
        .animate-fadeIn { animation: fadeIn 0.5s ease-out; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
