<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>商品編集 | 管理センター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[900px] mx-auto px-12 py-20">
        <nav class="mb-12">
            <a href="Product" class="inline-flex items-center gap-2 text-xs font-black text-slate-400 hover:text-emerald-600 transition-colors no-underline uppercase tracking-[0.2em]">
                <span class="text-lg">←</span> 商品一覧に戻る
            </a>
        </nav>

        <header class="mb-16">
            <div class="flex items-center gap-3 mb-4">
                <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full"></span>
                <span class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none italic">Asset Revision Module</span>
            </div>
            <h1 class="text-6xl font-black tracking-tighter text-slate-950 leading-tight">
                商品の${product.id == 0 ? '登録' : '編集'}<span class="text-emerald-600">.</span>
            </h1>
            <p class="text-slate-500 font-medium italic opacity-60">商品情報の${product.id == 0 ? '新規登録' : '修正'}および掲載ステータスの更新</p>
        </header>

        <c:choose>
            <c:when test="${param.msg == 'invalid_format'}">
                <div class="mb-10 p-5 rounded-2xl bg-red-50 border border-red-100 text-red-600 flex items-center gap-4 animate-fadeIn">
                    <span class="text-lg">⚠️</span>
                    <span class="text-xs font-bold uppercase tracking-wide">許可されていないファイル形式です。JPEG, PNG, WEBP, GIFのみアップロード可能です。</span>
                </div>
            </c:when>
            <c:when test="${param.msg == 'upload_failed'}">
                <div class="mb-10 p-5 rounded-2xl bg-red-50 border border-red-100 text-red-600 flex items-center gap-4 animate-fadeIn">
                    <span class="text-lg">⚠️</span>
                    <span class="text-xs font-bold uppercase tracking-wide">画像アップロードに失敗しました。Cloudinaryの設定またはネットワークを確認してください。</span>
                </div>
            </c:when>
            <c:when test="${not empty param.msg}">
                <div class="mb-10 p-5 rounded-2xl bg-red-50 border border-red-100 text-red-600 flex items-center gap-4 animate-fadeIn">
                    <span class="text-lg">⚠️</span>
                    <span class="text-xs font-bold uppercase tracking-wide">Validation error. 入力内容を確認してください (ID: ${product.id})</span>
                </div>
            </c:when>
        </c:choose>

        <main class="premium-card p-16 shadow-2xl border-none bg-white relative overflow-hidden">
            <!-- Background ID Decoration -->
            <div class="absolute -top-10 -right-10 text-[10rem] font-black text-slate-50 italic opacity-5 pointer-events-none select-none italic">
                #${product.id == 0 ? 'NEW' : product.id}
            </div>

            <form action="Product" method="post" enctype="multipart/form-data" class="relative z-10 space-y-12">
                <input type="hidden" name="csrf_token" value="${csrf_token}">
                <input type="hidden" name="id" value="${product.id}">
                
                <section class="space-y-10">
                    <div class="space-y-4">
                        <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">商品タイトル</label>
                        <input type="text" name="name" required placeholder="メニューに表示される名称" maxlength="100"
                               value="<c:out value='${product.name}' />"
                               class="w-full px-8 py-6 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-xl font-black shadow-inner">
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-10">
                        <div class="space-y-4">
                            <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">カテゴリー</label>
                            <div class="relative">
                                <select name="categoryId" required
                                        class="w-full px-8 py-6 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-sm font-black appearance-none shadow-inner h-full cursor-pointer">
                                    <c:forEach var="cat" items="${categoryList}">
                                        <option value="${cat.id}" ${product.categoryId == cat.id ? 'selected' : ''}>
                                            <c:out value="${cat.name}" />
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="absolute right-8 top-1/2 -translate-y-1/2 pointer-events-none text-slate-400">▼</div>
                            </div>
                        </div>
                        <div class="space-y-4">
                            <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">販売価格 (単位: 円)</label>
                            <div class="relative h-full">
                                <input type="number" name="price" required min="0" 
                                       value="${product.price}"
                                       class="w-full px-8 py-6 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-2xl font-black shadow-inner h-full">
                                <span class="absolute right-8 top-1/2 -translate-y-1/2 text-slate-300 font-bold italic">JPY</span>
                            </div>
                        </div>
                    </div>

                    <div class="space-y-4">
                        <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">商品説明 / meta</label>
                        <textarea name="description" rows="5" placeholder="商品のおすすめポイントや特徴を記述してください..."
                                  class="w-full px-8 py-6 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-sm font-medium resize-none shadow-inner leading-relaxed"><c:out value="${product.description}" /></textarea>
                    </div>

                    <div class="space-y-4">
                        <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">アレルギー情報 / 備考</label>
                        <input type="text" name="allergyInfo" placeholder="卵、乳、小麦など（不要な場合は空欄）"
                               value="<c:out value='${product.allergyInfo}' />"
                               class="w-full px-8 py-6 bg-slate-50 border border-slate-200 rounded-2xl focus:outline-none focus:border-emerald-500 focus:bg-white transition-all text-sm font-black shadow-inner">
                    </div>

                    <div class="space-y-4">
                        <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest ml-1">商品画像 (Cloudinary)</label>
                        <div class="flex items-center gap-8">
                            <div class="w-32 h-32 rounded-3xl bg-slate-100 border border-slate-200 overflow-hidden flex items-center justify-center text-4xl">
                                <c:choose>
                                    <c:when test="${not empty product.imagePath}">
                                        <img src="${product.imagePath}" alt="Current" class="w-full h-full object-cover">
                                    </c:when>
                                    <c:otherwise>🍲</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="flex-grow space-y-3">
                                <input type="file" name="imageFile" accept="image/*"
                                       class="block w-full text-xs text-slate-500 file:mr-4 file:py-3 file:px-6 file:rounded-full file:border-0 file:text-[10px] file:font-black file:uppercase file:tracking-widest file:bg-emerald-50 file:text-emerald-700 hover:file:bg-emerald-100 transition-all cursor-pointer">
                                <p class="text-[9px] font-bold text-slate-400 uppercase tracking-widest italic leading-none">Recommended size: 800x600px. Max 10MB.</p>
                            </div>
                        </div>
                    </div>

                    <div class="p-8 bg-[#f8fafc] rounded-3xl border border-slate-100 flex items-center justify-between">
                        <div class="space-y-1">
                            <span class="text-xs font-black text-slate-900 uppercase tracking-widest">お品書きに表示する</span>
                            <p class="text-[9px] font-bold text-slate-400 uppercase tracking-widest italic leading-none">Catalog Availability Status</p>
                        </div>
                        <label class="relative inline-flex items-center cursor-pointer group">
                            <input type="checkbox" name="isAvailable" ${product.available ? 'checked' : ''} class="sr-only peer">
                            <div class="w-14 h-8 bg-slate-200 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-1 after:left-1 after:bg-white after:border-slate-300 after:border after:rounded-full after:h-6 after:w-6 after:transition-all peer-checked:bg-emerald-500"></div>
                        </label>
                    </div>
                </section>

                <footer class="pt-12 border-t border-slate-50 flex gap-6">
                    <button type="submit" class="btn-primary bg-emerald-600 hover:bg-emerald-700 flex-grow py-6 text-base tracking-[0.4em] shadow-2xl shadow-emerald-600/20">
                        ${product.id == 0 ? '新規登録を実行' : '変更を保存する'}
                    </button>
                    <a href="Product" class="inline-flex items-center justify-center px-10 py-6 bg-slate-100 text-slate-400 text-xs font-black rounded-2xl hover:bg-red-50 hover:text-red-500 transition-all uppercase tracking-widest no-underline">
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
