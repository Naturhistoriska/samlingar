<template>
  <div class="card">
    <Galleria
      v-if="images"
      v-model:visible="displayBasic"
      :value="images"
      :responsiveOptions="responsiveOptions"
      :numVisible="5"
      containerStyle="max-width: 60%"
      :circular="true"
      :fullScreen="true"
      :showItemNavigators="true"
    >
      <template #item="slotProps">
        <img
          :src="'https://media-service.nrm.se/images?id=' + slotProps.item + dataset"
          :alt="slotProps.item"
          style="width: 100%"
        />
      </template>
      <template #thumbnail="slotProps">
        <img
          :src="'https://media-service.nrm.se/images?id=' + slotProps.item + dataset"
          :alt="slotProps.item.alt"
          style="width: 70px; padding: 0px"
        />
      </template>
      <template #caption="slotProps">
        <div class="text-xl mb-2" @click="onClick">{{ scientificName }} [{{ catalogNum }} ]</div>
      </template>
    </Galleria>
  </div>
</template>

<script setup>
import Galleria from 'primevue/galleria'
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

let dataset = ref()
let images = ref()
const displayBasic = ref(false)
let catalogNum = ref()
let scientificName = ref()
const position = ref('right')

const responsiveOptions = ref([
  {
    breakpoint: '1300px',
    numVisible: 4
  },
  {
    breakpoint: '575px',
    numVisible: 1
  }
])

watch(displayBasic, async (newDisplayBasic, oldDisplayBasic) => {
  console.log('displayBasic', newDisplayBasic, oldDisplayBasic)

  if (!newDisplayBasic) {
    setTimeout(() => {
      store.commit('setOpenGalleria', false)
    }, 1000)
  }
})

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
    displayBasic.value = true
  }
})

// const showGalleria = computed(() => {
//   return store.getters['openGalleria']
// })

// function viewDetail() {
//   console.log('clicked...')
//   // displayBasic.value = false
//   store.commit('setOpenGalleria', false)
//   store.commit('setShowDetail', true)
// }
</script>
<style scoped>
.p-button-link {
  color: #0dff5c;
}
</style>
