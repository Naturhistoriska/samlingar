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
          {{ collectionName }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.scientificName') }}:</div>
        <div class="col-9">{{ scientificName }} {{ author }}</div>
        <div class="col-3" no-gutters>{{ $t('results.vernacularName') }}:</div>
        <div class="col-9">
          {{ vernacularName }}
        </div>

        <div class="col-3" no-gutters>{{ $t('results.highClassification') }}:</div>
        <div class="col-9">
          {{ higherClassification }}
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
        <div class="col-3" no-gutters>{{ $t('results.continet') }}:</div>
        <div class="col-9">
          {{ continent }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.contury') }}:</div>
        <div class="col-9">
          {{ country }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.state') }}:</div>
        <div class="col-9">
          {{ stateProvince }}
        </div>
        <div class="col-3" no-gutters>county:</div>
        <div class="col-9">
          {{ county }}
        </div>

        <div class="col-3" no-gutters>{{ $t('results.latitude') }}:</div>
        <div class="col-9">
          {{ latitude }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.longitude') }}:</div>
        <div class="col-9">
          {{ longitude }}
        </div>
        <div class="col-3" no-gutters>Synonym:</div>
        <div class="col-9">
          {{ synonymAuthor }}
        </div>
        <div class="col-3" no-gutters>Determiner:</div>
        <div class="col-9">
          {{ determiner }}
        </div>
        <div class="col-3" no-gutters>Collectors:</div>
        <div class="col-9">
          {{ collector }}
        </div>
        <div class="col-3" no-gutters>{{ $t('results.eventDate') }}:</div>
        <div class="col-9">
          {{ eventDate }}
        </div>
        <div class="col-3" no-gutters>Cataloged Date:</div>
        <div class="col-9">
          {{ catalogedDate }}
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
    author: null,
    catalogedDate: null,
    catalogNumber: null,
    collectionName: null,
    collector: null,
    continent: null,
    country: null,
    county: null,
    determination: null,
    determiner: null,
    district: null,
    higherClassification: null,
    latitude: null,
    locality: null,
    longitude: null,
    occurrenceRemark: null,
    preparations: null,
    serie: null,
    eventDate: null,
    stateProvince: null,
    scientificName: null,
    synonymAuthor: null,
    typeStatus: null,
    vernacularName: null
  }),

  mounted() {
    const {
      author,
      catalogedDate,
      catalogNumber,
      collectionName,
      collector,
      commonName,
      continent,
      country,
      county,
      determination,
      determiner,
      district,
      higherTx,
      latitudeText,
      longitudeText,
      locality,
      prepration,
      remarks,
      serie,
      startDate,
      state,
      synonymAuthor,
      txFullName,
      typeStatus
    } = this.selectedResult

    this.author = author
    this.catalogedDate = catalogedDate
    this.catalogNumber = catalogNumber
    this.collectionName = collectionName
    this.collector = collector
    this.continent = continent
    this.country = country
    this.county = county
    this.determination = determination
    this.determiner = determiner
    this.district = district
    this.eventDate = startDate
    this.higherClassification = higherTx
    this.latitude = latitudeText
    this.locality = locality
    this.longitude = longitudeText
    this.occurrenceRemark = remarks
    this.preparations = prepration
    this.scientificName = txFullName
    this.serie = serie
    this.stateProvince = state
    this.synonymAuthor = synonymAuthor
    this.typeStatus = typeStatus
    this.vernacularName = commonName

    // const { attribution, classification, location, event, occurrence } =
    // this.selectedResult.processed
    // const { raw } = this.selectedResult

    // this.catalogNumber = raw.occurrence.catalogNumber
    // this.preparations = raw.occurrence.preparations
    // this.higherClassification = raw.classification.higherClassification
    // this.typeStatus = raw.identification.typeStatus
    // this.occurrenceRemark = raw.occurrence.occurrenceRemarks

    // this.collection = attribution.collectionName
    // this.scientificName = classification.scientificName
    // this.vernacularName = classification.vernacularName
    // this.taxonRank = classification.taxonRank
    // this.locality = location.locality

    // this.decimalLongitude = location.decimalLongitude
    // this.decimalLatitude = location.decimalLatitude
    // this.continent = location.continent
    // this.country = location.country
    // this.stateProvince = location.stateProvince

    // this.recordedBy = occurrence.recordedBy
    // this.year = event.year
    // this.eventDate = event.eventDate
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
