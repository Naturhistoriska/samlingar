<template>
  <div class="grid">
    <div>
      {{ $t('search.freeTextSearch') }}
      <small style="font-size: 11px">[{{ $t('search.searchAll') }}]</small>
    </div>
    <div class="flex flex-col gap-2" style="width: 100%">
      <div class="flex-col gap-2 searchInput">
        <input-with-icon
          @doAction="handleFreeTextSearch"
          v-bind:placehold="label"
          v-bind:size="size"
          v-bind:icon="icon"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import InputWithIcon from './baseComponents/InputWithIcon.vue'

const emits = defineEmits(['freeTextSearch'])

const store = useStore()

const icon = ref('pi pi-search')
const size = ref('small')

const label = ref('search.searchAll')

function handleFreeTextSearch(value) {
  const searchText = value ? value : '*'
  store.commit('setSearchText', searchText)

  store.commit('setScientificName', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setCollectionGroup', null)
  store.commit('setFields', [])
  store.commit('setDataResource', null)
  store.commit('setDates', null)

  emits('freeTextSearch', searchText)
}
</script>
<style scoped>
.searchInput {
  min-width: 90%;
  text-align: left;
  float: left;
}
.advanceLink {
  float: right;
}
</style>
