<template>
  <div class="justify-right" style="float: right">
    <FloatLabel>
      <InputGroup>
        <AutoComplete
          id="autosearch"
          v-model="search"
          :suggestions="items"
          :minLength="3"
          @complete="apiAutoComplete"
          @keydown.enter="apiSearch"
          @change="onChange"
          @itemSelect="onItemSelect"
          inputId="ac"
          :inputStyle="acwidth"
        />
        <Button
          icon="pi pi-search"
          :loading="loading"
          @click="apiSearch"
          text
          style="background-color: #144836 !important; border-color: #1d634a !important"
        />
        <label for="ac">{{ $t('search.searchSpecies') }} </label>
      </InputGroup>
    </FloatLabel>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()

const router = useRouter()
const route = useRoute()

const currentUrl = computed(() => route.fullPath)

const acwidth = ref()
const loading = ref(false)
const items = ref([])
const itemSelected = ref(false)
const search = ref()

onMounted(() => {
  acwidth.value = { width: '300px' }

  console.log('currentUrl', currentUrl)
})

function onChange() {
  itemSelected.value = false
}

function onItemSelect() {
  itemSelected.value = true
}

function apiAutoComplete(event) {
  console.log('apiAutoComplete', event.query)
  let searchText = event.query

  service
    .apiAutoCompleteSearch(searchText, 'copy_scientificName')
    .then((response) => {
      const facets = response.facets.facet
      if (facets) {
        items.value = facets.buckets.map((a) => a.val)
      } else {
        items.value = null
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

function apiSearch() {
  let searchText = search.value
  const fuzzySearch = !itemSelected.value
  const searchMode = fuzzySearch ? 'contains' : 'equals'

  const params = new URLSearchParams({
    copy_scientificName: searchText,
    searchMode
  })

  service
    .apiScientificnameSearch(searchText, searchMode)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)
      store.commit('setScientificName', searchText)
      store.commit('setSearchMode', searchMode)
      store.commit('setSearchParams', params)

      if (total > 0) {
        const facets = response.facets
        const totalGeoData = facets.coordinates.count
        store.commit('setTotalGeoData', totalGeoData)

        const collectionfacet = facets.collectionName.buckets
        store.commit('setSelectedCollectionGroup', collectionfacet)
      } else {
        store.commit('setTotalGeoData', 0)
        store.commit('setSelectedCollectionGroup', null)
      }
      reset()
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      search.value = undefined
      store.commit('setResetMapData', true)

      if (currentUrl.value !== '/search') {
        router.push('/search')
      } else {
        store.commit('setClearSearch', true)
      }
    })
}

function reset() {
  store.commit('setSearchText', null)
  store.commit('setFullTextSearchMode', 'contains')

  store.commit('setCollectionGroup', null)
  store.commit('setSelectedCollection', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setFields', [])
  store.commit('setDataResource', null)
  store.commit('setDates', null)

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)
}
</script>

<style scoped>
.p-floatlabel label {
  color: #c0bdbd !important;
}
.p-button-text {
  color: #fff !important;
  background: var(--p-emerald-500) !important;
  border-start-end-radius: var(--p-inputgroup-addon-border-radius) !important;
  border-end-end-radius: var(--p-inputgroup-addon-border-radius) !important;
  border: 1px solid var(--p-button-primary-border-color) !important;
  text-decoration: none !important;
}
.p-button-text:hover {
  /* color: #fff !important; */
  text-decoration: none !important;
  background: var(--p-emerald-500) !important;
  border-start-end-radius: var(--p-inputgroup-addon-border-radius);
  border-end-end-radius: var(--p-inputgroup-addon-border-radius);
  border: 1px solid var(--p-button-primary-border-color);
}
</style>
