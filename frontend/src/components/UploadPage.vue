<template>
  <div class="space-y-6">
    <!-- 四个上传卡片区域 -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <Card
        v-for="category in categories"
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
          <Button 
            class="w-full transition-all duration-200 hover:bg-blue-600 hover:text-white hover:border-blue-600 hover:shadow-md" 
            variant="outline" 
            @click="handleOpenUpload(category)"
          >
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
    <Dialog :open="showUploadDialog" @update:open="showUploadDialog = $event">
      <DialogContent>
        <div class="mb-4">
          <h3 class="text-lg font-medium leading-6 text-gray-900">{{ currentCategory?.title }}</h3>
          <p class="text-sm text-gray-500 mt-1">{{ currentCategory?.description }}</p>
        </div>

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
                step="0.1"
                :min="0.1"
                :max="maxPoints"
                placeholder="请输入申请分值"
                :value="formData.points || ''"
                @input="formData.points = parseFloat(($event.target as HTMLInputElement).value) || 0"
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
            <div 
              class="border-2 border-dashed border-gray-300 rounded-lg p-4 space-y-4 transition-colors"
              :class="{
                'border-blue-400 bg-blue-50': isDragging,
                'border-gray-300': !isDragging
              }"
              @drop="handleDrop"
              @dragover.prevent="isDragging = true"
              @dragleave="isDragging = false"
              @dragenter.prevent="isDragging = true"
            >
              <input
                ref="fileInputRef"
                type="file"
                multiple
                accept=".pdf,.jpg,.jpeg,.png,.gif,.doc,.docx"
                class="hidden"
                @change="handleFileSelect"
              />
              
              <div class="text-center">
                <Button 
                  type="button" 
                  variant="outline" 
                  class="transition-all duration-200 hover:bg-blue-600 hover:text-white hover:border-blue-600 hover:shadow-md" 
                  @click="triggerFileSelect"
                  :disabled="isUploading"
                >
                  <Upload class="h-4 w-4 mr-2" />
                  {{ isUploading ? '上传中...' : '选择文件' }}
                </Button>
                <p class="text-sm text-gray-500 mt-2">或拖拽文件到此处</p>
              </div>

              <!-- 文件列表 -->
              <div v-if="fileList.length > 0" class="space-y-2">
                <p class="text-sm font-medium">已选择文件：</p>
                <div
                  v-for="(file, index) in fileList"
                  :key="index"
                  class="flex items-center justify-between bg-gray-50 p-3 rounded-lg border border-gray-200"
                >
                  <div class="flex items-center space-x-3 flex-1 min-w-0">
                    <!-- 文件图标/预览 -->
                    <div class="flex-shrink-0 cursor-pointer" @click="previewFile(file, index)">
                      <FileText v-if="file.type === 'pdf' || file.type === 'doc' || file.type === 'docx'" class="h-5 w-5 text-red-500 hover:text-red-700" />
                      <img 
                        v-else-if="file.preview" 
                        :src="file.preview" 
                        alt="预览" 
                        class="h-10 w-10 object-cover rounded hover:opacity-80 transition-opacity cursor-pointer" 
                      />
                      <FileText v-else class="h-5 w-5 text-gray-400" />
                    </div>
                    
                    <!-- 文件信息 -->
                    <div class="flex-1 min-w-0">
                      <p 
                        class="text-sm font-medium text-gray-900 truncate cursor-pointer hover:text-blue-600"
                        @click="previewFile(file, index)"
                        :title="file.name"
                      >
                        {{ file.name }}
                      </p>
                      <p class="text-xs text-gray-500">
                        {{ formatFileSize(file.size) }}
                        <span v-if="file.uploadStatus === 'uploading'" class="ml-2 text-blue-600">
                          上传中... {{ file.uploadProgress }}%
                        </span>
                        <span v-else-if="file.uploadStatus === 'success'" class="ml-2 text-green-600">
                          ✓ 上传成功
                        </span>
                        <span v-else-if="file.uploadStatus === 'error'" class="ml-2 text-red-600">
                          ✗ 上传失败
                        </span>
                      </p>
                      <!-- 上传进度条 -->
                      <div v-if="file.uploadStatus === 'uploading'" class="mt-1 w-full bg-gray-200 rounded-full h-1.5">
                        <div 
                          class="bg-blue-600 h-1.5 rounded-full transition-all duration-300"
                          :style="{ width: file.uploadProgress + '%' }"
                        ></div>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 删除按钮 -->
                  <Button 
                    type="button" 
                    variant="ghost" 
                    size="sm" 
                    @click="removeFile(index)"
                    :disabled="file.uploadStatus === 'uploading'"
                    class="ml-2 flex-shrink-0"
                  >
                    <X class="h-4 w-4" />
                  </Button>
                </div>
              </div>

              <p class="text-xs text-gray-500">
                支持上传PDF、JPG、PNG、DOC、DOCX格式文件，单个文件不超过10MB，建议上传清晰的扫描件或照片
              </p>
              
              <!-- 错误提示 -->
              <div v-if="uploadError" class="text-sm text-red-600 bg-red-50 p-2 rounded">
                {{ uploadError }}
              </div>
            </div>
          </div>

          <!-- 按钮 -->
          <div class="flex justify-end space-x-4">
            <Button variant="outline" @click="handleCloseDialog">取消</Button>
            <Button
              @click="handleSubmit"
              :disabled="isSubmitDisabled"
              :title="getSubmitDisabledReason"
            >
              {{ isSubmitting ? '提交中...' : '提交申请' }}
            </Button>
            <!-- 调试信息（显示为什么按钮被禁用） -->
            <div v-if="isSubmitDisabled && !isSubmitting" class="text-xs text-orange-600 mt-1 space-y-1">
              <div v-if="!formData.subType">⚠ 请选择具体类型</div>
              <div v-if="!formData.title">⚠ 请填写申请标题</div>
              <div v-if="!formData.description">⚠ 请填写详细描述</div>
              <div v-if="formData.points <= 0">⚠ 请填写申请分值（必须大于0）</div>
              <div v-if="formData.points > maxPoints && maxPoints > 0">⚠ 申请分值不能超过{{ maxPoints }}分</div>
              <div v-if="files.length === 0">⚠ 请至少上传一个证明文件</div>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>

    <!-- 文件预览对话框 -->
    <Dialog :open="showPreviewDialog" @update:open="showPreviewDialog = $event">
      <DialogContent class="max-w-4xl max-h-[90vh]">
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-medium">{{ previewFileItem?.name }}</h3>
            <Button variant="ghost" size="sm" @click="showPreviewDialog = false">
              <X class="h-4 w-4" />
            </Button>
          </div>
          
          <div class="flex justify-center items-center bg-gray-100 rounded-lg p-4 min-h-[400px] max-h-[70vh] overflow-auto">
            <!-- 图片预览 -->
            <img 
              v-if="previewFileItem && ['jpg', 'jpeg', 'png', 'gif'].includes(previewFileItem.type) && previewFileItem.preview"
              :src="previewFileItem.preview" 
              alt="预览" 
              class="max-w-full max-h-full object-contain"
            />
            <!-- PDF预览 -->
            <iframe 
              v-else-if="previewFileItem && previewFileItem.type === 'pdf'"
              :src="getPdfPreviewUrl(previewFileItem)" 
              class="w-full h-[600px] border-0"
            ></iframe>
            <!-- 其他文件类型 -->
            <div v-else class="text-center text-gray-500">
              <FileText class="h-16 w-16 mx-auto mb-4 text-gray-400" />
              <p>该文件类型不支持预览</p>
              <p class="text-sm mt-2">{{ previewFileItem?.name }}</p>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import type { Application, CreateApplicationRequest } from '../types';
