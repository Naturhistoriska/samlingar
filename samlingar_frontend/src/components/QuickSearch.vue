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
      'setGeoData',
      'setIsFuzzySearch',
      'setResults',
      'setSearchText',
      'setScientificName',
      'setTotalRecords'
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
      console.log('apiAutoComplete', event.query)
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
    },

    apiSearch() {
      let searchText = this.search
      const fuzzySearch = !this.itemSelected

      service
        .apiQuickSearch(searchText, fuzzySearch, 0, 50)
        .then((response) => {
          const total = response.response.numFound
          const results = response.response.docs

          console.log('total', total)

          if (total > 0) {
            const geofacet = response.facets.geo.buckets
            this.setGeoData(geofacet)
          }

          this.setResults(results)
          this.setTotalRecords(total)
          this.setScientificName(searchText)
          this.setIsFuzzySearch(fuzzySearch)
          this.setSearchText(null)
        })
        .catch()
        .finally(() => {
          this.search = undefined
          this.$router.push('/search')
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
