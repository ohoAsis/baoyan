<template>
  <textarea
    data-slot="textarea"
    :class="cn(
      'resize-none border-input placeholder:text-muted-foreground focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:bg-input/30 flex field-sizing-content min-h-16 w-full rounded-md border bg-input-background px-3 py-2 text-base transition-[color,box-shadow] outline-none focus-visible:ring-[3px] disabled:cursor-not-allowed disabled:opacity-50 md:text-sm',
      className
    )"
    :value="value"
    @input="onInput"
    v-bind="$attrs"
  />
</template>

<script setup lang="ts">
import { cn } from './utils';

interface TextareaProps {
  value?: string;
  className?: string;
}

const props = withDefaults(defineProps<TextareaProps>(), {
  value: ''
});

const emit = defineEmits<{
  'update:value': [value: string];
}>();

const onInput = (event: Event) => {
  const target = event.target as HTMLTextAreaElement;
  emit('update:value', target.value);
};
</script>