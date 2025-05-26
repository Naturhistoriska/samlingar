<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.locality') }}</p>
    <div class="grid">
      <div class="col-3 reducePadding">{{ $t('results.scientificName') }}</div>
      <div class="col-9 reducePadding">
        {{ localityName }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.continet') }}</div>
      <div class="col-9 reducePadding">
        {{ theContinent }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.country') }}</div>
      <div class="col-9 reducePadding">
        {{ theCountry }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.stateProvince') }}</div>
      <div class="col-9 reducePadding">
        {{ state }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.county') }}</div>
      <div class="col-9 reducePadding">
        {{ theCounty }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.higherGeography') }}</div>
      <div class="col-9 reducePadding">
        {{ highGeo }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.latitude') }}</div>
      <div class="col-9 reducePadding">
        {{ latitude }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.longitude') }}</div>
      <div class="col-9 reducePadding">
        {{ longigude }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.coordinateUncertaintyInMeters') }}</div>
      <div class="col-9 reducePadding">
        {{ uncertaintyInMeters }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.geodeticDatum') }}</div>
      <div class="col-9 reducePadding">
        {{ datum }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.minElevationInMeters') }}</div>
      <div class="col-9 reducePadding">
        {{ minElevationInMeters }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.maxElevationInMeters') }}</div>
      <div class="col-9 reducePadding">
        {{ maxElevationInMeters }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.waterBody') }}</div>
      <div class="col-9 reducePadding">
        {{ water }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.island') }}</div>
      <div class="col-9 reducePadding">
        {{ theIsland }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.islandGroup') }}</div>
      <div class="col-9 reducePadding">
        {{ theIslandGroup }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.locationRemarks') }}</div>
      <div class="col-9 reducePadding">
        {{ remarks }}
      </div>

      <!-- <div class="col-3 reducePadding">{{ $t('results.rank') }}</div>
      <div class="col-9 reducePadding">
        {{ rank }}
      </div>






      -->
    </div>
  </div>
</template>
<script setup>
import { geoJSON } from 'leaflet'
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

onMounted(async () => {
  console.log('onMounted dataset')
  const record = store.getters['selectedRecord']

  const {
    coordinateUncertaintyInMeters,
    continent,
    country,
    county,
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
  latitude.value = decimalLatitude
  longigude.value = decimalLongitude
  localityName.value = locality
  minElevationInMeters.value = minimumElevationInMeters
  maxElevationInMeters.value = maximumElevationInMeters
  remarks.value = locationRemarks
  water.value = waterBody
  uncertaintyInMeters.value = coordinateUncertaintyInMeters
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
