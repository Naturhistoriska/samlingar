<template>
  <div class="viewer-wrapper">
    <!-- Main Viewer -->
    <div class="osd-container">
      <div ref="osdContainer" class="osd-viewer"></div>
    </div>

    <!-- Thumbnail Panel -->
    <div v-if="pageCount > 1" class="thumbnails">
      <div
        v-for="(thumb, index) in canvasThumbnails"
        :key="index"
        :class="['thumb', { active: currentPage === index }]"
        @click="goToPage(index)"
      >
        <img :src="thumb" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import OpenSeadragon from 'openseadragon'

const props = defineProps({
  source: { type: String, required: true }, // raw image, IIIF info.json, or manifest URL
  overlays: { type: Array, default: () => [] },
  prefixUrl: { type: String, default: 'https://openseadragon.github.io/openseadragon/images/' },
  showNavigator: { type: Boolean, default: true },
  navigatorPosition: { type: String, default: 'BOTTOM_RIGHT' }
})

const osdContainer = ref(null)
let viewer = null

// Manifest/page tracking
const manifestData = ref(null)
const canvasImages = ref([])
const canvasThumbnails = ref([])
const currentPage = ref(0)
const pageCount = ref(1)

// Helpers
const isIiifInfoJson = (url) => url.includes('/info.json')
const isIiifImageUrl = (url) => url.match(/\/full\/.*\.(jpg|png|tif|webp)$/)
const isManifest = (url) => url.endsWith('manifest.json') || url.includes('/manifest')

// Convert URL to OpenSeadragon TileSource
const formatTileSource = (url) => {
  if (isIiifInfoJson(url)) {
    return new OpenSeadragon.IIIFTileSource({ url, buildPyramid: true })
  }
  return { type: 'image', url } // raw images
}

// Initialize OpenSeadragon
const initViewer = (tileSource) => {
  if (viewer) viewer.destroy()
  viewer = OpenSeadragon({
    element: osdContainer.value,
    prefixUrl: props.prefixUrl,
    showNavigator: props.showNavigator,
    navigatorPosition: props.navigatorPosition,
    tileSources: tileSource
  })

  viewer.addOnceHandler('open', () => addOverlays())
}

// Add overlays
const addOverlays = () => {
  if (!viewer) return
  viewer.clearOverlays()
  props.overlays.forEach((overlay) => {
    const el = document.createElement('div')
    el.style.position = 'absolute'
    el.style.background = overlay.color || 'red'
    el.style.width = (overlay.width || 20) + 'px'
    el.style.height = (overlay.height || 20) + 'px'
    el.style.borderRadius = overlay.shape === 'circle' ? '50%' : '0'
    el.style.opacity = overlay.opacity || '0.5'
    if (overlay.label) {
      const label = document.createElement('span')
      label.innerText = overlay.label
      label.style.color = 'white'
      label.style.fontSize = '12px'
      label.style.position = 'absolute'
      label.style.top = '-20px'
      el.appendChild(label)
    }
    viewer.addOverlay({ element: el, location: overlay.location })
  })
}

// Load source
const loadSource = async () => {
  canvasImages.value = []
  canvasThumbnails.value = []

  if (isManifest(props.source)) {
    try {
      const res = await fetch(props.source)
      manifestData.value = await res.json()
      // Extract each canvas tile source
      canvasImages.value = manifestData.value.items.map((canvas) => {
        const annotation = canvas.items[0].items[0].body
        if (annotation.service) {
          const infoUrl = annotation.service[0].id + '/info.json'
          return new OpenSeadragon.IIIFTileSource({ url: infoUrl, buildPyramid: true })
        }
        return { type: 'image', url: annotation.id }
      })
      // Thumbnails
      canvasThumbnails.value = manifestData.value.items.map((canvas) => {
        const annotation = canvas.items[0].items[0].body
        if (annotation.service) {
          return `${annotation.service[0].id}/full/!100,100/0/default.jpg`
        }
        return annotation.id
      })
      pageCount.value = canvasImages.value.length
      currentPage.value = 0
      initViewer(canvasImages.value[0])
    } catch (err) {
      console.error('Error loading manifest:', err)
    }
  } else if (isIiifInfoJson(props.source)) {
    const tileSource = new OpenSeadragon.IIIFTileSource({ url: props.source, buildPyramid: true })
    canvasImages.value = [tileSource]
    canvasThumbnails.value = [props.source]
    pageCount.value = 1
    currentPage.value = 0
    initViewer(tileSource)
  } else {
    // Raw image
    canvasImages.value = [{ type: 'image', url: props.source }]
    canvasThumbnails.value = [props.source]
    pageCount.value = 1
    currentPage.value = 0
    initViewer(canvasImages.value[0])
  }
}

// Pagination
const goToPage = (page) => {
  if (page < 0 || page >= pageCount.value) return
  currentPage.value = page
  initViewer(canvasImages.value[page])
}

// Lifecycle
onMounted(loadSource)
watch(() => props.source, loadSource)
watch(
  () => props.overlays,
  () => nextTick(() => addOverlays()),
  { deep: true }
)
onBeforeUnmount(() => {
  if (viewer) viewer.destroy()
})
</script>

<style scoped>
.viewer-wrapper {
  display: flex;
  flex-direction: column;
}

.osd-container {
  width: 100%;
  height: 600px;
  background-color: #000;
}

.thumbnails {
  display: flex;
  overflow-x: auto;
  gap: 5px;
  margin-top: 10px;
}

.thumb {
  cursor: pointer;
  border: 2px solid transparent;
}

.thumb.active {
  border-color: yellow;
}

.thumb img {
  height: 100px;
  object-fit: contain;
}
</style>
