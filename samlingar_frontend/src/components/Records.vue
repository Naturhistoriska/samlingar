<template>
  <div>
    <RecordsTabs @freeTextSearch="handleFreeTextSearch" />
  </div>
</template>
<script setup>
import RecordsTabs from './RecordsTabs.vue'

const emits = defineEmits(['freeTextSearch'])

function handleFreeTextSearch(searchText, start, numPerPage) {
  console.log('handleFreeTextSearch', searchText, start, numPerPage)
  emits('freeTextSearch', searchText, start, numPerPage)
}
</script>