import { Trophy, FileText, Lightbulb, Award, Upload, X } from 'lucide-vue-next';
import { uploadApi } from '../api/upload';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardDescription from './ui/CardDescription.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Dialog from './ui/SimpleDialog.vue';
import DialogContent from './ui/SimpleDialogContent.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import Textarea from './ui/Textarea.vue';
import Select from './ui/Select.vue';
import SelectContent from './ui/SelectContent.vue';
import SelectItem from './ui/SelectItem.vue';
import SelectTrigger from './ui/SelectTrigger.vue';
import SelectValue from './ui/SelectValue.vue';
import Progress from './ui/Progress.vue';
import { applicationsApi } from '../api/applications';
import { useAuth } from '../stores/auth';

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

// 分类配置
const categories: CategoryConfig[] = [
  {
    id: 'competition',
    title: '竞赛加分',
    description: '上传竞赛获奖证书（国家级、省级、校级等）',
    icon: Trophy,
    color: 'text-yellow-600',
    types: [
      { value: '国家级一等奖及以上-A+类', label: '国家级一等奖及以上（A+类）', maxPoints: 30 },
      { value: '国家级一等奖及以上-A类', label: '国家级一等奖及以上（A类）', maxPoints: 15 },
      { value: '国家级一等奖及以上-A-类', label: '国家级一等奖及以上（A-类）', maxPoints: 10 },
      { value: '国家级二等奖-A+类', label: '国家级二等奖（A+类）', maxPoints: 15 },
      { value: '国家级二等奖-A类', label: '国家级二等奖（A类）', maxPoints: 10 },
      { value: '国家级二等奖-A-类', label: '国家级二等奖（A-类）', maxPoints: 5 },
      { value: '国家级三等奖-A+类', label: '国家级三等奖（A+类）', maxPoints: 10 },
      { value: '国家级三等奖-A类', label: '国家级三等奖（A类）', maxPoints: 5 },
      { value: '国家级三等奖-A-类', label: '国家级三等奖（A-类）', maxPoints: 2 },
      { value: '省级一等奖及以上-A+类', label: '省级一等奖及以上（A+类）', maxPoints: 5 },
      { value: '省级一等奖及以上-A类', label: '省级一等奖及以上（A类）', maxPoints: 2 },
      { value: '省级一等奖及以上-A-类', label: '省级一等奖及以上（A-类）', maxPoints: 1 },
      { value: '省级二等奖-A+类', label: '省级二等奖（A+类）', maxPoints: 2 },
      { value: '省级二等奖-A类', label: '省级二等奖（A类）', maxPoints: 1 },
      { value: '省级二等奖-A-类', label: '省级二等奖（A-类）', maxPoints: 0.5 },
    ],
  },
  {
    id: 'paper',
    title: '论文加分',
    description: '上传发表论文或会议收录证明（PDF/图片）',
    icon: FileText,
    color: 'text-blue-600',
    types: [
      { value: 'Nature/Science/Cell主刊或IF≥10的CELL子刊', label: 'Nature/Science/Cell主刊或IF≥10的CELL子刊', maxPoints: 20 },
      { value: 'A类期刊/会议长文', label: 'A类期刊/会议长文', maxPoints: 10 },
      { value: 'B类期刊/会议长文', label: 'B类期刊/会议长文', maxPoints: 6 },
      { value: 'C类期刊/会议长文', label: 'C类期刊/会议长文', maxPoints: 1 },
    ],
  },
  {
    id: 'patent',
    title: '专利加分',
    description: '上传国家发明专利或实用新型专利证书',
    icon: Lightbulb,
    color: 'text-purple-600',
    types: [
      { value: '国家发明专利授权', label: '国家发明专利授权', maxPoints: 2 },
    ],
  },
  {
    id: 'comprehensive',
    title: '综合表现',
    description: '上传荣誉证书、志愿服务证明、学生工作证明等',
    icon: Award,
    color: 'text-green-600',
    types: [
      { value: '国际组织实习', label: '国际组织实习（≥1学年）', maxPoints: 1 },
      { value: '参军入伍-1年', label: '参军入伍（≥1年且<2年）', maxPoints: 1 },
      { value: '参军入伍-2年', label: '参军入伍（≥2年）', maxPoints: 2 },
      { value: '志愿服务', label: '志愿服务（工时≥200h，0.05/每2h）', maxPoints: 10 },
      { value: '志愿服务表彰-国家级个人', label: '志愿服务表彰（国家级个人）', maxPoints: 1 },
      { value: '志愿服务表彰-国家级团队队长', label: '志愿服务表彰（国家级团队队长）', maxPoints: 0.5 },
      { value: '志愿服务表彰-省级个人', label: '志愿服务表彰（省级个人）', maxPoints: 0.5 },
      { value: '志愿服务表彰-省级团队队长', label: '志愿服务表彰（省级团队队长）', maxPoints: 0.25 },
      { value: '志愿服务表彰-校级个人', label: '志愿服务表彰（校级个人）', maxPoints: 0.25 },
      { value: '志愿服务表彰-校级团队队长', label: '志愿服务表彰（校级团队队长）', maxPoints: 0.125 },
      { value: '荣誉称号-国家级', label: '荣誉称号（国家级）', maxPoints: 2 },
      { value: '荣誉称号-省级', label: '荣誉称号（省级）', maxPoints: 1 },
      { value: '荣誉称号-校级', label: '荣誉称号（校级）', maxPoints: 0.2 },
      { value: '学生干部', label: '学生干部（任期满1学年，系数×老师评分/100）', maxPoints: 5 },
      { value: '体育比赛-国际冠军', label: '体育比赛（国际冠军）', maxPoints: 8 },
      { value: '体育比赛-国际亚军', label: '体育比赛（国际亚军）', maxPoints: 6.5 },
      { value: '体育比赛-国际季军or国家冠军', label: '体育比赛（国际季军or国家冠军）', maxPoints: 5 },
      { value: '体育比赛-国家亚军or国际4-8名', label: '体育比赛（国家亚军or国际4–8名）', maxPoints: 3.5 },
      { value: '体育比赛-国家季军', label: '体育比赛（国家季军）', maxPoints: 2 },
      { value: '体育比赛-国家4-8名', label: '体育比赛（国家4–8名）', maxPoints: 1 },
    ],
  },
];

