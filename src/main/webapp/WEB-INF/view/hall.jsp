<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>配膳管理 | ホールスタッフコンソール</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-950 font-sans antialiased text-slate-100 min-h-screen">
    <div class="px-8 py-10">
        <header class="mb-12 flex justify-between items-end border-b border-white/5 pb-10">
            <div class="space-y-3">
                <div class="flex items-center gap-3">
                    <span class="w-3 h-3 bg-emerald-500 rounded-full animate-float shadow-[0_0_15px_rgba(16,185,129,0.5)]"></span>
                    <span class="text-[10px] font-black text-emerald-500 uppercase tracking-[0.4em] leading-none italic">Delivery Hub</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-white">
                    配膳待ち一覧<span class="text-emerald-500">.</span>
                </h1>
                <p class="text-slate-500 font-bold italic text-sm">調理完了済み商品の配膳ステータス管理</p>
            </div>
            
            <div class="bg-white/5 border border-white/10 px-8 py-6 rounded-3xl backdrop-blur-md text-center">
                <div class="text-[10px] font-black text-slate-500 uppercase tracking-widest mb-1">配膳待ち</div>
                <div class="text-3xl font-black text-emerald-500 tracking-tighter">${readyItems.size()} <span class="text-sm text-slate-600">件</span></div>
            </div>
        </header>

        <main class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-8">
            <c:forEach var="item" items="${readyItems}">
                <div class="bg-slate-900 border border-white/10 rounded-4xl p-8 relative overflow-hidden group hover:border-emerald-500/50 transition-all duration-500">
                    <!-- Table Identification -->
                    <div class="absolute -right-6 -top-6 text-9xl font-black text-white/5 italic select-none group-hover:text-emerald-500/10 transition-colors">
                        #${item.tableName}
                    </div>

                    <header class="relative z-10 flex justify-between items-start mb-8">
                        <div>
                            <p class="text-[10px] font-black text-emerald-500 uppercase tracking-widest leading-none mb-2 italic">Table Destination</p>
                            <h2 class="text-5xl font-black text-white tracking-tighter leading-none">${item.tableName}<span class="text-lg text-slate-600 ml-2 font-bold select-none italic">席</span></h2>
                        </div>
                    </header>

                    <div class="relative z-10 mb-10 min-h-[140px] flex flex-col justify-center">
                        <div class="text-[10px] font-black text-slate-500 uppercase tracking-[0.3em] mb-4 italic">Delivery Asset</div>
                        <h3 class="text-2xl font-black text-white leading-tight mb-2 group-hover:text-emerald-400 transition-colors">
                            <c:out value="${item.productName}" />
                        </h3>
                        <div class="flex items-center gap-3 mt-4 text-emerald-500/80 font-black text-xs uppercase tracking-widest italic">
                            <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full"></span> Ready to Serve
                        </div>
                    </div>

                    <form action="Home" method="post" class="relative z-10 pt-6 border-t border-white/5 text-center">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="serve">
                        <input type="hidden" name="itemId" value="${item.orderItemId}">
                        <div class="flex items-center justify-center gap-4">
                            <div class="px-5 py-3 bg-slate-800 rounded-xl text-xl font-black text-white border border-white/5">
                                × ${item.quantity}
                            </div>
                            <button type="submit" class="flex-grow py-5 bg-white text-slate-900 rounded-2xl font-black text-xs tracking-[0.3em] uppercase hover:bg-emerald-500 transition-all active:scale-95 shadow-[0_10px_30px_rgba(255,255,255,0.05)]">
                                配膳を完了
                            </button>
                        </div>
                    </form>
                </div>
            </c:forEach>

            <c:if test="${empty readyItems}">
                <div class="col-span-full border-2 border-dashed border-white/10 rounded-5xl py-40 flex flex-col items-center justify-center opacity-30 grayscale">
                    <div class="text-8xl mb-8 animate-float">🍽️</div>
                    <p class="text-xs font-black text-slate-500 uppercase tracking-[0.6em] italic leading-relaxed">All deliveries processed.<br>Service queue is empty.</p>
                </div>
            </c:if>
        </main>
    </div>
</body>
</html>
