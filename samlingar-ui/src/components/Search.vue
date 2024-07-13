<template>
  <v-text-field
    class="search-input"
    clearable
    single-line
    hint="Free text search"
    append-inner-icon="mdi-magnify"
    density="compact"
    v-model="search"
    :label="$t('search')"
    type="input"
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
    ...mapMutations(['setResults', 'setTotalRecords']),
    onSearch() {
      service
        .fetchSeachResult(this.search)
        .then((response) => {
          this.totalResults = response.totalRecords
          this.results = response.occurrences
          this.setResults(this.results)
          this.setTotalRecords(this.totalResults)
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
