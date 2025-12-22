<template>
  <LoginPage @login="handleLogin" />
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import type { User } from '../types';
import { useAuth } from '../stores/auth';

const router = useRouter();
const { login } = useAuth();

const handleLogin = (user: User, token: string) => {
  login(user, token);
  // 根据角色跳转不同页面
  if (user.role === 'student') {
    router.push('/student');
  } else if (user.role === 'reviewer') {
    router.push('/reviewer');
  } else if (user.role === 'admin') {
    router.push('/admin');
  } else {
    router.push('/login');
  }
};
</script>

