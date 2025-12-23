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
        redirect: '/admin/users',
        meta: { requiresAuth: true, role: 'admin' },
        children: [
          {
            path: 'users',
            name: 'AdminUsers',
            component: () => import('../views/admin/UsersView.vue')
          }
        ]
      }
  ]
});

// 导入auth store
import { useAuth } from '../stores/auth';

// 路由守卫
router.beforeEach(async (to, _from, next) => {
  const { isInitialized, currentUser, initializeUser, getToken } = useAuth();
  
  // 1. 如果用户信息尚未初始化，先初始化
  if (!isInitialized.value) {
    await initializeUser();
  }
  
  // 2. 获取最新的token和用户信息
  const token = getToken();
  const user = currentUser.value;
  
  // 3. 首先处理 /login 路径
  if (to.path === '/login') {
    // 如果已登录，根据role跳转到对应首页
    if (token && user && user.role) {
      if (user.role === 'student') {
        next('/student');
      } else if (user.role === 'reviewer') {
        next('/reviewer');
      } else if (user.role === 'admin') {
        next('/admin/users');
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
  
  // 4. 处理需要认证的页面
  if (to.meta.requiresAuth === true) {
    // 未登录，重定向到登录页
    if (!token || !user || !user.role) {
      next('/login');
      return;
    }
    
    // 5. 检查角色权限
    if (to.meta.role && user.role !== to.meta.role) {
      // 角色不匹配，重定向到登录页
      next('/login');
      return;
    }
  }
  
  // 6. 其他情况，允许访问
  next();
});

export default router;