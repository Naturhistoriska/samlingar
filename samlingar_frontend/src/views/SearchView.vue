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
        <Records @freeTextSearch="handleFreeTextSearch" @fetchMedia="handleMediaSearch" />
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

onMounted(async () => {
  console.log('onMounted')
})

onBeforeRouteLeave((to, from) => {
  console.log('onBeforeRouteLeave', to, from)
  const { name } = to
  if (name === 'Home') {
  }
})

const AsyncMap = defineAsyncComponent({
  loader: () => import('../components/MyMap.vue')
})

function search(start, numPerPage) {
  const scientificName = store.getters['scientificName']
  const isFuzzy = store.getters['isFuzzySearch']

  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']
  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  const fields = store.getters['fields']

  // const params = new URLSearchParams({
  //   text: searchText,
  //   scientificName: scientificName,
  //   fuzzySearch: isFuzzy,
  //   hasImages: hasImages,
  //   isType: isType,
  //   isInSweden: isInSweden,
  //   hasCoordinates: hasCoordinates,
  //   startDate: startDate,
  //   endDate: endDate
  // })

  const params = new URLSearchParams({
    text: searchText
  })

  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('fuzzySearch', isFuzzy)
  }

  if (isType) {
    params.set('isType', isType)
  }

  if (isInSweden) {
    params.set('isInSweden', isInSweden)
  }

  if (hasImages) {
    params.set('hasImages', hasImages)
  }

  if (hasCoordinates) {
    params.set('hasCoordinates', hasCoordinates)
  }

  if (startDate) {
    params.set('startDate', startDate)
  }

  if (endDate) {
    params.set('endDate', endDate)
  }

  fields
    .filter((field) => field.text)
    .forEach((field) => {
      console.log('what...', field.value, field.text)
      params.set(field.value, field.text)
    })

  service
    .apiSearch(params, start, numPerPage)
    .then((response) => {
      const total = response.response.numFound
      const results = response.response.docs

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const geofacet = response.facets.geo.buckets
        store.commit('setGeoData', geofacet)
      } else {
        store.commit('setGeoData', null)
      }
    })
    .catch(() => {
      console.err('error')
    })
    .finally(() => {})
}

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
