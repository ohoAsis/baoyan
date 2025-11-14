import { ref, computed } from 'vue';
import { Student } from '../types/index';

// 学生状态
const students = ref<Student[]>([]);
const isLoading = ref(false);
const error = ref<string | null>(null);

// 计算属性
const studentCount = computed(() => students.value.length);

// 学生相关方法
export function useStudents() {
  const setStudents = (studentList: Student[]) => {
    students.value = studentList;
  };

  const addStudent = (student: Student) => {
    students.value.push(student);
  };

  const updateStudent = (id: string, updates: Partial<Student>) => {
    const index = students.value.findIndex(student => student.id === id);
    if (index !== -1) {
      students.value[index] = { ...students.value[index], ...updates };
    }
  };

  const getStudentById = (id: string) => {
    return students.value.find(student => student.id === id);
  };

  const getStudentByStudentId = (studentId: string) => {
    return students.value.find(student => student.studentId === studentId);
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
    students,
    isLoading,
    error,
    studentCount,
    setStudents,
    addStudent,
    updateStudent,
    getStudentById,
    getStudentByStudentId,
    setLoading,
    setError,
    clearError
  };
}