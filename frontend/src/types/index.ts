export type UserRole = 'student' | 'reviewer';

export interface User {
  id: string;
  name: string;
  username: string;
  studentId?: string;
  major?: string;
  role: UserRole;
  email?: string;
}

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

export interface CreateApplicationRequest {
  studentId: string;
  studentName: string;
  type: string;
  title: string;
  description: string;
  points: number;
  files?: string[];
}

export interface UpdateApplicationRequest {
  status?: 'pending' | 'approved' | 'rejected';
  reviewComment?: string;
}

