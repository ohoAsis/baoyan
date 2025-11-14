import apiClient from './client';
import { Application } from '../types';

export interface CreateApplicationRequest {
  studentId: string;
  type: string;
  title: string;
  description: string;
  points: number;
  files?: File[];
}

export interface UpdateApplicationRequest {
  title?: string;
  description?: string;
  points?: number;
  status?: 'pending' | 'approved' | 'rejected';
  reviewComment?: string;
}

export const applicationsApi = {
  // 获取所有申请
  list: async () => {
    // 在实际应用中，这里会调用真实的API
    // const response = await apiClient.get<Application[]>('/applications');
    // return response.data;
    
    // 模拟申请列表API
    return new Promise<{ data: Application[] }>((resolve) => {
      setTimeout(() => {
        resolve({
          data: [
            {
              id: '1',
              studentId: '2021001001',
              studentName: '张三',
              type: 'competition',
              title: 'ACM程序设计竞赛',
              description: '参加ACM程序设计竞赛并获得省级二等奖',
              points: 15,
              status: 'approved',
              submitTime: '2024-02-20',
              reviewTime: '2024-02-22',
              reviewComment: '材料齐全，符合加分标准'
            },
            {
              id: '2',
              studentId: '2021001001',
              studentName: '张三',
              type: 'paper',
              title: '基于深度学习的图像识别研究',
              description: '发表SCI期刊论文一篇',
              points: 20,
              status: 'approved',
              submitTime: '2024-02-18',
              reviewTime: '2024-02-21',
              reviewComment: '论文质量高，影响因子符合要求'
            },
            {
              id: '3',
              studentId: '2021001002',
              studentName: '李四',
              type: 'patent',
              title: '智能物联网控制系统',
              description: '申请发明专利一项',
              points: 18,
              status: 'pending',
              submitTime: '2024-02-25',
              reviewTime: '',
              reviewComment: ''
            },
            {
              id: '4',
              studentId: '2021001003',
              studentName: '王五',
              type: 'competition',
              title: '数学建模竞赛',
              description: '参加全国大学生数学建模竞赛',
              points: 12,
              status: 'rejected',
              submitTime: '2024-02-15',
              reviewTime: '2024-02-18',
              reviewComment: '材料不完整，缺少获奖证书'
            }
          ]
        });
      }, 300);
    });
  },
  
  // 根据ID获取申请
  getById: async (id: string) => {
    // 在实际应用中，这里会调用真实的API
    // const response = await apiClient.get<Application>(`/applications/${id}`);
    // return response.data;
    
    // 模拟获取单个申请API
    return new Promise<Application>((resolve, reject) => {
      setTimeout(() => {
        const applications = [
          {
            id: '1',
            studentId: '2021001001',
            studentName: '张三',
            type: 'competition',
            title: 'ACM程序设计竞赛',
            description: '参加ACM程序设计竞赛并获得省级二等奖',
            points: 15,
            status: 'approved',
            submitTime: '2024-02-20',
            reviewTime: '2024-02-22',
            reviewComment: '材料齐全，符合加分标准'
          },
          {
            id: '2',
            studentId: '2021001001',
            studentName: '张三',
            type: 'paper',
            title: '基于深度学习的图像识别研究',
            description: '发表SCI期刊论文一篇',
            points: 20,
            status: 'approved',
            submitTime: '2024-02-18',
            reviewTime: '2024-02-21',
            reviewComment: '论文质量高，影响因子符合要求'
          },
          {
            id: '3',
            studentId: '2021001002',
            studentName: '李四',
            type: 'patent',
            title: '智能物联网控制系统',
            description: '申请发明专利一项',
            points: 18,
            status: 'pending',
            submitTime: '2024-02-25',
            reviewTime: '',
            reviewComment: ''
          },
          {
            id: '4',
            studentId: '2021001003',
            studentName: '王五',
            type: 'competition',
            title: '数学建模竞赛',
            description: '参加全国大学生数学建模竞赛',
            points: 12,
            status: 'rejected',
            submitTime: '2024-02-15',
            reviewTime: '2024-02-18',
            reviewComment: '材料不完整，缺少获奖证书'
          }
        ];
        
        const application = applications.find(a => a.id === id);
        if (application) {
          resolve(application);
        } else {
          reject(new Error('申请不存在'));
        }
      }, 300);
    });
  },
  
  // 根据学生ID获取申请
  getByStudentId: async (studentId: string) => {
    // 在实际应用中，这里会调用真实的API
    // const response = await apiClient.get<Application[]>(`/applications/student/${studentId}`);
    // return response.data;
    
    // 模拟根据学生ID获取申请API
    return new Promise<Application[]>((resolve) => {
      setTimeout(() => {
        const applications = [
          {
            id: '1',
            studentId: '2021001001',
            studentName: '张三',
            type: 'competition',
            title: 'ACM程序设计竞赛',
            description: '参加ACM程序设计竞赛并获得省级二等奖',
            points: 15,
            status: 'approved',
            submitTime: '2024-02-20',
            reviewTime: '2024-02-22',
            reviewComment: '材料齐全，符合加分标准'
          },
          {
            id: '2',
            studentId: '2021001001',
            studentName: '张三',
            type: 'paper',
            title: '基于深度学习的图像识别研究',
            description: '发表SCI期刊论文一篇',
            points: 20,
            status: 'approved',
            submitTime: '2024-02-18',
            reviewTime: '2024-02-21',
            reviewComment: '论文质量高，影响因子符合要求'
          },
          {
            id: '3',
            studentId: '2021001002',
            studentName: '李四',
            type: 'patent',
            title: '智能物联网控制系统',
            description: '申请发明专利一项',
            points: 18,
            status: 'pending',
            submitTime: '2024-02-25',
            reviewTime: '',
            reviewComment: ''
          },
          {
            id: '4',
            studentId: '2021001003',
            studentName: '王五',
            type: 'competition',
            title: '数学建模竞赛',
            description: '参加全国大学生数学建模竞赛',
            points: 12,
            status: 'rejected',
            submitTime: '2024-02-15',
            reviewTime: '2024-02-18',
            reviewComment: '材料不完整，缺少获奖证书'
          }
        ];
        
        const studentApplications = applications.filter(a => a.studentId === studentId);
        resolve(studentApplications);
      }, 300);
    });
  },
  
  // 创建申请
  create: async (application: CreateApplicationRequest) => {
    // 在实际应用中，这里会调用真实的API
    // const formData = new FormData();
    // formData.append('studentId', application.studentId);
    // formData.append('type', application.type);
    // formData.append('title', application.title);
    // formData.append('description', application.description);
    // formData.append('points', application.points.toString());
    // if (application.files) {
    //   application.files.forEach(file => {
    //     formData.append('files', file);
    //   });
    // }
    // const response = await apiClient.post<Application>('/applications', formData, {
    //   headers: {
    //     'Content-Type': 'multipart/form-data'
    //   }
    // });
    // return response.data;
    
    // 模拟创建申请API
    return new Promise<Application>((resolve) => {
      setTimeout(() => {
        const newApplication: Application = {
          id: Date.now().toString(),
          studentId: application.studentId,
          studentName: '张三', // 在实际应用中会从后端获取
          type: application.type as any,
          title: application.title,
          description: application.description,
          points: application.points,
          status: 'pending',
          submitTime: new Date().toISOString().split('T')[0],
          reviewTime: '',
          reviewComment: ''
        };
        resolve(newApplication);
      }, 300);
    });
  },
  
  // 更新申请
  update: async (id: string, application: UpdateApplicationRequest) => {
    // 在实际应用中，这里会调用真实的API
    // const response = await apiClient.put<Application>(`/applications/${id}`, application);
    // return response.data;
    
    // 模拟更新申请API
    return new Promise<Application>((resolve, reject) => {
      setTimeout(() => {
        const applications = [
          {
            id: '1',
            studentId: '2021001001',
            studentName: '张三',
            type: 'competition',
            title: 'ACM程序设计竞赛',
            description: '参加ACM程序设计竞赛并获得省级二等奖',
            points: 15,
            status: 'approved',
            submitTime: '2024-02-20',
            reviewTime: '2024-02-22',
            reviewComment: '材料齐全，符合加分标准'
          },
          {
            id: '2',
            studentId: '2021001001',
            studentName: '张三',
            type: 'paper',
            title: '基于深度学习的图像识别研究',
            description: '发表SCI期刊论文一篇',
            points: 20,
            status: 'approved',
            submitTime: '2024-02-18',
            reviewTime: '2024-02-21',
            reviewComment: '论文质量高，影响因子符合要求'
          },
          {
            id: '3',
            studentId: '2021001002',
            studentName: '李四',
            type: 'patent',
            title: '智能物联网控制系统',
            description: '申请发明专利一项',
            points: 18,
            status: 'pending',
            submitTime: '2024-02-25',
            reviewTime: '',
            reviewComment: ''
          },
          {
            id: '4',
            studentId: '2021001003',
            studentName: '王五',
            type: 'competition',
            title: '数学建模竞赛',
            description: '参加全国大学生数学建模竞赛',
            points: 12,
            status: 'rejected',
            submitTime: '2024-02-15',
            reviewTime: '2024-02-18',
            reviewComment: '材料不完整，缺少获奖证书'
          }
        ];
        
        const applicationIndex = applications.findIndex(a => a.id === id);
        if (applicationIndex !== -1) {
          const updatedApplication = { 
            ...applications[applicationIndex], 
            ...application,
            reviewTime: application.status ? new Date().toISOString().split('T')[0] : applications[applicationIndex].reviewTime
          };
          resolve(updatedApplication);
        } else {
          reject(new Error('申请不存在'));
        }
      }, 300);
    });
  },
  
  // 删除申请
  delete: async (id: string) => {
    // 在实际应用中，这里会调用真实的API
    // await apiClient.delete(`/applications/${id}`);
    
    // 模拟删除申请API
    return new Promise<void>((resolve, reject) => {
      setTimeout(() => {
        const applications = [
          {
            id: '1',
            studentId: '2021001001',
            studentName: '张三',
            type: 'competition',
            title: 'ACM程序设计竞赛',
            description: '参加ACM程序设计竞赛并获得省级二等奖',
            points: 15,
            status: 'approved',
            submitTime: '2024-02-20',
            reviewTime: '2024-02-22',
            reviewComment: '材料齐全，符合加分标准'
          },
          {
            id: '2',
            studentId: '2021001001',
            studentName: '张三',
            type: 'paper',
            title: '基于深度学习的图像识别研究',
            description: '发表SCI期刊论文一篇',
            points: 20,
            status: 'approved',
            submitTime: '2024-02-18',
            reviewTime: '2024-02-21',
            reviewComment: '论文质量高，影响因子符合要求'
          },
          {
            id: '3',
            studentId: '2021001002',
            studentName: '李四',
            type: 'patent',
            title: '智能物联网控制系统',
            description: '申请发明专利一项',
            points: 18,
            status: 'pending',
            submitTime: '2024-02-25',
            reviewTime: '',
            reviewComment: ''
          },
          {
            id: '4',
            studentId: '2021001003',
            studentName: '王五',
            type: 'competition',
            title: '数学建模竞赛',
            description: '参加全国大学生数学建模竞赛',
            points: 12,
            status: 'rejected',
            submitTime: '2024-02-15',
            reviewTime: '2024-02-18',
            reviewComment: '材料不完整，缺少获奖证书'
          }
        ];
        
        const applicationExists = applications.some(a => a.id === id);
        if (applicationExists) {
          resolve();
        } else {
          reject(new Error('申请不存在'));
        }
      }, 300);
    });
  }
};