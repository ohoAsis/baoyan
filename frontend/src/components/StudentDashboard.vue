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
            <Button variant="ghost" @click="handleLogout">
              <LogOut class="h-4 w-4 mr-2" />
              退出登录
            </Button>
          </div>
        </div>
      </div>
    </header>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Score Overview -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle class="text-sm">当前总分</CardTitle>
            <Trophy class="h-4 w-4 text-yellow-600" />
          </CardHeader>
          <CardContent>
            <div class="text-2xl">{{ totalPoints }} 分</div>
            <p class="text-xs text-muted-foreground">已通过审核的加分项</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle class="text-sm">待审核申请</CardTitle>
            <FileText class="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div class="text-2xl">
              {{ pendingCount }} 项
            </div>
            <p class="text-xs text-muted-foreground">等待管理员审核</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle class="text-sm">总申请数</CardTitle>
            <Users class="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div class="text-2xl">{{ applications.length }} 项</div>
            <p class="text-xs text-muted-foreground">累计提交申请</p>
          </CardContent>
        </Card>
      </div>

      <!-- Main Content -->
      <Tabs v-model="activeTab" class="space-y-6">
        <TabsList>
          <TabsTrigger value="upload">
            <Upload class="h-4 w-4 mr-2" />
            材料上传
          </TabsTrigger>
          <TabsTrigger value="applications">我的申请</TabsTrigger>
          <TabsTrigger value="ranking">排名公示</TabsTrigger>
        </TabsList>

        <TabsContent value="upload">
          <UploadPage
            :student-name="user.name"
            :student-id="user.studentId!"
            :major="user.major || '未设置专业'"
            :applications="applications"
            @submit="handleApplicationSubmit"
          />
        </TabsContent>

        <TabsContent value="applications" class="space-y-6">
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
        </TabsContent>

        <TabsContent value="ranking">
          <RankingList :current-user-id="user.studentId!" />
        </TabsContent>
      </Tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import type { User, Application } from '../App.vue';
import { Trophy, FileText, Users, Upload, LogOut } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardDescription from './ui/CardDescription.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Tabs from './ui/Tabs.vue';
import TabsContent from './ui/TabsContent.vue';
import TabsList from './ui/TabsList.vue';
import TabsTrigger from './ui/TabsTrigger.vue';
import Table from './ui/Table.vue';
import TableBody from './ui/TableBody.vue';
import TableCell from './ui/TableCell.vue';
import TableHead from './ui/TableHead.vue';
import TableHeader from './ui/TableHeader.vue';
import TableRow from './ui/TableRow.vue';
import UploadPage from './UploadPage.vue';
import RankingList from './RankingList.vue';
import StatusBadge from './StatusBadge.vue';

const props = defineProps<{
  user: User;
}>();

const emit = defineEmits<{
  logout: [];
}>();

const applications = ref<Application[]>([]);
const activeTab = ref('upload');

const totalPoints = computed(() => {
  return applications.value
    .filter((app) => app.status === 'approved')
    .reduce((sum, app) => sum + app.points, 0);
});

const pendingCount = computed(() => {
  return applications.value.filter((app) => app.status === 'pending').length;
});

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
    detail: { message: '申请提交成功！等待管理员审核' },
  });
  window.dispatchEvent(toastEvent);
};

const handleLogout = () => {
  emit('logout');
};
</script>