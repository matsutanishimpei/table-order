<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page import="util.CloudinaryUtil" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>商品管理 | 管理センター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[1200px] mx-auto px-12 py-20">
        <header class="mb-16 flex justify-between items-end">
            <div class="space-y-4">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Asset Catalog System</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950">
                    商品管理<span class="text-emerald-600">.</span>
                </h1>
                <p class="text-slate-500 font-medium italic opacity-60">メニュー商品の登録・編集および在庫ステータスの制御</p>
            </div>
            
            <a href="Home" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-xs font-black text-slate-400 hover:text-slate-950 transition-all uppercase tracking-widest no-underline">
                管理ホームへ戻る
            </a>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="mb-12 animate-fadeIn p-5 rounded-2xl bg-emerald-50 border border-emerald-100 text-emerald-600 flex items-center gap-4 text-xs font-bold uppercase tracking-wide">
                <span>✅</span> データベースの更新が正常に完了しました。
            </div>
        </c:if>

        <main class="premium-card bg-white overflow-hidden shadow-2xl border-none">
            <header class="data-header bg-slate-50/50">
                <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none">Registered Products</h2>
                <div class="flex items-center gap-6">
                    <span class="text-[10px] font-black uppercase text-slate-400 border border-slate-200 px-3 py-1 rounded-full">${productList.size()} <span class="opacity-40">Items</span></span>
                    <a href="Product?action=add" class="inline-flex items-center gap-2 px-5 py-2.5 bg-emerald-600 text-white text-[10px] font-black rounded-xl hover:bg-emerald-700 transition-all uppercase tracking-widest no-underline shadow-lg shadow-emerald-600/20">
                        <span>+</span> 新規商品を追加
                    </a>
                </div>
            </header>

            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse">
                    <thead>
                        <tr class="text-[10px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-50">
                            <th class="px-10 py-6 w-24 text-center">画像</th>
                            <th class="px-10 py-6">商品名 / ID</th>
                            <th class="px-6 py-6">カテゴリー</th>
                            <th class="px-6 py-6 text-right">単価</th>
                            <th class="px-10 py-6 text-center w-32">ステータス</th>
                            <th class="px-10 py-6 text-right w-40">操作</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-50">
                        <c:forEach var="p" items="${productList}">
                            <tr class="group hover:bg-slate-50/50 transition-colors">
                                <td class="px-10 py-8 text-center">
                                    <div class="w-16 h-16 rounded-2xl bg-slate-100 border border-slate-200 overflow-hidden mx-auto flex items-center justify-center text-2xl group-hover:scale-105 transition-transform duration-500">
                                        <c:choose>
                                            <c:when test="${not empty p.imagePath}">
                                                <img src="<%= util.CloudinaryUtil.staticGetResizedUrl(((model.Product)pageContext.getAttribute("p")).imagePath(), 200, 200) %>" 
                                                     alt="<c:out value='${p.name}' />" class="w-full h-full object-cover" loading="lazy">
                                            </c:when>
                                            <c:otherwise>🍲</c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                                <td class="px-10 py-8">
                                    <div class="text-lg font-black text-slate-900 leading-tight group-hover:text-emerald-600 transition-colors">
                                        <c:out value="${p.name}" />
                                    </div>
                                    <div class="text-[9px] font-black text-slate-300 uppercase tracking-[0.2em] mt-1 font-mono italic">#${p.id}</div>
                                </td>
                                <td class="px-6 py-8">
                                    <span class="px-3 py-1 rounded-lg bg-slate-100 text-[10px] font-black text-slate-500 uppercase tracking-widest leading-none border border-slate-200/50">
                                        <c:forEach var="cat" items="${categoryList}">
                                            <c:if test="${p.categoryId == cat.id}"><c:out value="${cat.name}" /></c:if>
                                        </c:forEach>
                                    </span>
                                </td>
                                <td class="px-6 py-8 text-right">
                                    <div class="flex items-baseline justify-end gap-1 font-black">
                                        <span class="text-xs text-slate-300 italic">¥</span>
                                        <span class="text-xl text-slate-900 tracking-tighter"><fmt:formatNumber value="${p.price}" /></span>
                                    </div>
                                </td>
                                <td class="px-10 py-8 text-center">
                                    <c:choose>
                                        <c:when test="${p.isAvailable}">
                                            <span class="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full bg-emerald-50 text-emerald-600 text-[9px] font-black uppercase tracking-widest border border-emerald-100 italic">
                                                <span class="w-1 h-1 bg-emerald-500 rounded-full"></span> Active
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full bg-red-50 text-red-400 text-[9px] font-black uppercase tracking-widest border border-red-100 italic">
                                                <span class="w-1 h-1 bg-red-400 rounded-full"></span> Disabled
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-10 py-8 text-right">
                                    <div class="flex items-center justify-end gap-2">
                                        <a href="Product?action=edit&id=${p.id}" class="inline-flex items-center justify-center p-3 rounded-xl bg-slate-50 text-slate-400 hover:text-emerald-600 hover:bg-emerald-50 transition-all no-underline border border-transparent hover:border-emerald-100" title="編集">
                                            <span class="text-lg">⚙️</span>
                                        </a>
                                        <form action="Product" method="post" class="inline" onsubmit="return confirm('「<c:out value="${p.name}" />」を削除しますか？\nこの操作は論理削除です。データは保持されます。');">
                                            <input type="hidden" name="csrf_token" value="${csrf_token}">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <button type="submit" class="inline-flex items-center justify-center p-3 rounded-xl bg-slate-50 text-slate-400 hover:text-red-500 hover:bg-red-50 transition-all border border-transparent hover:border-red-100" title="削除">
                                                <span class="text-lg">🗑️</span>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty productList}">
                            <tr>
                                <td colspan="6" class="px-10 py-32 text-center">
                                    <p class="text-xs font-black text-slate-300 uppercase tracking-[0.5em] italic leading-relaxed">No culinary assets detected.<br>Product matrix is void.</p>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </main>
    </div>

    <style>
        .animate-fadeIn { animation: fadeIn 0.5s ease-out; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
