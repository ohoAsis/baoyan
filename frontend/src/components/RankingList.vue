<template>
  <div class="space-y-6">
    <Card>
      <CardHeader>
        <div class="flex items-center justify-between">
          <div>
            <CardTitle class="flex items-center space-x-2">
              <Trophy class="h-6 w-6 text-yellow-500" />
              <span>保研加分排名公示</span>
            </CardTitle>
            <CardDescription class="flex items-center space-x-2 mt-2">
              <Calendar class="h-4 w-4" />
              <span>公示时间：{{ publishDate }}</span>
            </CardDescription>
          </div>
          <div class="flex space-x-2">
            <Button 
              variant="outline" 
              size="sm"
              class="transition-all duration-200 hover:bg-green-600 hover:text-white hover:border-green-600"
              @click="handleExportExcel"
            >
              <FileSpreadsheet class="h-4 w-4 mr-2" />
              导出Excel
            </Button>
            <Button 
              variant="outline" 
              size="sm"
              class="transition-all duration-200 hover:bg-green-600 hover:text-white hover:border-green-600"
              @click="handleExportPDF"
            >
              <FileDown class="h-4 w-4 mr-2" />
              导出PDF
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <div class="space-y-4">
          <div class="bg-blue-50 p-4 rounded-lg">
            <h4 class="text-sm">公示说明</h4>
            <ul class="text-sm text-gray-600 mt-2 space-y-1">
              <li>• 排名基于已审核通过的加分项目计算</li>
              <li>• 公示期为7个工作日，如有异议请及时联系管理员</li>
              <li>• 最终排名将作为保研推荐的重要参考依据</li>
              <li>• 数据实时更新，以最新审核结果为准</li>
            </ul>
          </div>

          <Table>
            <TableHeader>
              <TableRow>
                <TableHead class="text-center">排名</TableHead>
                <TableHead class="text-center">学号</TableHead>
                <TableHead class="text-center">姓名</TableHead>
                <TableHead class="text-center">总分</TableHead>
                <TableHead class="text-center">论文</TableHead>
                <TableHead class="text-center">竞赛</TableHead>
                <TableHead class="text-center">荣誉</TableHead>
                <TableHead class="text-center">专利</TableHead>
                <TableHead class="text-center">通过项目数</TableHead>
                <TableHead class="text-center">最后更新</TableHead>
                <TableHead class="text-center">状态</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow
                v-for="student in rankings"
                :key="student.studentId"
                :class="isCurrentUser(student.studentId) ? 'bg-blue-50' : ''"
              >
                <TableCell>
                  <div class="flex items-center justify-center">
                    <component :is="getRankIcon(student.rank)" />
                  </div>
                </TableCell>
                <TableCell>
                  <div class="flex items-center space-x-2">
                    <span>{{ student.studentId }}</span>
                    <Badge v-if="isCurrentUser(student.studentId)" variant="outline" class="text-xs">
                      我
                    </Badge>
                  </div>
                </TableCell>
                <TableCell>{{ student.studentName }}</TableCell>
                <TableCell class="text-center">
                  <span class="text-lg font-semibold">{{ student.totalPoints }}</span>
                  <span class="text-sm text-gray-500 ml-1">分</span>
                </TableCell>
                <TableCell class="text-center">
                  <span class="text-blue-600">{{ student.paperPoints || 0 }}</span>
                </TableCell>
                <TableCell class="text-center">
                  <span class="text-green-600">{{ student.competitionPoints || 0 }}</span>
                </TableCell>
                <TableCell class="text-center">
                  <span class="text-purple-600">{{ student.honorPoints || 0 }}</span>
                </TableCell>
                <TableCell class="text-center">
                  <span class="text-orange-600">{{ student.patentPoints || 0 }}</span>
                </TableCell>
                <TableCell class="text-center">{{ student.approvedApplications }}</TableCell>
                <TableCell class="text-center text-sm text-gray-500">{{ student.lastUpdate }}</TableCell>
                <TableCell class="text-center">
                  <component :is="getRankBadge(student.rank)" />
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>

          <div class="text-center space-y-2">
            <p class="text-sm text-gray-500">共有 {{ rankings.length }} 名学生参与排名</p>
            <Button variant="outline" size="sm">查看完整排名</Button>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, h } from 'vue';
import { Trophy, Medal, Award, Calendar, FileDown, FileSpreadsheet } from 'lucide-vue-next';
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
import Badge from './ui/Badge.vue';
import { studentsApi, papersApi, competitionsApi, honorsApi, patentsApi } from '../api';

