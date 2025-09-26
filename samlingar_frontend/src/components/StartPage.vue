<template>
  <div class="grid">
    <div class="col-12" no-gutters>
      <Search @freeTextSearch="freeTextSearch" style="max-width: 800px" />
    </div>
    <Divider class="custom-divider" />
    <div class="grid">
      <div class="col-6 textStyle" no-gutters>
        <start-page-text />

        <Filter
          style="padding-top: 1rem"
          @filterSearch="filterSearch"
          @freeTextSearch="freeTextSearch"
        />
      </div>

      <div class="col-6 textStyle" no-gutters>
        <Collections @searchCollections="searchCollections" />
      </div>
    </div>
  </div>
</template>
<script setup>
import StartPageText from './StartPageText.vue'
import Search from './SearchAllFields.vue'
import Collections from './CollectionsFilter.vue'
import Filter from './Filter.vue'

const emits = defineEmits(['collectionsSearch', 'filterSearch', 'freeTextSearch'])

function filterSearch(params) {
  emits('filterSearch', params)
}

function searchCollections(params) {
  emits('collectionsSearch', params)
}

function freeTextSearch(value, mode) {
  emits('freeTextSearch', value, mode)
}
</script>
<style scoped>
.textStyle {
  padding-top: 3em;
  padding-right: 2em;
  font-size: 15px;
}

.custom-divider.p-divider-horizontal::before {
  border-top: 1px solid #ccc; /* Change color here */
}
</style>
