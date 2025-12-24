<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center space-x-3">
            <div>
              <h1 class="text-xl">用户管理</h1>
              <p class="text-sm text-gray-500">管理员可查看和管理系统用户</p>
            </div>
          </div>
          <div>
            <button @click="handleLogout" class="text-sm text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md">
              退出登录
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Filter Section -->
      <div class="flex flex-wrap items-center gap-4 mb-8">
        <div class="flex items-center gap-2">
          <label for="role" class="text-sm font-medium text-gray-700">角色:</label>
          <select 
            id="role" 
            v-model="filterRole" 
            class="border border-gray-300 rounded-md px-3 py-2 text-sm"
          >
            <option value="">全部</option>
            <option value="student">student</option>
            <option value="reviewer">reviewer</option>
            <option value="admin">admin</option>
          </select>
        </div>
        
        <div class="flex-1 min-w-[200px]">
          <input 
            type="text" 
            v-model="filterKeyword" 
            placeholder="用户名 / 学号 / 邮箱" 
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
          >
        </div>
        
        <Button @click="handleSearch">查询</Button>
        <Button variant="primary" @click="openCreateUserModal">新建用户</Button>
        <Button 
          variant="secondary" 
          @click="fileInputRef?.click()" 
          :disabled="isImporting"
        >
          {{ isImporting ? '导入中...' : '导入学生 Excel' }}
        </Button>
        <input
          ref="fileInputRef"
          type="file"
          accept=".xlsx"
          class="hidden"
          @change="handleFileSelect"
        >
      </div>

      <!-- Users Table -->
      <div class="space-y-4">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>学号 / 工号</TableHead>
              <TableHead>用户名</TableHead>
              <TableHead>角色</TableHead>
              <TableHead>状态</TableHead>
              <TableHead>创建时间</TableHead>
              <TableHead>操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-if="isLoading">
              <TableCell colspan="6" class="text-center py-8">
                加载中...
              </TableCell>
            </TableRow>
            <TableRow v-else-if="users.length === 0">
              <TableCell colspan="6" class="text-center py-8 text-gray-500">
                暂无数据
              </TableCell>
            </TableRow>
            <TableRow v-for="user in users" :key="user.id">
              <TableCell>{{ user.username || '-' }}</TableCell>
              <TableCell>{{ user.username }}</TableCell>
              <TableCell>{{ user.role }}</TableCell>
              <TableCell>{{ user.isEnabled ? '启用' : '禁用' }}</TableCell>
              <TableCell>{{ user.createdAt }}</TableCell>
              <TableCell>
                <Button 
                  :variant="user.isEnabled ? 'secondary' : 'primary'" 
                  size="sm" 
                  @click="toggleUserStatus(user)"
                  :disabled="user.isLoading"
                >
                  {{ user.isLoading ? '处理中...' : (user.isEnabled ? '禁用' : '启用') }}
                </Button>
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>

        <!-- Pagination -->
        <div class="flex justify-between items-center py-4">
          <div class="text-sm text-gray-500">
            第 {{ currentPage }} / {{ totalPages }} 页
          </div>
          <div class="flex items-center gap-2">
            <Button 
              size="sm" 
              :disabled="currentPage === 1" 
              @click="changePage(currentPage - 1)"
            >
              上一页
            </Button>
            <Button 
              v-for="page in totalPages" 
              :key="page" 
              size="sm" 
              :variant="currentPage === page ? 'primary' : 'default'" 
              @click="changePage(page)"
            >
              {{ page }}
            </Button>
            <Button 
              size="sm" 
              :disabled="currentPage === totalPages" 
              @click="changePage(currentPage + 1)"
            >
              下一页
            </Button>
          </div>
        </div>
      </div>

      <!-- New User Modal -->
      <div v-if="showCreateUserModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
        <div class="bg-white rounded-lg shadow-lg max-w-md w-full">
          <div class="p-6 space-y-4">
            <h3 class="text-lg font-medium">新建用户</h3>
            
            <div class="space-y-2">
              <label for="identifier" class="text-sm font-medium text-gray-700">学号 / 工号</label>
              <input 
                id="identifier" 
                v-model="newUser.identifier" 
                type="text" 
                class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
              >
              <p v-if="formErrors.identifier" class="text-xs text-red-500">{{ formErrors.identifier }}</p>
            </div>
            
            <div class="space-y-2">
              <label for="new-role" class="text-sm font-medium text-gray-700">角色</label>
              <select 
                id="new-role" 
                v-model="newUser.role" 
                class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
              >
                <option value="">请选择角色</option>
                <option value="student">student</option>
                <option value="reviewer">reviewer</option>
                <option value="admin">admin</option>
              </select>
              <p v-if="formErrors.role" class="text-xs text-red-500">{{ formErrors.role }}</p>
            </div>
            
            <p class="text-xs text-gray-500 mt-2">
              新建用户将生成初始密码，用户首次登录后请及时修改
            </p>
            
            <div class="flex justify-end gap-2 mt-6">
              <Button variant="ghost" @click="closeCreateUserModal">取消</Button>
              <Button 
                variant="primary" 
                @click="handleCreateUser" 
                :disabled="isCreating"
              >
                {{ isCreating ? '创建中...' : '创建' }}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuth } from '../../stores/auth';
