<template>
  <v-card class="mt-2 pl-10 card-sm" id="v-card-advance-search">
    <v-card-title>Dataset</v-card-title>
    <v-card-item>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> {{ $t('search.searchCatalogNumber') }}: </v-col>
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
        <v-col class="pl-3" cols="3"> Recorded by: </v-col>
        <v-col class="pl-3" cols="9">
          {{ recordedBy }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Preparations: </v-col>
        <v-col class="pl-3" cols="9">
          {{ preparations }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Type status: </v-col>
        <v-col class="pl-3" cols="9">
          {{ typeStatus }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Locality: </v-col>
        <v-col class="pl-3" cols="9">
          {{ locality }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Latitude: </v-col>
        <v-col class="pl-3" cols="9">
          {{ decimalLatitude }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Longitude: </v-col>
        <v-col class="pl-3" cols="9">
          {{ decimalLongitude }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> State or Territory: </v-col>
        <v-col class="pl-3" cols="9">
          {{ stateProvince }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Country: </v-col>
        <v-col class="pl-3" cols="9">
          {{ country }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="pl-3" cols="3"> Continent: </v-col>
        <v-col class="pl-3" cols="9">
          {{ continent }}
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
    preparations: null,
    decimalLatitude: null,
    decimalLongitude: null,
    higherClassification: null,
    kingdom: null,
    typeStatus: null,
    scientificName: null,
    scientificNameAuthorship: null,
    taxonRank: null,
    locality: null,
    continent: null,
    country: null,
    stateProvince: null,
    recordedBy: null,
    year: null

    // showDetail: false
  }),

  mounted() {
    const { classification, location, event, identification, occurrence } = this.selectedResult.raw
    const { processed } = this.selectedResult
    this.selectedResult.raw
    this.catalogNumber = occurrence.catalogNumber
    this.preparations = occurrence.preparations
    this.higherClassification = classification.higherClassification
    this.kingdom = classification.kingdom
    this.scientificName = classification.scientificName
    this.scientificNameAuthorship = classification.scientificNameAuthorship
    this.taxonRank = classification.taxonRank
    this.locality = location.locality
    this.decimalLongitude = location.decimalLongitude
    this.decimalLatitude = location.decimalLatitude
    this.continent = location.continent
    this.country = location.country
    this.stateProvince = location.stateProvince
    this.recordedBy = processed.occurrence.recordedBy
    this.year = event.year
    this.typeStatus = identification.typeStatus
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
