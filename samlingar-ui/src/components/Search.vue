<template>
  <v-text-field
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
    ...mapMutations(['setResults']),
    onSearch() {
      service
        .fetchSeachResult(this.search)
        .then((response) => {
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
.search-input {
  padding-top: 40px;
  padding-right: 40px;
}
</style>
