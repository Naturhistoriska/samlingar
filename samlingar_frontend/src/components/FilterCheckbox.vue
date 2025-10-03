<template>
  <div class="card flex flex-wrap justify-center gap-2">
    <div class="flex items-center gap-1">
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
    <div class="flex items-center gap-1">
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
    <div class="flex items-center gap-1">
      <Checkbox
        v-model="type"
        inputId="types"
        name="type"
        :binary="true"
        size="small"
        @click="typeClicked"
      />
      <label for="types" class="text-sm">{{ $t('search.isType') }}</label>
    </div>
    <div class="flex items-center gap-1">
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
    <div class="flex items-center gap-2">
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

// watch(
//   () => store.getters['filterImage'],
//   (newValue, oldValue) => {
//     image.value = newValue
//   }
// )

// watch(
//   () => store.getters['filterCoordinates'],
//   (newValue, oldValue) => {
//     coordinates.value = newValue
//   }
// )

// watch(
//   () => store.getters['filterInSweden'],
//   (newValue, oldValue) => {
//     console.log('old and new', oldValue, newValue)
//     sweden.value = newValue
//   }
// )

// watch(
//   () => store.getters['filterType'],
//   (newValue, oldValue) => {
//     console.log('newValue', newValue, oldValue)
//     type.value = newValue
//   }
// )

watch(
  () => store.getters['clearSearch'],
  () => {
    const reset = store.getters['clearSearch']
    if (reset) {
      image.value = store.getters['filterImage']
      coordinates.value = store.getters['filterCoordinates']
      type.value = store.getters['filterType']
      sweden.value = store.getters['filterInSweden']
      store.commit('setClearSearch', false)
    }
  }
)

onMounted(() => {
  console.log('onMounted')

  image.value = store.getters['filterImage']
  coordinates.value = store.getters['filterCoordinates']
  type.value = store.getters['filterType']
  sweden.value = store.getters['filterInSweden']
})

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
