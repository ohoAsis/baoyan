<template>
  <div class="relative">
    <select
      :value="modelValue"
      @change="handleChange"
      :class="cn(
        // 基础样式
        'flex w-full items-center justify-between gap-2 h-9 px-3 py-2 text-sm rounded-md border outline-none transition-colors',
        'bg-white dark:bg-neutral-800 hover:bg-neutral-50 dark:hover:bg-neutral-700',
        'focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:border-blue-500',
        'disabled:cursor-not-allowed disabled:opacity-50',
        props.class
      )"
    >
      <option v-if="placeholder" value="" disabled>{{ placeholder }}</option>
      <slot />
    </select>
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

const props = defineProps<{
  modelValue?: string;
  placeholder?: string;
  class?: string;
}>();

const emit = defineEmits<{
  'update:modelValue': [value: string];
}>();

const handleChange = (e: Event) => {
  emit('update:modelValue', (e.target as HTMLSelectElement).value);
};
</script>
