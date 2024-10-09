<template>
  <div class="card">
    <div class="grid">
      <div class="flex flex-column col-4">
        <SearchFilter
          @searchByYear="handleSearchByYear"
          @searchByCollection="handleSearchByCollection"
        />
      </div>
      <div class="col-8">
        <div class="grid">
          <div class="col-6" style="font-size: 20px">
            <legend>
              {{ $t('results.searchResults') }} [{{ $t('results.num_results', totalRecords) }}]
              <br />
              <Button link @click="onClick">
                <small>{{ mapLinkText }}</small>
              </Button>
            </legend>
          </div>
        </div>
        <Map v-if="showMap" />
        <!-- <Map v-if="showMap" @search="handleMapSearch" /> -->
        <div v-else>
          <ResultDetail v-if="showDetail" />
          <ResultList v-else @search="handlePaginateSearch" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue'
import { useStore } from 'vuex'

import ResultDetail from './ResultDetail.vue'
import ResultList from './ResultList.vue'
import SearchFilter from './SearchFilter.vue'
import Map from './Map.vue'

import Service from '../Service'
const service = new Service()

const store = useStore()

let loading = ref(false)
let showMap = ref(false)

// watch(
//   () => showMap.value,
//   (newValue, oldValue) => {
//     console.log('showmap changed...', showMap)
//     showMap.value = newValue
//   }
// )

const mapLinkText = computed(() => {
  if (showMap.value) {
    return showDetail.value ? 'Back to result view' : 'Back to result list view'
  }
  return 'Show map view'
})

const showDetail = computed(() => {
  return store.getters['showDetail']
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

async function onClick() {
  const isMap = showMap.value
  console.log('isMap ...', isMap)

  showMap.value = !isMap
  if (showMap) {
    await handleMapSearch()
  }
}

function handleSearchByYear() {
  search('yearSearch')
}

function handleSearchByCollection() {
  console.log('handleSearchByCollection')

  if (showMap.value) {
    handleMapSearch()
  } else {
    search('collectionSearch')
  }
}

function handlePaginateSearch() {
  search('paginateSearch')
}

async function handleMapSearch() {
  console.log('handleMapSearch')

  loading = true
  const collection = store.getters['selectedCollection']
  const totalRecords = store.getters['totalRecords']
  const searchText = store.getters['searchText']
  const year = store.getters['year']

  service
    .conditionalSearch(searchText, 1, collection, year, totalRecords)
    .then((response) => {
      const results = response.occurrences

      store.commit('setMapRecords', results)
      setTimeout(() => {
        loading = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function search(value) {
  const collection = store.getters['selectedCollection']
  const numPerPage = store.getters['numPerPage']
  const searchText = store.getters['searchText']
  const start = store.getters['startRecord']
  const year = store.getters['year']

  service
    .searchFiltByCollection(searchText, start, collection, year, numPerPage)
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences
      const facetResults = response.facetResults

      if (value === 'paginateSearch') {
        store.commit('setNumPerPage', numPerPage)
        store.commit('setStartRecord', start)
      } else if (value === 'collectionSearch') {
        const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
        const occurrenceYears = yearFacet.fieldResult

        store.commit('setOccurrenceYears', occurrenceYears)
        store.commit('setSelectedCollection', collection)
      } else if (value === 'yearSearch') {
        const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
        const collections = collectionFacet.fieldResult

        store.commit('setCollections', collections)
        store.commit('setYear', year)
      }
      store.commit('setResults', results)
      store.commit('setTotalRecords', total)
      setTimeout(() => {
        loading = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}
</script>
<style scoped></style>
