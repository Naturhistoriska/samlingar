<template>
  <div>
    <div class="grid">
      <div class="col-5" no-gutters>
        <search-records @search="search" />
      </div>
      <div class="col-7" no-gutters>
        <Suspense>
          <template #default>
            <async-map />
          </template>
          <template #fallback>
            <VueSpinnerDots size="20" color="red" />
          </template>
        </Suspense>
      </div>
    </div>
    <div class="grid">
      <div class="col-12" no-gutters>
        <Records
          @freeTextSearch="handleFreeTextSearch"
          @fetchMedia="handleMediaSearch"
          @search="search"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { defineAsyncComponent, onMounted } from 'vue'
import { useStore } from 'vuex'
import { onBeforeRouteLeave } from 'vue-router'
import Service from '../Service'
import SearchRecords from '../components/SearchRecords.vue'
import Records from '../components/Records.vue'

const store = useStore()

const service = new Service()

onBeforeRouteLeave((to, from) => {
  console.log('onBeforeRouteLeave', to, from)
  const { name } = to
  if (name === 'Home') {
  }
})

const AsyncMap = defineAsyncComponent({
  loader: () => import('../components/MyMap.vue')
})

onMounted(async () => {
  search(0, 10, true)
})

function search(start, numPerPage, saveData) {
  const fields = store.getters['fields']

  const scientificName = store.getters['scientificName']
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
    params.set('fuzzySearch', isFuzzy)
  }

  if (isType) {
    params.set('typeStatus', '*')
  }

  if (isInSweden) {
    params.set('country', 'Sweden')
  }

  if (hasImages) {
    params.set('hasImage', hasImages)
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

  service
    .apiSearch(params, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (saveData) {
        if (total > 0) {
          const geofacet = response.facets.map.buckets
          store.commit('setGeoData', geofacet)
        } else {
          store.commit('setGeoData', null)
        }
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
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

function handleMediaSearch() {
  console.log('handleMeadSearch')
}

function handleFreeTextSearch(value, start, numPerPage) {
  console.log('handleFreeTextSearch...', value, start, numPerPage)

  service
    .apiFreeTextSearch(value, start, numPerPage)
    .then((response) => {
      const total = response.response.numFound
      const results = response.response.docs

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      console.log('total:', total)
      console.log('results:', results)
    })
    .catch((error) => {
      console.error('Fetch error:', error)
    })
    .finally(() => {})
}
</script>
<style scoped>
/* .fullWidth {
  margin: 0 auto;
} */
</style>
