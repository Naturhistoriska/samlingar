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

      <div class="col-4 reducePadding">{{ $t('results.stateProvince') }}</div>
      <div class="col-8 reducePadding">
        {{ state }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.country') }}</div>
      <div class="col-8 reducePadding">
        {{ theCountry }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latitude') }}</div>
      <div class="col-8 reducePadding">
        {{ latitude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.longitude') }}</div>
      <div class="col-8 reducePadding">
        {{ longigude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.minimumDepthInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ minDepthInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.maximumDepthInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ maxDepthInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.minElevationInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ minElevationInMeters }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.maxElevationInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ maxElevationInMeters }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const theCountry = ref()
const theContinent = ref()
const latitude = ref()
const longigude = ref()
const localityName = ref()
const minDepthInMeters = ref()
const maxDepthInMeters = ref()
const maxElevationInMeters = ref()
const minElevationInMeters = ref()
const state = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    continent,
    country,
    decimalLongitude,
    decimalLatitude,
    locality,
    maximumDepthInMeters,
    minimumDepthInMeters,
    maximumElevationInMeters,
    minimumElevationInMeters,
    stateProvince
  } = record

  latitude.value = decimalLatitude
  longigude.value = decimalLongitude
  localityName.value = locality

  minDepthInMeters.value = minimumDepthInMeters
  maxDepthInMeters.value = maximumDepthInMeters
  minElevationInMeters.value = minimumElevationInMeters
  maxElevationInMeters.value = maximumElevationInMeters

  state.value = stateProvince
  theCountry.value = country
  theContinent.value = continent
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
