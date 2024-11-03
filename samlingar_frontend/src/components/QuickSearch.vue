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
      'setFamily',
      'setGenus',
      'setHasCoordinatesCount',
      'setImageCount',
      'setInSwedenCount',
      'setResetPaging',
      'setResults',
      'setSearchText',
      'setShowDetail',
      'setShowResults',
      'setTotalRecords',
      'setIsTypeCount',
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
      this.apiSearch()
    },

    apiAutoComplete(event) {
      let searchText = event.query
      if (!this.itemSelected) {
        searchText += '*'
      }

      searchText = searchText.replace(/^./, searchText[0].toUpperCase()) // Capitalize first letter
      console.log('searchText', searchText)

      service
        .apiAutoCompleteSearch(searchText, 0, 10)
        .then((response) => {
          const facets = response.facets.txFullName
          if (facets) {
            this.items = facets.buckets.map((a) => a.val)
          }
          // this.items = response.response.map((a) => a.txFullName)
        })
        .catch()
        .finally(() => {})
    },

    apiSearch() {
      let searchText = this.search

      searchText = searchText.replace(/^./, searchText[0].toUpperCase())
      if (this.itemSelected) {
        searchText = '%2BtxFullName:"' + searchText + '"'
      } else {
        searchText = '%2B(txFullName:' + searchText + '*' + ' txFullName:"' + searchText + '")'
      }

      // searchText = '%2B(txFullName:' + searchText + '*' + ' txFullName:"' + searchText + '")'

      console.log('what..2..', searchText)
      service
        .apiSimpleSearch(searchText, 0, 10)
        .then((response) => {
          const total = response.response.numFound
          const results = response.response.docs

          if (total > 0) {
            const facets = response.facets

            this.setCommentFacet(facets)

            const familyFacet = facets.family
            if (familyFacet !== undefined) {
              const family = familyFacet.buckets
              console.log('family length', family.length)
              family.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
              this.setFamily(family)
            } else {
              this.setFamily([])
            }
            const genusFacet = facets.genus
            if (genusFacet !== undefined) {
              const genus = genusFacet.buckets
              console.log('genus length', genus.length)
              genus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
              this.setGenus(genus)
            } else {
              this.setGenus([])
            }

            const collections = facets.collectionName.buckets
            this.setCollections(collections)

            const typeStatus = facets.typeStatus.buckets
            console.log('typeStatus length', typeStatus.length)

            this.setTypeStatus(typeStatus)
          }
          this.setResults(results)
          this.setTotalRecords(total)
          this.setSearchText(searchText)

          this.setShowDetail(false)
          this.setShowResults(true)
          this.setResetPaging(true)
        })
        .catch()
        .finally(() => {
          this.search = null
          this.$router.push('/results')
        })
    },

    setCommentFacet(facets) {
      const imageFacet = facets.image.buckets
      if (imageFacet.length > 0) {
        const imageCount = imageFacet[0].count
        this.setImageCount(imageCount)
      } else {
        this.setImageCount(0)
      }

      const isTyypeFacet = facets.isType.buckets
      if (isTyypeFacet.length > 0) {
        const isTypeCount = isTyypeFacet[0].count
        this.setIsTypeCount(isTypeCount)
      } else {
        this.setIsTypeCount(0)
      }

      const inSwedenFacet = facets.inSweden.buckets
      if (inSwedenFacet.length > 0) {
        const inSwedenCount = inSwedenFacet[0].count
        this.setInSwedenCount(inSwedenCount)
      } else {
        this.setInSwedenCount(0)
      }

      const mapFacet = facets.map.buckets
      if (mapFacet.length > 0) {
        const mapCount = mapFacet[0].count
        this.setHasCoordinatesCount(mapCount)
      } else {
        this.setHasCoordinatesCount(0)
      }
    },

    // sbdi
    sbdiAutoComplete(event) {
      service
        .autoComplete(searchText, 0, 10)
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
  }
}
</script>
<style scoped></style>
