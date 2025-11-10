<template>
  <div class="iiif-viewer">
    <!-- Main image viewer -->
    <div ref="viewer" class="osd-viewer"></div>

    <!-- Metadata Display -->
    <div class="metadata">
      <div class="info">
        <h3>{{ currentLabel }}</h3>
      </div>
      <div class="actions">
        <Button
          icon="pi pi-external-link"
          @click="openInNewTab"
          label="Open in new tab"
          class="viewer-btn"
        />
        <Button icon="pi pi-times-circle" @click="closeViewer" label="Close" class="viewer-btn" />
      </div>
    </div>

    <!-- Thumbnail toggle (mobile only) -->
    <div class="thumbnail-toggle">
      <Button
        icon="pi pi-images"
        text
        class="viewer-btn"
        :label="showThumbnails ? 'Hide thumbnails' : 'Show thumbnails'"
        @click="toggleThumbnails"
      />
    </div>

    <!-- Thumbnail strip -->
    <transition name="fade">
      <div v-if="showThumbnails" class="thumbnail-panel">
        <!-- <button class="nav-btn left" @click="scrollThumbnails(-1)">◀</button> -->
        <div
          v-for="(thumb, i) in thumbnails"
          :key="i"
          class="thumb"
          :class="{ active: currentIndex === i }"
          @click="openImage(i)"
        >
          <img :src="thumb" alt="thumbnail" />
        </div>
        <!-- <button class="nav-btn right" :disabled="isAtEnd" @click="scrollThumbnails(1)">▶</button> -->
      </div>
    </transition>
  </div>
</template>

<script setup>
import { computed, nextTick, ref, onMounted, onBeforeUnmount } from 'vue'
import OpenSeadragon from 'openseadragon'
import { useStore } from 'vuex'

const store = useStore()

const viewer = ref(null)
// const thumbnailPanel = ref(null)

let osdViewer = null
let mainItem = null

const images = ref([]) // store image info (IIIF or raw)
const thumbnails = ref([]) // thumbnail URLs
const currentIndex = ref(0)
const currentLabel = ref('')
const currentDescription = ref('')

const showThumbnails = ref(true)

// const scrollAmount = 300 // px per navigation click

// Example IIIF v3 manifest
// const iiifManifestUrl = 'https://assets.nrm.se/manifest/NHRS-HEMI000000010.json'
// const iiifManifestUrl = 'https://assets.nrm.se/manifest/NHRS-COLE000007412.json'

//  Initialize OpenSeadragon
onMounted(() => {
  osdViewer = OpenSeadragon({
    element: viewer.value,
    prefixUrl: 'https://openseadragon.github.io/openseadragon/images/',
    showNavigator: true,
    showRotationControl: true,
    gestureSettingsTouch: { pinchRotate: true }
  })
  const record = store.getters['selectedRecord']
  if (record) {
    const { associatedMedia, catalogNumber, collectionCode } = record

    if (isSpecifyCollections(collectionCode)) {
      loadIIIFManifest(associatedMedia, catalogNumber)
    } else {
      loadRawImages(associatedMedia, catalogNumber, collectionCode)
    }
  }
})

onBeforeUnmount(() => osdViewer?.destroy())

// Load IIIF v3 Manifest
async function loadIIIFManifest(manifestUrl, catalogNumber) {
  const manifest = await fetch(manifestUrl).then((r) => r.json())
  const canvases = manifest.items || []

  images.value = canvases.map((c) => {
    const body = c.items?.[0]?.items?.[0]?.body
    const service = body?.service?.[0]?.['@id'] || body?.service?.[0]?.id
    const thumb = c.thumbnail?.[0]?.id || `${service}/full/!200,200/0/default.jpg`
    const label = catalogNumber
    const desc = getIIIFText(c.summary) || getIIIFText(manifest.summary)
    return {
      type: 'iiif',
      source: `${service}/info.json`,
      thumb,
      label,
      desc
    }
  })

  thumbnails.value = images.value.map((i) => i.thumb)
  await nextTick()
  openImage(0)
}

//  Load Raw Image URLs Example
function loadRawImages(associatedMedia, catalogNumber, collectionCode) {
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
        .filter((media) => !media.includes(smallImage))
        .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))
    } else {
      console.log('here...', collectionCode)
      smallImage = 'thumb'
      if (paleo.includes(collectionCode)) {
        dataset = '&dataset=pal'
      } else if (zoo.includes(collectionCode)) {
        if (collectionCode === 'HE') {
          dataset = '&dataset=herps'
        } else if (collectionCode === 'PI') {
          dataset = '&dataset=fish'
        } else {
          console.log('here...111')
          dataset = '&dataset=' + collectionCode
          console.log('dataset', dataset)
        }
      }
      media = associatedMedia.filter((media) => !media.includes(smallImage))
      console.log('dataset', dataset)
    }
    console.log('dataset', dataset)
    images.value = media.map((url) => ({
      type: 'raw',
      source: `https://media-service.nrm.se/images?id=${url}${dataset}`,
      thumb: `https://media-service.nrm.se/images?id=${url}${dataset}`, // can use smaller thumbs if you have them
      label: catalogNumber,
      desc: catalogNumber
    }))
  }

  // thumbnails.value = media.map(
  //   (media) => `https://media-service.nrm.se/images?id=${media}${dataset}`
  // )

  thumbnails.value = images.value.map((i) => i.thumb)
  openImage(0)
}

