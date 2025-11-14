<template>
  <span data-slot="select-value" :class="cn('', className)">
    <slot />
  </span>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  placeholder?: string;
  className?: string;
}>();
</script>