import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { useAuth } from './stores/auth';
import './index.css';

const app = createApp(App);

// 初始化应用
async function initApp() {
  // 1. 初始化用户信息
  const { initializeUser } = useAuth();
  await initializeUser();
  
  // 2. 挂载路由
  app.use(router);
  
  // 3. 挂载应用
  app.mount('#app');
}

// 启动应用
initApp();