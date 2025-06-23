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
      @doSearch="handleFreeTextSearch"
    />

    <filt-link
      v-bind:text="coordinatesText"
      v-bind:total="coordinatesCount"
      @doSearch="handleCoordinatesSearch"
    />

    <filt-link
      v-bind:text="inSwedenText"
      v-bind:total="inSwedenCount"
      @doSearch="handleInSwedenSearch"
    />

    <filt-link
      v-bind:text="hasImageText"
      v-bind:total="imageCount"
      @doSearch="handleHasImageSearch"
    />

    <filt-link
      v-bind:text="isTypeText"
      v-bind:total="isTypeCount"
      @doSearch="handleTypeStatusSearch"
    />
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
// import { useI18n } from 'vue-i18n'
import FiltLink from './baseComponents/FiltLink.vue'

// const { t } = useI18n()

const emits = defineEmits(['freeTextSearch', 'filterSearch'])

const store = useStore()

const props = defineProps(['filterSearchLoading'])

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
  search(false, false, false, false)
}

function handleCoordinatesSearch() {
  search(true, false, false, false)
}

function handleInSwedenSearch() {
  search(false, false, true, false)
}

function handleHasImageSearch() {
  search(false, true, false, false)
}

function handleTypeStatusSearch() {
  search(false, false, false, true)
}

function search(filtCoordinates, filtImages, filtInSweden, filtTypeStatus) {
  const searchText = '*'
  store.commit('setSearchText', searchText)

  store.commit('setFilterCoordinates', filtCoordinates)
  store.commit('setFilterInSweden', filtInSweden)
  store.commit('setFilterImage', filtImages)
  store.commit('setFilterType', filtTypeStatus)

  store.commit('setCollectionGroup', null)
  emits('filterSearch')
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
