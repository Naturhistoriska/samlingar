<template>
  <div class="card" style="cursor: pointer; font-size: 12px">
    <div class="grid" @click="onclick()">
      <div class="col-3" style="padding: 0px">Scientific Name</div>
      <div class="col-9" style="padding: 0px">
        {{ scientificName }}
      </div>
      <div class="col-3" style="padding: 0px">Catalognumber</div>
      <div class="col-9" style="padding: 0px">
        {{ result.catalogNumber }}
      </div>
      <div class="col-3" style="padding: 0px">High classification</div>
      <div class="col-9" style="padding: 0px">
        {{ result.higherTx }}
      </div>
      <div class="col-3" style="padding: 0px">Collection name</div>
      <div class="col-9" style="padding: 0px">
        {{ collectionName }}
      </div>
      <div class="col-3" style="padding: 0px">Locality</div>
      <div class="col-9" style="padding: 0px">{{ result.locality }} {{ result.country }}</div>
    </div>
    <div class="grid">
      <Thumbnail
        v-bind:associatedMedias="associatedMedias"
        v-bind:collectionId="collectionId"
        v-bind:dataset="dataset"
      />
    </div>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import Thumbnail from './Thumbnail.vue'

const store = useStore()

const props = defineProps(['result'])

let dataset = ref()

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
  const nvertebrate = 'ev, et, fish, herps'
  // &dataset=herps
  let list
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

    if (nvertebrate.includes(collectionId)) {
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
  store.commit('setSelectedResult', props.result)
  store.commit('setShowDetail', true)
}
</script>

<style scoped>
.selected {
  background: #e6f2ff;
}

.unselected {
  background: transparent;
}

.unselected:hover {
  color: #1976d2 !important;
}
</style>
