<template>
  <div class="iiif-viewer">
    <!-- Main IIIF Viewer -->
    <div ref="viewer" class="osd-viewer" @dblclick="downloadFullImage"></div>

    <!-- Thumbnail Gallery -->
    <div class="thumbnail-gallery">
      <div
        v-for="(canvas, i) in canvases"
        :key="i"
        class="thumbnail-wrapper"
        :class="{ active: currentIndex === i }"
      >
        <div class="thumbnail-container" @click="loadCanvas(i)">
          <img :src="getThumbnail(canvas)" class="thumbnail" />

          <!-- Live viewport rectangle for the active image -->
          <div
            v-if="currentIndex === i"
            class="thumbnail-viewport"
            :style="thumbnailViewportStyle"
          ></div>
        </div>
        <div class="thumbnail-label">
          {{ getLabel(canvas) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, reactive } from 'vue'
import OpenSeadragon from 'openseadragon'
import { useStore } from 'vuex'

const store = useStore()

const viewer = ref(null)
const canvases = ref([])
const currentIndex = ref(0)
let osdViewer = null
let mainItem = null
let currentService = null

// Reactive style for the live viewport box inside the thumbnail
const thumbnailViewportStyle = reactive({
  left: '0%',
  top: '0%',
  width: '0%',
  height: '0%'
})

// Example IIIF v3 manifest
const iiifManifestUrl = 'https://assets.nrm.se/manifest/NHRS-HEMI000000010.json'
// Example sources
const rawImages = [
  'https://media-service.nrm.se/images?id=Full_size/EVMain_182143_2.jpg&dataset=ev',
  'https://media-service.nrm.se/images?id=Full_size/EVType_1956_Trochoidea_(Xerocrassa)_cretica_8.jpg&dataset=ev',
  'https://media-service.nrm.se/images?id=Full_size/EVType_1956_Trochoidea_(Xerocrassa)_cretica_5.jpg&dataset=ev',
  'https://media-service.nrm.se/images?id=Full_size/EVType_1956_Trochoidea_(Xerocrassa)_cretica_11.jpg&dataset=ev'
]

onMounted(async () => {
  const record = store.getters['selectedRecord']

  if (record) {
    const { associatedMedia, collectionCode } = record

    if (isSpecifyCollections(collectionCode)) {
      const manifest = await fetch(iiifManifestUrl).then((r) => r.json())
      canvases.value = manifest.items || []
      console.log('canvases.value', canvases.value)
    } else {
    }
  }
  osdViewer = OpenSeadragon({
    element: viewer.value,
    prefixUrl: 'https://openseadragon.github.io/openseadragon/images/',
    showNavigator: false,
    visibilityRatio: 1.0,
    minZoomLevel: 0.5
  })

  loadCanvas(0)

  // When user pans or zooms, update the thumbnail viewport
  osdViewer.addHandler('animation', updateThumbnailViewport)
  osdViewer.addHandler('open', () => {
    mainItem = osdViewer.world.getItemAt(0)
    updateThumbnailViewport()
    // addDownloadButton()
  })
})

onBeforeUnmount(() => {
  osdViewer?.destroy()
})

const isSpecifyCollections = (code) => {
  return code === 'NHRS'
  // return (
  //   code === 'NHRS' ||
  //   code === 'SMTP_INV' ||
  //   code === 'SMTP_SPPLST' ||
  //   code === 'NRMLIG' ||
  //   code === 'NRMMIN' ||
  //   code === 'NRMNOD'
  // )
}

function loadCanvas(index) {
  currentIndex.value = index
  const canvas = canvases.value[index]
  console.log('canvas', canvas)

  if (isSpecifyCollections) {
    const body = canvas?.items?.[0]?.items?.[0]?.body
    const imageService = body?.service?.[0]?.['@id'] || body?.service?.[0]?.id
    if (!imageService) return
    currentService = imageService
    console.log('currentService', imageService)
    osdViewer.open(`${imageService}/info.json`)
  } else {
    // do image
    currentService =
      'https://media-service.nrm.se/images?id=Full_size/EVMain_182143_2.jpg&dataset=ev'
    osdViewer.open(
      'https://media-service.nrm.se/images?id=Full_size/EVMain_182143_2.jpg&dataset=ev'
    )
  }
}

// Compute live viewport rectangle
function updateThumbnailViewport() {
  if (!osdViewer || !mainItem) return

  const bounds = osdViewer.viewport.getBounds(true)
  const imgRect = mainItem.viewportToImageRectangle(bounds)
  const imgInfo = mainItem.source.dimensions

  const left = (imgRect.x / imgInfo.x) * 100
  const top = (imgRect.y / imgInfo.y) * 100
  const width = (imgRect.width / imgInfo.x) * 100
  const height = (imgRect.height / imgInfo.y) * 100

  thumbnailViewportStyle.left = `${left}%`
  thumbnailViewportStyle.top = `${top}%`
  thumbnailViewportStyle.width = `${width}%`
  thumbnailViewportStyle.height = `${height}%`
}

// Thumbnail + labels
function getThumbnail(canvas) {
  if (isSpecifyCollections) {
    const thumb =
      canvas.thumbnail?.[0]?.id ||
      canvas.thumbnail?.[0]?.['@id'] ||
      canvas.items?.[0]?.items?.[0]?.body?.id
    return thumb ? `${thumb}/full/!200,200/0/default.jpg` : ''
  } else {
    return rawImages
  }
}

function getLabel(canvas) {
  const label =
    canvas.label?.none?.[0] ||
    canvas.label?.en?.[0] ||
    `Canvas ${canvases.value.indexOf(canvas) + 1}`
  return label
}

// function addDownloadButton() {
//   // Create a simple button element
//   const buttonElement = document.createElement('button')
//   buttonElement.title = 'Download current view'
//   buttonElement.innerHTML = '⬇️'
//   Object.assign(buttonElement.style, {
//     fontSize: '20px',
//     cursor: 'pointer',
//     background: 'white',
//     border: '1px solid #ccc',
//     borderRadius: '4px',
//     padding: '2px 6px'
//   })
//   buttonElement.addEventListener('click', downloadFullImage)

//   // Use OpenSeadragon’s addControl to anchor it
//   osdViewer.addControl(buttonElement, {
//     anchor: OpenSeadragon.ControlAnchor.TOP_LEFT
//   })
// }

function downloadFullImage() {
  if (!currentService) return
  const url = `${currentService}/full/!200,200/0/default.jpg`
  // const a = document.createElement('a')
  // a.href = url
  // a.download = 'iiif-full.jpg'
  // a.click()
  window.open(url, '_blank')
}
</script>

<style scoped>
.iiif-viewer {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.osd-viewer {
  width: 800px;
  height: 600px;
  border: 1px solid #ccc;
}

/* Thumbnails layout */
.thumbnail-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
  max-width: 900px;
}

.thumbnail-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 0.85rem;
  text-align: center;
}

.thumbnail-container {
  position: relative;
  width: 120px;
  height: 120px;
  overflow: hidden;
  border: 1px solid #999;
  cursor: pointer;
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumbnail-wrapper.active .thumbnail-container {
  border-color: #007bff;
}

/* Live viewport rectangle */
.thumbnail-viewport {
  position: absolute;
  border: 2px solid #00aaff;
  background: rgba(0, 170, 255, 0.2);
  pointer-events: none;
  transition: all 0.05s linear;
}

.thumbnail-label {
  margin-top: 4px;
  color: #444;
}
</style>
