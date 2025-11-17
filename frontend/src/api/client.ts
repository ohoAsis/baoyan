import axios from 'axios';

// 创建axios实例
const apiClient = axios.create({
  baseURL: '/api', // 基础URL，可以根据实际后端API地址调整
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 响应拦截器 - 处理错误
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // 处理通用错误
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default apiClient;