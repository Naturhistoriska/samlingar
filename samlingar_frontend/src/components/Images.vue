<template>
  <div class="card flex justify-center">
    <Galleria
      v-model:activeIndex="activeIndex"
      v-model:visible="displayCustom"
      :value="images"
      :responsiveOptions="responsiveOptions"
      :numVisible="5"
      containerStyle="max-width: 800px"
      :circular="true"
      :fullScreen="true"
      :showItemNavigators="true"
      :showThumbnails="false"
    >
      <template #item="slotProps">
        <Image
          :src="'https://media-service.nrm.se/images?id=' + slotProps.item + dataset"
          alt="Image"
          width="100%"
          preview
        />
      </template>
      <template #thumbnail="slotProps">
        <img
          :src="'https://media-service.nrm.se/images?id=' + slotProps.item + dataset"
          :alt="slotProps.item.alt"
          style="display: block; width: 200px"
        />
      </template>
    </Galleria>

    <div v-if="images" class="grid grid-cols-12 gap-4" style="max-width: 600px">
      <div v-for="(image, index) of images" :key="index" class="col-span-4">
        <img
          :src="'https://media-service.nrm.se/images?id=' + image + dataset"
          :alt="image.alt"
          style="cursor: pointer; width: 100px"
          @click="imageClick(index)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

let dataset = ref()
const images = ref()
const activeIndex = ref(0)
const responsiveOptions = ref([
  {
    breakpoint: '1024px',
    numVisible: 5
  },
  {
    breakpoint: '768px',
    numVisible: 3
  },
  {
    breakpoint: '560px',
    numVisible: 1
  }
])
const displayCustom = ref(false)
let catalogNum = ref()
let taxon = ref()

onMounted(() => {
  const record = store.getters['selectedRecord']

  if (record) {
    const { associatedMedia, catalogNumber, collectionCode, scientificName } = record

    catalogNum.value = catalogNumber
    taxon.value = scientificName

    const botnayColection = 'vp, fungi, mosses, algae'
    const kbo = 'algae, fungi, mosses'
    const paleo = 'pz, pa'
    const zoo = 'ev, et, pi, he'

    let smallImage = 'tumme'
    if (associatedMedia) {
      if (botnayColection.includes(collectionCode)) {
        if (kbo.includes(collectionId)) {
          dataset.value = '&dataset=kbo'
        } else {
          smallImage = 'mini'
          dataset.value = '&dataset=fbo'
        }
        images.value = associatedMedia
          .filter((media) => !media.includes(smallImage))
          .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))
      } else {
        smallImage = 'thumb'
        if (paleo.includes(collectionCode)) {
          dataset.value = '&dataset=pal'
        } else if (zoo.includes(collectionCode)) {
          dataset.value = '&dataset=' + collectionCode
        }
        images.value = associatedMedia.filter((media) => !media.includes(smallImage))
      }
    }
  }
})

const imageClick = (index) => {
  activeIndex.value = index
  displayCustom.value = true
}
</script>
<style scoped>
/* :v-deep {
  font-size: 16px;
} */
</style>
