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
          <div v-if="hasReviewHistory">
            <h4 class="text-sm font-medium text-gray-900">历史审核记录</h4>
            <div class="mt-2 space-y-2">
              <div v-for="(record, index) in reviewHistory" :key="index" class="bg-gray-50 p-3 rounded-md border border-gray-200">
                <div class="flex justify-between items-start">
                  <div class="flex-1">
                    <div class="flex items-center space-x-2 mb-1">
                      <span :class="getStatusBadgeClass(record.status)" class="px-2 py-1 text-xs rounded-full font-medium">
                        {{ getStatusText(record.status) }}
                      </span>
                      <span class="text-xs text-gray-500">{{ record.reviewTime }}</span>
                    </div>
                    <p class="text-sm text-gray-700 mt-2 whitespace-pre-wrap">{{ record.comment }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 审核意见 -->
          <div>
            <h4 class="text-sm font-medium text-gray-900">审核意见</h4>
            <Textarea
              placeholder="请输入审核意见"
              v-model="reviewComment"
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

  <!-- 确认修改审核对话框 -->
  <Dialog :open="showConfirmDialog" @update:open="showConfirmDialog = $event">
    <DialogContent class="max-w-lg w-full">
      <div class="space-y-4">
        <div>
          <h3 class="text-lg font-medium text-gray-900">确认修改审核</h3>
          <p class="text-sm text-gray-500 mt-1">该申请已有审核记录，确认要修改审核结果吗？</p>
        </div>
        
        <!-- 原审核信息和新审核信息并排显示 -->
        <div class="grid grid-cols-2 gap-3">
          <!-- 原审核信息 -->
          <div class="bg-gray-50 p-3 rounded-md space-y-2">
            <p class="text-xs font-medium text-gray-600 mb-2">原审核信息</p>
            <div>
              <p class="text-xs text-gray-500">状态</p>
              <p class="text-sm font-medium mt-1">
                <span :class="getStatusBadgeClass(currentApplication?.status || '')" class="px-2 py-1 rounded-full text-xs">
                  {{ getStatusText(currentApplication?.status || '') }}
                </span>
              </p>
            </div>
            <div>
              <p class="text-xs text-gray-500">审核意见</p>
              <p class="text-sm text-gray-700 mt-1 line-clamp-3">{{ currentApplication?.reviewComment || '无' }}</p>
            </div>
            <div v-if="currentApplication?.reviewedAt" class="text-xs text-gray-500">
              {{ formatDateTime(currentApplication.reviewedAt) }}
            </div>
          </div>

          <!-- 新审核信息预览 -->
          <div class="bg-blue-50 p-3 rounded-md space-y-2">
            <p class="text-xs font-medium text-blue-600 mb-2">新审核信息</p>
            <div>
              <p class="text-xs text-blue-600">状态</p>
              <p class="text-sm font-medium mt-1">
                <span :class="getStatusBadgeClass(pendingReviewStatus)" class="px-2 py-1 rounded-full text-xs">
                  {{ getStatusText(pendingReviewStatus) }}
                </span>
              </p>
            </div>
            <div>
              <p class="text-xs text-blue-600">审核意见</p>
              <p class="text-sm text-gray-700 mt-1 line-clamp-3">{{ reviewComment }}</p>
            </div>
          </div>
        </div>

        <!-- 按钮 -->
        <div class="flex justify-end space-x-3 pt-2">
          <Button variant="outline" @click="showConfirmDialog = false">取消</Button>
          <Button @click="confirmReview">确认修改</Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
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
const showConfirmDialog = ref(false);
const pendingReviewStatus = ref<'approved' | 'rejected'>('approved');
const pendingReviewAction = ref<(() => Promise<void>) | null>(null);

// 计算历史审核记录
const reviewHistory = computed(() => {
  const history: Array<{
    status: string;
    comment: string;
    reviewTime: string;
  }> = [];
  
  if (currentApplication.value?.reviewComment && currentApplication.value?.reviewedAt) {
    history.push({
      status: currentApplication.value.status,
      comment: currentApplication.value.reviewComment,
      reviewTime: formatDateTime(currentApplication.value.reviewedAt)
    });
  }
  
  return history;
});

// 是否有历史审核记录
const hasReviewHistory = computed(() => {
  return reviewHistory.value.length > 0;
});

// 是否是第一次审核
const isFirstReview = computed(() => {
  return !currentApplication.value?.reviewComment && !currentApplication.value?.reviewedAt;
});

// 格式化日期时间
const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return '';
  try {
    const date = new Date(dateTime);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch {
    return dateTime;
  }
};

// 监听application prop的变化，当有新的application时自动打开弹窗
watch(() => props.application, (newApplication) => {
  if (newApplication) {
    currentApplication.value = newApplication;
    showReviewDialog.value = true;
    reviewComment.value = '';
    errorMessage.value = '';
    console.log('Opening review dialog for application:', newApplication);
    console.log('历史审核记录:', reviewHistory.value);
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
  
  if (!currentApplication.value || !currentApplication.value.id) {
    errorMessage.value = '申请信息不完整';
    return;
  }
  
  // 如果不是第一次审核，需要确认
  if (!isFirstReview.value) {
    pendingReviewStatus.value = 'approved';
    pendingReviewAction.value = submitApprove;
    showConfirmDialog.value = true;
    return;
  }
  
  // 第一次审核，直接提交
  await submitApprove();
};

const submitApprove = async () => {
  try {
    isSubmitting.value = true;
    errorMessage.value = '';
    showConfirmDialog.value = false;
    
    // 确保id是字符串类型（后端会自动转换为Long）
    const applicationId = String(currentApplication.value.id);
    console.log('审核通过 - Application ID:', applicationId);
    console.log('审核通过 - Application:', currentApplication.value);
    console.log('审核通过 - 请求数据:', {
      status: 'approved',
      reviewComment: reviewComment.value
    });
    
    const updateData = {
      status: 'approved' as const,
      reviewComment: reviewComment.value
    };
    console.log('审核通过 - 发送请求:', `/api/applications/${applicationId}`, updateData);
    
    await applicationsApi.update(applicationId, updateData);
    
    emit('review-complete');
    handleCloseDialog();
  } catch (error: any) {
    console.error('审核通过失败:', error);
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      status: error.response?.status,
      data: error.response?.data,
      config: error.config
    });
    
    // 提取错误信息
    let errorMsg = '审核失败，请重试';
    if (error.response) {
      // 服务器返回了响应，但状态码不在2xx范围内
      if (error.response.data) {
        if (typeof error.response.data === 'string') {
          errorMsg = error.response.data;
        } else if (error.response.data.error) {
          errorMsg = error.response.data.error;
        } else if (error.response.data.message) {
          errorMsg = error.response.data.message;
        } else {
          errorMsg = `服务器错误 (${error.response.status}): ${JSON.stringify(error.response.data)}`;
        }
      } else {
        errorMsg = `请求失败 (${error.response.status})`;
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      errorMsg = '无法连接到服务器，请检查后端服务是否运行';
    } else {
      // 在设置请求时发生了错误
      errorMsg = error.message || '请求配置错误';
    }
    
    errorMessage.value = errorMsg;
  } finally {
    isSubmitting.value = false;
  }
};

const handleReject = async () => {
  console.log('审核驳回 - 审核意见:', reviewComment.value);
  console.log('审核驳回 - 审核意见长度:', reviewComment.value.length);
  console.log('审核驳回 - 审核意见trim后:', reviewComment.value.trim());
  
  if (!reviewComment.value.trim()) {
    errorMessage.value = '请填写审核意见';
    console.error('审核意见为空');
    return;
  }
  
  if (!currentApplication.value || !currentApplication.value.id) {
    errorMessage.value = '申请信息不完整';
    return;
  }
  
  // 如果不是第一次审核，需要确认
  if (!isFirstReview.value) {
    pendingReviewStatus.value = 'rejected';
    pendingReviewAction.value = submitReject;
    showConfirmDialog.value = true;
    return;
  }
  
  // 第一次审核，直接提交
  await submitReject();
};

const submitReject = async () => {
  try {
    isSubmitting.value = true;
    errorMessage.value = '';
    showConfirmDialog.value = false;
    
    // 确保id是字符串类型（后端会自动转换为Long）
    const applicationId = String(currentApplication.value.id);
    
    await applicationsApi.update(applicationId, {
      status: 'rejected',
      reviewComment: reviewComment.value
    });
    
    emit('review-complete');
    handleCloseDialog();
  } catch (error: any) {
    console.error('审核驳回失败:', error);
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      status: error.response?.status,
      data: error.response?.data,
      config: error.config
    });
    
    // 提取错误信息
    let errorMsg = '审核失败，请重试';
    if (error.response) {
      // 服务器返回了响应，但状态码不在2xx范围内
      if (error.response.data) {
        if (typeof error.response.data === 'string') {
          errorMsg = error.response.data;
        } else if (error.response.data.error) {
          errorMsg = error.response.data.error;
        } else if (error.response.data.message) {
          errorMsg = error.response.data.message;
        } else {
          errorMsg = `服务器错误 (${error.response.status}): ${JSON.stringify(error.response.data)}`;
        }
      } else {
        errorMsg = `请求失败 (${error.response.status})`;
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      errorMsg = '无法连接到服务器，请检查后端服务是否运行';
    } else {
      // 在设置请求时发生了错误
      errorMsg = error.message || '请求配置错误';
    }
    
    errorMessage.value = errorMsg;
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

// 确认审核修改
const confirmReview = async () => {
  if (pendingReviewAction.value) {
    await pendingReviewAction.value();
  }
};
</script>