<template>
  <div>
    <div class="grid">
      <div class="col-4" no-gutters>
        <search-records @freeTextSearch="handleFreeTextSearch" />
      </div>
      <div class="col-8" no-gutters>
        <Map />
      </div>
    </div>
    <div class="grid">
      <div class="col-12" no-gutters>
        <Records @freeTextSearch="handleFreeTextSearch" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { useStore } from 'vuex'
import Service from '../Service'
import SearchRecords from '../components/SearchRecords.vue'
import Records from '../components/Records.vue'
import Map from '../components/Map.vue'

const store = useStore()

const service = new Service()

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
