<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.locality') }}</p>
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

      <div class="col-4 reducePadding">{{ $t('results.municipality') }}</div>
      <div class="col-8 reducePadding">
        {{ municipalityData }}
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

      <div class="col-4 reducePadding">{{ $t('results.minimumDepthInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ minDepthInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.maximumDepthInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ maxDepthInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.waterBody') }}</div>
      <div class="col-8 reducePadding">
        {{ water }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.island') }}</div>
      <div class="col-8 reducePadding">
        {{ theIsland }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.islandGroup') }}</div>
      <div class="col-8 reducePadding">
        {{ theIslandGroup }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.georeferencedDate') }}</div>
      <div class="col-8 reducePadding">
        {{ georeferencedDateData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.locationRemarks') }}</div>
      <div class="col-8 reducePadding">
        {{ remarks }}
      </div>
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
const georeferencedDateData = ref()
const highGeo = ref()
const latitude = ref()
const longigude = ref()
const localityName = ref()
const minElevationInMeters = ref()
const maxElevationInMeters = ref()

const minDepthInMeters = ref()
const maxDepthInMeters = ref()

const municipalityData = ref()

const remarks = ref()
const state = ref()
const theIsland = ref()
const theIslandGroup = ref()
const water = ref()
const uncertaintyInMeters = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    continent,
    country,
    county,
    coordinateUncertaintyInMeters,

    decimalLongitude,
    decimalLatitude,

    geodeticDatum,
    georeferencedDate,

    island,
    locality,
    locationRemarks,

    minimumElevationInMeters,
    maximumElevationInMeters,
    minimumDepthInMeters,
    maximumDepthInMeters,

    municipality,

    stateProvince,

    higherGeography,

    islandGroup,

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

  georeferencedDateData.value = georeferencedDate

  minElevationInMeters.value = minimumElevationInMeters
  maxElevationInMeters.value = maximumElevationInMeters

  minDepthInMeters.value = minimumDepthInMeters
  maxDepthInMeters.value = maximumDepthInMeters

  municipalityData.value = municipality

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
