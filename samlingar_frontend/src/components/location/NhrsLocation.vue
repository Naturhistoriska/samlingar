<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.locality') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.locality') }}</div>
      <div class="col-8 reducePadding">
        {{ localityName }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.continet') }}</div>
      <div class="col-8 reducePadding">
        {{ theContinent }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.country') }}</div>
      <div class="col-8 reducePadding">
        {{ theCountry }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.stateProvince') }}</div>
      <div class="col-8 reducePadding">
        {{ state }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.county') }}</div>
      <div class="col-8 reducePadding">
        {{ theCounty }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.higherGeography') }}</div>
      <div class="col-8 reducePadding">
        {{ highGeo }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latitude') }}</div>
      <div class="col-8 reducePadding">
        {{ latitude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.longitude') }}</div>
      <div class="col-8 reducePadding">
        {{ longigude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.coordinateUncertaintyInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ uncertaintyInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.geodeticDatum') }}</div>
      <div class="col-8 reducePadding">
        {{ datum }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.minElevationInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ minElevationInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.maxElevationInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ maxElevationInMeters }}
      </div>

      <div class="col-4 reducePadding" v-if="!isGeoData">{{ $t('results.waterBody') }}</div>
      <div class="col-8 reducePadding" v-if="!isGeoData">
        {{ water }}
      </div>

      <div class="col-4 reducePadding" v-if="!isGeoData">{{ $t('results.island') }}</div>
      <div class="col-8 reducePadding" v-if="!isGeoData">
        {{ theIsland }}
      </div>

      <div class="col-4 reducePadding" v-if="!isGeoData">{{ $t('results.islandGroup') }}</div>
      <div class="col-8 reducePadding" v-if="!isGeoData">
        {{ theIslandGroup }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.locationRemarks') }}</div>
      <div class="col-8 reducePadding">
        {{ remarks }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const theCountry = ref()
const theCounty = ref()
const theContinent = ref()
const datum = ref()
const highGeo = ref()
const latitude = ref()
const longigude = ref()
const localityName = ref()
const minElevationInMeters = ref()
const maxElevationInMeters = ref()

const remarks = ref()
const state = ref()
const theIsland = ref()
const theIslandGroup = ref()
const water = ref()
const uncertaintyInMeters = ref()

const isGeoData = ref(false)

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    collectionCode,
    continent,
    country,
    county,
    coordinateUncertaintyInMeters,
    decimalLongitude,
    decimalLatitude,
    geodeticDatum,
    higherGeography,
    island,
    islandGroup,
    locality,
    locationRemarks,
    minimumElevationInMeters,
    maximumElevationInMeters,
    stateProvince,
    waterBody
  } = record

  theCountry.value = country
  theCounty.value = county
  state.value = stateProvince
  theContinent.value = continent

  highGeo.value = higherGeography
  datum.value = geodeticDatum
  theIsland.value = island
  theIslandGroup.value = islandGroup

  if (decimalLatitude) {
    latitude.value = decimalLatitude.toFixed(5)
  }
  if (decimalLongitude) {
    longigude.value = decimalLongitude.toFixed(5)
  }

  localityName.value = locality

  minElevationInMeters.value = minimumElevationInMeters
  maxElevationInMeters.value = maximumElevationInMeters

  remarks.value = locationRemarks
  water.value = waterBody
  uncertaintyInMeters.value = coordinateUncertaintyInMeters

  isGeoData.value = collectionCode === 'NRMLIG' || 'NRMMIN' || 'NRMNOD'
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
