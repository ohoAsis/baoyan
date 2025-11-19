import apiClient from './client';
import { Honor, CreateHonorRequest } from '../types/index';

export const honorsApi = {
  // 获取学生荣誉记录
  getByStudentId: async (studentId: string): Promise<Honor[]> => {
    const response = await apiClient.get<Honor[]>(`/students/${studentId}/honors`);
    return response.data;
  },
  
  // 添加学生荣誉记录
  create: async (studentId: string, honor: CreateHonorRequest): Promise<Honor> => {
    const response = await apiClient.post<Honor>(`/students/${studentId}/honors`, honor);
    return response.data;
  },
  
  // 获取单个荣誉记录
  getById: async (studentId: string, honorId: string): Promise<Honor> => {
    const response = await apiClient.get<Honor>(`/students/${studentId}/honors/${honorId}`);
    return response.data;
  },
  
  // 更新荣誉记录
  update: async (studentId: string, honorId: string, honor: Partial<CreateHonorRequest>): Promise<Honor> => {
    const response = await apiClient.put<Honor>(`/students/${studentId}/honors/${honorId}`, honor);
    return response.data;
  },
  
  // 删除荣誉记录
  delete: async (studentId: string, honorId: string): Promise<void> => {
    await apiClient.delete(`/students/${studentId}/honors/${honorId}`);
  }
};