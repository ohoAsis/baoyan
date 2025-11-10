<template>
  <h2 :class="cn('text-lg leading-none font-semibold', className)">
    <slot />
  </h2>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>