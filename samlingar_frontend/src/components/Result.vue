<template>
  <div style="cursor: pointer; font-size: 12px" :class="{ active: hover }">
    <div class="grid" @mouseover="hover = true" @mouseleave="hover = false" @click="onclick()">
      <div class="col-3 reducePadding">Scientific Name</div>
      <div class="col-9 reducePadding">
        {{ scientificName }}
      </div>
      <div class="col-3 reducePadding">Catalognumber</div>
      <div class="col-9 reducePadding">
        {{ result.catalogNumber }}
      </div>
      <div class="col-3 reducePadding">High classification</div>
      <div class="col-9 reducePadding">
        {{ result.higherTx }}
      </div>
      <div class="col-3 reducePadding">Collection name</div>
      <div class="col-9 reducePadding">
        {{ collectionName }}
      </div>
      <div class="col-3 reducePadding">Locality</div>
      <div class="col-9 reducePadding">{{ result.locality }} {{ result.country }}</div>
    </div>
    <div class="grid" v-if="associatedMedias">
      <Thumbnail
        v-bind:associatedMedias="associatedMedias"
        v-bind:collectionId="collectionId"
        v-bind:dataset="dataset"
        v-bind:result="result"
      />
    </div>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import Thumbnail from './Thumbnail.vue'

const store = useStore()
const router = useRouter()

const props = defineProps(['result'])

let dataset = ref()
const hover = ref(false)

const result = computed(() => {
  return props.result
})

const scientificName = computed(() => {
  const { species, txFullName } = props.result
  return txFullName ? txFullName : species
})

const collectionName = computed(() => {
  return props.result.collectionName
})

const collectionId = computed(() => {
  return props.result.collectionId
})

const associatedMedias = computed(() => {
  const { associatedMedia, collectionId, morphbankImageId } = props.result

  const botnayColection = 'vp, fungi, mosses, algae'
  const kbo = 'algae, fungi, mosses'
  const paleo = 'pz, pa'
  const zoo = 'ev, et, fish, herps'

  let smallImage = 'mini'
  if (associatedMedia) {
    if (botnayColection.includes(collectionId)) {
      if (kbo.includes(collectionId)) {
        smallImage = 'tumme1'
        dataset.value = '&dataset=kbo'
      } else {
        dataset.value = '&dataset=fbo'
      }
      return associatedMedia
        .filter((media) => media.includes(smallImage))
        .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))
    }

    if (paleo.includes(collectionId)) {
      smallImage = 'thumb'
      dataset.value = '&dataset=pal'
      return associatedMedia.filter((media) => media.includes(smallImage))
    }

    if (zoo.includes(collectionId)) {
      smallImage = 'thumb'
      dataset.value = '&dataset=' + collectionId
      return associatedMedia.filter((media) => media.includes(smallImage))
    }
  } else if (morphbankImageId) {
    console.log('collectin id', collectionId)
    if (collectionId == '163840') {
      dataset.value = '&imgType=thumbs'
      console.log('collectin id 1', collectionId, morphbankImageId)
      return morphbankImageId
    }
  }
})

function onclick() {
  const id = props.result.id

  store.commit('setSelectedResult', props.result)
  store.commit('setShowDetail', true)
  router.push(`/result/${id}`)
}
</script>

<style scoped>
.active {
  background: #082405;
}

.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
