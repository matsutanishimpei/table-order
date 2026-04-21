<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@300;400;500;700;900&family=Outfit:wght@300;400;500;600;700;800;900&family=JetBrains+Mono:wght@400;700&display=swap" rel="stylesheet">
<script src="https://cdn.tailwindcss.com"></script>
<script>
    tailwind.config = {
        theme: {
            extend: {
                colors: {
                    primary: {
                        50: '#f0f4ff', 100: '#e0e9ff', 200: '#c1d2fe', 300: '#91affd', 400: '#5c81f8', 500: '#3752ee', 600: '#2535e2', 700: '#1d27cb'
                    },
                    accent: {
                        500: '#8b5cf6', 600: '#7c3aed'
                    }
                },
                fontFamily: {
                    sans: ['Noto Sans JP', 'Outfit', 'sans-serif'],
                    mono: ['JetBrains Mono', 'monospace']
                }
            }
        }
    }
</script>
<style type="text/tailwindcss">
    @layer base {
        body { @apply bg-[#f8fafc] text-slate-900 antialiased font-sans; }
    }
    @layer components {
        .glass-panel { @apply bg-white/70 backdrop-blur-xl border border-white/20 shadow-xl rounded-3xl; }
        .premium-card { @apply bg-white border border-slate-100 shadow-lg rounded-3xl overflow-hidden; }
        .btn-primary { @apply px-8 py-4 bg-primary-600 text-white rounded-2xl font-bold transition-all hover:bg-primary-700 active:scale-95 shadow-lg shadow-primary-600/20 disabled:opacity-50; }
        .btn-accent { @apply px-8 py-4 bg-accent-600 text-white rounded-2xl font-bold transition-all hover:bg-accent-700 active:scale-95 shadow-lg shadow-accent-600/20; }
        .nav-link { @apply flex items-center gap-4 px-6 py-4 rounded-2xl text-slate-500 font-bold hover:bg-slate-50 hover:text-primary-600 transition-all; }
        .nav-link.active { @apply bg-primary-50 text-primary-600 border border-primary-100; }
        .data-header { @apply px-8 py-6 bg-slate-50/80 border-b border-slate-100 flex justify-between items-center; }
    }
</style>
