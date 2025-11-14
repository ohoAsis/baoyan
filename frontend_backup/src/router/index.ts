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
      path: '/admin',
      name: 'Admin',
      component: () => import('../views/AdminView.vue'),
      meta: { requiresAuth: true, role: 'admin' }
    }
  ]
});

// 路由守卫
router.beforeEach((to, from, next) => {
  // 直接从localStorage获取用户信息，避免在路由守卫中使用store
  const savedUser = localStorage.getItem('currentUser');
  const currentUser = savedUser ? JSON.parse(savedUser) : null;
  
  // 检查是否需要认证
  if (to.meta.requiresAuth && !currentUser) {
    next('/login');
    return;
  }
  
  // 检查角色权限
  if (to.meta.role && currentUser?.role !== to.meta.role) {
    // 如果是学生但访问管理员页面，重定向到学生页面
    if (currentUser?.role === 'student') {
      next('/student');
    } 
    // 如果是管理员但访问学生页面，重定向到管理员页面
    else if (currentUser?.role === 'admin') {
      next('/admin');
    }
    // 其他情况重定向到登录页
    else {
      next('/login');
    }
    return;
  }
  
  // 如果已登录但访问登录页，重定向到相应角色页面
  if (to.path === '/login' && currentUser) {
    if (currentUser.role === 'student') {
      next('/student');
    } else {
      next('/admin');
    }
    return;
  }
  
  next();
});

export default router;