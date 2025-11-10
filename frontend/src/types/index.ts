export type UserRole = 'student' | 'admin';

export interface User {
  id: string;
  name: string;
  studentId?: string;
  major?: string;
  role: UserRole;
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

