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
        v-model="coordinates"
        inputId="coordinates"
        name="coordinates"
        binary
        size="small"
        @click="coordinatesClicked"
      />
      <label for="image" class="text-sm">Have coordinates</label>
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

onMounted(() => {
  image.value = store.getters['filterImage']
  coordinates.value = store.getters['filterCoordinates']
})

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
