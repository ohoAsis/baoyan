<template>
  <p :class="cn('text-muted-foreground text-sm', className)">
    <slot />
  </p>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>