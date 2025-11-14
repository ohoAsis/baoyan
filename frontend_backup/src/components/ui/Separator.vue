<template>
  <div
    data-slot="separator-root"
    :class="cn(
      'bg-border shrink-0',
      orientation === 'horizontal' ? 'h-px w-full' : 'h-full w-px',
      className
    )"
  ></div>
</template>

<script setup lang="ts">
import { cn } from './utils';

withDefaults(
  defineProps<{
    orientation?: 'horizontal' | 'vertical';
    decorative?: boolean;
    className?: string;
  }>(),
  {
    orientation: 'horizontal',
    decorative: true,
  }
);
</script>