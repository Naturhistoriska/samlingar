<template>
  <div>
    <div class="grid">
      <div class="col-12" no-gutters>
        <AdvanceSearch v-if="isAdvanceSearch" @advanceSearch="handleAdvanceSearch" />
        <SimpleSearch
          v-else
          @simpleSearch="handleSimpleSearch"
          @searchWithFilter="handleSearchWithFilter"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'

import AdvanceSearch from './AdvanceSearch.vue'
import SimpleSearch from './SimpleSearch.vue'

import { useStore } from 'vuex'
const store = useStore()
const emits = defineEmits(['advanceSearch', 'searchWithFilter', 'statiscSearch', 'simpleSearch'])

onMounted(() => {
  console.log('mounted...')
  emits('statiscSearch')
})

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

function handleSearchWithFilter() {
  emits('searchWithFilter')
}
</script>
<style scoped>
.searchInput {
  width: 400px;
}
</style>
