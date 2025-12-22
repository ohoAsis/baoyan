import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/LoginView.vue')
    },
    {
        path: '/student',
        name: 'Student',
        component: () => import('../views/StudentView.vue'),
        meta: { requiresAuth: true, role: 'student' }
      },
      {
        path: '/reviewer',
        name: 'Reviewer',
        component: () => import('../views/ReviewerView.vue'),
        meta: { requiresAuth: true, role: 'reviewer' }
      },
      {
        path: '/admin',
        name: 'Admin',
        component: () => import('../views/AdminHome.vue'),
        meta: { requiresAuth: true, role: 'admin' }
      }
  ]
});

// 路由守卫
router.beforeEach((to, _from, next) => {
  // 获取token和用户信息
  const token = localStorage.getItem('auth_token');
  const savedUser = localStorage.getItem('currentUser');
  const currentUser = savedUser ? JSON.parse(savedUser) : null;
  
  // 1. 首先处理 /login 路径
  if (to.path === '/login') {
    // 如果已登录，根据role跳转到对应首页
    if (token && currentUser && currentUser.role) {
      if (currentUser.role === 'student') {
        next('/student');
      } else if (currentUser.role === 'reviewer') {
        next('/reviewer');
      } else if (currentUser.role === 'admin') {
        next('/admin');
      } else {
        // 未知角色，允许访问登录页
        next();
      }
    } else {
      // 未登录，允许访问登录页
      next();
    }
    return;
  }
  
  // 2. 处理需要认证的页面
  if (to.meta.requiresAuth === true) {
    // 未登录，重定向到登录页
    if (!token || !currentUser || !currentUser.role) {
      next('/login');
      return;
    }
    
    // 3. 检查角色权限
    if (to.meta.role && currentUser.role !== to.meta.role) {
      // 角色不匹配，重定向到登录页
      next('/login');
      return;
    }
  }
  
  // 4. 其他情况，允许访问
  next();
});

export default router;