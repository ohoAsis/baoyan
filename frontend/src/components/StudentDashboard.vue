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
              <p class="text-sm text-gray-500">学生端</p>
            </div>
          </div>
          <div class="flex items-center space-x-4">
            <div class="text-right">
              <p class="text-sm">{{ user.name }}</p>
              <p class="text-xs text-gray-500">学号: {{ user.studentId }}</p>
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
          <!-- Material Upload Content -->
          <div v-show="activeTab === 'upload'" class="space-y-6">
            <UploadPage
              :student-name="user.name"
              :student-id="user.studentId!"
              :major="user.major || '未设置专业'"
              :applications="applications"
              @submit="reloadApplications"
            />
          </div>

          <!-- My Applications Content -->
          <div v-show="activeTab === 'applications'" class="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>我的加分申请</CardTitle>
                <CardDescription>查看您提交的所有加分申请及其审核状态</CardDescription>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>类型</TableHead>
                      <TableHead>标题</TableHead>
                      <TableHead>分值</TableHead>
                      <TableHead>状态</TableHead>
                      <TableHead>提交时间</TableHead>
                      <TableHead>审核意见</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    <TableRow v-for="app in applications" :key="app.id">
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
                        <div v-if="app.reviewComment" class="max-w-xs">
                          <p class="text-sm">{{ app.reviewComment }}</p>
                          <p v-if="app.reviewedAt" class="text-xs text-gray-500">
                            {{ app.reviewedAt }}
                          </p>
                        </div>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import type { User, Application } from '../types';
import { Trophy, Upload, FileText, LogOut } from 'lucide-vue-next';
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
import UploadPage from './UploadPage.vue';
import StatusBadge from './StatusBadge.vue';
import { applicationsApi } from '../api/applications';
import { useAuth } from '../stores/auth';

const props = defineProps<{
  user: User;
}>();

const emit = defineEmits<{
  logout: [];
}>();

const applications = ref<Application[]>([]);
const activeTab = ref('upload');
const { currentUser } = useAuth();

const tabs = [
  { value: 'upload', label: '材料上传', icon: Upload },
  { value: 'applications', label: '我的申请', icon: FileText }
];

// 从后端加载申请数据
const reloadApplications = async () => {
  try {
    // 获取当前登录学生的 studentId
    const studentId = currentUser.value?.studentId || props.user.studentId;
    
    if (!studentId) {
      console.error('无法获取学生ID');
      return;
    }
    
    // 调用 API 获取申请数据
    const applicationsData = await applicationsApi.getByStudentId(studentId);
    
    // 处理返回的数据，适配类型
    applications.value = applicationsData.map(app => {
      // 处理 files 字段，如果是字符串则转换为数组
      let files: string[] = [];
      if (typeof app.files === 'string') {
        try {
          files = JSON.parse(app.files);
        } catch (e) {
          console.error('解析 files 字段失败:', e);
          files = [];
        }
      } else if (Array.isArray(app.files)) {
        files = app.files;
      }
      
      // 处理 studentId，如果存在 student.studentId 则使用它
      let studentId = app.studentId;
      if (app.student && typeof app.student === 'object' && app.student.studentId) {
        studentId = app.student.studentId;
      }
      
      return {
        ...app,
        studentId,
        files
      };
    });
  } catch (error) {
    console.error('加载申请数据失败:', error);
  }
};

onMounted(() => {
  reloadApplications();
});

const handleLogout = () => {
  emit('logout');
};
</script>