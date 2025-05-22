<template>
  <div class="grid">
    <div class="grid">
      <div class="col-6" no-gutters>
        <div class="grid">
          <Search v-bind:loading="loading" @freeTextSearch="handleFreeTextSearch" />
        </div>
        <div class="grid">
          <start-page-text />
        </div>

        <div class="grid">
          <Filter
            style="padding-top: 2rem"
            @freeTextSearch="handleFreeTextSearch"
            @filterWithCoordinates="handleFilterWithCoordinates"
            @filterWithInSweden="handleFilterWithInSweden"
            @filterWithImages="handleFilterWithImages"
            @filterWithInType="handleFilterWithType"
            v-bind:filterSearchLoading="filterSearchLoading"
            v-bind:loading="loading"
          />
        </div>
      </div>

      <div class="col-6" no-gutters>
        <Collections
          @searchBotanyCollections="handleSearchBotanyCollections"
          @searchZooCollections="handleSearchZooCollections"
          @searchPaleaCollections="handleSearchPaleaCollections"
          @searchGeoCollections="handleSearchGeoCollections"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import StartPageText from './StartPageText.vue'
import Search from './SearchAllFields.vue'
import Collections from './CollectionsFilter.vue'
import Filter from './Filter.vue'

const emits = defineEmits([
  'filterWithCoordinates',
  'filterWithImages',
  'filterWithInSweden',
  'filterWithType',
  'freeTextSearch',
  'searchBotanyCollections',
  'searchGeCololections',
  'searchPaleaCollections',
  'searchZooCollections'
])

const props = defineProps(['loading', 'filterSearchLoading'])

function handleFreeTextSearch(value) {
  emits('freeTextSearch', value, 0, 50)
}

function handleSearchBotanyCollections() {
  emits('searchBotanyCollections')
}

function handleSearchZooCollections() {
  emits('searchZooCollections')
}

function handleSearchPaleaCollections() {
  emits('searchPaleaCollections')
}

function handleSearchGeoCollections() {
  emits('searchGeCololections')
}

function handleFilterWithCoordinates() {
  emits('filterWithCoordinates')
}

function handleFilterWithImages() {
  emits('filterWithImages')
}

function handleFilterWithInSweden() {
  emits('filterWithInSweden')
}

function handleFilterWithType() {
  emits('filterWithType')
}
</script>
