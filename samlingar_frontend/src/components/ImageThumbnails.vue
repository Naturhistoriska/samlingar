<template>
  <div class="iiif-thumbnails">
    <div v-for="(thumb, index) in thumbnails" :key="index" class="thumbnail">
      <img :src="thumb" alt="thumbnail" @click="imageClick(index)" />
    </div>
  </div>
  <div v-if="showMetadata" class="metadata-row flex items-center gap-2">
    <img src="/iiif.png" alt="iiif" width="30px" />
    <Button
      text
      icon="pi pi-link"
      label="View manifest data"
      @click="viewMetadata"
      style="color: #1d634a"
      class="p-button-sm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

// const manifestUrl = 'https://assets.nrm.se/manifest/NHRS-HEMI000000010.json' //  IIIF manifest URL
const thumbnails = ref([])
const metadata = ref()
const showMetadata = ref(false)

onMounted(async () => {
  loadManifest()
})

async function loadManifest() {
  const record = store.getters['selectedRecord']
  if (record) {
    const { associatedMedia, collectionCode } = record

    if (isSpecifyCollections(collectionCode)) {
      metadata.value = associatedMedia
      try {
        const response = await fetch(associatedMedia)

        if (!response.ok) {
          showMetadata.value = false
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        showMetadata.value = true

        const manifest = await response.json()
        const canvasList = manifest.items
        thumbnails.value = canvasList
          .map((canvas) => {
            if (canvas.thumbnail?.[0]?.id) return canvas.thumbnail[0].id // v3

            // Otherwise derive from image service
            const imageService = canvas.items?.[0]?.items?.[0]?.body?.service?.[0]?.id // v3

            return imageService ? `${imageService}/full/!200,200/0/default.jpg` : ''
          })
          .filter(Boolean) // remove empty entries
      } catch (err) {
        console.error('Error fetching media:', err)
        showMetadata.value = false
      }
    } else {
      let dataset
      let media
      const botnayColection = 'vp, fungi, mosses, algae'
      const kbo = 'algae, fungi, mosses'
      const paleo = 'pz, pb'
      const zoo = 'ev, et, PI, HE, MA, AV'

      let smallImage = 'tumme'
      if (associatedMedia) {
        if (botnayColection.includes(collectionCode)) {
          if (kbo.includes(collectionCode)) {
            dataset = '&dataset=kbo'
          } else {
            smallImage = 'mini'
            dataset = '&dataset=fbo'
          }
          media = associatedMedia
            .filter((media) => media.includes(smallImage))
            .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))
        } else {
          smallImage = 'thumb'
          if (paleo.includes(collectionCode)) {
            dataset = '&dataset=pal'
          } else if (zoo.includes(collectionCode)) {
            if (collectionCode === 'HE') {
              dataset = '&dataset=herps'
            } else if (collectionCode === 'PI') {
              dataset = '&dataset=fish'
            } else {
              dataset = '&dataset=' + collectionCode
            }
          }
          media = associatedMedia.filter((media) => media.includes(smallImage))
        }
        thumbnails.value = media.map(
          (media) => `https://media-service.nrm.se/images?id=${media}${dataset}`
        )
      }
    }
  }
}

const isSpecifyCollections = (code) => {
  return code === 'NHRS'
  // return (
  // code === 'NHRS' ||
  // code === 'SMTP_INV' ||
  // code === 'SMTP_SPPLST' ||
  // code === 'NRMLIG' ||
  // code === 'NRMMIN' ||
  // code === 'NRMNOD'
  // )
}

function viewMetadata() {
  window.open(metadata.value, '_blank')
}

function imageClick(index) {
  store.commit('setShowImageView', true)
}
</script>

<style scoped>
.iiif-thumbnails {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.thumbnail img {
  width: 90px;
  height: auto;
  border: 1px solid #ccc;
  cursor: pointer;
  transition: transform 0.2s ease;
}
.thumbnail img:hover {
  transform: scale(1.05);
  border-color: #888;
}

.thumbnail:hover {
  transform: scale(1.05);
  transition: transform 0.2s;
}
</style>
