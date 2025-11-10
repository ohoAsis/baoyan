import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../views/LoginPage.vue';
import StudentDashboard from '../views/StudentDashboard.vue';
import AdminDashboard from '../views/AdminDashboard.vue';

const routes = [
  {
    path: '/',
    name: 'login',
    component: LoginPage
  },
  {
    path: '/student',
    name: 'student-dashboard',
    component: StudentDashboard
  },
  {
    path: '/admin',
    name: 'admin-dashboard',
    component: AdminDashboard
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;
