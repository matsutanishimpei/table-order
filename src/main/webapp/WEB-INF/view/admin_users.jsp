<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>ユーザー管理 | 管理センター</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[1200px] mx-auto px-12 py-20">
        <header class="mb-16 flex justify-between items-end">
            <div class="space-y-4">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-indigo-500 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Access Control System</span>
                </div>
                <h1 class="text-6xl font-black tracking-tighter text-slate-950">
                    ユーザー管理<span class="text-indigo-600">.</span>
                </h1>
                <p class="text-slate-500 font-medium italic opacity-60">スタッフ権限およびテーブル端末の統合アカウント管理</p>
            </div>
            
            <div class="flex items-center gap-4">
                <a href="Home" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-xs font-black text-slate-400 hover:text-slate-950 transition-all uppercase tracking-widest no-underline">
                    管理ホーム
                </a>
                <a href="User?action=add" class="px-8 py-4 bg-indigo-600 rounded-2xl text-xs font-black text-white hover:bg-indigo-700 hover:shadow-xl hover:shadow-indigo-500/20 transition-all uppercase tracking-widest no-underline">
                    新規登録
                </a>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="mb-12 animate-fadeIn p-5 rounded-2xl bg-indigo-50 border border-indigo-100 text-indigo-600 flex items-center gap-4 text-xs font-bold uppercase tracking-wide">
                <span>🛡️</span> 
                <c:choose>
                    <c:when test="${param.msg == 'success'}">新しいユーザーを登録しました。</c:when>
                    <c:when test="${param.msg == 'updatesuccess'}">ユーザー情報を更新しました。</c:when>
                    <c:when test="${param.msg == 'deletesuccess'}">ユーザーを削除しました。</c:when>
                    <c:otherwise>処理中にエラーが発生しました。</c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <main class="premium-card bg-white overflow-hidden shadow-2xl border-none">
            <header class="data-header bg-slate-50/50">
                <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-widest leading-none">Registered Users</h2>
                <div class="flex items-center gap-4">
                    <span class="text-[10px] font-black uppercase text-slate-400 border border-slate-200 px-3 py-1 rounded-full">${userList.size()} <span class="opacity-40">Accounts</span></span>
                </div>
            </header>

            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse">
                    <thead>
                        <tr class="text-[10px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-50">
                            <th class="px-10 py-6">ユーザーID</th>
                            <th class="px-6 py-6">権限 / ロール</th>
                            <th class="px-6 py-6">割り当てテーブル</th>
                            <th class="px-10 py-6 text-right w-40">操作</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-50">
                        <c:forEach var="u" items="${userList}">
                            <tr class="group hover:bg-slate-50/50 transition-colors">
                                <td class="px-10 py-8">
                                    <div class="text-lg font-black text-slate-900 leading-tight group-hover:text-indigo-600 transition-colors italic">
                                        <c:out value="${u.id}" />
                                    </div>
                                    <c:if test="${u.id == sessionScope.user.id}">
                                        <span class="text-[8px] font-black bg-indigo-50 text-indigo-400 px-2 py-0.5 rounded uppercase tracking-tighter italic">Logged In</span>
                                    </c:if>
                                </td>
                                <td class="px-6 py-8">
                                    <c:choose>
                                        <c:when test="${u.role == 1}">
                                            <span class="px-3 py-1 rounded-lg bg-red-50 text-[10px] font-black text-red-500 uppercase tracking-widest leading-none border border-red-100">Administrator</span>
                                        </c:when>
                                        <c:when test="${u.role == 2}">
                                            <span class="px-3 py-1 rounded-lg bg-amber-50 text-[10px] font-black text-amber-600 uppercase tracking-widest leading-none border border-amber-100">Kitchen</span>
                                        </c:when>
                                        <c:when test="${u.role == 3}">
                                            <span class="px-3 py-1 rounded-lg bg-emerald-50 text-[10px] font-black text-emerald-600 uppercase tracking-widest leading-none border border-emerald-100">Hall Staff</span>
                                        </c:when>
                                        <c:when test="${u.role == 4}">
                                            <span class="px-3 py-1 rounded-lg bg-indigo-50 text-[10px] font-black text-indigo-600 uppercase tracking-widest leading-none border border-indigo-100">Cashier</span>
                                        </c:when>
                                        <c:when test="${u.role == 10}">
                                            <span class="px-3 py-1 rounded-lg bg-slate-100 text-[10px] font-black text-slate-500 uppercase tracking-widest leading-none border border-slate-200">Table Terminal</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td class="px-6 py-8">
                                    <c:choose>
                                        <c:when test="${u.role == 10 && not empty u.tableId}">
                                            <div class="flex items-center gap-2">
                                                <span class="w-1.5 h-1.5 bg-slate-400 rounded-full"></span>
                                                <span class="text-xs font-black text-slate-600 uppercase italic">Table #${u.tableId}</span>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-[10px] font-black text-slate-300 italic">N/A</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-10 py-8 text-right space-x-2">
                                    <a href="User?action=edit&id=${u.id}" class="inline-flex items-center justify-center p-3 rounded-xl bg-slate-50 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 transition-all no-underline border border-transparent hover:border-indigo-100" title="編集">
                                        <span class="text-lg">⚙️</span>
                                    </a>
                                    <c:if test="${u.id != sessionScope.user.id}">
                                        <form action="User" method="post" class="inline" onsubmit="return confirm('ユーザー 「${u.id}」 を削除してもよろしいですか？');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <input type="hidden" name="csrf_token" value="${csrf_token}">
                                            <button type="submit" class="inline-flex items-center justify-center p-3 rounded-xl bg-slate-50 text-slate-300 hover:text-red-500 hover:bg-red-50 transition-all border border-transparent hover:border-red-100" title="削除">
                                                <span class="text-lg">🗑️</span>
                                            </button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>
    </div>

    <style>
        .animate-fadeIn { animation: fadeIn 0.5s ease-out; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</body>
</html>
