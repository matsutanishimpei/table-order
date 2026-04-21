<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>フロア監視 | 管理モニター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900 min-h-screen">
    <div class="px-8 py-10 max-w-[1400px] mx-auto">
        <header class="mb-12 flex justify-between items-end border-b border-slate-200/60 pb-10">
            <div class="space-y-3">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse shadow-[0_0_10px_rgba(16,185,129,0.6)]"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Floor Connectivity Terminal</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950">
                    フロア監視<span class="text-emerald-600">.</span>
                </h1>
                <p class="text-slate-500 font-bold italic text-sm opacity-60">全卓の稼働状況および未精算注文のリアルタイム追跡</p>
            </div>
            
            <a href="Home" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-[10px] font-black text-slate-400 hover:text-slate-950 hover:border-slate-300 transition-all no-underline uppercase tracking-widest shadow-sm">
                管理ホームへ戻る
            </a>
        </header>

        <main class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
            <c:forEach var="table" items="${tableList}">
                <div class="premium-card bg-white p-10 border border-slate-100 shadow-xl rounded-[2.5rem] relative overflow-hidden group hover:border-emerald-200 hover:shadow-2xl hover:shadow-emerald-600/5 transition-all duration-500 min-h-[420px] flex flex-col justify-between">
                    <!-- Table ID Decoration (Background) -->
                    <div class="absolute -right-6 -bottom-6 text-9xl font-black text-slate-50 italic select-none pointer-events-none group-hover:text-emerald-50 transition-colors duration-700">#${table.tableId}</div>

                    <!-- Table Identification -->
                    <div class="relative z-10 flex justify-between items-start">
                        <div>
                            <p class="text-[10px] font-black text-emerald-600 uppercase tracking-widest leading-none mb-3 italic">Active Node</p>
                            <h2 class="text-6xl font-black text-slate-900 tracking-tighter leading-none group-hover:text-emerald-600 transition-colors">
                                <c:out value="${table.tableName}" /><span class="text-xl text-slate-300 font-bold ml-2 select-none italic">番</span>
                            </h2>
                        </div>
                        <div class="flex flex-col items-end gap-3">
                            <c:choose>
                                <c:when test="${table.statusCode != '0'}">
                                    <span class="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full bg-emerald-50 text-emerald-600 text-[9px] font-black uppercase tracking-widest border border-emerald-100 italic animate-pulse">
                                        <span class="w-1 h-1 bg-emerald-500 rounded-full"></span> <c:out value="${table.statusLabel}" />
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full bg-slate-50 text-slate-400 text-[9px] font-black uppercase tracking-widest border border-slate-100 italic">
                                        <span class="w-1 h-1 bg-slate-300 rounded-full"></span> <c:out value="${table.statusLabel}" />
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Statistics Panel -->
                    <div class="relative z-10 space-y-8 mt-12 bg-slate-50/80 p-8 rounded-[2rem] border border-slate-100 shadow-inner group-hover:bg-white group-hover:border-emerald-100 transition-all duration-300">
                        <div class="grid grid-cols-2 gap-6">
                            <div class="space-y-1">
                                <div class="text-[10px] font-black text-slate-400 uppercase tracking-widest italic opacity-80">Bill Total</div>
                                <div class="text-2xl font-black text-slate-900 tracking-tighter font-mono">
                                    <span class="text-xs text-slate-300 mr-0.5">¥</span><fmt:formatNumber value="${table.totalAmount}" />
                                </div>
                            </div>
                            <div class="space-y-1 text-right border-l border-slate-200/60 pl-6">
                                <div class="text-[10px] font-black text-slate-400 uppercase tracking-widest italic opacity-80">Dish Count</div>
                                <div class="text-2xl font-black text-emerald-600">
                                    ${table.orderCount} <span class="text-[10px] text-slate-400 font-bold lowercase">items</span>
                                </div>
                            </div>
                        </div>
                        <div class="pt-6 border-t border-slate-200/60 flex justify-between items-center">
                            <div class="text-[10px] font-black text-slate-300 uppercase tracking-widest italic leading-none">Access Timestamp</div>
                            <div class="text-xs font-black text-slate-500 font-mono italic leading-none">
                                <fmt:formatDate value="${table.lastOrderTime}" pattern="HH:mm" />
                            </div>
                        </div>
                    </div>

                    <!-- Quick Action (Optional visual cue) -->
                    <div class="relative z-10 flex justify-end mt-4">
                         <div class="text-[8px] font-black text-slate-300 uppercase tracking-[0.4em] leading-none opacity-0 group-hover:opacity-100 transition-opacity italic">Live data stream active</div>
                    </div>
                </div>
            </c:forEach>
        </main>
    </div>

    <style>
        .animate-fadeIn { animation: fadeIn 0.8s cubic-bezier(0.16, 1, 0.3, 1) forwards; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
