<template>
  <div class="min-h-screen bg-gray-50">
    <LoginPage v-if="!currentUser" @login="handleLogin" />
    <StudentDashboard
      v-else-if="currentUser.role === 'student'"
      :user="currentUser"
      @logout="handleLogout"
    />
    <AdminDashboard
      v-else
      :user="currentUser"
      @logout="handleLogout"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import LoginPage from './components/LoginPage.vue';
import StudentDashboard from './components/StudentDashboard.vue';
import AdminDashboard from './components/AdminDashboard.vue';

export type UserRole = 'student' | 'admin';

export interface User {
  id: string;
  name: string;
  studentId?: string;
  major?: string;
  role: UserRole;
}

export interface Application {
  id: string;
  studentId: string;
  studentName: string;
  type: string;
  title: string;
  description: string;
  points: number;
  status: 'pending' | 'approved' | 'rejected';
  submittedAt: string;
  reviewedAt?: string;
  reviewComment?: string;
  files: string[];
}

const currentUser = ref<User | null>(null);

const handleLogin = (user: User) => {
  currentUser.value = user;
};

const handleLogout = () => {
  currentUser.value = null;
};
</script>