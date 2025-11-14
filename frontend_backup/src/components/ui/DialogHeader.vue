<template>
  <div :class="cn('flex flex-col gap-2 text-center sm:text-left', className)">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>