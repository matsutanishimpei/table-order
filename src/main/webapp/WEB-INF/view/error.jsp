<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>Error | Table Order System</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-slate-50 font-sans antialiased text-slate-900">
    <div class="min-h-screen flex items-center justify-center px-6 py-20">
        <div class="max-w-[600px] w-full">
            <header class="text-center mb-16">
                <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-red-50 text-red-500 text-5xl mb-8 animate-bounce">
                    ⚠️
                </div>
                <div class="flex items-center justify-center gap-3 mb-4">
                    <span class="w-1.5 h-1.5 bg-red-500 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">System Exception Detected</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950 leading-tight">
                    Something went <br><span class="text-red-500 italic">wrong.</span>
                </h1>
            </header>

            <main class="premium-card p-12 bg-white shadow-2xl relative overflow-hidden">
                <!-- Background ID Decoration -->
                <div class="absolute -top-10 -right-10 text-[10rem] font-black text-slate-50 italic opacity-5 pointer-events-none select-none">
                    500
                </div>

                <div class="relative z-10 space-y-8">
                    <div class="space-y-4">
                        <h2 class="text-xs font-black text-slate-900 uppercase tracking-widest border-b border-slate-100 pb-4">Error Message</h2>
                        <p class="text-lg font-bold text-slate-600 leading-relaxed italic">
                            <c:out value="${errorMessage}" default="予期しないエラーが発生しました。" />
                        </p>
                    </div>

                    <c:if test="${not empty exception}">
                        <div class="space-y-4">
                            <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest">Debug Trace</h2>
                            <div class="p-6 bg-slate-50 rounded-2xl border border-slate-100 overflow-x-auto">
                                <code class="text-[10px] font-mono text-slate-400 whitespace-pre"><c:out value="${exception}" /></code>
                            </div>
                        </div>
                    </c:if>

                    <footer class="pt-8 flex gap-4">
                        <a href="${pageContext.request.contextPath}/Menu" class="btn-primary bg-slate-950 hover:bg-slate-800 flex-grow py-5 text-xs tracking-widest shadow-xl">
                            メニューへ戻る
                        </a>
                        <button onclick="history.back()" class="px-8 py-5 bg-slate-100 text-slate-400 text-[10px] font-black rounded-2xl hover:bg-slate-200 transition-all uppercase tracking-widest">
                            戻る
                        </button>
                    </footer>
                </div>
            </main>
            
            <p class="mt-12 text-center text-[10px] font-bold text-slate-300 uppercase tracking-[0.5em] italic">
                Ref: <c:out value="${exception.getClass().simpleName}" /> / Table Order System v1.0
            </p>
        </div>
    </div>

    <style>
        .animate-bounce { animation: bounce 2s infinite; }
        @keyframes bounce { 0%, 100% { transform: translateY(-5%); animation-timing-function: cubic-bezier(0.8, 0, 1, 1); } 50% { transform: translateY(0); animation-timing-function: cubic-bezier(0, 0, 0.2, 1); } }
    </style>
</body>
</html>
