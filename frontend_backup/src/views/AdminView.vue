<template>
  <AdminDashboard :user="currentUser" @logout="handleLogout" />
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuth } from '../stores/auth';
import AdminDashboard from '../components/AdminDashboard.vue';

const router = useRouter();
const { currentUser, logout } = useAuth();

const handleLogout = () => {
  logout();
  router.push('/login');
};
</script>