<template>
  <v-container>
    <Search />
    <legend>{{ $t('results.searchResults') }}</legend>

    <div id="scroll-target" class="overflow-y-auto">
      <div id="resultList">
        <p>{{ $t('results.num_results', results.length) }}</p>
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
import Service from '../Service'
const service = new Service()

// import {computed} from 'vue';
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
    ...mapGetters([
      'results'
      // ...
    ])
  },

  methods: {
    onSearch() {
      service
        .fetchSeachResult(this.search)
        .then((response) => {
          this.results = response.occurrences
        })
        .catch()
        .finally(() => {})
      // axios
      //   .get(
      //     'https://records.biodiversitydata.se/ws/occurrences/search?q=taxa:%22+`${this.search}`+%22%20AND%20institution_uid:in0'
      //   )
      //   .then((response) => {
      //     this.results = response.data.occurrences
      //     console.log('whgat,....' + this.results)
      //   })
    },
    onClear() {
      alert('User cleared the input')
    }
  }
}
</script>
