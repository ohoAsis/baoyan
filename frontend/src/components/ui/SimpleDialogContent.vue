<template>
  <div :class="className" class="w-full">
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  className?: string;
}>();
</script>