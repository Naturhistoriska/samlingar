<template>
  <div class="text-center">
    <v-container class="paginationWidth">
      <v-pagination
        v-model="page"
        :length="length"
        first-icon="mdi-menu-right"
        last-icon="mdi-menu-left"
        next-icon="mdi-menu-right"
        prev-icon="mdi-menu-left"
        rounded="circle"
        :total-visible="8"
      ></v-pagination>
    </v-container>
  </div>
</template>
<script>
import Service from '../Service'
const service = new Service()

import { mapGetters, mapMutations } from 'vuex'

export default {
  name: 'Pagination',
  data: () => ({
    page: 1
  }),
  watch: {
    page: function (newPage) {
      this.page = newPage
      this.onSearch()
    }
  },
  computed: {
    ...mapGetters([
      'catalogNumber',
      'isAdvanceSearch',
      'scientificName',
      'searchText',
      'speciesGrouup',
      'totalRecords'
    ]),
    length() {
      return Math.ceil(this.totalRecords / 10)
      // let count = Math.ceil(this.totalRecords / 10)
      // return count > 10 ? 10 : count
    }
  },
  methods: {
    ...mapMutations(['setResults']),
    onSearch() {
      const startNumber = this.page * 10 - 9
      if (this.isAdvanceSearch) {
        this.advanceSearch(startNumber)
      } else {
        this.simpleSearch(startNumber)
      }
    },

    simpleSearch(startNumber) {
      service
        .fetchSeachResult(this.searchText, startNumber)
        .then((response) => {
          this.results = response.occurrences
          this.setResults(this.results)
        })
        .catch()
        .finally(() => {})
    },
    advanceSearch(startNumber) {
      service
        .advanceSearch(this.scientificName, this.speciesGrouup, this.catalogNumber, startNumber)
        .then((response) => {
          this.totalResults = response.totalRecords
          this.results = response.occurrences

          this.setResults(this.results)
        })
        .catch()
        .finally(() => {})
    }
  }
}
</script>
<style scoped>
.paginationWidth {
  max-width: 520px;
}
</style>