const props = defineProps<{
  applications: Application[];
}>();

const emit = defineEmits<{
  submit: [application: Application];
}>();

const { currentUser } = useAuth();

const showUploadDialog = ref(false);
const currentCategory = ref<CategoryConfig | null>(null);
const isSubmitting = ref(false);
const formData = ref({
  type: '',
  subType: '',
  title: '',
  description: '',
  points: 0,
});
const files = ref<number[]>([]); // 存储上传后的file_record.id
const fileInputRef = ref<HTMLInputElement | null>(null);
const isUploading = ref(false);
const uploadError = ref('');
const isDragging = ref(false);
const showPreviewDialog = ref(false);
const previewFileItem = ref<FileItem | null>(null);

interface FileItem {
  name: string;
  size: number;
  type: string;
  file: File;
  preview?: string;
  uploadStatus: 'pending' | 'uploading' | 'success' | 'error';
  uploadProgress: number;
  fileUrl?: string;
  id?: number; // 添加file_record.id
}

const fileList = ref<FileItem[]>([]);

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

// 计算提交按钮是否禁用
const isSubmitDisabled = computed(() => {
  const conditions = {
    noSubType: !formData.value.subType,
    noTitle: !formData.value.title,
    noDescription: !formData.value.description,
    invalidPoints: formData.value.points <= 0,
    pointsExceedMax: formData.value.points > maxPoints.value,
    noFiles: files.value.length === 0,
    isSubmitting: isSubmitting.value
  };
  
  // 调试输出
  console.log('提交按钮禁用条件检查:', conditions);
  console.log('表单数据:', {
    subType: formData.value.subType,
    title: formData.value.title,
    description: formData.value.description,
    points: formData.value.points,
    maxPoints: maxPoints.value,
    filesCount: files.value.length
  });
  
  return conditions.noSubType ||
    conditions.noTitle ||
    conditions.noDescription ||
    conditions.invalidPoints ||
    conditions.pointsExceedMax ||
    conditions.noFiles ||
    conditions.isSubmitting;
});

