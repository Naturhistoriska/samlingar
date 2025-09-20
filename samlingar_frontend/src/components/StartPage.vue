<template>
  <div class="grid">
    <div class="col-6" no-gutters>
      <div class="grid startPageStyle">
        <Search @freeTextSearch="freeTextSearch" />
      </div>
      <div class="grid">
        <start-page-text />
      </div>

      <div class="grid">
        <Filter
          style="padding-top: 2rem"
          @filterSearch="filterSearch"
          @freeTextSearch="freeTextSearch"
        />
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
  emits('filterSearch', params)
}

function searchCollections(params) {
  emits('collectionsSearch', params)
}

function freeTextSearch(value) {
  emits('freeTextSearch', value)
}
</script>
