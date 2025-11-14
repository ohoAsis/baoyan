<template>
  <StudentDashboard
    v-if="studentUser"
    :user="studentUser"
    @logout="handleLogout"
  />
</template>

<script setup lang="ts">
import { computed, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import StudentDashboard from '../components/StudentDashboard.vue';
import { useAuth } from '../stores/auth';

const router = useRouter();
const { currentUser, logout } = useAuth();

const studentUser = computed(() => {
  if (!currentUser.value) {
    return null;
  }
  return currentUser.value.role === 'student' ? currentUser.value : null;
});

watchEffect(() => {
  if (!studentUser.value) {
    router.replace('/');
  }
});

const handleLogout = () => {
  logout();
  router.replace('/');
};
</script>

