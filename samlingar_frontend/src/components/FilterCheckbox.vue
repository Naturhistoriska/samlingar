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
      <label for="image" class="text-sm">Have images</label>
    </div>
    <div class="flex items-center gap-2">
      <Checkbox
        v-model="map"
        inputId="map"
        name="image"
        binary
        size="small"
        @click="coordinatesClicked"
      />
      <label for="map" class="text-sm">Have coordinages</label>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'

const emits = defineEmits(['search'])

const image = ref(false)
const map = ref(false)

function coordinatesClicked() {
  console.log('coordinatesClicked', image.value, map.value)

  const searchMap = !map.value

  emits('search', image.value, searchMap)
}

function imageClicked() {
  console.log('imageClicked', image.value)

  const searchImage = !image.value

  emits('search', searchImage, map.value)
}
</script>
