import { computed, reactive, ref } from 'vue';
import type { User } from '../types';
import { authApi } from '../api/auth';

const state = reactive<{ 
  currentUser: User | null,
  isInitialized: boolean // 标记用户信息是否已初始化
}>({
  currentUser: null,
  isInitialized: false
});

// 初始化状态的Promise，用于路由守卫等待
let initPromise: Promise<boolean> | null = null;

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

// 初始化用户信息
const initUser = async (): Promise<boolean> => {
  // 如果已经初始化，直接返回
  if (state.isInitialized) {
    return true;
  }
  
  // 获取token
  const token = getToken();
  
  // 如果没有token，直接标记为已初始化
  if (!token) {
    state.isInitialized = true;
    return false;
  }
  
  try {
    // 调用后端接口获取当前用户信息
    const userData = await authApi.getCurrentUser();
    
    // 保存用户信息
    state.currentUser = userData;
    state.isInitialized = true;
    
    // 更新localStorage中的用户信息
    localStorage.setItem('currentUser', JSON.stringify(userData));
    
    return true;
  } catch (error) {
    console.error('Failed to initialize user:', error);
    // 初始化失败，清除token和用户信息
    state.currentUser = null;
    removeToken();
    localStorage.removeItem('currentUser');
    state.isInitialized = true;
    return false;
  }
};

export function useAuth() {
  const currentUser = computed(() => state.currentUser);
  const isAuthenticated = computed(() => !!state.currentUser && !!getToken());
  const isInitialized = computed(() => state.isInitialized);

  const login = (user: User, token: string) => {
    state.currentUser = user;
    saveToken(token);
    // 保存用户信息到localStorage，以便路由守卫可以读取
    localStorage.setItem('currentUser', JSON.stringify(user));
    state.isInitialized = true;
  };

  const logout = () => {
    state.currentUser = null;
    removeToken();
    // 清除localStorage中的用户信息
    localStorage.removeItem('currentUser');
    state.isInitialized = true;
  };

  const refreshUser = (user: User) => {
    state.currentUser = user;
    // 更新localStorage中的用户信息
    localStorage.setItem('currentUser', JSON.stringify(user));
  };

  // 初始化用户信息的公共方法
  const initializeUser = async (): Promise<boolean> => {
    // 如果已经有初始化Promise，直接返回
    if (initPromise) {
      return initPromise;
    }
    
    // 创建新的初始化Promise
    initPromise = initUser();
    
    try {
      const result = await initPromise;
      return result;
    } finally {
      // 初始化完成后清空Promise
      initPromise = null;
    }
  };

  return {
    currentUser,
    isAuthenticated,
    isInitialized,
    login,
    logout,
    refreshUser,
    getToken,
    initializeUser
  };
}

export function getCurrentUser() {
  return state.currentUser;
}

export function getAuthToken() {
  return getToken();
}

export function getUserInitialized() {
  return state.isInitialized;
}

