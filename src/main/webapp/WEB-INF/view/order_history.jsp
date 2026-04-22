<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>注文履歴 | テーブルオーダーシステム</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[900px] mx-auto px-12 py-20">
        <nav class="mb-12">
            <a href="Menu" class="inline-flex items-center gap-2 text-xs font-black text-slate-400 hover:text-primary-600 transition-colors no-underline uppercase tracking-[0.2em]">
                <span class="text-lg">←</span> おしながきに戻る
            </a>
        </nav>

        <header class="mb-16 flex justify-between items-end">
            <div class="space-y-2">
                <div class="flex items-center gap-3">
                    <span class="w-2 h-2 bg-primary-500 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.3em] leading-none italic">Chronological Record</span>
                </div>
                <h1 class="text-5xl font-black tracking-tighter text-slate-950">
                    注文履歴<span class="text-primary-600">.</span>
                </h1>
            </div>
            <div class="text-right">
                <div class="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-1">Status</div>
                <div class="flex items-center gap-2 px-4 py-2 bg-emerald-50 text-emerald-600 rounded-full border border-emerald-100/50">
                    <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse"></span>
                    <span class="text-[10px] font-black uppercase tracking-widest">Active Table</span>
                </div>
            </div>
        </header>

        <main class="space-y-12">
            <section class="premium-card bg-white overflow-hidden">
                <header class="data-header">
                    <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none">Transaction Log</h2>
                    <span class="text-[10px] font-black uppercase text-slate-300 font-mono">Timestamped ISO/JST</span>
                </header>
                <div class="overflow-x-auto">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="text-[10px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-50">
                                <th class="px-10 py-6">商品名</th>
                                <th class="px-6 py-6 text-center">数量</th>
                                <th class="px-10 py-6 text-right">金額</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-50">
                            <c:set var="grandTotal" value="0" />
                            <c:forEach var="item" items="${summary.items}">
                                <tr class="group hover:bg-slate-50/50 transition-colors">
                                    <td class="px-10 py-8">
                                        <div class="text-lg font-bold text-slate-900 group-hover:text-primary-600 transition-colors leading-tight">
                                            <c:out value="${item.productName}" />
                                        </div>
                                        <div class="text-[9px] font-black text-slate-300 uppercase tracking-[0.2em] mt-1 italic">Culinary Asset</div>
                                    </td>
                                    <td class="px-6 py-8 text-center text-sm font-black text-slate-400">
                                        ${item.quantity} <span class="opacity-30 italic ml-1 select-none">pts</span>
                                    </td>
                                    <td class="px-10 py-8 text-right">
                                        <div class="flex items-baseline justify-end gap-1">
                                            <span class="text-slate-300 font-bold italic text-[10px] uppercase">jpy</span>
                                            <div class="text-xl font-black text-slate-950 tracking-tighter">
                                                <fmt:formatNumber value="${item.unitPrice * item.quantity}" />
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <c:set var="grandTotal" value="${grandTotal + (item.unitPrice * item.quantity)}" />
                            </c:forEach>
                            <c:if test="${empty summary.items}">
                                <tr>
                                    <td colspan="3" class="px-10 py-32 text-center">
                                        <div class="text-6xl mb-8 opacity-10">⏱</div>
                                        <p class="text-[10px] font-black text-slate-300 uppercase tracking-[0.5em] italic leading-relaxed">No orders committed yet.<br>Transaction buffer is currently empty.</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <!-- 会計サマリー -->
            <section class="max-w-md ml-auto">
                <div class="premium-card p-10 bg-slate-900 text-white relative overflow-hidden group">
                    <!-- Background Decoration -->
                    <div class="absolute -right-10 -bottom-10 text-[10rem] font-black text-white/5 italic select-none pointer-events-none group-hover:scale-110 transition-transform duration-700">¥</div>
                    
                    <div class="relative z-10 space-y-6">
                        <header class="flex justify-between items-center border-b border-white/10 pb-6">
                            <h3 class="text-[10px] font-black text-primary-400 uppercase tracking-[0.4em]">Settlement Request</h3>
                            <div class="w-10 h-10 rounded-xl bg-white/5 flex items-center justify-center text-lg">🏧</div>
                        </header>
                        
                        <div class="flex justify-between items-end">
                            <div class="space-y-1">
                                <p class="text-[10px] font-black text-slate-500 uppercase tracking-widest">Grand Total</p>
                                <p class="text-xs font-bold text-slate-400">現時点での合計金額</p>
                            </div>
                            <div class="text-right">
                                <div class="flex items-baseline justify-end gap-2">
                                    <span class="text-2xl font-black text-slate-500 italic uppercase tracking-tighter">jpy</span>
                                    <span class="text-6xl font-black text-white tracking-tighter">
                                        <fmt:formatNumber value="${grandTotal}" />
                                    </span>
                                </div>
                            </div>
                        </div>

                        <footer>
                            <p class="text-[10px] font-bold text-slate-600 leading-relaxed uppercase tracking-[0.2em] italic">
                                * Prices include consumption tax. Please proceed to the cashier for final settlement.
                            </p>
                        </footer>
                    </div>
                </div>
            </section>
        </main>
    </div>
</body>
</html>
