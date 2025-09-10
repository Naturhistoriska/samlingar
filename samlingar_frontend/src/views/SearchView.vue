<template>
  <div>
    <div class="grid">
      <div class="col-5" no-gutters>
        <search-records @search="handleSearch" />
      </div>
      <div class="col-7" no-gutters>
        <Suspense>
          <template #default>
            <keep-alive>
              <async-map />
            </keep-alive>
          </template>

          <!-- <async-map
                v-bind:total="totalCount"
                v-bind:entry="entryType"
                v-bind:from="previousRoute?.fullPath"
              /> -->
          <template #fallback>
            <VueSpinnerDots size="20" color="red" />
          </template>
        </Suspense>
      </div>
    </div>

    <div class="grid">
      <div class="col-12" no-gutters>
        <records-tabs @download="download" @exportData="preparaDataExport" @search="search" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { defineAsyncComponent, onMounted, ref, toRaw } from 'vue'
import { useStore } from 'vuex'
import Service from '../Service'

import SearchRecords from '../components/SearchRecords.vue'
import RecordsTabs from '../components/RecordsTabs.vue'

import { entryType, previousRoute, currentRoute } from '@/router'

const store = useStore()

const service = new Service()

const AsyncMap = defineAsyncComponent({
  loader: () => import('../components/NewMap.vue')
  // loader: () => import('../components/Map2.vue')
})

let loading = ref(true)
let totalCount = ref()

onMounted(async () => {
  console.log('onMounted SearchView')

  // const from = previousRoute.value?.fullPath
  // const to = currentRoute.value?.fullPath

  if (entryType.value === 'first-visit' || entryType.value === 'reload') {
    const queries = toRaw(currentRoute.value?.query)
    let params = new URLSearchParams({
      text: '*'
    })

    if (queries) {
      for (const [key, value] of Object.entries(queries)) {
        params.set(key, value)
      }
    }
    store.commit('setSearchParams', params)
    search(params, 0, 10, true)
  }

  setTimeout(() => {
    loading.value = false
  }, 2000)
})

function download() {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 2000)
}

function preparaDataExport() {
  loading.value = true
  const totalRecords = store.getters['totalRecords']
  let params = buildParams(false)

  service
    .apiPreparaExport(params, totalRecords)
    .then((response) => {
      const results = response

      store.commit('setExportData', results)

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function handleSearch() {
  const params = buildParams(true)
  search(params, 0, 10, true)
  store.commit('setSearchParams', params)
}

async function search(params, start, numPerPage, saveData) {
  console.log('do search....')
  loading.value = true
  // const params = buildParams()
  await service
    .apiSearch(params, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      totalCount.value = total

      if (saveData) {
        if (total > 0) {
          const collectionfacet = response.facets.dataResourceName.buckets
          store.commit('setCollections', collectionfacet)
        } else {
          store.commit('setCollections', null)
        }
      }

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

function buildParams(saveParams) {
  const fields = store.getters['fields']

  const scientificName = store.getters['scientificName']
  const searchMode = store.getters['searchMode']
  const isFuzzy = store.getters['isFuzzySearch']

  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']
  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'

  const dataResource = store.getters['dataResource']

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  // const collectionGroup = store.getters['collectionGroup']

  const params = new URLSearchParams({
    text: searchText
  })

  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('searchMode', searchMode)
    params.set('fuzzySearch', isFuzzy)
  }

  if (isType) {
    params.set('typeStatus', '*')
  }

  if (isInSweden) {
    params.set('country', 'Sweden')
  }

  if (hasImages) {
    params.set('associatedMedia', '*')
  }

  if (hasCoordinates) {
    params.set('lat_long', '*')
  }

  // if (collectionGroup) {
  //   params.set('collectionCode', collectionGroup)
  // }

  if (startDate) {
    params.set('startDate', startDate)
  }

  if (endDate) {
    params.set('endDate', endDate)
  }

  if (dataResource) {
    let newValue = dataResource.replace(/'/g, '"')
    params.set('dataResourceName', newValue)
  }

  if (fields) {
    fields
      .filter((field) => field.text)
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }

  if (saveParams) {
    store.commit('setSearchParams', params)
  }

  return params
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

// function handleMediaSearch() {
//   console.log('handleMeadSearch')
// }

// function handleFreeTextSearch(value, start, numPerPage) {
//   console.log('handleFreeTextSearch...', value, start, numPerPage)

//   service
//     .apiFreeTextSearch(value, start, numPerPage)
//     .then((response) => {
//       const total = response.response.numFound
//       const results = response.response.docs

//       store.commit('setResults', results)
//       store.commit('setTotalRecords', total)

//       console.log('total:', total)
//       console.log('results:', results)
//     })
//     .catch((error) => {
//       console.error('Fetch error:', error)
//     })
//     .finally(() => {})
// }
</script>
<style scoped>
.spinner-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.spinner {
  border: 6px solid #f3f3f3;
  border-top: 6px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
