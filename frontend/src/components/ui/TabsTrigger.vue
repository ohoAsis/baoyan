<template>
  <button
    data-slot="tabs-trigger"
    :class="cn(
      'data-[state=active]:bg-card dark:data-[state=active]:text-foreground focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:outline-ring dark:data-[state=active]:border-input dark:data-[state=active]:bg-input/30 text-foreground dark:text-muted-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-xl border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*="size-"])]:size-4',
      isActive ? 'data-[state=active]:bg-card' : '',
      className
    )"
    @click="handleClick"
  >
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed, inject } from 'vue';
import { cn } from './utils';

const props = defineProps<{
  value: string;
  className?: string;
}>();

const tabsContext = inject<{
  modelValue: { value: string };
  'update:modelValue': (v: string) => void;
}>('tabs');

const isActive = computed(() => tabsContext?.modelValue.value === props.value);

const handleClick = () => {
  tabsContext?.['update:modelValue'](props.value);
};
</script>