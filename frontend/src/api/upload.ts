import apiClient from './client';

export interface UploadFileResponse {
  success: boolean;
  message?: string;
  fileUrl?: string;
  fileName?: string;
  originalFileName?: string;
  fileSize?: number;
}

export interface UploadMultipleFilesResponse {
  success: boolean;
  files?: Array<{
    fileUrl: string;
    fileName: string;
    originalFileName: string;
    fileSize: number;
  }>;
  errors?: string[];
  message?: string;
}

export const uploadApi = {
  // 上传单个文件（支持进度回调）
  uploadFile: async (
    file: File,
    onProgress?: (progress: number) => void
  ): Promise<UploadFileResponse> => {
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await apiClient.post<UploadFileResponse>('/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress && progressEvent.total) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          onProgress(progress);
        }
      },
    });
    
    return response.data;
  },
  
  // 批量上传文件
  uploadMultipleFiles: async (files: File[]): Promise<UploadMultipleFilesResponse> => {
    const formData = new FormData();
    files.forEach((file) => {
      formData.append('files', file);
    });
    
    const response = await apiClient.post<UploadMultipleFilesResponse>('/upload/multiple', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    return response.data;
  },
};

