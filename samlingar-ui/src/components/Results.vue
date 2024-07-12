<template>
  <v-container>
    <Search />
    <legend>{{ $t('results.searchResults') }}</legend>

    <div id="scroll-target" class="overflow-y-auto">
      <div id="resultList">
        <p>{{ $t('results.num_results', totalRecords) }}</p>
        <template v-for="(result, index) in results" :key="`devider-${index}`">
          {{ result.institutionName }}
          {{ result.collectionName }} {{ result.raw_catalogNumber }}
          {{ result.scientificName }}
          <v-divider></v-divider>
        </template>
      </div>
    </div>
  </v-container>
</template>
<script>
import Search from '@/components/Search.vue'

import { mapGetters } from 'vuex'

export default {
  name: 'Results',
  components: {
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
