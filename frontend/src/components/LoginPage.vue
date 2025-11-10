<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
    <Card class="w-full max-w-md">
      <CardHeader class="text-center">
        <CardTitle class="text-2xl">保研加分小助手</CardTitle>
        <CardDescription>请登录您的账户以继续使用系统</CardDescription>
      </CardHeader>
      <CardContent class="space-y-4">
        <div class="space-y-2">
          <Label for="role">用户类型</Label>
          <Select v-model="role">
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="student">学生</SelectItem>
              <SelectItem value="admin">辅导员/管理员</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div class="space-y-2">
          <Label for="username">{{ role === 'student' ? '学号' : '工号' }}</Label>
          <Input
            id="username"
            :placeholder="role === 'student' ? '请输入学号' : '请输入工号'"
            v-model="username"
          />
        </div>

        <div class="space-y-2">
          <Label for="password">密码</Label>
          <Input
            id="password"
            type="password"
            placeholder="请输入密码"
            v-model="password"
          />
        </div>

        <Button
          class="w-full"
          @click="handleLogin"
          :disabled="!username || !password"
        >
          登录
        </Button>

        <div class="text-sm text-gray-500 text-center space-y-1">
          <p>演示账号：</p>
          <p>学生 - 用户名: student, 密码: 123456</p>
          <p>管理员 - 用户名: admin, 密码: 123456</p>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { User, UserRole } from '../types';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import CardDescription from './ui/CardDescription.vue';
import CardHeader from './ui/CardHeader.vue';
import CardTitle from './ui/CardTitle.vue';
import Button from './ui/Button.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import Select from './ui/Select.vue';
import SelectContent from './ui/SelectContent.vue';
import SelectItem from './ui/SelectItem.vue';
import SelectTrigger from './ui/SelectTrigger.vue';
import SelectValue from './ui/SelectValue.vue';

const emit = defineEmits<{
  login: [user: User];
}>();

const username = ref('');
const password = ref('');
const role = ref<UserRole>('student');

const handleLogin = () => {
  if (!username.value || !password.value) return;

  const mockUser: User = {
    id: Math.random().toString(36).substr(2, 9),
    name: username.value,
    studentId: role.value === 'student' ? '2021' + Math.random().toString().substr(2, 6) : undefined,
    major: role.value === 'student' ? '计算机科学与技术' : undefined,
    role: role.value,
  };

  emit('login', mockUser);
};
</script>