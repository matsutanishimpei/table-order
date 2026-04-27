<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="util.CloudinaryUtil" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>${product.name} | 詳細 | テーブルオーダーシステム</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-50 font-sans antialiased text-slate-900 min-h-screen">
    <div class="max-w-[1000px] mx-auto px-6 py-20">
        <nav class="mb-12">
            <a href="Menu?categoryId=${selectedCategoryId}" class="inline-flex items-center gap-2 text-xs font-bold text-slate-400 hover:text-primary-600 transition-colors no-underline uppercase tracking-widest">
                <span class="text-lg">←</span> おしながきに戻る
            </a>
        </nav>

        <main class="grid grid-cols-1 md:grid-cols-2 gap-16 items-start">
            <!-- 商品画像セクション -->
            <section class="relative aspect-square rounded-[3rem] overflow-hidden bg-white shadow-2xl shadow-slate-200 group">
                <c:choose>
                    <c:when test="${not empty product.imagePath}">
                        <img src="<%= util.CloudinaryUtil.staticGetResizedUrl(((model.Product)request.getAttribute("product")).getImagePath(), 800, 600) %>" 
                             alt="${product.name}" class="w-full h-full object-cover relative z-10" loading="lazy">
                    </c:when>
                    <c:otherwise>
                        <div class="absolute inset-0 flex items-center justify-center text-8xl bg-slate-50">
                            <span class="grayscale opacity-20">🍲</span>
                        </div>
                    </c:otherwise>
                </c:choose>
                
                <c:if test="${!product.available}">
                    <div class="absolute inset-0 bg-slate-950/70 backdrop-blur-md flex items-center justify-center z-20">
                        <span class="text-xl font-black text-white border-2 border-white/20 px-10 py-4 rounded-full tracking-widest">売り切れ</span>
                    </div>
                </c:if>
            </section>

            <!-- 商品情報セクション -->
            <section class="space-y-12">
                <div class="space-y-4">
                    <div class="flex items-center gap-3">
                        <span class="w-1.5 h-1.5 bg-primary-600 rounded-full"></span>
                        <span class="text-[10px] font-black text-primary-600 uppercase tracking-widest leading-none opacity-60">Product Excellence</span>
                    </div>
                    <h1 class="text-5xl font-black text-slate-950 tracking-tighter leading-tight">
                        <c:out value="${product.name}" />
                    </h1>
                    <div class="flex items-baseline gap-2">
                        <span class="text-4xl font-black text-slate-950 tracking-tighter">
                            <fmt:formatNumber value="${product.price}" />
                        </span>
                        <span class="text-slate-400 font-bold text-sm">円 (税込)</span>
                    </div>
                </div>

                <div class="prose prose-slate prose-sm">
                    <h4 class="text-[10px] font-black text-slate-400 uppercase tracking-[0.3em] mb-4">お料理の紹介</h4>
                    <p class="text-slate-600 leading-relaxed font-medium">
                        <c:out value="${product.description}" />
                    </p>
                </div>

                <c:if test="${not empty product.allergyInfo}">
                    <div class="p-6 bg-red-50/50 border border-red-100 rounded-2xl space-y-2">
                        <h4 class="text-[10px] font-black text-red-500 uppercase tracking-widest">アレルギー情報</h4>
                        <p class="text-xs font-bold text-red-800 tracking-wide"><c:out value="${product.allergyInfo}" /></p>
                    </div>
                </c:if>

                <form action="Cart" method="post" class="space-y-10 pt-4">
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                    
                    <div class="space-y-4">
                        <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">数量を選択</label>
                        <div class="flex items-center gap-4">
                            <input type="number" name="quantity" value="1" min="1" max="20" required
                                class="w-32 px-6 py-4 bg-white border border-slate-200 rounded-2xl focus:outline-none focus:border-primary-500 transition-all font-black text-xl text-center shadow-sm">
                            <span class="text-slate-400 font-bold text-sm">点</span>
                        </div>
                    </div>

                    <button type="submit" class="btn-primary w-full py-6 text-base tracking-[0.3em] shadow-2xl shadow-primary-600/30 group" ${!product.available ? 'disabled' : ''}>
                        カートに追加する <span class="ml-2 group-hover:translate-x-1 transition-transform inline-block">+</span>
                    </button>
                </form>
            </section>
        </main>

        <section class="mt-32 pt-20 border-t border-slate-100">
            <h3 class="text-center text-[10px] font-black text-slate-300 uppercase tracking-[0.5em] mb-12">Related Items</h3>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-8 opacity-60">
                <div class="premium-card aspect-video border-dashed flex items-center justify-center text-2xl">🍲</div>
                <div class="premium-card aspect-video border-dashed flex items-center justify-center text-2xl">🥬</div>
                <div class="premium-card aspect-video border-dashed flex items-center justify-center text-2xl">🥩</div>
                <div class="premium-card aspect-video border-dashed flex items-center justify-center text-2xl">🍵</div>
            </div>
        </section>
    </div>
</body>
</html>
