<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.locality') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.locality') }}</div>
      <div class="col-8 reducePadding">
        {{ localityName }}
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
const latitude = ref()
const longigude = ref()
const localityName = ref()
const minDepthInMeters = ref()
const maxDepthInMeters = ref()

const state = ref()
const water = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    country,
    decimalLongitude,
    decimalLatitude,
    locality,
    maximumDepthInMeters,
    minimumDepthInMeters,
    stateProvince,
    waterBody
  } = record

  latitude.value = decimalLatitude
  longigude.value = decimalLongitude
  localityName.value = locality

  minDepthInMeters.value = minimumDepthInMeters
  maxDepthInMeters.value = maximumDepthInMeters

  state.value = stateProvince
  theCountry.value = country
  water.value = waterBody
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
