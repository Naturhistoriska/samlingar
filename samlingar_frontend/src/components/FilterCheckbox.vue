<template>
  <div class="card flex flex-wrap justify-center gap-4">
    <div class="flex items-center gap-2">
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
    <div class="flex items-center gap-2">
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
    <div class="flex items-center gap-2">
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
    <div class="flex items-center gap-2">
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
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

let image = ref(false)
let coordinates = ref(false)
let type = ref(false)
let sweden = ref(false)

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
}

function typeClicked() {
  console.log('type.value', type.value)
  store.commit('setFilterType', !type.value)
}

function coordinatesClicked() {
  const searchMap = !coordinates.value

  store.commit('setFilterCoordinates', searchMap)
}

function imageClicked() {
  const searchImage = !image.value
  store.commit('setFilterImage', searchImage)
}
</script>
