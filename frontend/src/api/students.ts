import apiClient from './client';
import { Student } from '../types/index';

export interface StudentPayload {
	studentId: string
	name: string
	gender?: string
	major?: string
	grade?: string
	className?: string
	gpa?: number
}

export const studentsApi = {
	list: async (): Promise<Student[]> => {
		const response = await apiClient.get<Student[]>('/students');
		return response.data;
	},
	get: async (id: string): Promise<Student> => {
		const response = await apiClient.get<Student>(`/students/${id}`);
		return response.data;
	},
	create: async (data: StudentPayload): Promise<Student> => {
		const response = await apiClient.post<Student>('/students', data);
		return response.data;
	},
	update: async (id: string, data: Partial<StudentPayload>): Promise<Student> => {
		const response = await apiClient.put<Student>(`/students/${id}`, data);
		return response.data;
	},
	remove: async (id: string): Promise<void> => {
		await apiClient.delete(`/students/${id}`);
	},
	exists: async (studentId: string): Promise<boolean> => {
		const response = await apiClient.get<{exists: boolean}>(`/students/check/${studentId}`);
		return response.data.exists;
	}
}
