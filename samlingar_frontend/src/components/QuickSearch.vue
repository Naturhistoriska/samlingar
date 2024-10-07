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
          :placeholder="$t('search.simpleSearch')"
        />
        <Button icon="pi pi-search" :loading="loading" @click="completeSearch" text />
        <label for="ac">{{ $t('search.simpleSearch') }} </label>
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
    collections: [],
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
      'setOccurrenceYears',
      'setResults',
      'setSearchText',
      'setShowResults',
      'setShowDetail',
      'setSelectedCollection',
      'setTotalRecords',
      'setYear'
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

          let facetResults = response.facetResults
          let collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
          this.collections = collectionFacet.fieldResult

          const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
          const occurrenceYears = yearFacet.fieldResult

          this.setCollections(this.collections)
          this.setOccurrenceYears(occurrenceYears)
          this.setResults(this.results)
          this.setSearchText(searchText)
          this.setSelectedCollection(null)
          this.setYear(null)
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
