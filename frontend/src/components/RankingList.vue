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
                <TableHead class="text-center">通过项目数</TableHead>
                <TableHead class="text-center">最后更新</TableHead>
                <TableHead class="text-center">状态</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow
                v-for="student in displayedRankings"
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
            <Button 
              variant="outline" 
              size="sm"
              @click="showFullRanking = !showFullRanking"
              class="transition-all duration-200 hover:bg-blue-600 hover:text-white hover:border-blue-600"
            >
              {{ showFullRanking ? '收起排名' : '查看完整排名' }}
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, h, computed } from 'vue';
import { Trophy, Medal, Award, Calendar, FileDown, FileSpreadsheet } from 'lucide-vue-next';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
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
import { studentsApi, applicationsApi } from '../api';

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
const showFullRanking = ref(false);

// 计算学生的总加分
const calculateTotalPoints = async (student: any) => {
  try {
    // 获取学生的所有申请
    const applications = await applicationsApi.getByStudentId(student.studentId);
    
    // 过滤状态为"approved"的申请
    const approvedApplications = applications.filter(app => app.status === 'approved');
    
    // 计算已通过申请的points总和
    const totalApprovedPoints = approvedApplications.reduce((sum, app) => sum + (app.points || 0), 0);
    
    return {
      totalPoints: totalApprovedPoints,
      approvedApplications: approvedApplications.length
    };
  } catch (error) {
    console.error(`计算学生 ${student.studentId} 的总分失败:`, error);
    return {
      totalPoints: 0,
      approvedApplications: 0
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
          lastUpdate: new Date().toISOString().split('T')[0]
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

// 显示的排名列表：默认只显示前3名，点击后显示全部
const displayedRankings = computed(() => {
	if (showFullRanking.value) {
		return rankings.value;
	}
	return rankings.value.slice(0, 3);
});

const handleExportExcel = () => {
	try {
		// 准备Excel数据
		const excelData = rankings.value.map((student, index) => ({
			排名: student.rank,
			学号: student.studentId,
			姓名: student.studentName,
			总分: student.totalPoints,
			通过项目数: student.approvedApplications,
			最后更新: student.lastUpdate,
			状态: student.rank <= 3 ? '前三名' : student.rank <= 10 ? '前十名' : '其他'
		}));

		// 创建工作簿
		const wb = XLSX.utils.book_new();
		
		// 创建工作表
		const ws = XLSX.utils.json_to_sheet(excelData);
		
		// 设置列宽
		const colWidths = [
			{ wch: 8 },  // 排名
			{ wch: 15 }, // 学号
			{ wch: 12 }, // 姓名
			{ wch: 10 }, // 总分
			{ wch: 12 }, // 通过项目数
			{ wch: 12 }, // 最后更新
			{ wch: 10 }  // 状态
		];
		ws['!cols'] = colWidths;
		
		// 添加工作表到工作簿
		XLSX.utils.book_append_sheet(wb, ws, '保研加分排名');
		
		// 生成文件名
		const fileName = `保研加分排名_${publishDate.value.replace(/\//g, '-')}.xlsx`;
		
		// 导出文件
		XLSX.writeFile(wb, fileName);
		
		console.log('Excel文件导出成功:', fileName);
	} catch (error) {
		console.error('导出Excel文件失败:', error);
		alert('导出Excel文件失败，请稍后重试');
	}
};

const handleExportPDF = async () => {
	try {
		// 创建临时容器用于导出
		const exportContainer = document.createElement('div');
		exportContainer.style.position = 'absolute';
		exportContainer.style.left = '-9999px';
		exportContainer.style.width = '1200px';
		exportContainer.style.backgroundColor = 'white';
		exportContainer.style.padding = '20px';
		exportContainer.style.fontFamily = 'Arial, "Microsoft YaHei", "SimHei", sans-serif';
		
		// 添加标题
		const title = document.createElement('h1');
		title.textContent = '保研加分排名公示';
		title.style.fontSize = '24px';
		title.style.fontWeight = 'bold';
		title.style.marginBottom = '10px';
		title.style.textAlign = 'center';
		exportContainer.appendChild(title);
		
		// 添加副标题
		const subtitle = document.createElement('div');
		subtitle.style.fontSize = '14px';
		subtitle.style.color = '#666';
		subtitle.style.marginBottom = '20px';
		subtitle.style.textAlign = 'center';
		subtitle.innerHTML = `公示时间：${publishDate.value}<br>共 ${rankings.value.length} 名学生参与排名`;
		exportContainer.appendChild(subtitle);
		
		// 创建表格
		const table = document.createElement('table');
		table.style.width = '100%';
		table.style.borderCollapse = 'collapse';
		table.style.fontSize = '12px';
		
		// 表头
		const thead = document.createElement('thead');
		const headerRow = document.createElement('tr');
		headerRow.style.backgroundColor = '#3b82f6';
		headerRow.style.color = 'white';
		headerRow.style.fontWeight = 'bold';
		
		const headers = ['排名', '学号', '姓名', '总分', '通过项目数', '最后更新', '状态'];
		headers.forEach(headerText => {
			const th = document.createElement('th');
			th.textContent = headerText;
			th.style.padding = '10px';
			th.style.border = '1px solid #ddd';
			th.style.textAlign = 'center';
			headerRow.appendChild(th);
		});
		thead.appendChild(headerRow);
		table.appendChild(thead);
		
		// 表格内容
		const tbody = document.createElement('tbody');
		rankings.value.forEach((student, index) => {
			const row = document.createElement('tr');
			if (index % 2 === 0) {
				row.style.backgroundColor = '#f9fafb';
			}
			
			const cells = [
				student.rank.toString(),
				student.studentId,
				student.studentName,
				student.totalPoints.toString(),
				student.approvedApplications.toString(),
				student.lastUpdate,
				student.rank <= 3 ? '前三名' : student.rank <= 10 ? '前十名' : '其他'
			];
			
			cells.forEach(cellText => {
				const td = document.createElement('td');
				td.textContent = cellText;
				td.style.padding = '8px';
				td.style.border = '1px solid #ddd';
				td.style.textAlign = 'center';
				row.appendChild(td);
			});
			
			tbody.appendChild(row);
		});
		table.appendChild(tbody);
		exportContainer.appendChild(table);
		
		// 添加到页面
		document.body.appendChild(exportContainer);
		
		// 转换为canvas
		const canvas = await html2canvas(exportContainer, {
			scale: 2,
			useCORS: true,
			backgroundColor: '#ffffff',
			logging: false
		});
		
		// 从页面移除临时容器
		document.body.removeChild(exportContainer);
		
		// 创建PDF
		const imgWidth = 297; // A4横向宽度（mm）
		const imgHeight = (canvas.height * imgWidth) / canvas.width;
		const doc = new jsPDF('landscape', 'mm', 'a4');
		
		// 如果内容超过一页，需要分页
		const pageHeight = doc.internal.pageSize.getHeight();
		let heightLeft = imgHeight;
		let position = 0;
		
		// 添加第一页
		doc.addImage(canvas.toDataURL('image/png'), 'PNG', 0, position, imgWidth, imgHeight);
		heightLeft -= pageHeight;
		
		// 如果内容超过一页，添加后续页面
		while (heightLeft >= 0) {
			position = heightLeft - imgHeight;
			doc.addPage();
			doc.addImage(canvas.toDataURL('image/png'), 'PNG', 0, position, imgWidth, imgHeight);
			heightLeft -= pageHeight;
		}
		
		// 添加页脚
		const pageCount = doc.getNumberOfPages();
		for (let i = 1; i <= pageCount; i++) {
			doc.setPage(i);
			// 使用英文页脚避免字体问题
			doc.setFontSize(8);
			doc.setTextColor(150, 150, 150);
			doc.text(
				`Page ${i} / ${pageCount}`,
				doc.internal.pageSize.getWidth() / 2,
				doc.internal.pageSize.getHeight() - 10,
				{ align: 'center' }
			);
		}
		
		// 生成文件名
		const fileName = `保研加分排名_${publishDate.value.replace(/\//g, '-')}.pdf`;
		
		// 保存文件
		doc.save(fileName);
		
		console.log('PDF文件导出成功:', fileName);
	} catch (error) {
		console.error('导出PDF文件失败:', error);
		alert('导出PDF文件失败，请稍后重试');
	}
};
</script>