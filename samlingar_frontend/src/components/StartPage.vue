<template>
  <div>
    <AdvanceSearch v-if="isAdvanceSearch" @advanceSearch="handleAdvanceSearch" />
    <SimpleSearch v-else />
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'

import AdvanceSearch from './AdvanceSearch.vue'
import SimpleSearch from './SimpleSearch.vue'

import Service from '../Service'
const service = new Service()

import { useStore } from 'vuex'
const store = useStore()

const isAdvanceSearch = ref(false)
let loading = ref(false)

watch(
  () => store.getters['isAdvanceSearch'],
  () => {
    isAdvanceSearch.value = store.getters['isAdvanceSearch']
  }
)

function handleAdvanceSearch() {
  console.log('handleAdvanceSearch')

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const endDate = store.getters['endDate']

  loading = true
  service
    .advanceSearch(scientificName, speciesGroup, dataset, catalogNumber, startDate, endDate, 1, 10)
    .then((response) => {
      const total = response.totalRecords

      if (total > 0) {
        const results = response.occurrences
        const facetResults = response.facetResults

        const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
        const occurrenceYears = yearFacet.fieldResult

        store.commit('setOccurrenceYears', occurrenceYears)
        const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
        const collections = collectionFacet.fieldResult
        store.commit('setCollections', collections)
        store.commit('setResults', results)

        store.commit('setSelectedCollection', null)
        store.commit('setYear', null)
      }
      store.commit('setTotalRecords', total)
      store.commit('setShowDetail', false)
      store.commit('setShowResults', true)

      setTimeout(() => {
        loading = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}
</script>
<style scoped>
.searchInput {
  width: 400px;
}
</style>
