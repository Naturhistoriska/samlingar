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

      <div class="col-4 reducePadding">{{ $t('results.district') }}</div>
      <div class="col-8 reducePadding">
        {{ theDistrict }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latitude') }}</div>
      <div class="col-8 reducePadding">
        {{ latitude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.longitude') }}</div>
      <div class="col-8 reducePadding">
        {{ longigude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.verbatimCoordinates') }}</div>
      <div class="col-8 reducePadding">
        {{ coordinates }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const theCountry = ref()
const theDistrict = ref()
const theContinent = ref()

const latitude = ref()
const longigude = ref()
const localityName = ref()

const state = ref()
const coordinates = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    continent,
    country,
    district,
    locality,
    decimalLongitude,
    decimalLatitude,
    stateProvince,
    verbatimCoordinates
  } = record

  theCountry.value = country
  state.value = stateProvince
  theContinent.value = continent
  theDistrict.value = district

  if (decimalLatitude) {
    latitude.value = decimalLatitude.toFixed(5)
  }
  if (decimalLongitude) {
    longigude.value = decimalLongitude.toFixed(5)
  }

  localityName.value = locality
  coordinates.value = verbatimCoordinates
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
