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

      <div class="col-4 reducePadding">{{ $t('results.county') }}</div>
      <div class="col-8 reducePadding">
        {{ theCounty }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latitude') }}</div>
      <div class="col-8 reducePadding">
        {{ latitude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.longitude') }}</div>
      <div class="col-8 reducePadding">
        {{ longigude }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.waterBody') }}</div>
      <div class="col-8 reducePadding">
        {{ water }}
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

const latitude = ref()
const longigude = ref()
const localityName = ref()

const lithostratigraphy = ref()

const periodMin = ref()
const periodMax = ref()
const epochMax = ref()
const epochMin = ref()
const stageMax = ref()
const stageMin = ref()

const state = ref()
const water = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    continent,
    country,
    county,
    decimalLongitude,
    decimalLatitude,

    earliestPeriodOrLowestSystem,
    latestPeriodOrHighestSystem,
    latestEpochOrHighestSeries,
    earliestEpochOrLowestSeries,
    latestAgeOrHighestStage,
    earliestAgeOrLowestStage,

    lithostratigraphicTerms,

    locality,
    stateProvince,
    waterBody
  } = record

  theCountry.value = country
  theCounty.value = county
  state.value = stateProvince
  theContinent.value = continent

  if (decimalLatitude) {
    latitude.value = decimalLatitude.toFixed(5)
  }

  if (decimalLongitude) {
    longigude.value = decimalLongitude.toFixed(5)
  }

  localityName.value = locality

  lithostratigraphy.value = lithostratigraphicTerms

  periodMin.value = earliestPeriodOrLowestSystem
  periodMax.value = latestPeriodOrHighestSystem

  epochMin.value = earliestEpochOrLowestSeries
  epochMax.value = latestEpochOrHighestSeries

  stageMin.value = earliestAgeOrLowestStage
  stageMax.value = latestAgeOrHighestStage

  water.value = waterBody
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