// 获取提交按钮禁用原因
const getSubmitDisabledReason = computed(() => {
  if (isSubmitting.value) return '正在提交中...';
  if (!formData.value.subType) return '请选择具体类型';
  if (!formData.value.title) return '请填写申请标题';
  if (!formData.value.description) return '请填写详细描述';
  if (formData.value.points <= 0) return '请填写申请分值（必须大于0）';
  if (formData.value.points > maxPoints.value && maxPoints.value > 0) return `申请分值不能超过${maxPoints.value}分`;
  if (files.value.length === 0) return '请至少上传一个证明文件';
  return '';
});

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
  fileList.value = [];
  uploadError.value = '';
  isDragging.value = false;
  if (fileInputRef.value) {
    fileInputRef.value.value = '';
  }
};

// 触发文件选择
const triggerFileSelect = () => {
  fileInputRef.value?.click();
};


// 上传单个文件
const uploadSingleFile = async (fileItem: FileItem) => {
  try {
    fileItem.uploadStatus = 'uploading';
    fileItem.uploadProgress = 0;
    isUploading.value = true;
    uploadError.value = '';
    
    // 使用真实的上传进度
    const response = await uploadApi.uploadFile(
      fileItem.file,
      (progress) => {
        fileItem.uploadProgress = progress;
      }
    );
    
    if (response.error) {
      fileItem.uploadStatus = 'error';
      uploadError.value = response.message || response.error || '文件上传失败';
      console.error('文件上传失败:', response);
    } else if (response.id && response.fileUrl) {
      fileItem.uploadStatus = 'success';
      fileItem.uploadProgress = 100;
      fileItem.id = response.id; // 保存file_record.id
      fileItem.fileUrl = response.fileUrl;
      // 添加到files数组（用于提交申请）
      if (!files.value.includes(response.id)) {
        files.value.push(response.id);
        console.log('文件上传成功，已添加到files数组:', response.id);
        console.log('当前files数组:', files.value);
      }
    } else {
      // 响应中没有error也没有fileUrl，视为上传失败
      fileItem.uploadStatus = 'error';
      uploadError.value = '文件上传失败：未返回有效的文件信息';
      console.error('文件上传失败:', response);
    }
  } catch (error: any) {
    fileItem.uploadStatus = 'error';
    fileItem.uploadProgress = 0;
    
    // 提取详细的错误信息
    let errorMessage = '文件上传失败';
    if (error.response) {
      if (error.response.data) {
        if (typeof error.response.data === 'string') {
          errorMessage = error.response.data;
        } else if (error.response.data.error) {
          errorMessage = error.response.data.error;
        } else if (error.response.data.message) {
          errorMessage = error.response.data.message;
        } else {
          errorMessage = `上传失败 (${error.response.status})`;
        }
      } else {
        errorMessage = `上传失败 (${error.response.status})`;
      }
    } else if (error.request) {
      errorMessage = '无法连接到服务器，请检查后端服务是否运行';
    } else {
      errorMessage = error.message || '文件上传失败';
    }
    
    uploadError.value = errorMessage;
    console.error('上传文件失败:', error);
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      status: error.response?.status,
      data: error.response?.data
    });
  } finally {
    isUploading.value = fileList.value.some(f => f.uploadStatus === 'uploading');
  }
};

