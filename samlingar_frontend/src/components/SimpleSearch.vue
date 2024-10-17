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
        @click="onSearchClick"
      />
    </InputGroup>

    <div class="advanceLink">
      <Button link @click="onAdvanceSearchLinkClick">
        <small id="simpleSearchInput-help">{{ $t('search.advanceSearch') }} </small>
      </Button>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['simpleSearch'])

const value = ref()
let loading = ref(false)

function onPressEnter() {
  search()
}

function onSearchClick() {
  search()
}

function onAdvanceSearchLinkClick() {
  store.commit('setIsAdvanceSearch', true)
}

function search() {
  loading.value = true

  const searchText = value.value + '*'
  store.commit('setSearchText', searchText)
  emits('simpleSearch')

  setTimeout(() => {
    loading.value = false
  }, 2000)

  // const searchText = value.value + '*'

  // loading.value = true

  // service
  //   .quickSearch(searchText, 1, 10)
  //   .then((response) => {
  //     const total = response.totalRecords
  //     const results = response.occurrences

  //     let facetResults = response.facetResults
  //     let collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
  //     const collections = collectionFacet.fieldResult

  //     const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
  //     const occurrenceYears = yearFacet.fieldResult

  //     store.commit('setResults', results)
  //     store.commit('setTotalRecords', total)

  //     store.commit('setCollections', collections)
  //     store.commit('setOccurrenceYears', occurrenceYears)

  //     store.commit('setSearchText', searchText)
  //     store.commit('setSelectedCollection', null)

  //     store.commit('setYear', null)
  //     store.commit('setShowDetail', false)
  //     store.commit('setShowResults', true)

  //     setTimeout(() => {
  //       loading.value = false
  //     }, 2000)
  //   })
  //   .catch()
  //   .finally(() => {})
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
