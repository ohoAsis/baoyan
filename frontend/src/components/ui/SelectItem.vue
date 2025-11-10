<template>
  <option
    :value="value"
    :class="cn(
      'focus:bg-accent focus:text-accent-foreground relative flex w-full cursor-default items-center gap-2 rounded-sm py-1.5 pr-8 pl-2 text-sm outline-hidden select-none',
      className
    )"
  >
    <slot />
  </option>
</template>

<script setup lang="ts">
import { cn } from './utils';

defineProps<{
  value: string;
  className?: string;
}>();
</script>