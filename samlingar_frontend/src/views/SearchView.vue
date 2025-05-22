<template>
  <div>
    <div class="grid">
      <div class="col-4" no-gutters>
        <search-records @search="handleSearch" />
      </div>
      <div class="col-8" no-gutters><Map /></div>
    </div>
    <div class="grid">
      <div class="col-12" no-gutters>
        <Records @freeTextSearch="handleFreeTextSearch" />
      </div>
    </div>
  </div>
</template>
<script setup>
// import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { onBeforeRouteLeave, onBeforeRouteUpdate, useRouter } from 'vue-router'
import Service from '../Service'
import SearchRecords from '../components/SearchRecords.vue'
import Records from '../components/Records.vue'
import Map from '../components/MyMap.vue'

const store = useStore()

const service = new Service()

// onMounted(async () => {

//   console.log('onMounted')
//   const value = store.getters['searchText']
//   console.log('value....', value)
// })

onBeforeRouteLeave((to, from) => {
  console.log('onBeforeRouteLeave', to, from)
  const { name } = to
  if (name === 'Home') {
    // store.commit('setShowResults', false)
  }
})

function handleSearch(hasImage, hasMap, start, numPerPage) {
  console.log('handleSearch', hasImage, hasMap)
  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'

  const scientificName = store.getters['scientificName']
  const isFuzzy = store.getters['isFuzzySearch']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

  service
    .apiSearch(
      searchText,
      scientificName,
      isFuzzy,
      hasImage,
      hasMap,
      isType,
      isInSweden,
      start,
      numPerPage
    )
    .then((response) => {
      const total = response.response.numFound
      const results = response.response.docs

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      console.log('total:', total)
      console.log('results:', results)
    })
    .catch()
    .finally(() => {})
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
    .catch()
    .finally(() => {})
}
</script>
<style scoped>
/* .fullWidth {
  margin: 0 auto;
} */
</style>
