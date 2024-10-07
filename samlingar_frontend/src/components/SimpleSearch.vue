<template>
  <div class="flex-col gap-2 searchInput">
    <InputGroup>
      <InputText
        id="simpleSearchInput1"
        v-model="value"
        @keydown.enter="onPressEnter"
        placeholder="Search"
        aria-describedby="simpleSearchInput-help"
      />
      <Button
        icon="pi pi-search"
        style="max-width: 30px; max-height: 30px"
        :loading="loading"
        @click="onClickSearch"
      />
    </InputGroup>

    <div class="advanceLink">
      <Button link @click="onClick">
        <small id="simpleSearchInput-help">{{ $t('search.advanceSearch') }} </small>
      </Button>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()

const value = ref()
let loading = ref(false)

function onPressEnter() {
  search()
}

function onClickSearch() {
  search()
}

function onClick() {
  console.log('clicked...')
  store.commit('setIsAdvanceSearch', true)
}

function search() {
  const searchText = value.value + '*'

  loading = true
  service
    .quickSearch(searchText, 1, 10)
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences

      let facetResults = response.facetResults
      let collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
      const collections = collectionFacet.fieldResult

      const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
      const occurrenceYears = yearFacet.fieldResult

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      store.commit('setCollections', collections)
      store.commit('setOccurrenceYears', occurrenceYears)

      store.commit('setSearchText', searchText)
      store.commit('setSelectedCollection', null)

      store.commit('setYear', null)
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
  min-width: 30%;
  text-align: left;
  float: left;
}

.advanceLink {
  float: right;
}
</style>
