<template>
  <v-card class="mt-2 pl-10 card-sm" id="v-card-advance-search">
    <v-card-title>{{ $t('search.advanceSearch') }}</v-card-title>
    <v-card-item>
      <v-row no-gutters>
        <v-col class="pl-3" cols="2"> {{ $t('search.searchCollections') }}: </v-col>
        <v-col class="pl-3" cols="10">
          <v-select
            :items="supportedCollections"
            :label="$t('search.selectSpeciesGroup')"
            :clearable="true"
            density="compact"
            max-width="500"
            v-model="speciesGroup"
          />
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="2">{{ $t('search.providScientificName') }}: </v-col>
        <v-col class="pl-3" cols="10">
          <v-text-field
            class="search-input"
            clearable
            single-line
            density="compact"
            max-width="500"
            v-model="search"
            :label="$t('search.searchScientificName')"
            type="input"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="2">{{ $t('search.searchCatalogNumber') }}: </v-col>
        <v-col class="pl-3" cols="10">
          <v-text-field
            class="search-input"
            clearable
            single-line
            density="compact"
            max-width="500"
            v-model="catalogNumber"
            :label="$t('search.searchCatalogNumber')"
            type="input"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-card-item>

    <v-card-actions>
      <v-row no-gutters>
        <v-col class="pl-3" cols="6">
          <v-spacer></v-spacer>
        </v-col>
        <v-col class="pl-3" cols="2">
          <v-btn
            prepend-icon="mdi-magnify"
            density="compact"
            class="search-btn"
            @click="onSearch"
            >{{ $t('search.search') }}</v-btn
          >
        </v-col>
      </v-row>
    </v-card-actions>
  </v-card>
</template>

<script>
import Service from '../Service'
const service = new Service()

import { mapMutations } from 'vuex'
export default {
  name: 'AdvanceSearch',
  components: {},
  data: () => ({
    catalogNumber: null,
    results: [],
    search: null,
    speciesGroup: null
  }),
  computed: {
    supportedCollections() {
      return import.meta.env.VITE_SUPPORTED_COLLECTIONS.split(',')
    }
  },
  methods: {
    ...mapMutations(['setShowResults', 'setResults', 'setTotalRecords', 'setShowDetail']),

    onSearch() {
      const taxa = this.search === null ? '*' : this.search
      const group = this.speciesGroup === null ? '*' : this.speciesGroup
      const catalogNr = this.catalogNumber === null ? '*' : this.catalogNumber
      service
        .advanceSearch(taxa, group, catalogNr)
        .then((response) => {
          this.totalResults = response.totalRecords
          this.results = response.occurrences

          this.setShowResults(true)
          this.setResults(this.results)
          this.setTotalRecords(this.totalResults)
          this.setShowDetail(false)
        })
        .catch()
        .finally(() => {})
    }
  }
}
</script>
<style scoped>
.card-sm {
  width: 100%;
}
.search-btn {
  background-color: #144836 !important;
  color: #fff;
}
</style>
