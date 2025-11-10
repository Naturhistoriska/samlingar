<template>
  <div class="page-container">
    <div v-if="loading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>

    <div class="grid responsive-grid">
      <div class="col-12 md:col-5" no-gutters>
        <search-records @search="handleSearch" />
      </div>
      <div class="col-12 md:col-7" no-gutters v-if="isLargeMap">
        <Suspense>
          <template #default>
            <keep-alive>
              <async-map />
            </keep-alive>
          </template>
          <template #fallback>
            <VueSpinnerDots size="20" color="red" />
          </template>
        </Suspense>
      </div>
      <div class="col-12 md:col-7" no-gutters v-else>
        <Suspense>
          <template #default>
            <keep-alive>
              <async-map />
            </keep-alive>
          </template>
          <template #fallback>
            <VueSpinnerDots size="20" color="red" />
          </template>
        </Suspense>
      </div>
    </div>

    <div class="grid">
      <div class="col-12" no-gutters>
        <records-tabs @search="search" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { defineAsyncComponent, onMounted, ref, toRaw, watch } from 'vue'
import { useStore } from 'vuex'
import Service from '../Service'

import SearchRecords from '../components/SearchRecords.vue'
import RecordsTabs from '../components/RecordsTabs.vue'

import { entryType, previousRoute, currentRoute } from '@/router'

const store = useStore()

const service = new Service()

const AsyncMap = defineAsyncComponent({
  loader: () => import('../components/MapSwitch.vue')
})

let loading = ref(false)
let isLargeMap = ref(true)
// let totalCount = ref()

// watch(
//   () => store.getters['totalRecords'],
//   (newValue, oldValue) => {
//     console.log('TotalRecord', newValue)
//   }
// )

onMounted(async () => {
  console.log('onMounted SearchView')

  const from = previousRoute.value?.fullPath
  const to = currentRoute.value?.fullPath

  if (entryType.value === 'first-visit' || entryType.value === 'reload') {
    const queries = toRaw(currentRoute.value?.query)
    console.log('queries', queries)
    let params = new URLSearchParams({
      catchall: '*'
    })

    if (queries) {
      for (const [key, value] of Object.entries(queries)) {
        params.set(key, value)
      }
    }
    search(params, 0, 20, true)
  } else if (entryType.value === 'internal') {
    if (from === '/') {
      // do nothing
    } else {
      // do something

      let params = store.getters['searchParams']

      if (params === null) {
        params = buildParams()
        search(params, 0, 20, true)
      }
    }
  }
  // else if (entryType.value === 'internal') {
  // if (!from.includes('/record')) {
  // if (!isPushed) {
  // let params = new URLSearchParams({
  // catchall: '*'
  // })
  // search(params, 0, 20, true)
  // }
  // }
  // }

  // setTimeout(() => {
  //   loading.value = false
  // }, 2000)
})

// function download() {
// loading.value = true
//
// setTimeout(() => {
// loading.value = false
// }, 2000)
// }

function handleSearch() {
  const params = buildParams(true)
  search(params, 0, 20, true)
  // store.commit('setSearchParams', params)
}

async function search(params, start, numPerPage, saveData) {
  loading.value = true
  await service
    .apiSearch(params, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response
      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const totalGeoData = response.facets.coordinates.count
        const collectionfacet = response.facets.collectionName.buckets
        store.commit('setSelectedCollectionGroup', collectionfacet)
        store.commit('setTotalGeoData', totalGeoData)
      } else {
        store.commit('setSelectedCollectionGroup', null)
        store.commit('setTotalGeoData', 0)
      }
      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setSearchParams', params)
      store.commit('setResetMapData', true)

      store.commit('setPageNum', 0)
      store.commit('setRowsPerPage', 20)
      loading.value = false
    })
}

function buildParams() {
  const fields = store.getters['fields']

  const scientificName = store.getters['scientificName']
  const searchMode = store.getters['searchMode']

  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']
  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const nordicCountry = store.getters['filterNordicCountry']

  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'
  const fullTextSearchMode = store.getters['fullTextSearchMode']

  const selectedCollection = store.getters['selectedCollection']
  console.log('selectedCollection', selectedCollection)

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  const startYear = store.getters['startYear']
  const endYear = store.getters['endYear']

  const dateFilter = store.getters['dateFilter']

  const params = new URLSearchParams({
    catchall: searchText,
    mode: fullTextSearchMode
  })

  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('searchMode', searchMode)
  }

  if (isType) {
    params.set('typeStatus', '*')
  }

  if (isInSweden) {
    params.set('country', 'Sweden')
  }

  if (hasImages) {
    params.set('hasImage', '*')
  }

  if (hasCoordinates) {
    params.set('geo', '*')
  }

  if (nordicCountry && !isInSweden) {
    params.set('country', '(Sweden Denmark Finland Norway Iceland)')
  }

  if (dateFilter === 'date') {
    if (startDate) {
      params.set('startDate', startDate)
    }
    if (endDate) {
      params.set('endDate', endDate)
    }
  } else {
    if (startYear && endYear) {
      const yearQuery = `[${startYear.getFullYear()} TO ${endYear.getFullYear()}]`
      params.set('year', yearQuery)
    }
  }

  if (selectedCollection !== null) {
    const newValue = selectedCollection.replace(/'/g, '"')
    params.set('collectionCode', newValue)
  }

  if (fields) {
    fields
      .filter((field) => field.text)
      .forEach((field) => {
        params.set(field.value, field.text)
      })
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
.page-container {
  width: 100%;
  padding: 1rem;
  box-sizing: border-box;
}

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
  z-index: 9999;
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
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* @keyframes spin {
  to {
    transform: rotate(360deg);
  }
} */

/* Responsive layout grid */
.responsive-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.responsive-grid > div {
  flex: 1;
}

/* 📱 Mobile layout */
@media (max-width: 768px) {
  .responsive-grid {
    flex-direction: column;
  }

  .responsive-grid > div {
    width: 100%;
  }

  .page-container {
    padding: 0.5rem;
  }

  .spinner {
    width: 30px;
    height: 30px;
  }
}
</style>
