<template>
  <tbody data-slot="table-body" :class="cn('[&_tr:last-child]:border-0', className)">
    <slot />
  </tbody>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>