//  Open a specific image
function openImage(index) {
  console.log('index', index)
  currentIndex.value = index
  const img = images.value[index]
  if (!img) return

  currentLabel.value = img.label || 'Untitled Image'
  currentDescription.value = img.desc || ''

  if (img.type === 'iiif') {
    osdViewer.open(img.source)
  } else {
    osdViewer.open({
      type: 'image',
      url: img.source
    })
  }

  osdViewer.addOnceHandler('open', () => {
    mainItem = osdViewer.world.getItemAt(0)
  })
}

// function scrollThumbnails(direction) {
//   if (!thumbnailPanel.value) return
//   thumbnailPanel.value.scrollBy({
//     left: direction * scrollAmount,
//     behavior: 'smooth'
//   })
// }

// const isAtStart = computed(() =>
//   thumbnailPanel.value ? thumbnailPanel.value.scrollLeft <= 0 : true
// )

// const isAtEnd = computed(() => {
//   if (!thumbnailPanel.value) return false
//   const { scrollLeft, scrollWidth, clientWidth } = thumbnailPanel.value
//   return scrollLeft + clientWidth >= scrollWidth - 5
// })

function openInNewTab() {
  const img = images.value[currentIndex.value]
  if (!img) return

  if (img.type === 'iiif' && mainItem) {
    const url = img.thumb
    window.open(url, '_blank')
  } else {
    window.open(img.source, '_blank')
  }
}

//  Utility: handle multilingual IIIF fields
function getIIIFText(field) {
  if (!field) return ''
  if (typeof field === 'string') return field
  if (Array.isArray(field)) return field[0]
  if (field.en) return field.en[0]
  const firstLang = Object.values(field)[0]
  return Array.isArray(firstLang) ? firstLang[0] : firstLang
}

function closeViewer() {
  store.commit('setShowImageView', false)
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

function toggleThumbnails() {
  showThumbnails.value = !showThumbnails.value
}
</script>

<style scoped>
.iiif-viewer {
  display: flex;
  flex-direction: column;
  /* align-items: center; */
}

.osd-viewer {
  width: 90%;
  height: 600px;
  border: 1px solid #ccc;
  background: #000;
}

.metadata {
  width: 90%;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  background: #fafafa;
  border-top: 1px solid #ccc;
  padding: 10px;
}

.metadata .info h3 {
  margin: 0.5rem 0;
  font-size: 1.3rem;
  color: #144836;
}

/* .metadata h3 { */
/* margin: 0 0 4px; */
/* font-size: 18px; */
/* } */

/* .metadata p {
  margin: 0;
  font-size: 14px;
  color: #555;
} */

.actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.viewer-btn {
  background-color: #144836 !important;
  border-color: #1d634a !important;
  color: #fff !important;
  padding-bottom: 2em;
}

.thumbnail-toggle {
  /* display: none; */
  margin-top: 1rem;
  text-align: center;
}

.thumbnail-panel {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  gap: 0.5rem;
  width: 100%;
  max-width: 900px;
  margin-top: 1rem;
  padding: 0.5rem 0;
  scroll-snap-type: x mandatory;
}

.thumb {
  flex: 0 0 auto;
  width: 80px;
  height: 80px;
  border: 2px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  scroll-snap-align: start;
}

.thumb.active {
  border-color: #144836;
}

/* .actions button {
  margin-left: 8px;
  padding: 6px 10px;
  border: 1px solid #bbb;
  border-radius: 4px;
  background: white;
  cursor: pointer;
}

.actions button:hover {
  background: #007bff;
  color: white;
} */

/* ===============================
   Thumbnail panel & nav buttons
=============================== */
/* .thumbnail-container {
  width: 90%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f0f0;
  padding: 5px 0;
  border-top: 1px solid #ccc;
  position: relative;
}

.nav-btn {
  font-size: 20px;
  padding: 6px 12px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;
  z-index: 2;
}

.nav-btn:hover:not(:disabled) {
  background: #007bff;
  color: white;
}

.nav-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
} */

/* .thumbnail-panel {
  flex: 1;
  display: flex;
  overflow-x: auto;
  scroll-behavior: smooth;
  scrollbar-width: none;
  padding: 10px;
} */

/* .thumbnail-panel::-webkit-scrollbar {
  display: none;
}

.thumb {
  flex: 0 0 auto;
  margin-right: 8px;
  border: 2px solid transparent;
  cursor: pointer;
  position: relative;
}

.thumb.active {
  border-color: #007bff;
}

.thumb-wrapper {
  position: relative;
} */

/* .thumb img {
  height: 80px;
  object-fit: cover;
  display: block;
}

.viewport-rect {
  box-sizing: border-box;
  pointer-events: none;
} */

@media (max-width: 768px) {
  .osd-viewer {
    /* max-width: 100%;
    min-height: 300px; */
    width: 90%;
    height: 300px;
  }

  .metadata {
    padding: 0 0.5rem;
  }

  .metadata .info h3 {
    font-size: 1.1rem;
  }

  .actions {
    flex-direction: column;
    gap: 0.5rem;
  }

  .thumbnail-panel {
    max-width: 100%;
  }

  .thumb {
    width: 60px;
    height: 60px;
  }

  .thumbnail-toggle {
    display: block;
  }
}

/* .thumb {
  flex: 0 0 auto;
  margin-right: 8px;
  border: 2px solid transparent;
  cursor: pointer;
}

.thumb.active {
  border-color: #007bff;
}

.thumb img {
  height: 80px;
  object-fit: cover;
  display: block;
} */
</style>
