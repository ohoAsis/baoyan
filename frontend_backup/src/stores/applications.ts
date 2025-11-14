import { ref, computed } from 'vue';
import { Application } from '../types';

// 应用状态
const applications = ref<Application[]>([]);
const isLoading = ref(false);
const error = ref<string | null>(null);

// 计算属性
const pendingApplications = computed(() => 
  applications.value.filter(app => app.status === 'pending')
);

const approvedApplications = computed(() => 
  applications.value.filter(app => app.status === 'approved')
);

const rejectedApplications = computed(() => 
  applications.value.filter(app => app.status === 'rejected')
);

// 应用相关方法
export function useApplications() {
  const setApplications = (apps: Application[]) => {
    applications.value = apps;
  };

  const addApplication = (app: Application) => {
    applications.value.push(app);
  };

  const updateApplication = (id: string, updates: Partial<Application>) => {
    const index = applications.value.findIndex(app => app.id === id);
    if (index !== -1) {
      applications.value[index] = { ...applications.value[index], ...updates };
    }
  };

  const getApplicationsByStudentId = (studentId: string) => {
    return applications.value.filter(app => app.studentId === studentId);
  };

  const setLoading = (loading: boolean) => {
    isLoading.value = loading;
  };

  const setError = (errorMessage: string | null) => {
    error.value = errorMessage;
  };

  const clearError = () => {
    error.value = null;
  };

  return {
    applications,
    isLoading,
    error,
    pendingApplications,
    approvedApplications,
    rejectedApplications,
    setApplications,
    addApplication,
    updateApplication,
    getApplicationsByStudentId,
    setLoading,
    setError,
    clearError
  };
}