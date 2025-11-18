<template>
  <Dialog v-model:open="showReviewDialog">
    <DialogContent>
      <div class="mb-4">
        <h3 class="text-lg font-medium leading-6 text-gray-900">审核申请</h3>
      </div>

      <div class="space-y-6">
        <!-- 申请信息 -->
        <div class="space-y-4">
          <div>
            <h4 class="text-sm font-medium text-gray-900">申请信息</h4>
            <div class="mt-2 bg-gray-50 p-3 rounded-md">
              <p class="text-sm"><strong>姓名:</strong> {{ currentApplication?.studentName }}</p>
              <p class="text-sm"><strong>学号:</strong> {{ currentApplication?.studentId }}</p>
              <p class="text-sm"><strong>类别:</strong> {{ getCategoryName(currentApplication?.category) }}</p>
              <p class="text-sm"><strong>类型:</strong> {{ getTypeName(currentApplication?.category, currentApplication?.subType) }}</p>
              <p class="text-sm"><strong>标题:</strong> {{ currentApplication?.title }}</p>
              <p class="text-sm"><strong>描述:</strong> {{ currentApplication?.description }}</p>
              <p class="text-sm"><strong>申请分值:</strong> {{ currentApplication?.points }}</p>
            </div>
          </div>

          <!-- 上传文件 -->
          <div>
            <h4 class="text-sm font-medium text-gray-900">上传文件</h4>
            <div class="mt-2">
              <div v-if="currentApplication?.files && currentApplication.files.length > 0" class="space-y-2">
                <div v-for="(file, index) in currentApplication.files" :key="index" class="flex items-center space-x-2">
                  <FileText class="h-4 w-4 text-gray-500" />
                  <span class="text-sm text-blue-600 underline cursor-pointer">{{ file }}</span>
                </div>
              </div>
              <div v-else class="text-sm text-gray-500">无上传文件</div>
            </div>
          </div>

          <!-- 历史审核记录 -->
          <div>
            <h4 class="text-sm font-medium text-gray-900">历史审核记录</h4>
            <div class="mt-2">
              <div v-if="currentApplication?.reviewHistory && currentApplication.reviewHistory.length > 0" class="space-y-2">
                <div v-for="(record, index) in currentApplication.reviewHistory" :key="index" class="bg-gray-50 p-3 rounded-md">
                  <div class="flex justify-between items-start">
                    <div>
                      <p class="text-sm font-medium">{{ record.reviewerName }} ({{ record.reviewerRole }})</p>
                      <p class="text-sm text-gray-600">{{ record.reviewTime }}</p>
                    </div>
                    <span :class="getStatusBadgeClass(record.status)" class="px-2 py-1 text-xs rounded-full">
                      {{ getStatusText(record.status) }}
                    </span>
                  </div>
                  <p class="text-sm mt-2">{{ record.comment }}</p>
                </div>
              </div>
              <div v-else class="text-sm text-gray-500">暂无审核记录</div>
            </div>
          </div>

          <!-- 审核意见 -->
          <div>
            <h4 class="text-sm font-medium text-gray-900">审核意见</h4>
            <Textarea
              placeholder="请输入审核意见"
              :value="reviewComment"
              @update:value="reviewComment = $event"
              class="mt-2"
              :disabled="isSubmitting"
            />
            <!-- 错误提示 -->
            <div v-if="errorMessage" class="mt-2 text-sm text-red-600">
              {{ errorMessage }}
            </div>
          </div>
        </div>

        <!-- 按钮 -->
        <div class="flex justify-end space-x-4">
          <Button variant="outline" @click="handleCloseDialog" :disabled="isSubmitting">关闭</Button>
          <Button variant="destructive" @click="handleReject" :disabled="isSubmitting">
            <span v-if="isSubmitting" class="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-gray-300 border-t-blue-600"></span>
            驳回
          </Button>
          <Button @click="handleApprove" :disabled="isSubmitting">
            <span v-if="isSubmitting" class="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-gray-300 border-t-blue-600"></span>
            通过
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import type { Application } from '../types';
import {
  FileText,
} from 'lucide-vue-next';
import Button from './ui/Button.vue';
import Dialog from './ui/SimpleDialog.vue';
import DialogContent from './ui/SimpleDialogContent.vue';
import Textarea from './ui/Textarea.vue';
import { applicationsApi } from '../api';

const props = defineProps<{
  application: Application;
}>();

const emit = defineEmits<{
  'review-complete': [];
  close: [];
}>();

const showReviewDialog = ref(false);
const reviewComment = ref('');
const currentApplication = ref(props.application);
const isSubmitting = ref(false);
const errorMessage = ref('');

// 监听application prop的变化，当有新的application时自动打开弹窗
watch(() => props.application, (newApplication) => {
  if (newApplication) {
    currentApplication.value = newApplication;
    showReviewDialog.value = true;
    reviewComment.value = '';
    errorMessage.value = '';
    console.log('Opening review dialog for application:', newApplication);
    console.log('showReviewDialog.value:', showReviewDialog.value);
  }
}, { immediate: true });

const handleCloseDialog = () => {
  showReviewDialog.value = false;
  emit('close');
};

const handleApprove = async () => {
  if (!reviewComment.value.trim()) {
    errorMessage.value = '请填写审核意见';
    return;
  }
  
  try {
    isSubmitting.value = true;
    errorMessage.value = '';
    
    await applicationsApi.update(currentApplication.value.id, {
      status: 'approved',
      reviewComment: reviewComment.value
    });
    
    emit('review-complete');
    handleCloseDialog();
  } catch (error) {
    console.error('审核通过失败:', error);
    errorMessage.value = '审核失败，请重试';
  } finally {
    isSubmitting.value = false;
  }
};

const handleReject = async () => {
  if (!reviewComment.value.trim()) {
    errorMessage.value = '请填写审核意见';
    return;
  }
  
  try {
    isSubmitting.value = true;
    errorMessage.value = '';
    
    await applicationsApi.update(currentApplication.value.id, {
      status: 'rejected',
      reviewComment: reviewComment.value
    });
    
    emit('review-complete');
    handleCloseDialog();
  } catch (error) {
    console.error('审核驳回失败:', error);
    errorMessage.value = '审核失败，请重试';
  } finally {
    isSubmitting.value = false;
  }
};

const getCategoryName = (category: string) => {
  // 这里应该根据实际的类别映射返回对应的名称
  const categoryMap: Record<string, string> = {
    'academic': '学术科研',
    'competition': '学科竞赛',
    'innovation': '创新创业',
    'social': '社会实践',
    'culture': '文化艺术',
    'sports': '体育竞技'
  };
  return categoryMap[category] || category;
};

const getTypeName = (category: string, subType: string) => {
  // 这里应该根据实际的类型映射返回对应的名称
  // 简化处理，实际项目中应该从配置中获取
  return subType;
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'pending': '待审核',
    'approved': '已通过',
    'rejected': '已驳回'
  };
  return statusMap[status] || status;
};

const getStatusBadgeClass = (status: string) => {
  const statusClassMap: Record<string, string> = {
    'pending': 'bg-yellow-100 text-yellow-800',
    'approved': 'bg-green-100 text-green-800',
    'rejected': 'bg-red-100 text-red-800'
  };
  return statusClassMap[status] || 'bg-gray-100 text-gray-800';
};
</script>