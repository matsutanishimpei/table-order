<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="util.CloudinaryUtil" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>おしながき | テーブルオーダーシステム</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-50 font-sans antialiased text-slate-900 min-h-screen">
    <!-- 背景デコレーション -->
    <div class="fixed inset-0 -z-10 overflow-hidden pointer-events-none">
        <div class="absolute -top-[10%] -left-[10%] w-[40%] h-[40%] bg-primary-50 rounded-full blur-[120px] opacity-50"></div>
        <div class="absolute top-[40%] -right-[5%] w-[30%] h-[30%] bg-slate-100 rounded-full blur-[100px] opacity-60"></div>
    </div>

    <!-- グローバルナビゲーション -->
    <nav class="fixed top-8 left-1/2 -translate-x-1/2 z-50 w-full max-w-[600px] px-6">
        <div class="glass-panel py-3 px-8 flex items-center justify-between border-white/40 shadow-2xl shadow-slate-200/50">
            <div class="flex items-center gap-6">
                <a href="Menu" class="text-[10px] font-black uppercase tracking-widest text-primary-600 no-underline flex items-center gap-2">
                    <span class="w-2 h-2 bg-primary-600 rounded-full"></span> おしながき
                </a>
                <a href="OrderHistory" class="text-[10px] font-black uppercase tracking-widest text-slate-400 hover:text-slate-900 transition-colors no-underline">
                    注文履歴
                </a>
            </div>
            
            <div class="flex items-center gap-4 border-l border-slate-100 pl-6">
                <div class="flex items-center gap-2">
                    <span class="text-xl">🛒</span>
                    <span class="text-xs font-black text-slate-900">
                        <c:set var="totalItems" value="0" />
                        <c:forEach var="item" items="${sessionScope.cart}">
                            <c:set var="totalItems" value="${totalItems + item.quantity}" />
                        </c:forEach>
                        ${totalItems}
                    </span>
                </div>
            </div>
        </div>
    </nav>

    <div class="max-w-[1200px] mx-auto px-6 py-16">
        <header class="mb-20 text-center space-y-4">
            <div class="flex items-center justify-center gap-3 mb-2">
                <span class="w-10 h-[1px] bg-slate-200"></span>
                <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.5em] leading-none italic">Traditional Excellence</span>
                <span class="w-10 h-[1px] bg-slate-200"></span>
            </div>
            <h1 class="text-7xl font-black tracking-tighter text-slate-950">
                おしながき<span class="text-primary-600">.</span>
            </h1>
            <p class="text-slate-500 font-medium italic opacity-60 tracking-wide">厳選された素材と、職人の技が織りなす極上のひととき</p>
        </header>

        <!-- カテゴリーナビゲーション -->
        <nav class="mb-16">
            <div class="flex flex-wrap justify-center gap-3">
                <a href="Menu" class="px-8 py-3.5 rounded-full text-xs font-black uppercase tracking-widest transition-all no-underline ${empty selectedCategoryId ? 'bg-slate-950 text-white shadow-xl shadow-slate-900/20' : 'bg-white text-slate-400 hover:text-slate-950 hover:bg-slate-50 border border-slate-100'}">
                    すべて
                </a>
                <c:forEach var="cat" items="${categoryList}">
                    <a href="Menu?categoryId=${cat.id}" 
                       class="px-8 py-3.5 rounded-full text-xs font-black uppercase tracking-widest transition-all no-underline ${selectedCategoryId == cat.id ? 'bg-primary-600 text-white shadow-xl shadow-primary-600/30' : 'bg-white text-slate-400 hover:text-slate-950 hover:bg-slate-50 border border-slate-100'}">
                        <c:out value="${cat.name}" />
                    </a>
                </c:forEach>
            </div>
        </nav>

        <!-- 商品一覧 -->
        <main class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
            <c:forEach var="p" items="${productList}">
                <a href="Product?id=${p.id}&categoryId=${selectedCategoryId}" class="group no-underline block">
                    <article class="premium-card bg-white p-0 overflow-hidden shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border-none relative">
                        <c:if test="${!p.isAvailable}">
                            <div class="absolute inset-0 bg-slate-950/60 backdrop-blur-[2px] z-20 flex items-center justify-center">
                                <span class="px-6 py-2 border border-white/30 rounded-full text-[10px] font-black text-white uppercase tracking-widest">売り切れ</span>
                            </div>
                        </c:if>
                        
                        <!-- 商品画像 -->
                        <div class="aspect-[4/3] bg-slate-100 flex items-center justify-center text-5xl group-hover:scale-105 transition-transform duration-700 overflow-hidden relative">
                            <c:choose>
                                <c:when test="${not empty p.imagePath}">
                                    <img src="<%= util.CloudinaryUtil.staticGetResizedUrl(((model.Product)pageContext.getAttribute("p")).getImagePath(), 400, 300) %>" 
                                         alt="${p.name}" class="w-full h-full object-cover" loading="lazy">
                                </c:when>
                                <c:otherwise>
                                    <span class="opacity-80 translate-y-2">🍲</span>
                                </c:otherwise>
                            </c:choose>
                            <div class="absolute inset-0 bg-gradient-to-t from-slate-950/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity"></div>
                        </div>

                        <!-- 商品情報 -->
                        <div class="p-8 space-y-4">
                            <div class="flex justify-between items-start gap-4">
                                <h2 class="text-xl font-black text-slate-900 group-hover:text-primary-600 transition-colors leading-tight tracking-tight">
                                    <c:out value="${p.name}" />
                                </h2>
                                <div class="flex items-baseline gap-0.5 font-black text-slate-950">
                                    <span class="text-[10px] italic opacity-30">¥</span>
                                    <span class="text-2xl tracking-tighter"><fmt:formatNumber value="${p.price}" /></span>
                                </div>
                            </div>
                            <p class="text-slate-400 text-xs font-medium leading-relaxed line-clamp-2 italic opacity-80 group-hover:opacity-100 transition-opacity">
                                <c:out value="${p.description}" />
                            </p>
                            <div class="pt-2 flex items-center justify-between">
                                <span class="text-[9px] font-black text-primary-600 uppercase tracking-widest opacity-0 group-hover:opacity-100 group-hover:translate-x-1 transition-all">View Details →</span>
                                <c:if test="${not empty p.allergyInfo}">
                                    <span class="w-1.5 h-1.5 bg-red-400 rounded-full animate-pulse" title="アレルギー情報あり"></span>
                                </c:if>
                            </div>
                        </div>
                    </article>
                </a>
            </c:forEach>
        </main>

        <c:if test="${empty productList}">
            <div class="py-40 text-center space-y-6 bg-white/50 rounded-[3rem] border border-dashed border-slate-200">
                <div class="text-6xl grayscale opacity-20">🍱</div>
                <p class="text-slate-400 font-black text-xs uppercase tracking-[0.5em] italic">No items found in this category.</p>
            </div>
        </c:if>

        <!-- カートサマリー（画面下部） -->
        <c:if test="${not empty sessionScope.cart}">
            <div class="mt-32 p-12 bg-slate-900 rounded-[3rem] text-white shadow-2xl relative overflow-hidden group">
                <div class="absolute -right-10 -bottom-10 text-[10rem] font-black text-white/5 italic select-none pointer-events-none group-hover:scale-110 transition-transform duration-700">CART</div>
                <div class="relative z-10 flex flex-col md:flex-row justify-between items-center gap-8">
                    <div class="space-y-4">
                        <h3 class="text-[10px] font-black text-primary-400 uppercase tracking-[0.5em]">Current Order Session</h3>
                        <p class="text-2xl font-black tracking-tight">カート内に ${totalItems} 点の商品が入っています。</p>
                        <div class="flex gap-4">
                            <form action="Cart" method="post" class="inline">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="text-[10px] font-black text-slate-500 hover:text-white uppercase tracking-widest transition-colors">
                                    カートを空にする
                                </button>
                            </form>
                        </div>
                    </div>
                    <form action="Order" method="post" class="w-full md:w-auto">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <button type="submit" class="btn-primary w-full md:w-auto bg-primary-500 hover:bg-primary-400 text-white shadow-primary-500/20">
                            注文を確定する (ORDER NOW)
                        </button>
                    </form>
                </div>
            </div>
        </c:if>
    </div>

    <footer class="mt-40 py-20 border-t border-slate-100 text-center">
        <p class="text-[9px] font-black text-slate-300 uppercase tracking-[0.8em] italic">Table Order System Premium</p>
    </footer>
</body>
</html>
