<template>
  <div
    data-slot="progress"
    :class="cn('bg-primary/20 relative h-2 w-full overflow-hidden rounded-full', className)"
  >
    <div
      data-slot="progress-indicator"
      class="bg-primary h-full flex-1 transition-all"
      :style="{ transform: `translateX(-${100 - (value || 0)}%)` }"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  value?: number;
  className?: string;
}>();
</script>