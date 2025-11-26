<template>
  <span
    class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&>svg]:size-3 gap-1 [&>svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden"
    :class="getVariantClasses()"
  >
    <slot />
  </span>
</template>

<script setup lang="ts">
const props = defineProps<{
  variant?: string;
}>();

function getVariantClasses() {
  const classes: Record<string, string> = {
    'default': 'border-transparent bg-green-500 text-white',
    'secondary': 'border-transparent bg-yellow-500 text-black',
    'destructive': 'border-transparent bg-red-500 text-white',
    'outline': 'text-foreground'
  };
  return classes[props.variant || 'default'] || classes['default'];
}
</script>
