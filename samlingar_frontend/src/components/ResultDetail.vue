<template>
  <Card>
    <template #title>Dataset</template>
    <template #content>
      <div class="grid">
        <div class="col-2" no-gutters>Catalognumber</div>
        <div class="col-10">
          {{ catalogNumber }}
        </div>
        <div class="col-2" no-gutters>Scientific Name:</div>
        <div class="col-10">
          {{ scientificName }}
        </div>
        <div class="col-2" no-gutters>Vernacular Name:</div>
        <div class="col-10">
          {{ vernacularName }}
        </div>
        <div class="col-2" no-gutters>Identified to rank:</div>
        <div class="col-10">
          {{ taxonRank }}
        </div>

        <div class="col-2" no-gutters>High Classification:</div>
        <div class="col-10">
          {{ higherClassification }}
        </div>
        <!-- <div class="col-2" no-gutters>Scientific name authorship:</div>
        <div class="col-10">
          {{ scientificNameAuthorship }}
        </div> -->
        <div class="col-2" no-gutters>Recorded by:</div>
        <div class="col-10">
          {{ recordedBy }}
        </div>

        <div class="col-2" no-gutters>Preparations:</div>
        <div class="col-10">
          {{ preparations }}
        </div>
        <div class="col-2" no-gutters>Type status:</div>
        <div class="col-10">
          {{ typeStatus }}
        </div>
        <div class="col-2" no-gutters>Locality:</div>
        <div class="col-10">
          {{ locality }}
        </div>

        <div class="col-2" no-gutters>Latitude:</div>
        <div class="col-10">
          {{ decimalLatitude }}
        </div>
        <div class="col-2" no-gutters>Longitude:</div>
        <div class="col-10">
          {{ decimalLongitude }}
        </div>

        <div class="col-2" no-gutters>Continent:</div>
        <div class="col-10">
          {{ continent }}
        </div>
        <div class="col-2" no-gutters>State or Territory:</div>
        <div class="col-10">
          {{ stateProvince }}
        </div>
        <div class="col-2" no-gutters>Contury</div>
        <div class="col-10">
          {{ country }}
        </div>
        <div class="col-2" no-gutters>Event date</div>
        <div class="col-10">
          {{ eventDate }}
        </div>
      </div>
    </template>
  </Card>
  <Button link @click.prevent="onclick()"> Back to result list </Button>
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
    eventDate: null,
    higherClassification: null,
    // kingdom: null,
    typeStatus: null,
    scientificName: null,
    // scientificNameAuthorship: null,
    taxonRank: null,
    locality: null,
    continent: null,
    country: null,
    stateProvince: null,
    recordedBy: null,
    vernacularName: null,
    year: null
  }),

  mounted() {
    const { classification, location, event, occurrence } = this.selectedResult.processed
    const { raw } = this.selectedResult

    this.catalogNumber = raw.occurrence.catalogNumber
    this.preparations = raw.occurrence.preparations
    this.higherClassification = raw.classification.higherClassification
    this.typeStatus = raw.identification.typeStatus

    this.scientificName = classification.scientificName
    this.vernacularName = classification.vernacularName
    this.taxonRank = classification.taxonRank
    this.locality = location.locality

    this.decimalLongitude = location.decimalLongitude
    this.decimalLatitude = location.decimalLatitude
    this.continent = location.continent
    this.country = location.country
    this.stateProvince = location.stateProvince

    this.recordedBy = occurrence.recordedBy
    this.year = event.year
    this.eventDate = event.eventDate
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
