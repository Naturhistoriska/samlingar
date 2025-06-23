<template>
  <div class="card flex flex-wrap justify-center gap-4">
    <div class="flex items-center gap-2">
      <Checkbox
        v-model="image"
        inputId="image"
        name="image"
        binary
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
      <label for="image" class="text-sm">{{ $t('search.haveCoordinates') }}</label>
    </div>
    <div class="flex items-center gap-2">
      <Checkbox
        v-model="type"
        inputId="types"
        name="type"
        binary
        size="small"
        @click="typeClicked"
      />
      <label for="image" class="text-sm">{{ $t('search.isType') }}</label>
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
      <label for="image" class="text-sm">{{ $t('search.inSweden') }}</label>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['search'])

let image = ref(false)
let coordinates = ref(false)
let type = ref(false)
let sweden = ref(false)

onMounted(() => {
  image.value = store.getters['filterImage']
  coordinates.value = store.getters['filterCoordinates']
  type.value = store.getters['filterType']
  sweden.value = store.getters['filterInSweden']
})

function swedenClicked() {
  const searchInSweden = !sweden.value
  store.commit('setFilterInSweden', searchInSweden)
  // emits('search')
}

function typeClicked() {
  const searchType = !type.value
  store.commit('setFilterType', searchType)
  // emits('search')
}

function coordinatesClicked() {
  const searchMap = !coordinates.value

  store.commit('setFilterCoordinates', searchMap)
  // emits('search')
}

function imageClicked() {
  const searchImage = !image.value
  store.commit('setFilterImage', searchImage)
  // emits('search')
}
</script>
