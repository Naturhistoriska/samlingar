<template>
  <div class="justify-right" style="float: right">
    <FloatLabel>
      <InputGroup>
        <AutoComplete
          id="autosearch"
          v-model="search"
          :suggestions="items"
          :minLength="3"
          @complete="acSearch"
          @keydown.enter="onPressEnter"
          @change="onChange"
          @itemSelect="onItemSelect"
          inputId="ac"
          :inputStyle="acwidth"
        />
        <Button icon="pi pi-search" :loading="loading" @click="completeSearch" text />
        <label for="ac">{{ $t('search.freeTextSearch') }} </label>
      </InputGroup>
    </FloatLabel>
  </div>
</template>
<script>
import Service from '../Service'
const service = new Service()
import { mapMutations } from 'vuex'

export default {
  name: 'QuickSearch',
  components: {},
  data: () => ({
    acwidth: {},
    loading: false,
    items: [],
    itemSelected: false,
    results: [],
    search: ''
  }),

  mounted() {
    this.acwidth = { width: '300px' }
  },

  methods: {
    ...mapMutations([
      'setCollections',
      'setLatLong',
      'setOccurrenceYears',
      'setResults',
      'setSearchText',
      'setShowResults',
      'setShowDetail',
      'setSelectedCollection',
      'setTotalRecords',
      'setTypeStatus'
    ]),

    onChange() {
      this.itemSelected = false
    },
    onItemSelect() {
      console.log('onItemSelect')
      this.itemSelected = true
    },
    onPressEnter() {
      this.completeSearch()
    },

    acSearch(event) {
      console.log(this.search)
      const searchText = event.query
      service
        .autoComplete(searchText, 1, 10)
        .then((response) => {
          this.items = response.occurrences
            .filter((item) =>
              item.scientificName.toLowerCase().startsWith(searchText.toLowerCase())
            )
            .map((a) => a.scientificName)
            .filter((value, index, self) => self.indexOf(value) === index)
        })
        .catch()
        .finally(() => {})
    },

    completeSearch() {
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

            const collectionFacet = facetResults.find(
              (facet) => facet.fieldName === 'collectionName'
            )
            const collections = collectionFacet.fieldResult
            this.setCollections(collections)

            const pointFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
            const point = pointFacet.fieldResult
            this.setLatLong(point)

            console.log('point length:', point.length)
          } else {
            store.commit('setCollections', [])
            store.commit('setLatLong', [])
            store.commit('setTypeStatus', [])
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
  }
}
</script>
<style scoped></style>
