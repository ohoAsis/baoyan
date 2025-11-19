<template>
  <ReviewerDashboard
    v-if="reviewerUser"
    :user="reviewerUser"
    @logout="handleLogout"
  />
</template>

<script setup lang="ts">
import { computed, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import ReviewerDashboard from '../components/ReviewerDashboard.vue';
import { useAuth } from '../stores/auth';

const router = useRouter();
const { currentUser, logout } = useAuth();

const reviewerUser = computed(() => {
  if (!currentUser.value) {
    return null;
  }
  return currentUser.value.role === 'reviewer' ? currentUser.value : null;
});

watchEffect(() => {
  if (!reviewerUser.value) {
    router.replace('/');
  }
});

const handleLogout = () => {
  logout();
  router.replace('/');
};
</script>