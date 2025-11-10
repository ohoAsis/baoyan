<template>
  <div class="space-y-6">
    <!-- 学生信息卡片 -->
    <Card class="bg-gradient-to-r from-blue-50 to-indigo-50 border-blue-200">
      <CardHeader>
        <CardTitle>学生信息</CardTitle>
      </CardHeader>
      <CardContent>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <p class="text-sm text-gray-600">姓名</p>
            <p class="text-lg">{{ studentName }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600">学号</p>
            <p class="text-lg">{{ studentId }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600">专业</p>
            <p class="text-lg">{{ major }}</p>
          </div>
        </div>
      </CardContent>
    </Card>

    <!-- 四个上传卡片区域 -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <Card
        v-for="category in UPLOAD_CATEGORIES"
        :key="category.id"
        class="hover:shadow-lg transition-shadow"
      >
        <CardHeader>
          <div class="flex items-center space-x-3">
            <div class="p-2 rounded-lg bg-gray-100">
              <component :is="category.icon" :class="`h-6 w-6 ${category.color}`" />
            </div>
            <div>
              <CardTitle>{{ category.title }}</CardTitle>
              <CardDescription class="mt-1">{{ category.description }}</CardDescription>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <Button class="w-full" variant="outline" @click="handleOpenUpload(category)">
            <Upload class="h-4 w-4 mr-2" />
            上传材料
          </Button>
          <p class="text-xs text-gray-500 mt-3">支持格式：PDF、JPG、PNG</p>
        </CardContent>
      </Card>
    </div>

    <!-- 审核进度卡片 -->
    <Card>
      <CardHeader>
        <CardTitle>加分项审核进度</CardTitle>
        <CardDescription>已提交的申请材料审核情况</CardDescription>
      </CardHeader>
      <CardContent>
        <div class="space-y-3">
          <div class="flex justify-between text-sm">
            <span>
              已审核：
              <span class="text-green-600">{{ reviewedApplications }}</span> 项
            </span>
            <span>
              待审核：
              <span class="text-orange-600">{{ totalApplications - reviewedApplications }}</span> 项
            </span>
            <span>
              总计：
              <span class="text-blue-600">{{ totalApplications }}</span> 项
            </span>
          </div>
          <Progress :value="progressPercentage" class="h-2" />
          <p class="text-xs text-gray-500">
            {{
              totalApplications === 0
                ? '暂无申请记录，请点击上方卡片上传材料'
                : `${reviewedApplications} / ${totalApplications} 项已完成审核 (${Math.round(progressPercentage)}%)`
            }}
          </p>
        </div>
      </CardContent>
    </Card>

    <!-- 上传Dialog -->
    <Dialog v-model:open="showUploadDialog" @update:open="handleCloseDialog">
      <DialogContent class="max-w-2xl max-h-[80vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{{ currentCategory?.title }}</DialogTitle>
          <DialogDescription>{{ currentCategory?.description }}</DialogDescription>
        </DialogHeader>

        <div class="space-y-6">
          <!-- 具体分类选择 -->
          <div class="space-y-2">
            <Label for="subType">具体类型</Label>
            <Select v-model="formData.subType" @update:model-value="formData.points = 0">
              <SelectTrigger>
                <SelectValue placeholder="请选择具体类型" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem
                  v-for="type in currentCategory?.types"
                  :key="type.value"
                  :value="type.value"
                >
                  {{ type.label }} (最高{{ type.maxPoints }}分)
                </SelectItem>
              </SelectContent>
            </Select>
          </div>

          <!-- 标题 -->
          <div class="space-y-2">
            <Label for="title">申请标题</Label>
            <Input
              id="title"
              placeholder="请输入申请标题（如：全国大学生数学建模竞赛一等奖）"
              v-model="formData.title"
            />
          </div>

          <!-- 详细描述 -->
          <div class="space-y-2">
            <Label for="description">详细描述</Label>
            <Textarea
              id="description"
              placeholder="请详细描述您的成果（如：2024年全国大学生数学建模竞赛省级赛区一等奖，团队第一完成人）"
              rows="4"
              v-model="formData.description"
            />
          </div>

          <!-- 申请分值 -->
          <div class="space-y-2">
            <Label for="points">申请分值</Label>
            <div class="flex items-center space-x-2">
              <Input
                id="points"
                type="number"
                :min="1"
                :max="maxPoints"
                placeholder="请输入申请分值"
                :value="formData.points || ''"
                @input="formData.points = parseInt(($event.target as HTMLInputElement).value) || 0"
              />
              <span v-if="maxPoints > 0" class="text-sm text-gray-500">最高 {{ maxPoints }} 分</span>
            </div>
            <p v-if="formData.points > maxPoints && maxPoints > 0" class="text-sm text-red-600">
              申请分值不能超过该类型的最高分值
            </p>
          </div>

          <!-- 文件上传 -->
          <div class="space-y-2">
            <Label>上传证明文件</Label>
            <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 space-y-4">
              <Button type="button" variant="outline" class="w-full" @click="handleFileUpload">
                <Upload class="h-4 w-4 mr-2" />
                选择文件
              </Button>

              <div v-if="files.length > 0" class="space-y-2">
                <p class="text-sm">已上传文件：</p>
                <div
                  v-for="(file, index) in files"
                  :key="index"
                  class="flex items-center justify-between bg-gray-50 p-2 rounded"
                >
                  <span class="text-sm">{{ file }}</span>
                  <Button type="button" variant="ghost" size="sm" @click="removeFile(index)">
                    <X class="h-4 w-4" />
                  </Button>
                </div>
              </div>

              <p class="text-xs text-gray-500">
                支持上传PDF、JPG、PNG格式文件，建议上传清晰的扫描件或照片
              </p>
            </div>
          </div>

          <!-- 按钮 -->
          <div class="flex justify-end space-x-4">
            <Button variant="outline" @click="handleCloseDialog">取消</Button>
            <Button
              @click="handleSubmit"
              :disabled="
                !formData.subType ||
                !formData.title ||
                !formData.description ||
                formData.points <= 0 ||
                formData.points > maxPoints ||
                files.length === 0
              "
            >
              提交申请
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import type { Application } from '../types';
import { Trophy, FileText, Lightbulb, Award, Upload, X } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardDescription from './ui/CardDescription.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Dialog from './ui/Dialog.vue';
import DialogContent from './ui/DialogContent.vue';
import DialogDescription from './ui/DialogDescription.vue';
import DialogHeader from './ui/DialogHeader.vue';
import DialogTitle from './ui/DialogTitle.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import Textarea from './ui/Textarea.vue';
import Select from './ui/Select.vue';
import SelectContent from './ui/SelectContent.vue';
import SelectItem from './ui/SelectItem.vue';
import SelectTrigger from './ui/SelectTrigger.vue';
import SelectValue from './ui/SelectValue.vue';
import Progress from './ui/Progress.vue';

type UploadCategory = 'competition' | 'paper' | 'patent' | 'comprehensive';

interface CategoryConfig {
  id: UploadCategory;
  title: string;
  description: string;
  icon: any;
  color: string;
  types: Array<{
    value: string;
    label: string;
    maxPoints: number;
  }>;
}

const UPLOAD_CATEGORIES: CategoryConfig[] = [
  {
    id: 'competition',
    title: '竞赛加分',
    description: '上传竞赛获奖证书（国家级、省级、校级等）',
    icon: Trophy,
    color: 'text-yellow-600',
    types: [
      { value: '国家级竞赛特等奖', label: '国家级竞赛特等奖', maxPoints: 25 },
      { value: '国家级竞赛一等奖', label: '国家级竞赛一等奖', maxPoints: 20 },
      { value: '省级竞赛一等奖', label: '省级竞赛一等奖', maxPoints: 15 },
      { value: '省级竞赛二等奖', label: '省级竞赛二等奖', maxPoints: 10 },
      { value: '校级竞赛一等奖', label: '校级竞赛一等奖', maxPoints: 5 },
    ],
  },
  {
    id: 'paper',
    title: '论文加分',
    description: '上传发表论文或会议收录证明（PDF/图片）',
    icon: FileText,
    color: 'text-blue-600',
    types: [
      { value: 'SCI一区论文', label: 'SCI一区论文（第一作者）', maxPoints: 30 },
      { value: 'SCI二区论文', label: 'SCI二区论文（第一作者）', maxPoints: 25 },
      { value: '核心期刊论文', label: '核心期刊论文（第一作者）', maxPoints: 20 },
      { value: '国际会议论文', label: '国际会议论文（第一作者）', maxPoints: 15 },
      { value: '普通期刊论文', label: '普通期刊论文（第一作者）', maxPoints: 10 },
    ],
  },
  {
    id: 'patent',
    title: '专利加分',
    description: '上传国家发明专利或实用新型专利证书',
    icon: Lightbulb,
    color: 'text-purple-600',
    types: [
      { value: '发明专利', label: '国家发明专利（第一发明人）', maxPoints: 25 },
      { value: '实用新型专利', label: '实用新型专利（第一发明人）', maxPoints: 15 },
      { value: '外观设计专利', label: '外观设计专利（第一发明人）', maxPoints: 10 },
      { value: '软件著作权', label: '软件著作权（第一作者）', maxPoints: 8 },
    ],
  },
  {
    id: 'comprehensive',
    title: '综合表现',
    description: '上传荣誉证书、志愿服务证明、学生工作证明等',
    icon: Award,
    color: 'text-green-600',
    types: [
      { value: '国家级荣誉', label: '国家级荣誉称号', maxPoints: 15 },
      { value: '省级荣誉', label: '省级荣誉称号', maxPoints: 10 },
      { value: '校级荣誉', label: '校级荣誉称号', maxPoints: 5 },
      { value: '志愿服务', label: '志愿服务（累计100小时以上）', maxPoints: 8 },
      { value: '学生工作', label: '学生干部工作（一年以上）', maxPoints: 6 },
      { value: '社会实践', label: '社会实践优秀个人', maxPoints: 5 },
    ],
  },
];

const props = defineProps<{
  studentName: string;
  studentId: string;
  major: string;
  applications: Application[];
}>();

const emit = defineEmits<{
  submit: [
    application: Omit<Application, 'id' | 'studentId' | 'studentName' | 'status' | 'submittedAt'>
  ];
}>();

const showUploadDialog = ref(false);
const currentCategory = ref<CategoryConfig | null>(null);
const formData = ref({
  type: '',
  subType: '',
  title: '',
  description: '',
  points: 0,
});
const files = ref<string[]>([]);

const totalApplications = computed(() => props.applications.length);

const reviewedApplications = computed(() => {
  return props.applications.filter((app) => app.status !== 'pending').length;
});

const progressPercentage = computed(() => {
  return totalApplications.value > 0
    ? (reviewedApplications.value / totalApplications.value) * 100
    : 0;
});

const selectedSubType = computed(() => {
  return currentCategory.value?.types.find((t) => t.value === formData.value.subType);
});

const maxPoints = computed(() => selectedSubType.value?.maxPoints || 0);

const handleOpenUpload = (category: CategoryConfig) => {
  currentCategory.value = category;
  formData.value = {
    type: category.title,
    subType: '',
    title: '',
    description: '',
    points: 0,
  };
  files.value = [];
  showUploadDialog.value = true;
};

const handleCloseDialog = () => {
  showUploadDialog.value = false;
  currentCategory.value = null;
  formData.value = {
    type: '',
    subType: '',
    title: '',
    description: '',
    points: 0,
  };
  files.value = [];
};

const handleFileUpload = () => {
  const mockFiles = [
    '证书扫描件.pdf',
    '获奖证书.jpg',
    '论文首页.png',
    '专利证书.pdf',
    '荣誉证书.jpg',
  ];
  const randomFile = mockFiles[Math.floor(Math.random() * mockFiles.length)];
  files.value.push(randomFile);
};

const removeFile = (index: number) => {
  files.value = files.value.filter((_, i) => i !== index);
};

const handleSubmit = () => {
  if (!formData.value.subType || !formData.value.title || !formData.value.description || formData.value.points <= 0) {
    alert('请填写完整的申请信息');
    return;
  }

  if (files.value.length === 0) {
    alert('请至少上传一个证明文件');
    return;
  }

  if (formData.value.points > maxPoints.value) {
    alert(`申请分值不能超过${maxPoints.value}分`);
    return;
  }

  emit('submit', {
    type: formData.value.type,
    title: formData.value.title,
    description: `${formData.value.subType} - ${formData.value.description}`,
    points: formData.value.points,
    files: files.value,
  });

  handleCloseDialog();
};
</script>