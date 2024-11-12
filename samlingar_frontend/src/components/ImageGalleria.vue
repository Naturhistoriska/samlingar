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
import Galleria from 'primevue/galleria'

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
let scientificName = ref()

onMounted(() => {
  console.log('mounted...')
  const result = store.getters['selectedResult']

  if (result) {
    const { associatedMedia, catalogNumber, collectionId, morphbankImageId, species, txFullName } =
      result

    catalogNum.value = catalogNumber
    scientificName.value = txFullName ? txFullName : species

    const botnayColection = 'vp, fungi, mosses, algae'
    const kbo = 'algae, fungi, mosses'
    const paleo = 'pz, pa'
    const zoo = 'ev, et, fish, herps'

    let smallImage = 'tumme'
    if (associatedMedia) {
      if (botnayColection.includes(collectionId)) {
        if (kbo.includes(collectionId)) {
          dataset.value = '&dataset=kbo'
        } else {
          smallImage = 'mini'
          dataset.value = '&dataset=fbo'
        }
        images.value = associatedMedia
          .filter((media) => !media.includes(smallImage))
          .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))

        console.log('images', images.value)
      } else {
        smallImage = 'thumb'
        if (paleo.includes(collectionId)) {
          dataset.value = '&dataset=pal'
        } else if (zoo.includes(collectionId)) {
          dataset.value = '&dataset=' + collectionId
        }
        images.value = associatedMedia.filter((media) => !media.includes(smallImage))
      }
    } else if (morphbankImageId) {
      if (collectionId == '163840') {
        dataset.value = '&imgType=thumbs'
        images.value = morphbankImageId
      }
    }
    // displayBasic.value = true
  }
})

const imageClick = (index) => {
  activeIndex.value = index
  displayCustom.value = true
}
</script>