// 处理拖拽上传
const handleDrop = (event: DragEvent) => {
  event.preventDefault();
  isDragging.value = false;
  
  const droppedFiles = event.dataTransfer?.files;
  if (!droppedFiles || droppedFiles.length === 0) {
    return;
  }
  
  // 转换为FileList并触发文件选择处理
  const fileArray = Array.from(droppedFiles);
  processFiles(fileArray);
};

// 处理文件（从选择或拖拽）
const processFiles = async (fileArray: File[]) => {
  uploadError.value = '';
  
  // 验证并添加文件
  for (const file of fileArray) {
    // 验证文件大小（10MB）
    if (file.size > 10 * 1024 * 1024) {
      uploadError.value = `文件 "${file.name}" 超过10MB限制`;
      continue;
    }
    
    // 验证文件类型
    const allowedTypes = ['pdf', 'jpg', 'jpeg', 'png', 'gif', 'doc', 'docx'];
    const fileExtension = file.name.split('.').pop()?.toLowerCase();
    if (!fileExtension || !allowedTypes.includes(fileExtension)) {
      uploadError.value = `文件 "${file.name}" 格式不支持，仅支持：PDF、JPG、PNG、DOC、DOCX`;
      continue;
    }
    
    // 检查是否已存在同名文件
    if (fileList.value.some(f => f.name === file.name && f.uploadStatus !== 'error')) {
      uploadError.value = `文件 "${file.name}" 已存在`;
      continue;
    }
    
    // 创建文件项
    const fileItem: FileItem = {
      name: file.name,
      size: file.size,
      type: fileExtension,
      file: file,
      uploadStatus: 'pending',
      uploadProgress: 0,
    };
    
    // 如果是图片或PDF，生成预览
    if (['jpg', 'jpeg', 'png', 'gif', 'pdf'].includes(fileExtension)) {
      const reader = new FileReader();
      reader.onload = (e) => {
        fileItem.preview = e.target?.result as string;
      };
      reader.readAsDataURL(file);
    }
    
    fileList.value.push(fileItem);
    
    // 自动上传文件
    await uploadSingleFile(fileItem);
  }
};

