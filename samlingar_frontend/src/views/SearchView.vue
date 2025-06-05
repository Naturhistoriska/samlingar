<template>
  <div>
    <!-- <div class="grid">
      <nav-link style="font-size: 10px" />
    </div> -->
    <div class="grid">
      <div class="col-4" no-gutters>
        <search-records @search="handleSearch" />
      </div>
      <div class="col-8" no-gutters>
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
        <Records @freeTextSearch="handleFreeTextSearch" @fetchMedia="handleMeadSearch" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, defineAsyncComponent, onMounted, ref, Suspense, watch } from 'vue'
import { useStore } from 'vuex'
import { onBeforeRouteLeave, onBeforeRouteUpdate, useRouter } from 'vue-router'
import Service from '../Service'
import SearchRecords from '../components/SearchRecords.vue'
import Records from '../components/Records.vue'
import LoadingSkeleton from '../components/baseComponents/LoadingSkeleton.vue'
// import Map from '../components/MyMap.vue'
// import NavLink from '../components/NavLink.vue'

const store = useStore()

const service = new Service()

onMounted(async () => {
  console.log('onMounted')
})

onBeforeRouteLeave((to, from) => {
  console.log('onBeforeRouteLeave', to, from)
  const { name } = to
  if (name === 'Home') {
    // store.commit('setShowResults', false)
  }
})

const AsyncMap = defineAsyncComponent({
  // the loader function
  loader: () => import('../components/MyMap.vue')

  //hydrate: hydrateOnVisible()
  // A component to use while the async component is loading
  // loadingComponent: () => import('../components/baseComponents/LoadingSkeleton.vue')
  // Delay before showing the loading component. Default: 200ms.
  // delay: 200

  // A component to use if the load fails
  // errorComponent: ErrorComponent,
  // The error component will be displayed if a timeout is
  // provided and exceeded. Default: Infinity.
  // timeout: 3000
})

// const AsyncMap = defineAsyncComponent(

//   () => import('../components/MyMap.vue')
// )

function handleMeadSearch() {
  console.log('handleMeadSearch')
}

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
