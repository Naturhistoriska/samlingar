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
        <Button icon="pi pi-search" :loading="loading" @click="apiSearch" text />
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
      }
    })
    .catch()
    .finally(() => {})
}

function apiSearch() {
  let searchText = search.value
  const fuzzySearch = !itemSelected.value
  const searchMode = fuzzySearch ? 'contains' : 'equals'

  const params = new URLSearchParams({
    copy_scientificName: searchText,
    searchMode,
    fuzzySearch
  })

  service
    .apiScientificnameSearch(searchText, searchMode)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      if (total > 0) {
        const collectionCodefacet = response.facets.collectionCode.buckets
        const collectionNamefacet = response.facets.collectionName.buckets

        store.commit('setSelectedCollectionGroup', collectionNamefacet)
        store.commit('setSelectedCollection', collectionCodefacet)
      }

      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterImage', false)
      store.commit('setFilterType', false)

      store.commit('setSelectedCollection', null)

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)
      store.commit('setScientificName', searchText)
      store.commit('setIsFuzzySearch', fuzzySearch)
      store.commit('setSearchMode', searchMode)
      store.commit('setSearchText', null)

      store.commit('setSearchParams', params)
    })
    .catch()
    .finally(() => {
      console.log('currentUrl....', currentUrl.value)
      search.value = undefined
      store.commit('setIsUrlPush', true)

      // if (currentUrl.value !== '/search') {
      //   router.push('/search')
      // }
      router.push('/search')
    })
}

// export default {
//   // name: 'QuickSearch',
//   // components: {},
//   // data: () => ({
//   //   acwidth: {},
//   //   loading: false,
//   //   items: [],
//   //   itemSelected: false,
//   //   results: [],
//   //   search: ''
//   // }),

//   // mounted() {
//   //   this.acwidth = { width: '300px' }
//   //   console.log('currentUrl', currentUrl)
//   // },

//   methods: {
//     ...mapMutations([
//       'setCollections',
//       'setIsFuzzySearch',
//       'setResults',
//       'setSearchMode',
//       'setSearchText',
//       'setScientificName',
//       'setTotalRecords',
//       'setIsUrlPush'
//     ]),

//     onChange() {
//       this.itemSelected = false
//     },

//     onItemSelect() {
//       this.itemSelected = true
//     },

//     apiAutoComplete(event) {
//       console.log('apiAutoComplete', event.query)
//       let searchText = event.query

//       service
//         .apiAutoCompleteSearch(searchText, 'scientificName')
//         .then((response) => {
//           const facets = response.facets.facet
//           if (facets) {
//             this.items = facets.buckets.map((a) => a.val)
//           }
//         })
//         .catch()
//         .finally(() => {})
//     },

//     apiSearch() {
//       let searchText = this.search
//       const fuzzySearch = !this.itemSelected
//       const searchMode = fuzzySearch ? 'contains' : 'equals'

//       service
//         .apiScientificnameSearch(searchText, searchMode, fuzzySearch, 0, 50)
//         .then((response) => {
//           const total = response.facets.count
//           const results = response.response

//           if (total > 0) {
//             const collectionsfacet = response.facets.dataResourceName.buckets
//             this.setCollections(collectionsfacet)
//           }

//           this.setResults(results)
//           this.setTotalRecords(total)
//           this.setScientificName(searchText)
//           this.setIsFuzzySearch(fuzzySearch)
//           this.setSearchMode(searchMode)
//           this.setSearchText(null)
//         })
//         .catch()
//         .finally(() => {
//           console.log('currentUrl....', currentUrl)
//           this.search = undefined
//           this.setIsUrlPush(true)
//           this.$router.push('/search')
//         })
//     },

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
// setCommentFacet(facets) {
//   const imageFacet = facets.image.buckets
//   if (imageFacet.length > 0) {
//     const imageCount = imageFacet[0].count
//     this.setImageCount(imageCount)
//   } else {
//     this.setImageCount(0)
//   }

//   const isTyypeFacet = facets.isType.buckets
//   if (isTyypeFacet.length > 0) {
//     const isTypeCount = isTyypeFacet[0].count
//     this.setIsTypeCount(isTypeCount)
//   } else {
//     this.setIsTypeCount(0)
//   }

//   const inSwedenFacet = facets.inSweden.buckets
//   if (inSwedenFacet.length > 0) {
//     const inSwedenCount = inSwedenFacet[0].count
//     this.setInSwedenCount(inSwedenCount)
//   } else {
//     this.setInSwedenCount(0)
//   }

//   const mapFacet = facets.map.buckets
//   if (mapFacet.length > 0) {
//     const mapCount = mapFacet[0].count
//     this.setHasCoordinatesCount(mapCount)
//   } else {
//     this.setHasCoordinatesCount(0)
//   }
// },

// sbdi
//     sbdiAutoComplete(event) {
//       service
//         .autoComplete(searchText, 0, 10)
//         .then((response) => {
//           this.items = response.occurrences
//             .filter((item) =>
//               item.scientificName.toLowerCase().startsWith(searchText.toLowerCase())
//             )
//             .map((a) => a.scientificName)
//             .filter((value, index, self) => self.indexOf(value) === index)
//         })
//         .catch()
//         .finally(() => {})
//     },

//     completeSearch() {
//       this.loading = true
//       const searchText = this.itemSelected ? this.search : this.search + '*'
//       service
//         .quickSearch(searchText, 1, 10)
//         .then((response) => {
//           const total = response.totalRecords
//           this.results = response.occurrences

//           if (total > 0) {
//             const facetResults = response.facetResults

//             const typeStatusFacet = facetResults.find((facet) => facet.fieldName === 'typeStatus')
//             const typeStatus = typeStatusFacet.fieldResult
//             this.setTypeStatus(typeStatus)

//             const collectionFacet = facetResults.find(
//               (facet) => facet.fieldName === 'collectionName'
//             )
//             const collections = collectionFacet.fieldResult
//             this.setCollections(collections)

//             const pointFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
//             const point = pointFacet.fieldResult
//             this.setLatLong(point)

//             console.log('point length:', point.length)
//           } else {
//             this.setCollections([])
//             this.setTypeStatus([])
//           }
//           this.setResults(this.results)
//           this.setSearchText(searchText)
//           this.setSelectedCollection(null)
//           this.setShowDetail(false)
//           this.setShowResults(true)
//           this.setTotalRecords(total)

//           setTimeout(() => {
//             this.loading = false
//           }, 2000)
//         })
//         .catch()
//         .finally(() => {})
//     }
//   }
// }
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
