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
                <TableHead class="w-20">排名</TableHead>
                <TableHead>学号</TableHead>
                <TableHead>姓名</TableHead>
                <TableHead class="text-center">总分</TableHead>
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
                  <span class="text-lg">{{ student.totalPoints }}</span>
                  <span class="text-sm text-gray-500 ml-1">分</span>
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
import { studentsApi } from '../api/students';

interface RankingData {
	rank: number;
	studentId: string;
	studentName: string;
	totalPoints: number;
	approvedApplications: number;
	lastUpdate: string;
}

const props = defineProps<{
	currentUserId?: string;
}>();

const rankings = ref<RankingData[]>([]);
const publishDate = ref('');

const loadFromBackend = async () => {
	try {
		const res = await studentsApi.list();
		const list = Array.isArray(res.data) ? res.data : [];
		// 由于暂未接入申请/审核数据，这里以0分占位，rank 按姓名排序示例
		const mapped: RankingData[] = list
			.map((s: any) => ({
				studentId: s.studentId ?? '',
				studentName: s.name ?? '',
				totalPoints: 0,
				approvedApplications: 0,
				lastUpdate: new Date().toISOString().split('T')[0],
				rank: 0
			}))
			.sort((a, b) => a.studentId.localeCompare(b.studentId))
			.map((s, idx) => ({ ...s, rank: idx + 1 }));
		rankings.value = mapped;
		publishDate.value = new Date().toLocaleDateString('zh-CN');
	} catch (e) {
		// 回退到原有的演示数据
		const mockRankings: RankingData[] = [
			{ rank: 1, studentId: '2021001001', studentName: '张三', totalPoints: 35, approvedApplications: 2, lastUpdate: '2024-02-25' },
			{ rank: 2, studentId: '2021001002', studentName: '李四', totalPoints: 28, approvedApplications: 2, lastUpdate: '2024-02-24' },
			{ rank: 3, studentId: '2021001003', studentName: '王五', totalPoints: 22, approvedApplications: 1, lastUpdate: '2024-02-23' },
			{ rank: 4, studentId: '2021001004', studentName: '赵六', totalPoints: 18, approvedApplications: 1, lastUpdate: '2024-02-22' },
			{ rank: 5, studentId: '2021001005', studentName: '钱七', totalPoints: 15, approvedApplications: 1, lastUpdate: '2024-02-21' },
			{ rank: 6, studentId: '2021001006', studentName: '孙八', totalPoints: 12, approvedApplications: 1, lastUpdate: '2024-02-20' },
			{ rank: 7, studentId: '2021001007', studentName: '周九', totalPoints: 8, approvedApplications: 1, lastUpdate: '2024-02-19' },
			{ rank: 8, studentId: '2021001008', studentName: '吴十', totalPoints: 5, approvedApplications: 1, lastUpdate: '2024-02-18' }
		];
		rankings.value = mockRankings;
		publishDate.value = '2024年2月25日';
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