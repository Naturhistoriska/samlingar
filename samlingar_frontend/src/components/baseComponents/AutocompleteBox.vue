<template>
  <div class="justify-right" style="float: right">
    <FloatLabel>
      <InputGroup>
        <AutoComplete
          forceSelection
          v-model="value"
          size="small"
          :inputId="id"
          :suggestions="items"
          :minLength="3"
          :placeholder="placeholder"
          :multiple="multiple"
          @complete="apiAutoComplete"
          :inputStyle="acwidth"
        />
        <Button icon="pi pi-search" :loading="loading" @click="apiSearch" text />
        <label for="ac">{{ $t('search.searchSpecies') }} </label>
      </InputGroup>
    </FloatLabel>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'

import Service from '../Service'
const service = new Service()

// const emits = defineEmits(['autoCompleteSearch'])

const props = defineProps(['id', 'multiple', 'placeholder'])

const acwidth = ref()
const items = ref([])
const value = ref('')

onMounted(() => {
  acwidth.modelValue = { width: '300px' }
})

const apiAutoComplete = (event) => {
  console.log('search', event.query)
  items.value = [...Array(10).keys()].map((item) => event.query + '-' + item)
}

function autoCompleteSearch() {
  console.log('apiAutoComplete', value.query)
  let searchText = event.query

  service
    .apiAutoCompleteSearch(searchText)
    .then((response) => {
      const facets = response.facets.scientificName
      if (facets) {
        this.items = facets.buckets.map((a) => a.val)
      }
    })
    .catch()
    .finally(() => {})

  // emits('freeTextSearch', value, last, 10)
}

// onChange() {
//   this.itemSelected = false
// },

// onItemSelect() {
//   console.log('onItemSelect')
//   this.itemSelected = true
// },

// onPressEnter() {
//   this.apiSearch()
// },

// function search() {
//   console.log('search')

//   let searchText = value.value

//   service
//     .apiAutoCompleteSearch(searchText)
//     .then((response) => {
//       const facets = response.facets.scientificName
//       if (facets) {
//         this.items = facets.buckets.map((a) => a.val)
//       }
//     })
//     .catch()
//     .finally(() => {})
// }

// sbdi
function sbdiAutoComplete(event) {
  service
    .autoComplete(searchText, 0, 10)
    .then((response) => {
      this.items = response.occurrences
        .filter((item) => item.scientificName.toLowerCase().startsWith(searchText.toLowerCase()))
        .map((a) => a.scientificName)
        .filter((value, index, self) => self.indexOf(value) === index)
    })
    .catch()
    .finally(() => {})
}

function completeSearch() {
  this.loading = true
  const searchText = this.itemSelected ? this.search : this.search + '*'
  service
    .quickSearch(searchText, 1, 10)
    .then((response) => {
      const total = response.totalRecords
      this.results = response.occurrences

      if (total > 0) {
        const facetResults = response.facetResults

        const typeStatusFacet = facetResults.find((facet) => facet.fieldName === 'typeStatus')
        const typeStatus = typeStatusFacet.fieldResult
        this.setTypeStatus(typeStatus)

        const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
        const collections = collectionFacet.fieldResult
        this.setCollections(collections)

        const pointFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
        const point = pointFacet.fieldResult
        this.setLatLong(point)

        console.log('point length:', point.length)
      } else {
        this.setCollections([])
        this.setTypeStatus([])
      }
      this.setResults(this.results)
      this.setSearchText(searchText)
      this.setSelectedCollection(null)
      this.setShowDetail(false)
      this.setShowResults(true)
      this.setTotalRecords(total)

      setTimeout(() => {
        this.loading = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
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
