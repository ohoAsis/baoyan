<template>
  <Badge :variant="variant">{{ text }}</Badge>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import Badge from './ui/Badge.vue';

const props = defineProps<{
  status: 'pending' | 'approved' | 'rejected';
}>();

const variants = {
  pending: { variant: 'secondary' as const, text: '待审核' },
  approved: { variant: 'default' as const, text: '已通过' },
  rejected: { variant: 'destructive' as const, text: '已驳回' },
};

const config = computed(() => variants[props.status]);
const variant = computed(() => config.value.variant);
const text = computed(() => config.value.text);
</script>