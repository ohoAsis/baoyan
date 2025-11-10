<template>
  <tr
    data-slot="table-row"
    :class="cn('hover:bg-muted/50 data-[state=selected]:bg-muted border-b transition-colors', className)"
  >
    <slot />
  </tr>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  className?: string;
}>();
</script>