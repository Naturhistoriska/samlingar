<template>
  <div
    class="card flex flex-wrap justify-center gap-2"
    role="searchOptionGroup"
    aria-label="Search options"
  >
    <div class="flex items-center gap-1" @keydown.enter="toggleImageCheck">
      <Checkbox
        v-model="image"
        inputId="image"
        name="image"
        :binary="true"
        size="small"
        @click="imageClicked"
      />
      <label for="image" class="text-sm">{{ $t('search.haveImages') }}</label>
    </div>
    <div class="flex items-center gap-1" @keydown.enter="toggleCoordinatesCheck">
      <Checkbox
        v-model="coordinates"
        inputId="coordinates"
        name="coordinates"
        binary
        size="small"
        @click="coordinatesClicked"
      />
      <label for="coordinates" class="text-sm">{{ $t('search.haveCoordinates') }}</label>
    </div>
    <div class="flex items-center gap-1" @keydown.enter="toggleTypeCheck">
      <Checkbox
        v-model="type"
        inputId="types"
        name="type"
        :binary="true"
        size="small"
        @click="typeClicked"
        @keydown.enter="checked = !checked"
      />
      <label for="types" class="text-sm">{{ $t('search.isType') }}</label>
    </div>
    <div class="flex items-center gap-1" @keydown.enter="toggleSwedenCheck">
      <Checkbox
        v-model="sweden"
        inputId="sweden"
        name="sweden"
        binary
        size="small"
        @click="swedenClicked"
      />
      <label for="sweden" class="text-sm">{{ $t('search.inSweden') }}</label>
    </div>
    <div class="flex items-center gap-2" @keydown.enter="toggleNordicCountriesCheck">
      <Checkbox
        v-model="nordicCountries"
        inputId="nordic"
        name="Nordic countries"
        binary
        size="small"
        @click="nordicClicked"
      />
      <label for="nordic" class="text-sm">{{ $t('search.nordic') }}</label>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

const emits = defineEmits(['search'])

const store = useStore()

let image = ref(false)
let coordinates = ref(false)
let type = ref(false)
let sweden = ref(false)
let nordicCountries = ref(false)

watch(
  () => store.getters['clearSearch'],
  () => {
    const reset = store.getters['clearSearch']
    console.log('reset', reset)
    if (reset) {
      image.value = store.getters['filterImage']
      coordinates.value = store.getters['filterCoordinates']
      type.value = store.getters['filterType']
      sweden.value = store.getters['filterInSweden']
      nordicCountries.value = store.getters['filterNordicCountry']
      store.commit('setClearSearch', false)
    }
  }
)

onMounted(() => {
  image.value = store.getters['filterImage']
  coordinates.value = store.getters['filterCoordinates']
  type.value = store.getters['filterType']
  sweden.value = store.getters['filterInSweden']
})

function toggleCoordinatesCheck() {
  coordinates.value = !coordinates.value
  store.commit('setFilterCoordinates', coordinates.value)
  search()
}

function toggleImageCheck() {
  image.value = !image.value
  store.commit('setFilterImage', image.value)
  search()
}

function toggleNordicCountriesCheck() {
  nordicCountries.value = !nordicCountries.value
  store.commit('setFilterNordicCountry', nordicCountries.value)
  search()
}

function toggleSwedenCheck() {
  sweden.value = !sweden.value
  store.commit('setFilterInSweden', sweden.value)
  search()
}

function toggleTypeCheck() {
  type.value = !type.value
  store.commit('setFilterType', type.value)
  search()
}

function swedenClicked() {
  const searchInSweden = !sweden.value
  store.commit('setFilterInSweden', searchInSweden)
  search()
}

function typeClicked() {
  store.commit('setFilterType', !type.value)
  search()
}

function coordinatesClicked() {
  const searchMap = !coordinates.value
  store.commit('setFilterCoordinates', searchMap)
  search()
}

function imageClicked() {
  const searchImage = !image.value
  store.commit('setFilterImage', searchImage)
  search()
}

function nordicClicked() {
  const nodicSearch = !nordicCountries.value
  store.commit('setFilterNordicCountry', nodicSearch)
  search()
}

function search() {
  emits('search')
}
</script>
<style scoped>
.p-checkbox-box.p-highlight,
.p-checkbox.p-highlight .p-checkbox-box {
  background-color: #007bff !important;
  border-color: #007bff !important;
  color: white !important;
}
</style>
