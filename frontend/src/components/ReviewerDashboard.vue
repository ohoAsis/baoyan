<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center space-x-3">
            <Trophy class="h-8 w-8 text-blue-600" />
            <div>
              <h1 class="text-xl">保研加分小助手</h1>
              <p class="text-sm text-gray-500">审核老师端</p>
            </div>
          </div>
          <div class="flex items-center space-x-4">
            <div class="text-right">
              <p class="text-sm">{{ user.name }}</p>
              <p class="text-xs text-gray-500">审核老师</p>
            </div>
            <Button 
              variant="ghost" 
              class="transition-all duration-200 hover:bg-red-100 hover:text-red-600"
              @click="handleLogout"
            >
              <LogOut class="h-4 w-4 mr-2" />
              退出登录
            </Button>
          </div>
        </div>
      </div>
    </header>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="flex flex-col md:flex-row gap-6">
        <!-- Left Sidebar Navigation -->
        <div class="w-full md:w-64 flex-shrink-0">
          <div class="sticky top-8 space-y-1">
            <button
              v-for="tab in tabs"
              :key="tab.value"
              @click="activeTab = tab.value"
              :class="[
                'w-full text-left px-4 py-3 rounded-lg transition-colors flex items-center space-x-3',
                activeTab === tab.value
                  ? 'bg-blue-100 text-blue-700 font-medium'
                  : 'text-gray-600 hover:bg-gray-100'
              ]"
            >
              <component :is="tab.icon" class="h-5 w-5" />
              <span>{{ tab.label }}</span>
            </button>
          </div>
        </div>

        <!-- Main Content Area -->
        <div class="flex-1">
          <!-- Application Review Content -->
          <div v-show="activeTab === 'review'" class="space-y-6">
            <h2 class="text-xl">申请审核</h2>

            <Card>
              <CardHeader>
                <CardTitle>待审核申请</CardTitle>
                <CardDescription>点击申请可查看详情并进行审核</CardDescription>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>学生</TableHead>
                      <TableHead>类型</TableHead>
                      <TableHead>标题</TableHead>
                      <TableHead>申请分值</TableHead>
                      <TableHead>状态</TableHead>
                      <TableHead>提交时间</TableHead>
                      <TableHead>操作</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    <TableRow v-for="app in applications" :key="app.id">
                      <TableCell>
                        <div>
                          <p>{{ app.studentName }}</p>
                          <p class="text-sm text-gray-500">{{ app.studentId }}</p>
                        </div>
                      </TableCell>
                      <TableCell>{{ app.type }}</TableCell>
                      <TableCell>
                        <div>
                          <p>{{ app.title }}</p>
                          <p class="text-sm text-gray-500">{{ app.description }}</p>
                        </div>
                      </TableCell>
                      <TableCell>{{ app.points }}</TableCell>
                      <TableCell>
                        <StatusBadge :status="app.status" />
                      </TableCell>
                      <TableCell>{{ app.submittedAt }}</TableCell>
                      <TableCell>
                        <Button
                          variant="outline"
                          size="sm"
                          class="transition-all duration-200 hover:bg-blue-600 hover:text-white hover:border-blue-600"
                          @click="setSelectedApplication(app)"
                        >
                          {{ app.status === 'pending' ? '审核' : '查看' }}
                        </Button>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </div>

          <!-- Student Management Content -->
          <div v-show="activeTab === 'students'" class="space-y-6">
            <h2 class="text-xl">学生管理</h2>
            <Card>
              <CardHeader>
                <CardTitle>学生列表</CardTitle>
                <CardDescription>管理参与保研的学生账户信息</CardDescription>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>学号</TableHead>
                      <TableHead>姓名</TableHead>
                      <TableHead>申请数量</TableHead>
                      <TableHead>已获得分数</TableHead>
                      <TableHead>操作</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    <TableRow v-for="student in students" :key="student.id">
                      <TableCell>{{ student.studentId }}</TableCell>
                      <TableCell>{{ student.name }}</TableCell>
                      <TableCell>
                        {{ getStudentApplications(student.studentId!).length }}
                      </TableCell>
                      <TableCell>
                        {{ getStudentApprovedPoints(student.studentId!) }} 分
                      </TableCell>
                      <TableCell>
                        <Button 
                          variant="outline" 
                          size="sm"
                          class="transition-all duration-200 hover:bg-blue-600 hover:text-white hover:border-blue-600"
                          @click="setSelectedStudent(student)"
                        >
                          查看详情
                        </Button>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </div>

          <!-- Ranking Publication Content -->
          <div v-show="activeTab === 'ranking'" class="space-y-6">
            <RankingList />
          </div>
        </div>
      </div>
    </div>

    <ReviewPanel
      v-if="selectedApplication"
      :application="selectedApplication"
      @review-complete="handleReviewComplete"
      @close="selectedApplication = null"
    />

    <!-- 学生详情对话框 -->
    <Dialog :open="showStudentDetail" @update:open="showStudentDetail = $event">
      <DialogContent class="max-w-4xl max-h-[90vh] overflow-y-auto">
        <div class="space-y-6">
          <!-- 加分情况统计 -->
          <div>
            <h3 class="text-lg font-medium text-gray-900 mb-4">加分情况统计</h3>
            <div class="grid grid-cols-3 gap-4">
              <div class="bg-green-50 p-4 rounded-lg border border-green-200">
                <p class="text-xs text-green-600 font-medium">已通过</p>
                <p class="text-2xl font-bold text-green-700 mt-1">{{ studentApprovedPoints }} 分</p>
                <p class="text-xs text-green-600 mt-1">{{ studentApprovedCount }} 项申请</p>
              </div>
              <div class="bg-yellow-50 p-4 rounded-lg border border-yellow-200">
                <p class="text-xs text-yellow-600 font-medium">待审核</p>
                <p class="text-2xl font-bold text-yellow-700 mt-1">{{ studentPendingPoints }} 分</p>
                <p class="text-xs text-yellow-600 mt-1">{{ studentPendingCount }} 项申请</p>
              </div>
              <div class="bg-red-50 p-4 rounded-lg border border-red-200">
                <p class="text-xs text-red-600 font-medium">已驳回</p>
                <p class="text-2xl font-bold text-red-700 mt-1">{{ studentRejectedPoints }} 分</p>
                <p class="text-xs text-red-600 mt-1">{{ studentRejectedCount }} 项申请</p>
              </div>
            </div>
            <div class="mt-4 bg-blue-50 p-4 rounded-lg border border-blue-200">
              <div class="flex justify-between items-center">
                <p class="text-sm font-medium text-blue-900">总分</p>
                <p class="text-2xl font-bold text-blue-700">{{ studentTotalPoints }} 分</p>
              </div>
            </div>
          </div>

          <!-- 申请记录列表 -->
          <div>
            <h3 class="text-lg font-medium text-gray-900 mb-4">申请记录</h3>
            <div v-if="studentApplications.length > 0" class="space-y-3">
              <div
                v-for="app in studentApplications"
                :key="app.id"
                class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
              >
                <div class="flex justify-between items-start mb-3">
                  <div class="flex-1">
                    <div class="flex items-center space-x-3 mb-2">
                      <h4 class="text-sm font-medium text-gray-900">{{ app.title }}</h4>
                      <StatusBadge :status="app.status" />
                    </div>
                    <p class="text-xs text-gray-500 mb-1">类型：{{ app.type }}</p>
                    <p class="text-xs text-gray-600">{{ app.description }}</p>
                  </div>
                  <div class="text-right ml-4">
                    <p class="text-lg font-bold text-blue-600">{{ app.points }} 分</p>
                    <p class="text-xs text-gray-500 mt-1">{{ formatDate(app.submittedAt) }}</p>
                  </div>
                </div>
                
                <!-- 审核信息 -->
                <div v-if="app.reviewComment || app.reviewedAt" class="mt-3 pt-3 border-t border-gray-200">
                  <div v-if="app.reviewedAt" class="text-xs text-gray-500 mb-1">
                    审核时间：{{ formatDateTime(app.reviewedAt) }}
                  </div>
                  <div v-if="app.reviewComment" class="text-sm text-gray-700 bg-gray-50 p-2 rounded mt-1">
                    <p class="text-xs text-gray-500 mb-1">审核意见：</p>
                    <p class="whitespace-pre-wrap">{{ app.reviewComment }}</p>
                  </div>
                </div>

                <!-- 文件列表 -->
                <div v-if="app.files && app.files.length > 0" class="mt-3 pt-3 border-t border-gray-200">
                  <p class="text-xs text-gray-500 mb-2">附件文件：</p>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="file in app.files"
                      :key="typeof file === 'string' ? file : file.id || index"
                      class="text-xs text-blue-600 hover:text-blue-800 underline cursor-pointer"
                      @click="handleOpenFile(file)"
                    >
                      {{ getFileName(file) }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 text-gray-500">
              <p>该学生暂无申请记录</p>
            </div>
          </div>

          <!-- 关闭按钮 -->
          <div class="flex justify-end pt-4 border-t">
            <Button variant="outline" @click="showStudentDetail = false">关闭</Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import type { User, Application, Student } from '../types';
import { LogOut, Users, FileCheck, Trophy } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardDescription from './ui/CardDescription.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Table from './ui/Table.vue';
import TableBody from './ui/TableBody.vue';
import TableCell from './ui/TableCell.vue';
import TableHead from './ui/TableHead.vue';
import TableHeader from './ui/TableHeader.vue';
import TableRow from './ui/TableRow.vue';
import ReviewPanel from './ReviewPanel.vue';
import RankingList from './RankingList.vue';
import StatusBadge from './StatusBadge.vue';
import Dialog from './ui/SimpleDialog.vue';
import DialogContent from './ui/SimpleDialogContent.vue';
import { studentsApi, applicationsApi } from '../api';
import apiClient from '../api/client';

const { user } = defineProps<{
  user: User;
}>();

const emit = defineEmits<{
  logout: [];
}>();

const applications = ref<Application[]>([]);
const students = ref<Student[]>([]);
const selectedApplication = ref<Application | null>(null);
const selectedStudent = ref<Student | null>(null);
const showStudentDetail = ref(false);
const activeTab = ref('review');

// 定义导航标签页
const tabs = [
  { value: 'review', label: '申请审核', icon: FileCheck },
  { value: 'students', label: '学生管理', icon: Users },
  { value: 'ranking', label: '排名公示', icon: Trophy }
];

onMounted(async () => {
  try {
    // 获取学生数据
    const studentsData = await studentsApi.list();
    students.value = studentsData;
    
    // 获取申请数据
    const applicationsData = await applicationsApi.list();
    applications.value = applicationsData;
  } catch (error) {
    console.error('获取数据失败:', error);
    // 可以在这里添加错误提示
  }
});

const handleReviewComplete = async () => {
  try {
    // 重新获取申请列表以刷新数据
    const applicationsData = await applicationsApi.list();
    applications.value = applicationsData;
  } catch (error) {
    console.error('刷新申请列表失败:', error);
    // 可以在这里添加错误提示
  }
};

const getStudentApplications = (studentId: string) => {
  return applications.value.filter((app) => app.studentId === studentId);
};

const getStudentApprovedPoints = (studentId: string) => {
  return getStudentApplications(studentId)
    .filter((app) => app.status === 'approved')
    .reduce((sum, app) => sum + app.points, 0);
};


const setSelectedApplication = (app: Application) => {
  selectedApplication.value = app;
};

const setSelectedStudent = (student: Student) => {
  selectedStudent.value = student;
  showStudentDetail.value = true;
};

// 获取学生的申请记录
const studentApplications = computed(() => {
  if (!selectedStudent.value?.studentId) return [];
  return getStudentApplications(selectedStudent.value.studentId);
});

// 学生加分统计
const studentApprovedPoints = computed(() => {
  if (!selectedStudent.value?.studentId) return 0;
  return getStudentApprovedPoints(selectedStudent.value.studentId);
});

const studentApprovedCount = computed(() => {
  return studentApplications.value.filter(app => app.status === 'approved').length;
});

const studentPendingPoints = computed(() => {
  return studentApplications.value
    .filter(app => app.status === 'pending')
    .reduce((sum, app) => sum + app.points, 0);
});

const studentPendingCount = computed(() => {
  return studentApplications.value.filter(app => app.status === 'pending').length;
});

const studentRejectedPoints = computed(() => {
  return studentApplications.value
    .filter(app => app.status === 'rejected')
    .reduce((sum, app) => sum + app.points, 0);
});

const studentRejectedCount = computed(() => {
  return studentApplications.value.filter(app => app.status === 'rejected').length;
});

const studentTotalPoints = computed(() => {
  return studentApprovedPoints.value + studentPendingPoints.value;
});

// 格式化日期
const formatDate = (dateString: string): string => {
  if (!dateString) return '';
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  } catch {
    return dateString;
  }
};

