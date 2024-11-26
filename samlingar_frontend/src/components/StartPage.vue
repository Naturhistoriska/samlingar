<template>
  <div class="grid">
    <div class="grid">
      <div class="col-6" no-gutters>
        <div class="grid">
          <Search @simpleSearch="handleSimpleSearch" />
        </div>
        <div class="grid">
          <Filter
            style="padding-top: 2rem"
            @simpleSearch="handleSimpleSearch"
            @filterWithCoordinates="handleFilterWithCoordinates"
            @filterWithImages="handleFilterWithImages"
            @filterWithInSweden="handleFilterWithInSweden"
            @filterWithInType="handleFilterWithType"
          />
        </div>
        <!-- <MonthChart />
        <YearChart /> -->
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
import Search from '../components/Search.vue'
import Collections from '../components/CollectionsFilter.vue'
import Filter from '../components/Filter.vue'
import MonthChart from '../components/MonthChart.vue'
import YearChart from '../components/YearChart.vue'

const emits = defineEmits([
  'filterWithCoordinates',
  'filterWithImages',
  'filterWithInSweden',
  'filterWithType',
  'searchBotanyCollections',
  'searchGeCololections',
  'searchPaleaCollections',
  'searchZooCollections',
  'simpleSearch'
])

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

function handleSimpleSearch() {
  emits('simpleSearch')
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
