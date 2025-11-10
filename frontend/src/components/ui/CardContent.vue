<template>
  <div data-slot="card-content" :class="cn('px-6 [&:last-child]:pb-6', className)">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>