<template>
  <StudentDashboard @logout="handleLogout" />
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuth } from '../stores/auth';
import StudentDashboard from '../components/StudentDashboard.vue';

const router = useRouter();
const { logout } = useAuth();

const handleLogout = () => {
  logout();
  router.push('/login');
};
</script>