<template>
  <div class="card">
    <Carousel :value="thumbnails" :numVisible="5" :numScroll="1" circular style="width: 600px">
      <template #item="slotProps">
        <div
          class="border border-surface-200 dark:border-surface-700 rounded m-2 p-4"
          style="width: 70px; padding: 0px"
        >
          <img
            :src="'https://media-service.nrm.se/images?id=' + slotProps.data + dataset"
            :alt="slotProps.data.index"
            style="width: 60px"
            @click="openImage"
          />
        </div>
      </template>
    </Carousel>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import Carousel from 'primevue/carousel'

const store = useStore()

const props = defineProps(['associatedMedias', 'collectionId', 'dataset', 'result'])

const dataset = computed(() => {
  return props.dataset
})

const thumbnails = computed(() => {
  return props.associatedMedias
})

function openImage() {
  console.log('openImage')
  store.commit('setOpenGalleria', false)

  store.commit('setSelectedResult', props.result)

  store.commit('setOpenGalleria', true)
}

// const thumbnailsStyle = computed(() => {
//   const collectionId = props.collectionId
//   if (collectionId === 'mosses') {
//     return "width: 60px"
//   } else if (collectionId === "vp") {
//     return
//   }

//   return props.associatedMedias
// })
</script>
<style scoped>
/* .p-carousel-indicator-active .p-carousel-indicator-button {
  background: #0dff5c;
} */

/* .p-carousel-indicator-active .p-carousel-indicator-button {
  background: red;
} */

/* .p-carousel-indicator-active .p-carousel-indicator-button {
    background: var(--p-carousel-indicator-active-background);
} */
</style>
