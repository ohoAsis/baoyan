<template>
  <div
    data-slot="select-content"
    :class="cn(
      'bg-popover text-popover-foreground relative z-50 max-h-96 min-w-[8rem] overflow-x-hidden overflow-y-auto rounded-md border shadow-md',
      className
    )"
  >
    <slot />
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>