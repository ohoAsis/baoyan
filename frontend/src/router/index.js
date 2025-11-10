import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import StudentDashboardView from '../views/StudentDashboardView.vue';
import AdminDashboardView from '../views/AdminDashboardView.vue';
import { getCurrentUser } from '../stores/auth';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/student',
      name: 'student-dashboard',
      component: StudentDashboardView,
      meta: { requiresAuth: true, role: 'student' },
    },
    {
      path: '/admin',
      name: 'admin-dashboard',
      component: AdminDashboardView,
      meta: { requiresAuth: true, role: 'admin' },
    },
  ],
});

router.beforeEach((to, from, next) => {
  const currentUser = getCurrentUser();
  const requiresAuth = to.meta?.requiresAuth;
  const requiredRole = to.meta?.role;

  if (requiresAuth && !currentUser) {
    next({ path: '/' });
    return;
  }

  if (requiredRole && currentUser && currentUser.role !== requiredRole) {
    next({ path: currentUser.role === 'student' ? '/student' : '/admin' });
    return;
  }

  if (to.path === '/' && currentUser) {
    next({ path: currentUser.role === 'student' ? '/student' : '/admin' });
    return;
  }

  next();
});

export default router;