// 格式化日期时间
const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return '';
  try {
    const date = new Date(dateTime);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch {
    return dateTime;
  }
};

// 获取文件名（支持 string / object(FileRecord) / null / undefined）
const getFileName = (file: any): string => {
  if (!file) return '';
  
  // 如果是字符串，按原逻辑从路径提取文件名
  if (typeof file === 'string') {
    const parts = file.split('/');
    return parts[parts.length - 1] || file;
  }
  
  // 如果是对象，优先使用 originalFileName，其次 storedFileName，最后从 fileUrl 提取
  if (typeof file === 'object') {
    if (file.originalFileName) return file.originalFileName;
    if (file.storedFileName) return file.storedFileName;
    if (file.fileUrl) {
      const parts = file.fileUrl.split('/');
      return parts[parts.length - 1] || '';
    }
  }
  
  // 其他情况返回 unknown
  return 'unknown';
};

// 处理文件打开/下载
const handleOpenFile = async (file: any) => {
  try {
    // 如果是字符串，直接作为URL打开
    if (typeof file === 'string') {
      window.open(file, '_blank');
      return;
    }
    
    // 如果是对象且有id，使用apiClient获取文件
    if (typeof file === 'object' && file.id) {
      // 使用 apiClient 获取文件，responseType 设为 blob
      const response = await apiClient.get(`/files/${file.id}`, { 
        responseType: 'blob' 
      });
      
      // 创建 blob URL
      const blobUrl = URL.createObjectURL(response.data);
      
      // 根据文件类型判断处理方式
      if (file.fileType && (file.fileType.startsWith('image/') || file.fileType === 'application/pdf')) {
        // 图片或 PDF，在新窗口预览
        window.open(blobUrl, '_blank');
      } else {
        // 其他类型，触发下载
        const link = document.createElement('a');
        link.href = blobUrl;
        link.download = file.originalFileName || file.storedFileName || 'unknown_file';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        // 释放 blob URL
        setTimeout(() => {
          URL.revokeObjectURL(blobUrl);
        }, 100);
      }
    } else if (typeof file === 'object' && file.fileUrl) {
      // 如果有fileUrl，直接打开
      window.open(file.fileUrl, '_blank');
    }
  } catch (error: any) {
    console.error('文件访问失败:', error);
    alert('文件访问失败或无权限');
  }
};

const handleLogout = () => {
  emit('logout');
};
</script>