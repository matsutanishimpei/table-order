<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>メニュー選択 | テーブルオーダーシステム</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-50 font-sans antialiased text-slate-900 flex h-screen overflow-hidden">
    <!-- サイドナビゲーション: カテゴリ選択 -->
    <aside class="w-72 bg-white border-r border-slate-200 flex flex-col z-30 shadow-2xl">
        <header class="p-10 border-b border-slate-100 flex flex-col gap-4">
            <div class="flex items-center gap-3">
                <div class="w-3 h-3 bg-primary-600 rounded-full animate-pulse"></div>
                <div class="text-[10px] font-bold text-primary-600 uppercase tracking-widest leading-none">Connected Table</div>
            </div>
            <h2 class="text-3xl font-black text-slate-900 tracking-tighter leading-none">
                <span class="text-slate-400 text-sm font-bold align-middle mr-1">NO.</span>${sessionScope.user.id}
            </h2>
        </header>

        <nav class="flex-grow overflow-y-auto px-4 py-8 space-y-2">
            <c:forEach var="cat" items="${categories}">
                <a href="Menu?categoryId=${cat.id}" class="nav-link ${selectedCategoryId == cat.id ? 'active' : ''} no-underline">
                    <span class="flex-grow"><c:out value="${cat.name}" /></span>
                    <c:if test="${selectedCategoryId == cat.id}">
                        <div class="w-1.5 h-1.5 bg-primary-600 rounded-full"></div>
                    </c:if>
                </a>
            </c:forEach>
        </nav>

        <footer class="p-6 border-t border-slate-100">
            <a href="OrderHistory" class="flex items-center justify-center gap-3 w-full py-4 bg-slate-50 border border-slate-200 rounded-2xl text-xs font-bold text-slate-500 hover:bg-slate-100 hover:text-slate-900 transition-all no-underline tracking-widest uppercase">
                <span class="text-lg">⏱</span> 注文履歴を確認
            </a>
        </footer>
    </aside>

    <!-- メインコンテンツ: 商品グリッド -->
    <main class="flex-grow flex flex-col relative z-10 bg-slate-50/50">
        <header class="h-28 px-12 flex items-center justify-between bg-white/80 backdrop-blur-md border-b border-slate-100 sticky top-0 z-20">
            <div>
                <h1 class="text-2xl font-black text-slate-900 tracking-tight">お品書き</h1>
                <p class="text-[10px] font-bold text-slate-400 uppercase tracking-[.2em] mt-1 italic">${products.size()} Items Available</p>
            </div>
            <div class="flex items-center gap-4">
                <c:if test="${not empty message || param.msg == 'success'}">
                    <div class="px-6 py-2.5 rounded-full bg-emerald-50 text-emerald-600 text-xs font-bold border border-emerald-100 animate-fadeIn">
                        ${not empty message ? message : '注文が確定しました！お届けまでしばらくお待ちください。'}
                    </div>
                </c:if>
                <c:if test="${not empty error || param.msg == 'error'}">
                    <div class="px-6 py-2.5 rounded-full bg-red-50 text-red-600 text-xs font-bold border border-red-100 animate-fadeIn">
                        ${not empty error ? error : '注文処理中にエラーが発生しました。'}
                    </div>
                </c:if>
            </div>
        </header>

        <div class="flex-grow overflow-y-auto p-12">
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-10">
                <c:forEach var="p" items="${products}">
                    <a href="ProductDetail?productId=${p.id}&categoryId=${selectedCategoryId}" 
                       class="premium-card group hover:-translate-y-2 hover:shadow-2xl transition-all duration-500 no-underline bg-white block">
                        <div class="aspect-[4/3] bg-slate-100 flex items-center justify-center text-5xl group-hover:scale-105 transition-transform duration-700 overflow-hidden relative">
                            <span class="opacity-80 translate-y-2">🍲</span>
                            <c:if test="${!p.available}">
                                <div class="absolute inset-0 bg-slate-950/60 backdrop-blur-sm flex items-center justify-center">
                                    <span class="text-sm font-black text-white border-2 border-white/30 px-6 py-2 rounded-full">SOLD OUT</span>
                                </div>
                            </c:if>
                        </div>
                        <div class="p-8">
                            <div class="text-[10px] font-bold text-primary-600 uppercase tracking-[0.2em] mb-2 opacity-60">Recommendation</div>
                            <h3 class="text-lg font-bold text-slate-900 mb-4 line-clamp-2 min-h-[3.5rem] group-hover:text-primary-600 transition-colors leading-snug">
                                <c:out value="${p.name}" />
                            </h3>
                            <div class="flex items-baseline gap-1">
                                <span class="text-3xl font-black text-slate-900 tracking-tighter">
                                    <fmt:formatNumber value="${p.price}" />
                                </span>
                                <span class="text-xs font-bold text-slate-400">円</span>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </div>
    </main>

    <!-- サイドパネル: カート -->
    <aside class="w-96 bg-white border-l border-slate-200 flex flex-col z-30 shadow-[-20px_0_40px_rgba(0,0,0,0.02)]">
        <header class="p-10 bg-slate-900 text-white flex justify-between items-center">
            <div>
                <div class="text-[10px] font-bold text-primary-400 uppercase tracking-[.3em] mb-1">Items in Cart</div>
                <h2 class="text-xl font-black tracking-tight italic uppercase">ご注文内容</h2>
            </div>
            <div class="w-12 h-12 rounded-2xl bg-white/5 border border-white/10 flex items-center justify-center text-xl">🛒</div>
        </header>

        <div class="flex-grow overflow-y-auto px-8 py-10 space-y-8">
            <c:choose>
                <c:when test="${not empty sessionScope.cart}">
                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${sessionScope.cart}">
                        <div class="flex gap-6 group items-center">
                            <div class="w-16 h-16 rounded-2xl bg-slate-50 flex items-center justify-center text-2xl flex-shrink-0 group-hover:bg-primary-50 transition-colors border border-slate-100">🍽️</div>
                            <div class="flex-grow space-y-1">
                                <div class="font-bold text-slate-900 text-sm leading-tight line-clamp-2">
                                    <c:out value="${item.name}" />
                                </div>
                                <div class="text-[10px] font-bold text-slate-400 uppercase tracking-widest italic">
                                    <fmt:formatNumber value="${item.unitPrice}" /> × ${item.quantity}
                                </div>
                            </div>
                            <div class="text-right space-y-2">
                                <div class="text-sm font-black text-primary-600">
                                    ¥<fmt:formatNumber value="${item.subtotal}" />
                                </div>
                                <form action="Cart" method="post">
                                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                                    <button type="submit" class="text-[9px] font-bold text-red-400 hover:text-red-600 uppercase tracking-widest transition-colors font-mono">Remove</button>
                                </form>
                            </div>
                        </div>
                        <c:set var="total" value="${total + item.subtotal}" />
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="h-full flex flex-col items-center justify-center text-center opacity-30 py-20 grayscale">
                        <div class="text-6xl mb-8">🍲</div>
                        <p class="font-bold text-[10px] uppercase tracking-[0.4em] text-slate-400">カートは空です</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <footer class="p-10 bg-slate-50 border-t border-slate-100 space-y-8">
            <div class="flex justify-between items-end">
                <div class="text-[10px] font-bold text-slate-400 uppercase tracking-[0.4em]">合計金額 (税込)</div>
                <div class="flex items-baseline gap-1">
                    <span class="text-5xl font-black text-slate-900 tracking-tighter">
                        <fmt:formatNumber value="${total != null ? total : 0}" />
                    </span>
                    <span class="text-slate-400 font-bold text-sm">円</span>
                </div>
            </div>
            <form action="Order" method="post">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <button type="submit" class="btn-primary w-full py-6 text-base tracking-[0.3em] shadow-2xl shadow-primary-600/30 group" ${empty sessionScope.cart ? 'disabled' : ''}>
                    注文を確定する <span class="ml-2 group-hover:translate-x-1 transition-transform inline-block">→</span>
                </button>
            </form>
        </footer>
    </aside>

    <style>
        .animate-fadeIn { animation: fadeIn 0.5s ease-out forwards; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-5px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
