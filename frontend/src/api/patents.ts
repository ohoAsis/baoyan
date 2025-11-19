import apiClient from './client';
import { Patent, CreatePatentRequest } from '../types/index';

export const patentsApi = {
  // 获取学生专利记录
  getByStudentId: async (studentId: string): Promise<Patent[]> => {
    const response = await apiClient.get<Patent[]>(`/students/${studentId}/patents`);
    return response.data;
  },
  
  // 添加学生专利记录
  create: async (studentId: string, patent: CreatePatentRequest): Promise<Patent> => {
    const response = await apiClient.post<Patent>(`/students/${studentId}/patents`, patent);
    return response.data;
  },
  
  // 获取单个专利记录
  getById: async (studentId: string, patentId: string): Promise<Patent> => {
    const response = await apiClient.get<Patent>(`/students/${studentId}/patents/${patentId}`);
    return response.data;
  },
  
  // 更新专利记录
  update: async (studentId: string, patentId: string, patent: Partial<CreatePatentRequest>): Promise<Patent> => {
    const response = await apiClient.put<Patent>(`/students/${studentId}/patents/${patentId}`, patent);
    return response.data;
  },
  
  // 删除专利记录
  delete: async (studentId: string, patentId: string): Promise<void> => {
    await apiClient.delete(`/students/${studentId}/patents/${patentId}`);
  }
};