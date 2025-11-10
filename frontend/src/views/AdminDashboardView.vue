<template>
  <AdminDashboard
    v-if="adminUser"
    :user="adminUser"
    @logout="handleLogout"
  />
</template>

<script setup lang="ts">
import { computed, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import AdminDashboard from '../components/AdminDashboard.vue';
import { useAuth } from '../stores/auth';

const router = useRouter();
const { currentUser, logout } = useAuth();

const adminUser = computed(() => {
  if (!currentUser.value) {
    return null;
  }
  return currentUser.value.role === 'admin' ? currentUser.value : null;
});

watchEffect(() => {
  if (!adminUser.value) {
    router.replace('/');
  }
});

const handleLogout = () => {
  logout();
  router.replace('/');
};
</script>

