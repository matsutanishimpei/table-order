<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>${empty targetUser ? '新規登録' : 'ユーザー編集'} | ユーザー管理</title>
    <jsp:include page="common/header.jsp" />
</head>
<body class="bg-[#f8fafc] font-sans antialiased text-slate-900">
    <div class="max-w-[800px] mx-auto px-12 py-20">
        <header class="mb-16 flex justify-between items-end">
            <div class="space-y-4">
                <div class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-indigo-500 rounded-full"></span>
                    <span class="text-[10px] font-black text-slate-400 uppercase tracking-[0.4em] leading-none italic">Credential Configuration</span>
                </div>
                <h1 class="text-5xl font-black tracking-tighter text-slate-950">
                    ${empty targetUser ? '新規登録' : 'ユーザー編集'}<span class="text-indigo-600">.</span>
                </h1>
                <p class="text-slate-500 font-medium italic opacity-60">アカウントの識別子、役割、およびセキュリティ情報の定義</p>
            </div>
            
            <a href="User" class="px-8 py-4 bg-white border border-slate-200 rounded-2xl text-xs font-black text-slate-400 hover:text-slate-950 transition-all uppercase tracking-widest no-underline">
                一覧へ戻る
            </a>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="mb-12 animate-fadeIn p-5 rounded-2xl bg-red-50 border border-red-100 text-red-500 flex items-center gap-4 text-xs font-bold uppercase tracking-wide">
                <span>⚠️</span> 
                <c:choose>
                    <c:when test="${param.msg == 'invalid'}">入力内容に不備があります。</c:when>
                    <c:when test="${param.msg == 'duplicate'}">そのユーザーIDは既に登録されています。</c:when>
                    <c:otherwise>エラーが発生しました。</c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <main class="premium-card bg-white shadow-2xl p-12 border-none">
            <form action="User" method="post" class="space-y-10">
                <c:if test="${not empty targetUser}">
                    <input type="hidden" name="isEdit" value="true">
                </c:if>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <!-- ユーザーID -->
                <div class="space-y-4">
                    <label for="id" class="text-[10px] font-black text-slate-400 uppercase tracking-widest block ml-1 italic">User Identifier</label>
                    <input type="text" id="id" name="id" value="<c:out value='${targetUser.id}' />" 
                           class="w-full px-6 py-4 bg-slate-50 border border-slate-100 rounded-2xl text-lg font-black text-slate-900 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:bg-white transition-all placeholder:text-slate-200"
                           placeholder="Ex: staff_01" required ${not empty targetUser ? 'readonly' : ''}>
                    <c:if test="${not empty targetUser}">
                        <p class="text-[9px] font-bold text-slate-300 italic uppercase tracking-wider ml-1">※ ユーザーIDは変更できません</p>
                    </c:if>
                </div>

                <!-- パスワード -->
                <div class="space-y-4">
                    <label for="password" class="text-[10px] font-black text-slate-400 uppercase tracking-widest block ml-1 italic">Security Credentials</label>
                    <input type="password" id="password" name="password" 
                           class="w-full px-6 py-4 bg-slate-50 border border-slate-100 rounded-2xl text-lg font-black text-slate-900 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:bg-white transition-all placeholder:text-slate-200"
                           placeholder="${not empty targetUser ? '変更する場合のみ入力' : 'パスワードを入力'}" ${empty targetUser ? 'required' : ''}>
                    <p class="text-[9px] font-bold text-slate-300 italic uppercase tracking-wider ml-1">※ 入力された値は強力なBCryptハッシュとして保存されます</p>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <!-- 権限ロール -->
                    <div class="space-y-4">
                        <label for="role" class="text-[10px] font-black text-slate-400 uppercase tracking-widest block ml-1 italic">Functional Role</label>
                        <select id="role" name="role" onchange="toggleTableSelect()"
                                class="w-full px-6 py-4 bg-slate-50 border border-slate-100 rounded-2xl text-sm font-black text-slate-900 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:bg-white transition-all appearance-none cursor-pointer">
                            <option value="1" ${targetUser.role == 1 ? 'selected' : ''}>Administrator (管理者)</option>
                            <option value="2" ${targetUser.role == 2 ? 'selected' : ''}>Kitchen (キッチン)</option>
                            <option value="3" ${targetUser.role == 3 ? 'selected' : ''}>Hall (ホール)</option>
                            <option value="4" ${targetUser.role == 4 ? 'selected' : ''}>Cashier (レジ)</option>
                            <option value="10" ${targetUser.role == 10 ? 'selected' : ''}>Table Terminal (テーブル端末)</option>
                        </select>
                    </div>

                    <!-- テーブル割り当て -->
                    <div id="table-select-container" class="space-y-4 ${targetUser.role == 10 ? '' : 'opacity-20 pointer-events-none transition-all duration-500'}">
                        <label for="tableId" class="text-[10px] font-black text-slate-400 uppercase tracking-widest block ml-1 italic">Terminal Assignment</label>
                        <select id="tableId" name="tableId"
                                class="w-full px-6 py-4 bg-slate-50 border border-slate-100 rounded-2xl text-sm font-black text-slate-900 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:bg-white transition-all appearance-none cursor-pointer">
                            <option value="">-- 未割り当て --</option>
                            <c:forEach var="t" items="${tableList}">
                                <option value="${t.tableId}" ${targetUser.tableId == t.tableId ? 'selected' : ''}>
                                    <c:out value="${t.tableName}" /> (#${t.tableId})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="pt-10 flex gap-4">
                    <button type="submit" class="flex-1 px-8 py-5 bg-indigo-600 rounded-2xl text-xs font-black text-white hover:bg-indigo-700 hover:shadow-2xl hover:shadow-indigo-500/30 transition-all uppercase tracking-[0.2em]">
                        ${empty targetUser ? 'ユーザーを登録する' : '変更を保存する'}
                    </button>
                </div>
            </form>
        </main>
    </div>

    <script>
        function toggleTableSelect() {
            const roleSelect = document.getElementById('role');
            const tableContainer = document.getElementById('table-select-container');
            const isTable = roleSelect.value === '10';
            
            if (isTable) {
                tableContainer.classList.remove('opacity-20', 'pointer-events-none');
            } else {
                tableContainer.classList.add('opacity-20', 'pointer-events-none');
                document.getElementById('tableId').value = "";
            }
        }
        
        // 初期状態の反映
        window.onload = toggleTableSelect;
    </script>

    <style>
        .animate-fadeIn { animation: fadeIn 0.5s ease-out; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
        select { background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%2394a3b8'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E"); background-position: right 1.5rem center; background-repeat: no-repeat; background-size: 1rem; }
    </style>
</body>
</html>
