import apiClient from './client';
import { User } from '../types';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  role: string;
  realName: string;
  token: string;
}

export const authApi = {
  // 登录
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    // 调用真实登录API
    const response = await apiClient.post<LoginResponse>('/auth/login', credentials);
    
    // 健壮性检查
    if (!response.data) {
      throw new Error('登录返回异常，无数据');
    }
    
    // 检查必要字段是否存在
    if (!response.data.token || !response.data.role || !response.data.userId) {
      throw new Error('登录返回异常，缺少必要字段');
    }
    
    return response.data;
  },
  
  // 登出
  logout: async (): Promise<void> => {
    // 调用真实登出API
    await apiClient.post('/auth/logout');
  },
  
  // 获取当前用户信息
  getCurrentUser: async (): Promise<User> => {
    // 调用真实获取当前用户API
    const response = await apiClient.get<User>('/auth/me');
    return response.data;
  }
};