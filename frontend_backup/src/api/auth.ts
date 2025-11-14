import apiClient from './client';
import { User } from '../types';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  user: User;
  token: string;
}

export const authApi = {
  // 登录
  login: async (credentials: LoginRequest) => {
    // 在实际应用中，这里会调用真实的API
    // const response = await apiClient.post<LoginResponse>('/auth/login', credentials);
    // return response.data;
    
    // 模拟登录API
    return new Promise<LoginResponse>((resolve, reject) => {
      setTimeout(() => {
        // 模拟用户验证
        const { username, password } = credentials;
        
        // 演示账号
        if (username === 'admin' && password === 'admin123') {
          resolve({
            user: {
              id: '1',
              username: 'admin',
              name: '管理员',
              role: 'admin',
              email: 'admin@example.com'
            },
            token: 'mock-admin-token'
          });
        } else if (username === 'student' && password === 'student123') {
          resolve({
            user: {
              id: '2',
              username: 'student',
              name: '学生',
              role: 'student',
              email: 'student@example.com'
            },
            token: 'mock-student-token'
          });
        } else if (username === 'student/2021001001' && password === 'student123') {
          resolve({
            user: {
              id: '2',
              username: 'student/2021001001',
              name: '张三',
              role: 'student',
              email: 'student@example.com'
            },
            token: 'mock-student-token'
          });
        } else {
          reject(new Error('用户名或密码错误'));
        }
      }, 500);
    });
  },
  
  // 登出
  logout: async () => {
    // 在实际应用中，这里会调用真实的API
    // await apiClient.post('/auth/logout');
    
    // 模拟登出API
    return new Promise<void>((resolve) => {
      setTimeout(() => {
        resolve();
      }, 200);
    });
  },
  
  // 获取当前用户信息
  getCurrentUser: async () => {
    // 在实际应用中，这里会调用真实的API
    // const response = await apiClient.get<User>('/auth/me');
    // return response.data;
    
    // 模拟获取当前用户API
    return new Promise<User>((resolve, reject) => {
      setTimeout(() => {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
          try {
            resolve(JSON.parse(savedUser));
          } catch (e) {
            reject(new Error('无法解析用户信息'));
          }
        } else {
          reject(new Error('未找到用户信息'));
        }
      }, 200);
    });
  }
};