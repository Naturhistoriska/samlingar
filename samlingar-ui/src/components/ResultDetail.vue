<template>
  <v-card class="mt-2 pl-10 card-sm" id="v-card-advance-search">
    <v-card-title>Dataset</v-card-title>
    <v-card-item>
      <v-row no-gutters>
        <v-col class="pl-" cols="3"> {{ $t('search.searchCatalogNumber') }}: </v-col>
        <v-col class="pl-3" cols="9">
          {{ catalogNumber }}
        </v-col>
      </v-row>

      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Scientific Name: </v-col>
        <v-col class="pl-3" cols="9">
          {{ scientificName }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Identified to rank: </v-col>
        <v-col class="pl-3" cols="9">
          {{ taxonRank }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> High Classification: </v-col>
        <v-col class="pl-3" cols="9">
          {{ higherClassification }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Scientific name authorship: </v-col>
        <v-col class="pl-3" cols="9">
          {{ scientificNameAuthorship }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Locality: </v-col>
        <v-col class="pl-3" cols="9">
          {{ locality }}
        </v-col>
      </v-row>
    </v-card-item>
  </v-card>
  <a
    href=""
    class="grey--text text--darken-3 body-2 pl-1"
    @click.prevent="onclick()"
    id="backResultListLink"
    >{{ $t('search.backToResultsList') }}</a
  >
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'

export default {
  name: 'ResultDetail',
  components: {},
  data: () => ({
    catalogNumber: null,
    higherClassification: null,
    kingdom: null,
    scientificName: null,
    scientificNameAuthorship: null,
    taxonRank: null,
    locality: null,
    year: null

    // showDetail: false
  }),

  mounted() {
    const { occurrence, classification, location, event } = this.selectedResult
    this.catalogNumber = occurrence.catalogNumber
    this.higherClassification = classification.higherClassification
    this.kingdom = classification.kingdom
    this.scientificName = classification.scientificName
    this.scientificNameAuthorship = classification.scientificNameAuthorship
    this.taxonRank = classification.taxonRank
    this.locality = location.locality
    this.year = event.year
  },
  computed: {
    ...mapGetters(['selectedResult'])
  },

  methods: {
    ...mapMutations(['setShowDetail']),
    onclick() {
      this.setShowDetail(false)
    }
  }
}
</script>
<style scoped></style>
