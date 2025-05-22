<template>
  <div class="grid">
    <div class="grid" style="min-width: 100%">
      <div class="col-12 w-full" no-gutters style="padding-top: 50px">
        <p class="m-0" style="font-weight: bold; font-size: 18px">
          {{ $t('startPage.commonSearch') }}
        </p>
      </div>
    </div>

    <filt-link
      v-bind:text="allSpecimensText"
      v-bind:total="totalCount"
      v-bind:loading="loading"
      @doSearch="handleFreeTextSearch"
    />

    <filt-link
      v-bind:text="coordinatesText"
      v-bind:total="coordinatesCount"
      v-bind:loading="filterSearchLoading"
      @doSearch="handleCoordinatesSearch"
    />

    <filt-link
      v-bind:text="inSwedenText"
      v-bind:total="inSwedenCount"
      v-bind:loading="filterSearchLoading"
      @doSearch="handleInSwedenSearch"
    />

    <filt-link
      v-bind:text="hasImageText"
      v-bind:total="imageCount"
      v-bind:loading="filterSearchLoading"
      @doSearch="handleHasImageSearch"
    />

    <filt-link
      v-bind:text="isTypeText"
      v-bind:total="isTypeCount"
      v-bind:loading="filterSearchLoading"
      @doSearch="handleIsTypeSearch"
    />
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'
import FiltLink from './baseComponents/FiltLink.vue'

const { t } = useI18n()

const emits = defineEmits([
  'freeTextSearch',
  'filterWithCoordinates',
  'filterWithImages',
  'filterWithInSweden',
  'filterWithInType'
])

const store = useStore()

const props = defineProps(['loading', 'filterSearchLoading'])

const allSpecimensText = ref('startPage.allSpecimens')
const coordinatesText = ref('startPage.specimensWithCoordinates')
const inSwedenText = ref('startPage.specimensFromSweden')
const hasImageText = ref('startPage.specimensWithImages')
const isTypeText = ref('startPage.specimensWithType')

const coordinatesCount = computed(() => {
  return store.getters['hasCoordinatesCount']
})

const imageCount = computed(() => {
  return store.getters['imageCount']
})

const isTypeCount = computed(() => {
  return store.getters['isTypeCount']
})

const totalCount = computed(() => {
  return store.getters['totalCount']
})

const inSwedenCount = computed(() => {
  return store.getters['inSwedenCount']
})

function handleFreeTextSearch() {
  const searchText = '*'
  store.commit('setSearchText', searchText)
  emits('freeTextSearch', searchText)
}

function handleCoordinatesSearch() {
  emits('filterWithCoordinates')
}

function handleInSwedenSearch() {
  emits('filterWithInSweden')
}

function handleHasImageSearch() {
  emits('filterWithImages')
}

function handleIsTypeSearch() {
  emits('filterWithInType')
}
</script>
<style scoped>
.p-button-text:hover {
  color: var(--p-emerald-500) !important;
  /* color: #9dabf3 !important; */
  text-decoration: none !important;
  background: transparent !important;
}

.p-button-text {
  /* color: #fff !important; */
  text-decoration: underline;
}
.divLink {
  min-width: 100% !important;
  cursor: pointer !important;
}
.divLink:hover {
  background: #0e3a12;
  color: var(--p-emerald-500) !important;
}
</style>
