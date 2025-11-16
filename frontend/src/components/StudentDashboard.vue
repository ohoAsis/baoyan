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
              @submit="handleApplicationSubmit"
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

const props = defineProps<{
  user: User;
}>();

const emit = defineEmits<{
  logout: [];
}>();

const applications = ref<Application[]>([]);
const activeTab = ref('upload');

const tabs = [
  { value: 'upload', label: '材料上传', icon: Upload },
  { value: 'applications', label: '我的申请', icon: FileText }
];

onMounted(() => {
  const mockApplications: Application[] = [
    {
      id: '1',
      studentId: props.user.studentId!,
      studentName: props.user.name,
      type: '学科竞赛',
      title: '全国大学生数学建模竞赛',
      description: '获得省级一等奖',
      points: 15,
      status: 'approved',
      submittedAt: '2024-01-15',
      reviewedAt: '2024-01-18',
      reviewComment: '材料齐全，符合加分标准',
      files: ['竞赛证书.pdf'],
    },
    {
      id: '2',
      studentId: props.user.studentId!,
      studentName: props.user.name,
      type: '学术论文',
      title: 'AI在教育领域的应用研究',
      description: '第一作者，发表在核心期刊',
      points: 20,
      status: 'pending',
      submittedAt: '2024-02-20',
      files: ['论文截图.png', '期刊封面.jpg'],
    },
    {
      id: '3',
      studentId: props.user.studentId!,
      studentName: props.user.name,
      type: '科研项目',
      title: '国家级大学生创新创业训练计划',
      description: '项目负责人，已结题',
      points: 10,
      status: 'rejected',
      submittedAt: '2024-01-10',
      reviewedAt: '2024-01-12',
      reviewComment: '缺少结题报告，请补充相关材料',
      files: ['项目申报书.pdf'],
    },
  ];
  applications.value = mockApplications;
});

const handleApplicationSubmit = (
  newApplication: Omit<Application, 'id' | 'studentId' | 'studentName' | 'status' | 'submittedAt'>
) => {
  const application: Application = {
    ...newApplication,
    id: Math.random().toString(36).substr(2, 9),
    studentId: props.user.studentId!,
    studentName: props.user.name,
    status: 'pending',
    submittedAt: new Date().toISOString().split('T')[0],
  };
  applications.value.push(application);

  const toastEvent = new CustomEvent('show-toast', {
    detail: { message: '申请提交成功！等待审核老师审核' },
  });
  window.dispatchEvent(toastEvent);
};

const handleLogout = () => {
  emit('logout');
};
</script>