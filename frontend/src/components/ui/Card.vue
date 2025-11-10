<template>
  <div data-slot="card" :class="cn('bg-card text-card-foreground flex flex-col gap-6 rounded-xl border', className)">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>