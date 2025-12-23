<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center space-x-3">
            <div>
              <h1 class="text-xl">管理员首页</h1>
              <p class="text-sm text-gray-500">学院内部管理系统</p>
            </div>
          </div>
          <div>
            <button @click="handleLogout" class="text-sm text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md">
              退出登录
            </button>
          </div>
        </div>
      </div>
    </header>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="text-center py-12">
        <h2 class="text-2xl font-medium text-gray-900">欢迎使用管理员系统</h2>
        <p class="mt-2 text-sm text-gray-500">请从左侧菜单选择要操作的功能</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuth } from '../stores/auth';

const router = useRouter();
const { logout } = useAuth();

// 退出登录处理函数
const handleLogout = () => {
  // 清除本地登录状态
  logout();
  // 跳转到登录页
  router.push('/login');
};
</script>