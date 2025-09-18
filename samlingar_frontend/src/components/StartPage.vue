<template>
  <div class="grid">
    <div class="col-6" no-gutters>
      <div class="grid startPageStyle">
        <Search @freeTextSearch="search" />
      </div>
      <div class="grid">
        <start-page-text />
      </div>

      <div class="grid">
        <Filter style="padding-top: 2rem" @filterSearch="filterSearch" @freeTextSearch="search" />
      </div>
    </div>

    <div class="col-6" no-gutters>
      <Collections @searchCollections="searchCollections" />
    </div>
  </div>
</template>
<script setup>
import StartPageText from './StartPageText.vue'
import Search from './SearchAllFields.vue'
import Collections from './CollectionsFilter.vue'
import Filter from './Filter.vue'

const emits = defineEmits(['filterSearch', 'search', 'collectionsSearch'])

function filterSearch(params) {
  console.log('params', params.toString())
  emits('filterSearch', params, 0, 10, true)
}

function searchCollections(params) {
  emits('collectionsSearch', params, 0, 10, true)
}

function search(value) {
  emits('search', value, 0, 10, true)
}
</script>
