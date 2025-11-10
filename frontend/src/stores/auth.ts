import { computed, reactive } from 'vue';
import type { User } from '../types';

const state = reactive<{ currentUser: User | null }>({
  currentUser: null,
});

export function useAuth() {
  const currentUser = computed(() => state.currentUser);

  const login = (user: User) => {
    state.currentUser = user;
  };

  const logout = () => {
    state.currentUser = null;
  };

  return {
    currentUser,
    login,
    logout,
  };
}

export function getCurrentUser() {
  return state.currentUser;
}

