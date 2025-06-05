<template>
  <div>
    <RecordsTabs @freeTextSearch="handleFreeTextSearch" @fetchMedia="handleMedia" />
  </div>
</template>
<script setup>
import RecordsTabs from './RecordsTabs.vue'

const emits = defineEmits(['fetchMedia', 'freeTextSearch'])

function handleMedia() {
  console.log('handleMedia')
  emits('fetchMedia')
}

function handleFreeTextSearch(searchText, start, numPerPage) {
  console.log('handleFreeTextSearch', searchText, start, numPerPage)
  emits('freeTextSearch', searchText, start, numPerPage)
}
</script>
