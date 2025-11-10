<template>
  <div
    v-if="isActive"
    data-slot="tabs-content"
    :class="cn('flex-1 outline-none', className)"
  >
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed, inject } from 'vue';
import { cn } from './utils';

const props = defineProps<{
  value: string;
  className?: string;
}>();

const tabsContext = inject<{
  modelValue: { value: string };
}>('tabs');

const isActive = computed(() => tabsContext?.modelValue.value === props.value);
</script>