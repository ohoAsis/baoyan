// 用户角色类型
export type UserRole = 'student' | 'reviewer' | 'admin';

// 用户信息类型
export interface User {
  id: string;
  name: string;
  username: string;
  studentId?: string;
  major?: string;
  role: UserRole;
  email?: string;
}

// 申请信息类型
export interface Application {
  id: string;
  studentId: string;
  studentName: string;
  type: string;
  title: string;
  description: string;
  points: number;
  status: 'pending' | 'approved' | 'rejected';
  submittedAt: string;
  reviewedAt?: string;
  reviewComment?: string;
  files: string[];
}

// 学生信息类型
export interface Student {
  id: string;
  studentId: string;
  name: string;
  gender?: string;
  major?: string;
  grade?: string;
  className?: string;
  gpa?: number;
  rankingPercent?: number;
  foreignLanguageScore?: number;
  foreignLanguageType?: string;
  eligibleForExemption?: boolean;
}

// 创建申请请求类型
export interface CreateApplicationRequest {
  studentId: string;
  studentName: string;
  type: string;
  title: string;
  description: string;
  points: number;
  files?: string[];
}

// 更新申请请求类型
export interface UpdateApplicationRequest {
  status?: 'pending' | 'approved' | 'rejected';
  reviewComment?: string;
}