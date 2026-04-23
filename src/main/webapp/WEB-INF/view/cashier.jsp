<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>精算管理 | レジ・決済コンソール</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#020617] font-sans antialiased text-slate-100 min-h-screen">
    <div class="px-8 py-10">
        <header class="mb-12 flex justify-between items-end border-b border-white/5 pb-10">
            <div class="space-y-3">
                <div class="flex items-center gap-3">
                    <span class="w-3 h-3 bg-primary-500 rounded-full animate-pulse shadow-[0_0_15px_rgba(59,130,246,0.5)]"></span>
                    <span class="text-[10px] font-black text-primary-400 uppercase tracking-[0.4em] leading-none italic">Settlement Hub</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-white">
                    精算待ち一覧<span class="text-primary-500">.</span>
                </h1>
                <p class="text-slate-500 font-bold italic text-sm">各テーブルの未決済注文の統合管理と会計処理</p>
            </div>
            
            <div class="flex items-center gap-6">
                <a href="Home" class="px-8 py-4 bg-white/5 border border-white/10 rounded-2xl text-xs font-black text-slate-400 hover:text-white hover:bg-white/10 transition-all no-underline uppercase tracking-widest">
                    管理ホームへ
                </a>
                <div class="bg-white/5 border border-white/10 px-8 py-6 rounded-3xl backdrop-blur-md">
                    <div class="text-[10px] font-black text-slate-500 uppercase tracking-widest mb-1">未決済数</div>
                    <div class="text-3xl font-black text-white tracking-tighter">${unsettledTables.size()} <span class="text-xs text-slate-600 font-bold">テーブル</span></div>
                </div>
            </div>
        </header>

        <main class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-12">
            <c:forEach var="entry" items="${unsettledTables}">
                <div class="bg-slate-900 border border-white/10 rounded-5xl overflow-hidden flex flex-col group hover:border-primary-500/50 transition-all duration-500 shadow-2xl">
                    <header class="p-10 bg-gradient-to-br from-slate-800 to-slate-900 border-b border-white/5 relative overflow-hidden">
                        <!-- Table Number Background -->
                        <div class="absolute -right-4 -top-8 text-[12rem] font-black text-white/5 italic select-none pointer-events-none group-hover:text-primary-500/10 transition-colors">
                            #${entry.tableId}
                        </div>
                        
                        <div class="relative z-10 flex justify-between items-start">
                            <div>
                                <p class="text-[10px] font-black text-primary-400 uppercase tracking-widest leading-none mb-3 italic">Floor Destination</p>
                                <h2 class="text-6xl font-black text-white tracking-tighter leading-none">
                                    <c:out value="${entry.tableName}" /><span class="text-xl text-slate-600 font-bold ml-2 select-none italic">席</span>
                                </h2>
                            </div>
                            <div class="text-right">
                                <p class="text-[9px] font-bold text-slate-500 uppercase tracking-widest mb-1 italic">Bill Items</p>
                                <p class="text-2xl font-black text-white font-mono">${entry.orderCount} <span class="text-xs text-slate-600">点</span></p>
                            </div>
                        </div>
                    </header>

                    <div class="flex-grow p-10 space-y-8">
                        <div class="space-y-4">
                            <div class="text-[10px] font-black text-slate-500 uppercase tracking-[0.3em] mb-2 italic">Receipt Subtotal</div>
                            <div class="flex justify-between items-baseline border-b border-white/5 pb-4">
                                <span class="text-slate-400 font-black italic text-lg uppercase">jpy</span>
                                <span class="text-5xl font-black text-white tracking-tighter">
                                    <fmt:formatNumber value="${entry.totalAmount}" />
                                </span>
                            </div>
                        </div>

                        <footer>
                            <form action="Home" method="post" data-unserved="${entry.unservedCount}" onsubmit="return confirmCheckout(this);">
                                <input type="hidden" name="csrf_token" value="${csrf_token}">
                                <input type="hidden" name="action" value="checkout">
                                <input type="hidden" name="tableId" value="${entry.tableId}">
                                <button type="submit" class="w-full py-6 bg-primary-600 text-white rounded-2xl font-black text-sm tracking-[0.3em] uppercase hover:bg-primary-500 transition-all active:scale-95 shadow-2xl shadow-primary-500/20 group">
                                    会計を完了する <span class="ml-2 group-hover:translate-x-1 transition-transform inline-block">→</span>
                                </button>
                            </form>
                        </footer>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty unsettledTables}">
                <div class="col-span-full border-2 border-dashed border-white/10 rounded-6xl py-48 flex flex-col items-center justify-center opacity-20 grayscale">
                    <div class="text-9xl mb-8 animate-pulse-subtle">🏧</div>
                    <p class="text-xs font-black text-slate-500 uppercase tracking-[0.6em] italic leading-relaxed">No pending settlements.<br>Cashier dashboard is clear.</p>
                </div>
            </c:if>
        </main>
    </div>

    <script>
        function confirmCheckout(form) {
            var unservedCount = parseInt(form.getAttribute('data-unserved') || '0', 10);
            if (unservedCount > 0) {
                return confirm('未提供の商品が ' + unservedCount + ' 点含まれていますが、強制的に会計を完了してよろしいですか？\n（これらの商品はキッチン・配膳の指示一覧から消去されます）');
            }
            return true;
        }
    </script>
</body>
</html>
