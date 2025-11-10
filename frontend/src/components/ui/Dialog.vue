<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50" @click.self="handleClose">
      <div
        class="fixed inset-0 z-50 bg-black/50 data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0"
      ></div>
      <div class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <slot />
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  modelValue?: boolean;
  open?: boolean;
}>()

const emit = defineEmits<{
  'update:open': [value: boolean]
  'update:modelValue': [value: boolean]
}>()

const isOpen = computed(() => props.modelValue ?? props.open ?? false)

const handleClose = () => {
  emit('update:open', false)
  emit('update:modelValue', false)
}
</script>
