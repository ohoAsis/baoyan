import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import StudentDashboardView from '../views/StudentDashboardView.vue';
import ReviewerDashboardView from '../views/ReviewerDashboardView.vue';
import { getCurrentUser } from '../stores/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
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
      path: '/reviewer',
      name: 'reviewer-dashboard',
      component: ReviewerDashboardView,
      meta: { requiresAuth: true, role: 'reviewer' },
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
    next({ path: currentUser.role === 'student' ? '/student' : '/reviewer' });
    return;
  }

  if (to.path === '/' && currentUser) {
    next({ path: currentUser.role === 'student' ? '/student' : '/reviewer' });
    return;
  }

  next();
});

export default router;
