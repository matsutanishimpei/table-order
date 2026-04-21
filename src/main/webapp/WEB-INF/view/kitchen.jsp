<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>調理指示 | キッチンモニター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-950 font-sans antialiased text-slate-100 min-h-screen overflow-x-hidden">
    <div class="px-8 py-10">
        <header class="mb-12 flex justify-between items-end border-b border-white/5 pb-10">
            <div class="space-y-3">
                <div class="flex items-center gap-3">
                    <span class="w-3 h-3 bg-amber-500 rounded-full animate-pulse shadow-[0_0_15px_rgba(245,158,11,0.5)]"></span>
                    <span class="text-[10px] font-black text-amber-500 uppercase tracking-[0.4em] leading-none italic">Production Queue</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-white">
                    調理指示<span class="text-amber-500">.</span>
                </h1>
                <p class="text-slate-500 font-bold italic text-sm">キッチン用高コントラスト・リアルタイムモニター</p>
            </div>
            
            <div class="flex items-center gap-8 bg-white/5 border border-white/10 px-8 py-6 rounded-3xl backdrop-blur-md">
                <div class="text-center">
                    <div class="text-[10px] font-black text-slate-500 uppercase tracking-widest mb-1">調理中</div>
                    <div class="text-3xl font-black text-amber-500 tracking-tighter">${activeItems.size()}</div>
                </div>
                <div class="w-px h-10 bg-white/10"></div>
                <div class="text-center">
                    <div class="text-[10px] font-black text-slate-500 uppercase tracking-widest mb-1">現在時刻</div>
                    <div class="text-xl font-black text-white tracking-tighter font-mono" id="clock">00:00:00</div>
                </div>
            </div>
        </header>

        <main class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-8">
            <c:forEach var="order" items="${activeItems}">
                <div class="bg-slate-900 border border-white/10 rounded-4xl p-8 relative overflow-hidden group hover:border-amber-500/50 transition-all duration-500">
                    <!-- Table Number Accent -->
                    <div class="absolute -right-6 -top-6 text-9xl font-black text-white/5 italic select-none group-hover:text-amber-500/10 transition-colors">
                        #${order.tableName}
                    </div>

                    <header class="relative z-10 flex justify-between items-start mb-8">
                        <div>
                            <p class="text-[10px] font-black text-amber-500 uppercase tracking-widest leading-none mb-2 italic">Table Identifier</p>
                            <h2 class="text-5xl font-black text-white tracking-tighter leading-none">${order.tableName}<span class="text-lg text-slate-600 ml-2 font-bold select-none italic">席</span></h2>
                        </div>
                        <div class="text-right">
                            <p class="text-[9px] font-bold text-slate-500 uppercase tracking-widest mb-1 italic">Ordered At</p>
                            <p class="text-xs font-black text-slate-400 font-mono"><fmt:formatDate value="${order.orderedAt}" pattern="HH:mm" /></p>
                        </div>
                    </header>

                    <div class="relative z-10 mb-10 min-h-[140px] flex flex-col justify-center">
                        <div class="text-[10px] font-black text-slate-500 uppercase tracking-[0.4em] mb-4 italic">Preparation Target</div>
                        <h3 class="text-2xl font-black text-white leading-tight mb-2 group-hover:text-amber-400 transition-colors">
                            <c:out value="${order.productName}" />
                        </h3>
                        <div class="flex items-center gap-4 mt-6">
                            <div class="px-6 py-2 bg-amber-500 text-slate-950 text-2xl font-black rounded-xl shadow-[0_10px_20px_rgba(245,158,11,0.2)]">
                                × ${order.quantity}
                            </div>
                        </div>
                    </div>

                    <form action="Home" method="post" class="relative z-10 pt-6 border-t border-white/5">
                        <input type="hidden" name="csrf_token" value="${csrf_token}">
                        <input type="hidden" name="action" value="complete">
                        <input type="hidden" name="itemId" value="${order.orderItemId}">
                        <button type="submit" class="w-full py-5 bg-white text-slate-900 rounded-2xl font-black text-xs tracking-[0.3em] uppercase hover:bg-amber-500 transition-all active:scale-95 shadow-xl">
                            調理完了報告
                        </button>
                    </form>
                </div>
            </c:forEach>

            <c:if test="${empty activeItems}">
                <div class="col-span-full border-2 border-dashed border-white/10 rounded-5xl py-40 flex flex-col items-center justify-center opacity-40">
                    <div class="text-8xl mb-8 animate-pulse-subtle">👨‍🍳</div>
                    <p class="text-xs font-black text-slate-500 uppercase tracking-[0.6em] italic leading-relaxed">No pending prep tasks.<br>Kitchen buffer is clear.</p>
                </div>
            </c:if>
        </main>
    </div>

    <script>
        function updateClock() {
            const now = new Date();
            const timeString = now.getHours().toString().padStart(2, '0') + ':' + 
                               now.getMinutes().toString().padStart(2, '0') + ':' + 
                               now.getSeconds().toString().padStart(2, '0');
            document.getElementById('clock').innerText = timeString;
        }
        setInterval(updateClock, 1000);
        updateClock();
    </script>
</body>
</html>
