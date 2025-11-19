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
            <div class="flex justify-between items-center mb-6">
              <h2 class="text-xl">排名公示</h2>
              <Button 
                class="transition-all duration-200 hover:bg-green-600 hover:text-white hover:border-green-600"
                @click="exportRankings"
              >
                <Download class="h-4 w-4 mr-2" />
                导出排名表格
              </Button>
            </div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import type { User, Application, Student } from '../types';
import { LogOut, Users, FileCheck, Trophy, Download } from 'lucide-vue-next';
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
import { studentsApi, applicationsApi } from '../api';

const { user } = defineProps<{
  user: User;
}>();

const emit = defineEmits<{
  logout: [];
}>();

const applications = ref<Application[]>([]);
const students = ref<Student[]>([]);
const selectedApplication = ref<Application | null>(null);
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

const exportRankings = () => {
  const rankings = students.value
    .map((student) => {
      const approvedApps = applications.value.filter(
        (app) => app.studentId === student.studentId && app.status === 'approved'
      );
      const totalPoints = approvedApps.reduce((sum, app) => sum + app.points, 0);
      return {
        studentId: student.studentId,
        name: student.name,
        totalPoints,
        applications: approvedApps.length,
      };
    })
    .sort((a, b) => b.totalPoints - a.totalPoints);

  console.log('导出排名表格:', rankings);
  alert('排名表格已导出到控制台（在实际应用中将下载Excel文件）');
};

const setSelectedApplication = (app: Application) => {
  selectedApplication.value = app;
};

const handleLogout = () => {
  emit('logout');
};
</script>