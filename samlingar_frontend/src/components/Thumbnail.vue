<template>
  <div class="card">
    <Carousel :value="thumbnails" :numVisible="5" :numScroll="1" circular style="width: 600px">
      <template #item="slotProps">
        <div
          class="border border-surface-200 dark:border-surface-700 rounded m-2 p-4"
          style="width: 70px"
        >
          <img
            :src="'https://media-service.nrm.se/images?id=' + slotProps.data + dataset"
            :alt="slotProps.data.index"
            style="width: 60px"
          />
        </div>
      </template>
    </Carousel>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
// import { useStore } from 'vuex'
import Carousel from 'primevue/carousel'

// const store = useStore()

const props = defineProps(['associatedMedias', 'collectionId', 'dataset'])

const dataset = computed(() => {
  return props.dataset
})

const thumbnails = computed(() => {
  return props.associatedMedias
  // const medias = props.associatedMedias

  // return medias
})

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
