<template>
  <v-text-field
    class="search-input"
    clearable
    single-line
    append-inner-icon="mdi-magnify"
    density="compact"
    v-model="search"
    :label="$t('search.search')"
    type="input"
    @keyup.enter="onSearch"
    @click:append-inner="onSearch"
  ></v-text-field>
</template>
<script>
import Service from '../Service'
const service = new Service()

import { mapMutations } from 'vuex'

export default {
  name: 'Results',

  data: () => ({
    results: [],
    search: ''
  }),
  mounted() {},
  methods: {
    ...mapMutations([
      'setResults',
      'setSearchText',
      'setShowResults',
      'setShowDetail',
      'setTotalRecords'
    ]),
    onSearch() {
      service
        .fetchSeachResult(this.search, 1)
        .then((response) => {
          this.totalResults = response.totalRecords
          this.results = response.occurrences

          this.setShowResults(true)
          this.setResults(this.results)
          this.setTotalRecords(this.totalResults)
          this.setShowDetail(false)
          this.setSearchText(this.search)
        })
        .catch()
        .finally(() => {})
    }
  }
}
</script>

<style scoped>
.search-input {
  max-width: 500px;
}
</style>
