<template>
  <div data-slot="table-container" class="relative w-full overflow-x-auto">
    <table data-slot="table" :class="cn('w-full caption-bottom text-sm', className)">
      <slot />
    </table>
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>