<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>売上分析 | 管理センター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[1200px] mx-auto px-12 py-20">
        <header class="mb-16 flex justify-between items-end">
            <div class="space-y-4">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-primary-500 rounded-full animate-pulse"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Revenue Analytics</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950">
                    売上分析<span class="text-primary-600">.</span>
                </h1>
                <p class="text-slate-500 font-medium italic opacity-60">財務パフォーマンスと需要動向の統合分析コンソール</p>
            </div>
            <a href="Home" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-xs font-black text-slate-400 hover:text-slate-950 transition-all uppercase tracking-widest no-underline">
                管理ホームへ戻る
            </a>
        </header>

        <!-- サマリーカード -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-10 mb-16">
            <div class="premium-card p-12 bg-white relative overflow-hidden group hover:border-primary-500/50 transition-all shadow-2xl border-none">
                <div class="absolute -right-8 -bottom-8 text-9xl font-black text-slate-50 italic pointer-events-none group-hover:text-primary-50 transition-colors uppercase select-none italic">¥</div>
                <div class="relative z-10 space-y-4">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em]">累計売上額</h2>
                    <div class="flex items-baseline gap-3">
                        <span class="text-slate-300 font-black italic text-2xl uppercase">jpy</span>
                        <div class="text-7xl font-black text-slate-950 tracking-tighter">
                            <fmt:formatNumber value="${totalSales}" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="premium-card p-12 bg-white relative overflow-hidden group hover:border-emerald-500/50 transition-all shadow-2xl border-none">
                <div class="absolute -right-8 -bottom-8 text-9xl font-black text-slate-50 italic pointer-events-none group-hover:text-emerald-50 transition-colors uppercase select-none italic">%</div>
                <div class="relative z-10 space-y-4">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em]">日次平均売上</h2>
                    <div class="flex items-baseline gap-3">
                        <span class="text-slate-300 font-black italic text-2xl uppercase">jpy</span>
                        <div class="text-7xl font-black text-slate-950 tracking-tighter">
                            <c:set var="sum" value="0" />
                            <c:forEach var="d" items="${dailySales}">
                                <c:set var="sum" value="${sum + d.amount}" />
                            </c:forEach>
                            <fmt:formatNumber value="${dailySales.size() > 0 ? sum / dailySales.size() : 0}" pattern="#,###" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
            <!-- 日次売上トレンド -->
            <section class="premium-card bg-white overflow-hidden shadow-2xl border-none">
                <header class="data-header bg-slate-50/50 px-10 py-8">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none italic">Daily Trajectory (Last 7 Days)</h2>
                    <span class="text-[10px] font-black uppercase text-emerald-500 bg-emerald-50 px-3 py-1 rounded-full border border-emerald-100 italic font-mono leading-none">Live Sync</span>
                </header>
                <div class="overflow-x-auto">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="text-[10px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-50">
                                <th class="px-10 py-6">集計日</th>
                                <th class="px-6 py-6 text-right">売上高</th>
                                <th class="px-10 py-6 text-center">注文数</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-50">
                            <c:forEach var="d" items="${dailySales}">
                                <tr class="group hover:bg-slate-50/50 transition-colors">
                                    <td class="px-10 py-10 text-sm font-black text-slate-400 font-mono italic">
                                        <fmt:formatDate value="${d.salesDate}" pattern="yyyy.MM.dd" />
                                    </td>
                                    <td class="px-6 py-10 text-right font-black text-slate-900 text-3xl tracking-tighter">
                                        ¥<fmt:formatNumber value="${d.amount}" />
                                    </td>
                                    <td class="px-10 py-10 text-center">
                                        <span class="inline-flex items-center px-4 py-2 rounded-xl bg-slate-900 text-white text-[10px] font-black tracking-widest uppercase italic">
                                            ${d.orderCount} Orders
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty dailySales}">
                                <tr><td colspan="3" class="px-10 py-40 text-center text-[10px] font-black text-slate-300 uppercase tracking-[0.4em] italic leading-relaxed">No chronological data committed.<br>Sales matrix is currently void.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <!-- 商品別ランキング -->
            <section class="premium-card bg-white overflow-hidden shadow-2xl border-none">
                <header class="data-header bg-slate-50/50 px-10 py-8">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none italic">Market Demand Ranking</h2>
                    <span class="text-[10px] font-black uppercase text-primary-500 bg-primary-50 px-3 py-1 rounded-full border border-primary-100 italic font-mono leading-none">Top Performers</span>
                </header>
                <div class="overflow-x-auto">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="text-[10px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-50">
                                <th class="px-10 py-6 text-center w-20">順位</th>
                                <th class="px-6 py-6">商品名</th>
                                <th class="px-10 py-6 text-right">売上累計</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-50">
                            <c:forEach var="p" items="${productRanking}" varStatus="status">
                                <tr class="group hover:bg-slate-50/50 transition-colors">
                                    <td class="px-10 py-10 text-center relative overflow-hidden">
                                        <div class="text-4xl font-black italic tracking-tighter leading-none ${status.index < 3 ? 'text-primary-600' : 'text-slate-100'}">
                                            0${status.index + 1}
                                        </div>
                                    </td>
                                    <td class="px-6 py-10">
                                        <div class="font-black text-slate-900 text-2xl group-hover:text-primary-600 transition-colors tracking-tight leading-none"><c:out value="${p.productName}" /></div>
                                        <div class="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-3 italic leading-none">${p.totalQuantity} <span class="opacity-40">Units Distribution</span></div>
                                    </td>
                                    <td class="px-10 py-10 text-right font-black text-slate-950 text-3xl tracking-tighter">
                                        ¥<fmt:formatNumber value="${p.totalAmount}" />
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty productRanking}">
                                <tr><td colspan="3" class="px-10 py-40 text-center text-[10px] font-black text-slate-300 uppercase tracking-[0.4em] italic leading-relaxed">No market data available.<br>Asset demand log is void.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    </div>
</body>
</html>
