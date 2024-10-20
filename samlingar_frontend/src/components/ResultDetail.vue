<template>
  <Card style="background-color: transparent">
    <template #header>
      <Button link @click.prevent="onclick()">
        <small>{{ $t('results.backToResultListView') }}</small>
      </Button>
    </template>
    <template #title>Dataset </template>
    <template #content>
      <div class="grid">
        <div class="col-3" no-gutters>{{ $t('results.catalogNumber') }}:</div>
        <div class="col-9">
          {{ catalogNumber }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.collectionName') }}:</div>
        <div class="col-9">
          {{ collection }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.scientificName') }}:</div>
        <div class="col-9">
          {{ scientificName }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.vernacularName') }}:</div>
        <div class="col-9">
          {{ vernacularName }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.rank') }}:</div>
        <div class="col-9">
          {{ taxonRank }}
        </div>

        <div class="col-3" no-gutters>{{ $t('results.highClassification') }}:</div>
        <div class="col-9">
          {{ higherClassification }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.recordedBy') }}:</div>
        <div class="col-9">
          {{ recordedBy }}
        </div>

        <div class="col-3" no-gutters>{{ $t('results.preparation') }}:</div>
        <div class="col-9">
          {{ preparations }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.typeStatus') }}:</div>
        <div class="col-9">
          {{ typeStatus }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.locality') }}:</div>
        <div class="col-9">
          {{ locality }}
        </div>

        <div class="col-3" no-gutters>{{ $t('results.latitude') }}:</div>
        <div class="col-9">
          {{ decimalLatitude }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.longitude') }}:</div>
        <div class="col-9">
          {{ decimalLongitude }}
        </div>

        <div class="col-3" no-gutters>{{ $t('results.continet') }}:</div>
        <div class="col-9">
          {{ continent }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.state') }}:</div>
        <div class="col-9">
          {{ stateProvince }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.contury') }}:</div>
        <div class="col-9">
          {{ country }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.eventDate') }}:</div>
        <div class="col-9">
          {{ eventDate }}
        </div>
        <div class="col-3" no-gutters>Occurrence Remark:</div>
        <div class="col-9">
          {{ occurrenceRemark }}
        </div>
      </div>
    </template>
  </Card>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'

export default {
  name: 'ResultDetail',
  components: {},
  data: () => ({
    catalogNumber: null,
    collection: null,
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
    occurrenceRemark: null,
    year: null
  }),

  mounted() {
    const { attribution, classification, location, event, occurrence } =
      this.selectedResult.processed
    const { raw } = this.selectedResult

    this.catalogNumber = raw.occurrence.catalogNumber
    this.preparations = raw.occurrence.preparations
    this.higherClassification = raw.classification.higherClassification
    this.typeStatus = raw.identification.typeStatus
    this.occurrenceRemark = raw.occurrence.occurrenceRemarks

    this.collection = attribution.collectionName
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
