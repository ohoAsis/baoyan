<template>
  <div class="min-h-screen flex items-center justify-center p-4 relative overflow-hidden" :style="backgroundStyle">
    <Card class="w-full max-w-md bg-white/60 backdrop-blur-md border-none shadow-xl transition-all duration-300 hover:shadow-2xl relative z-10">
      <CardHeader class="text-center space-y-2">
        <!-- 登录图标和标题 -->
        <div class="flex items-center justify-center gap-3">
          <div class="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-2xl shadow-lg text-white">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <CardTitle class="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">保研加分小助手</CardTitle>
        </div>
        <CardDescription class="text-gray-600">请登录您的账户以继续使用系统</CardDescription>
      </CardHeader>
      <CardContent class="space-y-5">
        <!-- 用户名输入 -->
        <div class="space-y-2">
          <Label for="username" class="text-gray-700 font-medium">工号/学号</Label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </span>
            <Input
              id="username"
              placeholder="请输入工号或学号"
              v-model="username"
              class="pl-10 border-gray-300 focus:border-blue-500 focus:ring-blue-500/20 transition-all"
              :class="{ 'ring-2 ring-blue-100': username.length > 0 }"
            />
          </div>
        </div>

        <!-- 密码输入 -->
        <div class="space-y-2">
          <Label for="password" class="text-gray-700 font-medium">密码</Label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </span>
            <Input
              id="password"
              type="password"
              placeholder="请输入密码"
              v-model="password"
              class="pl-10 border-gray-300 focus:border-blue-500 focus:ring-blue-500/20 transition-all"
              :class="{ 'ring-2 ring-blue-100': password.length > 0 }"
            />
          </div>
        </div>

        <!-- 登录按钮 - 增强状态变化提醒 -->
        <Button
          class="w-full h-11 text-base font-medium transition-all duration-300 transform relative overflow-hidden"
          :class="{
            'bg-blue-500 hover:bg-blue-600 text-white cursor-pointer shadow-md hover:shadow-lg hover:scale-[1.02] active:scale-[0.98]': !isButtonDisabled,
            'bg-gray-300 text-gray-500 cursor-not-allowed shadow-none': isButtonDisabled
          }"
          @click="handleLogin"
          :disabled="isButtonDisabled"
          :key="buttonKey"
        >
          <!-- 状态指示动画 -->
          <div v-if="!isButtonDisabled" class="absolute inset-0 bg-white/20 transform translate-y-full animate-pulse-wave"></div>
          
          <!-- 登录文本 -->
          <span class="relative z-10 flex items-center justify-center">
            <svg v-if="isLoggingIn" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            {{ isLoggingIn ? '登录中...' : '登录' }}
          </span>
        </Button>
        
        <!-- 错误信息显示 -->
        <div v-if="errorMessage" class="text-sm text-red-500 text-center bg-red-50 rounded-lg p-3 border border-red-100">
          {{ errorMessage }}
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type { User, UserRole } from '../types';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardDescription from './ui/CardDescription.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import loginBg from '../assets/login-bg.jpg';
import { authApi } from '../api/auth';

const emit = defineEmits<{
  login: [user: User, token: string];
}>();

const username = ref('');
const password = ref('');
const isLoggingIn = ref(false);
const buttonKey = ref(0); // 用于强制重新渲染按钮以触发动画
const errorMessage = ref('');

// 背景图片样式
const backgroundStyle = computed(() => ({
  backgroundImage: `url(${loginBg})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  backgroundAttachment: 'fixed'
}));

// 计算按钮是否应该禁用
const isButtonDisabled = computed(() => {
  return !username.value || !password.value || isLoggingIn.value;
});

// 监听表单输入，当按钮从禁用变为可用时添加动画提醒
watch([username, password], () => {
  const wasDisabled = buttonKey.value % 2 === 0;
  const willBeDisabled = isButtonDisabled.value;
  
  // 当按钮状态从禁用变为可用时
  if (wasDisabled && !willBeDisabled) {
    // 增加按钮key来触发过渡动画
    buttonKey.value++;
  }
  
  // 清除错误信息
  if (errorMessage.value) {
    errorMessage.value = '';
  }
});

const handleLogin = async () => {
  if (!username.value || !password.value) return;
  
  // 显示登录中状态，清除错误信息
  isLoggingIn.value = true;
  errorMessage.value = '';
  
  try {
    // 调用真实登录API
    const response = await authApi.login({ username: username.value, password: password.value });
    
    // 构造符合前端User接口的对象
    const user: User = {
      id: response.userId.toString(),
      name: response.realName,
      username: username.value,
      role: response.role as UserRole,
      // 根据角色设置可选字段
      studentId: response.role === 'student' ? username.value : undefined,
      major: undefined,
      email: undefined
    };
    
    // 登录成功，传递用户信息和token
    emit('login', user, response.token);
  } catch (error) {
    // 登录失败，显示错误信息
    errorMessage.value = error instanceof Error ? error.message : '登录失败，请检查用户名和密码';
  } finally {
    // 确保无论成功失败都重置登录状态
    isLoggingIn.value = false;
  }
};
</script>

<style scoped>
/* 按钮脉冲动画 */
@keyframes pulse-wave {
  0% {
    transform: translateX(-100%) skewX(-20deg);
    opacity: 0.3;
  }
  100% {
    transform: translateX(200%) skewX(-20deg);
    opacity: 0;
  }
}

.animate-pulse-wave {
  animation: pulse-wave 1.5s ease-in-out infinite;
}
</style>