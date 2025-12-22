import { computed, reactive } from 'vue';
import type { User } from '../types';

const state = reactive<{ currentUser: User | null }>({
  currentUser: null,
});

// JWT token 常量
const TOKEN_KEY = 'auth_token';

// 从localStorage获取token
const getToken = (): string | null => {
  return localStorage.getItem(TOKEN_KEY);
};

// 保存token到localStorage
const saveToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token);
};

// 清除token
const removeToken = (): void => {
  localStorage.removeItem(TOKEN_KEY);
};

export function useAuth() {
  const currentUser = computed(() => state.currentUser);
  const isAuthenticated = computed(() => !!state.currentUser && !!getToken());

  const login = (user: User, token: string) => {
    state.currentUser = user;
    saveToken(token);
    // 保存用户信息到localStorage，以便路由守卫可以读取
    localStorage.setItem('currentUser', JSON.stringify(user));
  };

  const logout = () => {
    state.currentUser = null;
    removeToken();
    // 清除localStorage中的用户信息
    localStorage.removeItem('currentUser');
  };

  const refreshUser = (user: User) => {
    state.currentUser = user;
    // 更新localStorage中的用户信息
    localStorage.setItem('currentUser', JSON.stringify(user));
  };

  return {
    currentUser,
    isAuthenticated,
    login,
    logout,
    refreshUser,
    getToken
  };
}

export function getCurrentUser() {
  return state.currentUser;
}

export function getAuthToken() {
  return getToken();
}

