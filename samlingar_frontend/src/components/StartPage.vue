<template>
  <div>
    <AdvanceSearch v-if="isAdvanceSearch" @advanceSearch="handleAdvanceSearch" />
    <SimpleSearch v-else @simpleSearch="handleSimpleSearch" />
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'

import AdvanceSearch from './AdvanceSearch.vue'
import SimpleSearch from './SimpleSearch.vue'

import { useStore } from 'vuex'
const store = useStore()
const emits = defineEmits(['advanceSearch', 'simpleSearch'])

const isAdvanceSearch = ref(false)
watch(
  () => store.getters['isAdvanceSearch'],
  () => {
    isAdvanceSearch.value = store.getters['isAdvanceSearch']
  }
)

function handleSimpleSearch() {
  emits('simpleSearch')
}

function handleAdvanceSearch() {
  console.log('handleAdvanceSearch')
  emits('advanceSearch')
}
</script>
<style scoped>
.searchInput {
  width: 400px;
}
</style>