// 处理文件选择
const handleFileSelect = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const selectedFiles = target.files;
  
  if (!selectedFiles || selectedFiles.length === 0) {
    return;
  }
  
  const fileArray = Array.from(selectedFiles);
  await processFiles(fileArray);
  
  // 清空input，允许重复选择同一文件
  if (target) {
    target.value = '';
  }
};

// 获取PDF预览URL
const getPdfPreviewUrl = (file: FileItem): string => {
  // 如果已上传，使用服务器URL
  if (file.fileUrl) {
    return file.fileUrl;
  }
  // 如果有预览URL（blob URL），使用预览
  if (file.preview) {
    return file.preview;
  }
  // 如果还没有上传，使用blob URL
  return URL.createObjectURL(file.file);
};

// 预览文件
const previewFile = (file: FileItem, index: number) => {
  previewFileItem.value = file;
  showPreviewDialog.value = true;
};

// 删除文件
const removeFile = (index: number) => {
  const fileItem = fileList.value[index];
  
  // 如果文件正在上传，不允许删除
  if (fileItem.uploadStatus === 'uploading') {
    return;
  }
  
  // 如果文件已上传成功，从files数组中移除
  if (fileItem.id !== undefined && files.value.includes(fileItem.id)) {
    files.value = files.value.filter(id => id !== fileItem.id);
  }
  
  // 从文件列表中移除
  fileList.value.splice(index, 1);
  
  // 如果没有错误，清空错误提示
  if (!fileList.value.some(f => f.uploadStatus === 'error')) {
    uploadError.value = '';
  }
};

// 格式化文件大小
const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
};

const handleSubmit = async () => {
  if (!formData.value.subType || !formData.value.title || !formData.value.description || formData.value.points <= 0) {
    alert('请填写完整的申请信息');
    return;
  }

  if (files.value.length === 0) {
    // 检查是否有上传失败的文件
    const failedFiles = fileList.value.filter(f => f.uploadStatus === 'error');
    if (failedFiles.length > 0) {
      alert(`有 ${failedFiles.length} 个文件上传失败，请重新上传文件后再提交`);
    } else {
      alert('请至少上传一个证明文件');
    }
    return;
  }

  if (formData.value.points > maxPoints.value) {
    alert(`申请分值不能超过${maxPoints.value}分`);
    return;
  }

  try {
    isSubmitting.value = true;
    
    console.log('提交申请 - 表单数据:', formData.value);
    console.log('提交申请 - 文件列表:', files.value);
    
    // 创建符合 CreateApplicationRequest 接口的请求对象
    const request: any = {
      studentId: currentUser.value.username, // 使用auth.currentUser.username作为学号
      studentName: currentUser.value.realName, // 使用auth.currentUser.realName作为真实姓名
      type: formData.value.subType, // 使用具体类型而不是大类
      title: formData.value.title,
      description: formData.value.description,
      points: formData.value.points,
      fileIds: files.value, // 使用file_record.id数组
    };

    console.log('提交申请 - 请求数据:', request);

    // 调用 applicationsApi.create() 提交申请
    const newApplication = await applicationsApi.create(request);
    
    console.log('提交申请成功:', newApplication);
    
    // 触发 submit 事件，通知父组件
    emit('submit', newApplication);
    
    // 显示成功提示
    alert('申请提交成功！');
    
    // 关闭对话框并重置表单
    handleCloseDialog();
  } catch (error: any) {
    console.error('提交申请失败:', error);
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      status: error.response?.status,
      data: error.response?.data
    });
    
    // 提取错误信息
    let errorMsg = '提交申请失败，请稍后重试';
    if (error.response) {
      if (error.response.data) {
        if (typeof error.response.data === 'string') {
          errorMsg = error.response.data;
        } else if (error.response.data.error) {
          errorMsg = error.response.data.error;
        } else if (error.response.data.message) {
          errorMsg = error.response.data.message;
        }
      }
    } else if (error.request) {
      errorMsg = '无法连接到服务器，请检查后端服务是否运行';
    }
    
    alert(errorMsg);
  } finally {
    isSubmitting.value = false;
  }
};
</script>