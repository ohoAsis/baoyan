import apiClient from './client';
import { Paper, CreatePaperRequest } from '../types/index';

export const papersApi = {
  // 获取学生论文记录
  getByStudentId: async (studentId: string): Promise<Paper[]> => {
    const response = await apiClient.get<Paper[]>(`/students/${studentId}/papers`);
    return response.data;
  },
  
  // 添加学生论文记录
  create: async (studentId: string, paper: CreatePaperRequest): Promise<Paper> => {
    const response = await apiClient.post<Paper>(`/students/${studentId}/papers`, paper);
    return response.data;
  },
  
  // 获取单个论文记录
  getById: async (studentId: string, paperId: string): Promise<Paper> => {
    const response = await apiClient.get<Paper>(`/students/${studentId}/papers/${paperId}`);
    return response.data;
  },
  
  // 更新论文记录
  update: async (studentId: string, paperId: string, paper: Partial<CreatePaperRequest>): Promise<Paper> => {
    const response = await apiClient.put<Paper>(`/students/${studentId}/papers/${paperId}`, paper);
    return response.data;
  },
  
  // 删除论文记录
  delete: async (studentId: string, paperId: string): Promise<void> => {
    await apiClient.delete(`/students/${studentId}/papers/${paperId}`);
  }
};