<template>
  <div class="grid">
    <div></div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const props = defineProps(['result'])

const record = computed(() => {
  return props.record
})
</script>
<style scoped></style>