interface RankingData {
  rank: number;
  studentId: string;
  studentName: string;
  totalPoints: number;
  approvedApplications: number;
  lastUpdate: string;
  paperPoints?: number;
  competitionPoints?: number;
  honorPoints?: number;
  patentPoints?: number;
}

const props = defineProps<{
	currentUserId?: string;
}>();

const rankings = ref<RankingData[]>([]);
const publishDate = ref('');

// 计算学生的总加分
const calculateTotalPoints = async (student: any) => {
  try {
    // 获取论文加分
    const papers = await papersApi.getByStudentId(student.studentId);
    const paperPoints = papers.reduce((sum, paper) => sum + (paper.computedScore || 0), 0);
    
    // 获取竞赛加分
    const competitions = await competitionsApi.getByStudentId(student.studentId);
    const competitionPoints = competitions.reduce((sum, competition) => sum + (competition.computedScore || 0), 0);
    
    // 获取荣誉加分
    const honors = await honorsApi.getByStudentId(student.studentId);
    const honorPoints = honors.reduce((sum, honor) => sum + (honor.computedScore || 0), 0);
    
    // 获取专利加分
    const patents = await patentsApi.getByStudentId(student.studentId);
    const patentPoints = patents.reduce((sum, patent) => sum + (patent.computedScore || 0), 0);
    
    // 计算通过项目数
    const approvedApplications = papers.length + competitions.length + honors.length + patents.length;
    
    return {
      totalPoints: paperPoints + competitionPoints + honorPoints + patentPoints,
      approvedApplications,
      paperPoints,
      competitionPoints,
      honorPoints,
      patentPoints
    };
  } catch (error) {
    console.error(`计算学生 ${student.studentId} 的总分失败:`, error);
    return {
      totalPoints: 0,
      approvedApplications: 0,
      paperPoints: 0,
      competitionPoints: 0,
      honorPoints: 0,
      patentPoints: 0
    };
  }
};

const loadFromBackend = async () => {
  try {
    // 获取学生列表
    const students = await studentsApi.list();
    
    // 为每个学生计算总分
    const rankingsData = await Promise.all(
      students.map(async (student) => {
        const pointsData = await calculateTotalPoints(student);
        
        return {
          rank: 0, // 稍后会重新计算排名
          studentId: student.studentId,
          studentName: student.name,
          totalPoints: pointsData.totalPoints,
          approvedApplications: pointsData.approvedApplications,
          lastUpdate: new Date().toISOString().split('T')[0],
          paperPoints: pointsData.paperPoints,
          competitionPoints: pointsData.competitionPoints,
          honorPoints: pointsData.honorPoints,
          patentPoints: pointsData.patentPoints
        };
      })
    );
    
    // 按总分降序排序并计算排名
    const sortedRankings = rankingsData
      .sort((a, b) => b.totalPoints - a.totalPoints)
      .map((student, index) => ({
        ...student,
        rank: index + 1
      }));
    
    rankings.value = sortedRankings;
    publishDate.value = new Date().toLocaleDateString('zh-CN');
  } catch (error) {
    console.error('从后端加载排名数据失败:', error);
    rankings.value = [];
    publishDate.value = new Date().toLocaleDateString('zh-CN');
  }
};

onMounted(() => {
	loadFromBackend();
});

const getRankIcon = (rank: number) => {
	if (rank === 1) return h(Trophy, { class: 'h-5 w-5 text-yellow-500' });
	if (rank === 2) return h(Medal, { class: 'h-5 w-5 text-gray-400' });
	if (rank === 3) return h(Award, { class: 'h-5 w-5 text-orange-500' });
	return h('span', { class: 'text-lg' }, rank);
};

const getRankBadge = (rank: number) => {
	if (rank <= 3) return h(Badge, { variant: 'default' }, () => '前三名');
	if (rank <= 10) return h(Badge, { variant: 'secondary' }, () => '前十名');
	return null;
};

const isCurrentUser = (studentId: string) => {
	return props.currentUserId === studentId;
};

const handleExportExcel = () => {
	alert('正在导出Excel文件...\n\n这是演示功能，实际应用中会生成包含排名数据的Excel文件');
};

const handleExportPDF = () => {
	alert('正在导出PDF文件...\n\n这是演示功能，实际应用中会生成包含排名数据的PDF文件');
};
</script>