import apiClient from '../../api/client';
import Button from '../../components/ui/Button.vue';
import Table from '../../components/ui/Table.vue';
import TableBody from '../../components/ui/TableBody.vue';
import TableCell from '../../components/ui/TableCell.vue';
import TableHead from '../../components/ui/TableHead.vue';
import TableHeader from '../../components/ui/TableHeader.vue';
import TableRow from '../../components/ui/TableRow.vue';

const router = useRouter();
const { logout } = useAuth();

// Filter state
const filterRole = ref('');
const filterKeyword = ref('');

// Pagination state
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(10);

// Users data
const users = ref([]);
const isLoading = ref(false);

// Create user modal state
const showCreateUserModal = ref(false);
const newUser = ref({ identifier: '', role: '' });
const formErrors = ref({ identifier: '', role: '' });
const isCreating = ref(false);

// Import Excel state
const isImporting = ref(false);
const fileInputRef = ref<HTMLInputElement | null>(null);

// Function to fetch users from API
const fetchUsers = async () => {
  isLoading.value = true;
  try {
    const response = await apiClient.get('/admin/users', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        role: filterRole.value || undefined,
        keyword: filterKeyword.value || undefined
      }
    });
    const rawUsers = response.data.content || response.data;
    // Convert backend string status to frontend boolean isEnabled
    users.value = rawUsers.map((user: any) => ({
      ...user,
      isEnabled: user.status === 'active'
    }));
    // Assume backend returns totalPages in response, otherwise calculate from total elements
    if (response.data.totalPages) {
      totalPages.value = response.data.totalPages;
    }
  } catch (error) {
    console.error('Failed to fetch users:', error);
    alert('获取用户列表失败');
  } finally {
    isLoading.value = false;
  }
};

// Function to handle search
const handleSearch = () => {
  currentPage.value = 1;
  fetchUsers();
};

// Function to open create user modal
const openCreateUserModal = () => {
  showCreateUserModal.value = true;
};

// Function to close create user modal
const closeCreateUserModal = () => {
  showCreateUserModal.value = false;
  resetForm();
};

// Function to reset form
const resetForm = () => {
  newUser.value = { identifier: '', role: '' };
  formErrors.value = { identifier: '', role: '' };
};

// Function to handle create user
const handleCreateUser = async () => {
  // Validate form
  let isValid = true;
  
  if (!newUser.value.identifier.trim()) {
    formErrors.value.identifier = '学号 / 工号不能为空';
    isValid = false;
  } else {
    formErrors.value.identifier = '';
  }
  
  if (!newUser.value.role) {
    formErrors.value.role = '请选择角色';
    isValid = false;
  } else {
    formErrors.value.role = '';
  }
  
  if (isValid) {
    isCreating.value = true;
    try {
      // Prepare request body
      const requestBody = {
        username: newUser.value.identifier,
        role: newUser.value.role
      };
      
      await apiClient.post('/admin/users', requestBody);
      
      // Success handling
      alert('用户创建成功');
      closeCreateUserModal();
      fetchUsers(); // Reload current page
    } catch (error) {
      console.error('Failed to create user:', error);
      const errorMessage = error.response?.data?.message || '请稍后重试';
      alert(`创建用户失败: ${errorMessage}`);
    } finally {
      isCreating.value = false;
    }
  }
};

// Function to toggle user status
const toggleUserStatus = async (user: any) => {
  if (confirm(`确定要${user.isEnabled ? '禁用' : '启用'}用户 ${user.username} 吗？`)) {
    user.isLoading = true;
    try {
      const newStatus = !user.isEnabled;
      await apiClient.patch(`/admin/users/status/${user.id}`, {
        status: newStatus
      });
      
      alert('用户状态更新成功');
      fetchUsers(); // Reload current page with same filters and pagination
    } catch (error) {
      console.error('Failed to toggle user status:', error);
      let errorMessage = '更新用户状态失败';
      if (error.response?.status === 403) {
        errorMessage = '无权限操作';
      }
      alert(errorMessage);
    } finally {
      user.isLoading = false;
    }
  }
};

// Function to handle file selection and upload
const handleFileSelect = async (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (!input.files || input.files.length === 0) return;
  
  const file = input.files[0];
  if (file.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' && !file.name.endsWith('.xlsx')) {
    alert('请选择 .xlsx 格式的 Excel 文件');
    input.value = '';
    return;
  }
  
  isImporting.value = true;
  try {
    const formData = new FormData();
    formData.append('file', file);
    
    await apiClient.post('/admin/users/import-students', formData);
    
    alert('学生导入成功');
    fetchUsers(); // Reload current page with same filters and pagination
  } catch (error) {
    console.error('Failed to import students:', error);
    const errorMessage = error.response?.data?.message || '导入失败，请稍后重试';
    alert(`导入失败: ${errorMessage}`);
  } finally {
    isImporting.value = false;
    input.value = ''; // Reset file input
  }
};

// Function to change page
const changePage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    fetchUsers();
  }
};

// 退出登录处理函数
const handleLogout = () => {
  // 清除本地登录状态
  logout();
  // 跳转到登录页
  router.push('/login');
};

// 组件挂载时自动加载用户列表
onMounted(() => {
  // 检查是否存在合法admin token且当前列表为空
  const token = localStorage.getItem('auth_token');
  if (token && users.value.length === 0) {
    fetchUsers();
  }
});
</script>