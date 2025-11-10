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

const handleLogin = (user: User) => {
  login(user);
  router.push(user.role === 'student' ? '/student' : '/admin');
};
</script>

