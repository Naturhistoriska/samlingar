<template>
  <div>
    <RecordsTabs
      @freeTextSearch="handleFreeTextSearch"
      @fetchMedia="handleMedia"
      @search="handleSearch"
    />
  </div>
</template>
<script setup>
import RecordsTabs from './RecordsTabs.vue'

const emits = defineEmits(['fetchMedia', 'freeTextSearch', 'search'])

function handleSearch(start, numPerPage, saveData) {
  emits('search', start, numPerPage, saveData)
}

function handleMedia() {
  console.log('handleMedia')
  emits('fetchMedia')
}

function handleFreeTextSearch(searchText, start, numPerPage) {
  console.log('handleFreeTextSearch', searchText, start, numPerPage)
  emits('freeTextSearch', searchText, start, numPerPage)
}
</script>
