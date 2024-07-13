<template>
  <v-container>
    <Search />
    <legend>{{ $t('results.searchResults') }}</legend>
    <div id="resultList">
      <p>{{ $t('results.num_results', totalRecords) }}</p>
      <template v-for="(result, index) in results" :key="result.uuid">
        <Result v-bind:result="result" />
        <v-divider v-if="index + 1 < results.length" :key="`devider-${index}`"></v-divider>
      </template>
    </div>
  </v-container>
</template>
<script>
import Result from '@/components/Result.vue'
import Search from '@/components/Search.vue'

import { mapGetters } from 'vuex'

export default {
  name: 'Results',
  components: {
    Result,
    Search
  },
  data: () => ({
    // results: [],
    search: ''
  }),

  computed: {
    ...mapGetters(['results', 'totalRecords'])
  },

  methods: {}
}
</script>
