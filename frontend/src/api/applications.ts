import apiClient from './client';
import { Application, CreateApplicationRequest, UpdateApplicationRequest } from '../types/index';

export const applicationsApi = {
  // 获取所有申请
  list: async (): Promise<Application[]> => {
    const response = await apiClient.get<Application[]>('/applications');
    return response.data;
  },
  
  // 根据ID获取申请
  getById: async (id: string): Promise<Application> => {
    const response = await apiClient.get<Application>(`/applications/${id}`);
    return response.data;
  },
  
  // 根据学生ID获取申请
  getByStudentId: async (studentId: string): Promise<Application[]> => {
    const response = await apiClient.get<Application[]>(`/students/${studentId}/applications`);
    return response.data;
  },
  
  // 创建申请
  create: async (application: CreateApplicationRequest): Promise<Application> => {
    const response = await apiClient.post<Application>('/applications', application);
    return response.data;
  },
  
  // 更新申请
  update: async (id: string, updateData: UpdateApplicationRequest): Promise<Application> => {
    const response = await apiClient.put<Application>(`/applications/${id}`, updateData);
    return response.data;
  },
  
  // 删除申请
  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/applications/${id}`);
  }
};