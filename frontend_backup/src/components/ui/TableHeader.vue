<template>
  <thead data-slot="table-header" :class="cn('[&_tr]:border-b', className)">
    <slot />
  </thead>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>