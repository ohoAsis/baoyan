<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
      <!-- 背景遮罩 -->
      <div class="fixed inset-0 bg-black bg-opacity-50 transition-opacity" @click="handleClose"></div>
      
      <!-- 弹窗内容 -->
      <div class="flex min-h-full items-center justify-center p-4">
        <div 
          class="relative bg-white rounded-lg shadow-xl max-w-lg w-full p-6 transform transition-all"
          @click.stop
        >
          <!-- 关闭按钮 -->
          <button
            class="absolute top-4 right-4 text-gray-400 hover:text-gray-600 focus:outline-none"
            @click="handleClose"
          >
            <X class="h-6 w-6" />
          </button>
          
          <!-- 插槽内容 -->
          <slot />
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { X } from 'lucide-vue-next'

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