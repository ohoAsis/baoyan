import axios from 'axios';
import { useAuth } from '../stores/auth';
import { getCurrentUser } from '../stores/auth';

// 创建axios实例
const apiClient = axios.create({
  baseURL: '/api', // 基础URL，可以根据实际后端API地址调整
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器 - 添加Authorization header
apiClient.interceptors.request.use(
  (config) => {
    // 从localStorage获取token
    const token = localStorage.getItem('auth_token');
    
    // 如果token存在，添加到请求头
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器 - 处理错误和401
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // 处理401错误
    if (error.response?.status === 401) {
      // 清除用户状态和token
      const { logout } = useAuth();
      logout();
      
      // 跳转到登录页
      window.location.href = '/login';
    }
    
    // 处理其他错误
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default apiClient;