<template>
  <div class="iiif-viewer">
    <!-- Main image viewer -->
    <iframe
      v-if="isSpecifyCollections"
      :src="iiifManifestUrl"
      width="90%"
      height="600"
      frameborder="0"
      allowfullscreen
    ></iframe>

    <div v-else ref="viewer" class="osd-viewer"></div>

    <!-- Metadata and actions -->
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
        class="p-button-text"
        :label="showThumbnails ? 'Hide thumbnails' : 'Show thumbnails'"
        @click="toggleThumbnails"
      />
    </div>

    <!-- Thumbnail strip -->
    <transition name="fade">
      <div class="thumbnail-panel">
        <div
          v-if="showThumbnails"
          v-for="(thumb, i) in thumbnails"
          :key="i"
          class="thumb"
          :class="{ active: currentIndex === i }"
          @click="openImage(i)"
        >
          <img :src="thumb" alt="thumbnail" />
        </div>
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

let osdViewer = null
let mainItem = null

const images = ref([]) // store image info (IIIF or raw)
const thumbnails = ref([]) // thumbnail URLs
const currentIndex = ref(0)
const currentLabel = ref('')
const currentDescription = ref('')

const showThumbnails = ref(true)
const catNumber = ref('')

const iiifManifestUrl = ref('')

// const iiifManifestUrl = 'https://assets.nrm.se/viewer/NHRS-TOBI000002720'
// const iiifManifestUrl = 'https://assets.nrm.se/viewer/uv.html/NHRS-TOBI000002720'

// const scrollAmount = 300 // px per navigation click

// Example IIIF v3 manifest
// const iiifManifestUrl = 'https://assets.nrm.se/manifest/NHRS-HEMI000000010.json'
// const iiifManifestUrl = 'https://assets.nrm.se/manifest/NHRS-COLE000007412.json'

const isSpecifyCollections = computed(() => {
  const record = store.getters['selectedRecord']
  const code = record?.collectionCode
  return code === 'NHRS' || code === 'NRMMIN'
})

onMounted(() => {
  const record = store.getters['selectedRecord']
  if (record) {
    const { associatedMedia, catalogNumber, collectionCode } = record

    catNumber.value = catalogNumber

    console.log('isSpecifyCollections', isSpecifyCollections.value)

    if (isSpecifyCollections.value) {
      // loadIIIFManifest(associatedMedia, catalogNumber)

      iiifManifestUrl.value = `https://assets.nrm.se/viewer/${catalogNumber}`
    } else {
      osdViewer = OpenSeadragon({
        element: viewer.value,
        prefixUrl: 'https://openseadragon.github.io/openseadragon/images/',

        // crossOriginPolicy: 'Anonymous', // important
        ajaxWithCredentials: false,
        showNavigator: true,
        showRotationControl: true,
        gestureSettingsTouch: { pinchRotate: true }
      })

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
    let largeImage = 'max'
    if (botnayColection.includes(collectionCode)) {
      if (kbo.includes(collectionCode)) {
        dataset = '&dataset=kbo'
      } else {
        largeImage = 'large'
        dataset = '&dataset=fbo'
      }
      media = associatedMedia
        .filter((media) => media.includes(largeImage))
        .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))
    } else {
      let fullSize = 'Full'
      if (paleo.includes(collectionCode)) {
        dataset = '&dataset=pal'
        fullSize = 'medium'
      } else if (zoo.includes(collectionCode)) {
        if (collectionCode === 'HE') {
          dataset = '&dataset=herps'
        } else if (collectionCode === 'PI') {
          dataset = '&dataset=fish'
        } else {
          dataset = '&dataset=' + collectionCode
          console.log('dataset', dataset)
        }
      }
      media = associatedMedia.filter((media) => media.includes(fullSize))
    }
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

function openInNewTab() {
  const img = images.value[currentIndex.value]

  if (!img) {
    const url = `https://assets.nrm.se/viewer/${catNumber.value}`

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

function toggleThumbnails() {
  showThumbnails.value = !showThumbnails.value
}
</script>

<style scoped>
.iiif-viewer {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 1rem;
  box-sizing: border-box;
}

.osd-viewer {
  width: 90%;
  height: 600px;
  border: 1px solid #ccc;
  background: #000;
  border-radius: 6px;
}

.viewer-btn {
  background-color: #144836 !important;
  border-color: #1d634a !important;
  color: #fff !important;
}

.metadata {
  width: 100%;
  max-width: 900px;
  margin-top: 1rem;
  text-align: center;
}

.metadata .info h3 {
  margin: 0.5rem 0;
  font-size: 1.3rem;
  color: #144836;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

/*  Thumbnail Toggle */
.thumbnail-toggle {
  display: none;
  margin-top: 1rem;
  text-align: center;
}

/* Thumbnails */
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

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb.active {
  border-color: #144836;
}

/*  Fade animation for show/hide */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .osd-viewer {
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
</style>
