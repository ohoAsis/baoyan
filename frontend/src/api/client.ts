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
    
    // 如果是FormData请求，删除默认的Content-Type，让axios自动生成
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type'];
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
    // 处理401错误 - 诊断模式降级
    if (error.response?.status === 401) {
      // 打印详细401响应内容，方便调试
      console.error('401 Unauthorized Response:', {
        url: error.config?.url,
        method: error.config?.method,
        status: error.response?.status,
        statusText: error.response?.statusText,
        data: error.response?.data,
        headers: error.response?.headers
      });
      
      // 简单提示错误，不执行logout/redirect
      alert('请求失败：未授权访问（401），请检查控制台获取详细信息');
    } else {
      // 处理其他错误
      console.error('API Error:', error);
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;