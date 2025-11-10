<template>
  <Dialog :open="true" @update:open="handleClose">
    <DialogContent class="max-w-4xl max-h-[85vh] overflow-y-auto">
      <DialogHeader>
        <DialogTitle class="flex items-center space-x-2">
          <FileText class="h-5 w-5" />
          <span>申请审核详情</span>
        </DialogTitle>
        <DialogDescription>请仔细查看申请材料并给出审核意见</DialogDescription>
      </DialogHeader>

      <div class="space-y-6">
        <!-- Application Overview -->
        <Card>
          <CardHeader>
            <CardTitle class="flex items-center justify-between">
              <span class="flex items-center space-x-2">
                <Award class="h-5 w-5" />
                <span>申请信息</span>
              </span>
              <StatusBadge :status="application.status" />
            </CardTitle>
          </CardHeader>
          <CardContent class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div class="space-y-2">
                <div class="flex items-center space-x-2 text-sm text-gray-600">
                  <User class="h-4 w-4" />
                  <span>申请人</span>
                </div>
                <p>{{ application.studentName }} ({{ application.studentId }})</p>
              </div>

              <div class="space-y-2">
                <div class="flex items-center space-x-2 text-sm text-gray-600">
                  <Tag class="h-4 w-4" />
                  <span>申请类型</span>
                </div>
                <p>{{ application.type }}</p>
              </div>

              <div class="space-y-2">
                <div class="flex items-center space-x-2 text-sm text-gray-600">
                  <Calendar class="h-4 w-4" />
                  <span>提交时间</span>
                </div>
                <p>{{ application.submittedAt }}</p>
              </div>

              <div class="space-y-2">
                <div class="flex items-center space-x-2 text-sm text-gray-600">
                  <Award class="h-4 w-4" />
                  <span>申请分值</span>
                </div>
                <p class="text-lg">{{ application.points }} 分</p>
              </div>
            </div>

            <Separator />

            <div class="space-y-2">
              <h4>申请标题</h4>
              <p class="text-lg">{{ application.title }}</p>
            </div>

            <div class="space-y-2">
              <h4>详细描述</h4>
              <p class="text-gray-700 bg-gray-50 p-3 rounded-md">{{ application.description }}</p>
            </div>
          </CardContent>
        </Card>

        <!-- Uploaded Files -->
        <Card>
          <CardHeader>
            <CardTitle class="flex items-center space-x-2">
              <FileText class="h-5 w-5" />
              <span>上传文件</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div class="space-y-2">
              <div
                v-for="(file, index) in application.files"
                :key="index"
                class="flex items-center justify-between bg-gray-50 p-3 rounded-md"
              >
                <div class="flex items-center space-x-2">
                  <FileText class="h-4 w-4 text-gray-500" />
                  <span>{{ file }}</span>
                </div>
                <Button variant="outline" size="sm">查看</Button>
              </div>
            </div>
            <p class="text-sm text-gray-500 mt-2">
              点击"查看"按钮可以预览或下载文件（演示环境下为模拟功能）
            </p>
          </CardContent>
        </Card>

        <!-- Previous Review (if exists) -->
        <Card v-if="application.status !== 'pending' && application.reviewComment">
          <CardHeader>
            <CardTitle class="flex items-center space-x-2">
              <MessageSquare class="h-5 w-5" />
              <span>历史审核记录</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div class="space-y-2">
              <div class="flex items-center justify-between">
                <span class="text-sm text-gray-600">审核时间：{{ application.reviewedAt }}</span>
                <StatusBadge :status="application.status" />
              </div>
              <div class="bg-gray-50 p-3 rounded-md">
                <p>{{ application.reviewComment }}</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <!-- Review Form (only for pending applications) -->
        <Card v-if="application.status === 'pending'">
          <CardHeader>
            <CardTitle class="flex items-center space-x-2">
              <MessageSquare class="h-5 w-5" />
              <span>审核意见</span>
            </CardTitle>
          </CardHeader>
          <CardContent class="space-y-4">
            <div class="flex space-x-4">
              <Button
                :variant="selectedStatus === 'approved' ? 'default' : 'outline'"
                @click="selectedStatus = 'approved'"
                class="flex-1"
              >
                通过
              </Button>
              <Button
                :variant="selectedStatus === 'rejected' ? 'destructive' : 'outline'"
                @click="selectedStatus = 'rejected'"
                class="flex-1"
              >
                驳回
              </Button>
            </div>

            <div class="space-y-2">
              <Label for="review-comment">审核意见</Label>
              <Textarea
                id="review-comment"
                :placeholder="getPlaceholder()"
                rows="4"
                v-model="reviewComment"
              />
            </div>

            <div class="text-sm text-gray-500 bg-blue-50 p-3 rounded-md">
              <p><strong>审核建议：</strong></p>
              <ul class="list-disc list-inside space-y-1 mt-1">
                <li>仔细核实证明材料的真实性和有效性</li>
                <li>确认申请分值是否符合相关标准</li>
                <li>如需驳回，请详细说明理由和改进建议</li>
                <li>审核意见将发送给申请学生，请用词准确、客观</li>
              </ul>
            </div>
          </CardContent>
        </Card>

        <!-- Action Buttons -->
        <div class="flex justify-end space-x-4">
          <Button variant="outline" @click="handleClose">关闭</Button>
          <Button
            v-if="application.status === 'pending'"
            @click="handleSubmitReview"
            :disabled="!selectedStatus || !reviewComment.trim()"
          >
            提交审核
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { Application } from '../App.vue';
import {
  FileText,
  Calendar,
  User,
  Tag,
  Award,
  MessageSquare,
} from 'lucide-vue-next';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Dialog from './ui/Dialog.vue';
import DialogContent from './ui/DialogContent.vue';
import DialogDescription from './ui/DialogDescription.vue';
import DialogHeader from './ui/DialogHeader.vue';
import DialogTitle from './ui/DialogTitle.vue';
import Label from './ui/Label.vue';
import Textarea from './ui/Textarea.vue';
import Separator from './ui/Separator.vue';
import StatusBadge from './StatusBadge.vue';

const props = defineProps<{
  application: Application;
}>();

const emit = defineEmits<{
  review: [applicationId: string, status: 'approved' | 'rejected', comment: string];
  close: [];
}>();

const reviewComment = ref('');
const selectedStatus = ref<'approved' | 'rejected' | null>(null);

const handleSubmitReview = () => {
  if (!selectedStatus.value) {
    alert('请选择审核结果');
    return;
  }

  if (!reviewComment.value.trim()) {
    alert('请填写审核意见');
    return;
  }

  emit('review', props.application.id, selectedStatus.value, reviewComment.value);
};

const handleClose = () => {
  emit('close');
};

const getPlaceholder = () => {
  if (selectedStatus.value === 'approved') {
    return '请填写通过理由（如：材料齐全，符合加分标准）';
  } else if (selectedStatus.value === 'rejected') {
    return '请填写驳回理由（如：缺少必要证明材料，建议补充...）';
  }
  return '请先选择审核结果，然后填写审核意见';
};
</script>