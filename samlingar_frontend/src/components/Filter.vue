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
  const searchText = '*'

  store.commit('setSearchText', searchText)
  store.commit('setScientificName', null)
  store.commit('setCollectionGroup', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setFields', [])
  store.commit('setDataResource', null)
  store.commit('setDates', null)

  emits('freeTextSearch', searchText)
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
  store.commit('setScientificName', null)
  store.commit('setCollectionGroup', null)

  store.commit('setFilterCoordinates', filtCoordinates)
  store.commit('setFilterInSweden', filtInSweden)
  store.commit('setFilterImage', filtImages)
  store.commit('setFilterType', filtTypeStatus)

  store.commit('setFields', [])
  store.commit('setDataResource', null)
  store.commit('setDates', null)

  emits('filterSearch', filtCoordinates, filtImages, filtInSweden, filtTypeStatus)
}
</script>
<style scoped></style>
