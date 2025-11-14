<template>
  <div data-slot="tabs" :class="cn('flex flex-col gap-2', className)">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { provide, ref, watch } from 'vue';
import { cn } from './utils';

const props = defineProps<{
  modelValue?: string;
  className?: string;
}>();

const emit = defineEmits<{
  'update:modelValue': [value: string];
}>();

const currentValue = ref(props.modelValue || '');

watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal !== undefined) {
      currentValue.value = newVal;
    }
  }
);

const updateValue = (value: string) => {
  currentValue.value = value;
  emit('update:modelValue', value);
};

provide('tabs', {
  modelValue: currentValue,
  'update:modelValue': updateValue,
});
</script>