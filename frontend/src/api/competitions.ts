import apiClient from './client';
import { Competition, CreateCompetitionRequest } from '../types/index';

export const competitionsApi = {
  // 获取学生竞赛记录
  getByStudentId: async (studentId: string): Promise<Competition[]> => {
    const response = await apiClient.get<Competition[]>(`/students/${studentId}/competitions`);
    return response.data;
  },
  
  // 添加学生竞赛记录
  create: async (studentId: string, competition: CreateCompetitionRequest): Promise<Competition> => {
    const response = await apiClient.post<Competition>(`/students/${studentId}/competitions`, competition);
    return response.data;
  },
  
  // 获取单个竞赛记录
  getById: async (studentId: string, competitionId: string): Promise<Competition> => {
    const response = await apiClient.get<Competition>(`/students/${studentId}/competitions/${competitionId}`);
    return response.data;
  },
  
  // 更新竞赛记录
  update: async (studentId: string, competitionId: string, competition: Partial<CreateCompetitionRequest>): Promise<Competition> => {
    const response = await apiClient.put<Competition>(`/students/${studentId}/competitions/${competitionId}`, competition);
    return response.data;
  },
  
  // 删除竞赛记录
  delete: async (studentId: string, competitionId: string): Promise<void> => {
    await apiClient.delete(`/students/${studentId}/competitions/${competitionId}`);
  }
};