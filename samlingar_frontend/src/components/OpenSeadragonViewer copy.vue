<template>
  <div>
    <div ref="osdContainer" class="osd-viewer"></div>
    <div v-if="pageCount > 1" class="pagination">
      <button @click="prevPage" :disabled="currentPage === 0">Prev</button>
      <span>Page {{ currentPage + 1 }} / {{ pageCount }}</span>
      <button @click="nextPage" :disabled="currentPage === pageCount - 1">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import OpenSeadragon from 'openseadragon'

const props = defineProps({
  source: { type: String, required: true }, // raw image, IIIF URL, or manifest URL
  rawsource: { type: Array, required: true }, // raw image, IIIF URL, or manifest URL
  type: { type: String, required: true },
  // currentSource: { type: Object, required: true },
  overlays: { type: Array, default: () => [] }, // optional overlays
  prefixUrl: { type: String, default: 'https://openseadragon.github.io/openseadragon/images/' },
  showNavigator: { type: Boolean, default: true },
  navigatorPosition: { type: String, default: 'BOTTOM_RIGHT' }
})

const osdContainer = ref(null)
let viewer = null

// Manifest and page tracking
const manifestData = ref(null)
const canvasImages = ref([])
const currentPage = ref(0)
const pageCount = ref(1)

// Helper functions
// const isIiifInfoJson = (url) => url.includes('/info.json')
const isIiifImageUrl = (url) => url.match(/\/full\/.*\.(jpg|png|tif|webp)$/)
// const isManifest = (url) => url.endsWith('manifest.json') || url.includes('/manifest')
const isManifest = (imageType) => imageType === 'iiif'

// Initialize viewer
const initViewer = (tileSource) => {
  console.log('tileSource', tileSource)
  if (viewer) {
    viewer.destroy()
    viewer = null
  }

  viewer = OpenSeadragon({
    element: osdContainer.value,
    prefixUrl: props.prefixUrl,
    showNavigator: true,
    navigatorPosition: 'BOTTOM_RIGHT',
    // showNavigator: props.showNavigator,
    // navigatorPosition: props.navigatorPosition,
    tileSources: tileSource
  })

  viewer.addOnceHandler('open', () => {
    addOverlays()
  })
}

// Add overlays
const addOverlays = () => {
  if (!viewer) return
  viewer.clearOverlays()

  props.overlays.forEach((overlay) => {
    const el = document.createElement('div')
    el.style.position = 'absolute'
    el.style.background = overlay.color || 'red'
    el.style.width = overlay.width + 'px' || '20px'
    el.style.height = overlay.height + 'px' || '20px'
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
    viewer.addOverlay({
      element: el,
      location: overlay.location
    })
  })
}

// Load tileSource from raw image, IIIF URL, or manifest
const loadSource = async () => {
  console.log('isIIIF', props.source)
  let tileSource

  // if (isManifest(props.source)) {
  if (isManifest(props.type)) {
    // Load IIIF Manifest
    try {
      const res = await fetch(props.source)
      manifestData.value = await res.json()
      canvasImages.value = manifestData.value.items.map((canvas) => {
        const annotation = canvas.items[0].items[0].body
        if (annotation.service) return annotation.service[0].id + '/info.json'
        return annotation.id
      })
      pageCount.value = canvasImages.value.length
      currentPage.value = 0
      tileSource = canvasImages.value[0]
    } catch (err) {
      console.error('Error loading manifest:', err)
      return
    }
  } else {
    // Raw image
    console.log('here....')
    const count = props.rawsource.length
    console.log('count : ', count)

    canvasImages.value = props.rawsource
    tileSource = { type: 'image', url: props.rawsource[0] }
    // tileSource = { type: 'image', url: props.source }
    pageCount.value = count
    currentPage.value = 0
  }

  initViewer(tileSource)
}

// Pagination
const goToPage = (page) => {
  if (page < 0 || page >= pageCount.value) return
  currentPage.value = page

  console.log('canvasImages.value', canvasImages.value)

  const canvasImage = canvasImages.value[page]
  let tileSource
  if (isManifest(props.type)) {
    tileSource = canvasImage
  } else {
    tileSource = { type: 'image', url: canvasImage }
  }

  initViewer(tileSource)
}
const nextPage = () => goToPage(currentPage.value + 1)
const prevPage = () => goToPage(currentPage.value - 1)

// Lifecycle
// onMounted(loadSource)

onMounted(() => {
  console.log('imageType', props.type)
  loadSource
})

watch(() => props.type, loadSource)
watch(
  () => props.overlays,
  () => {
    nextTick(() => addOverlays())
  },
  { deep: true }
)

onBeforeUnmount(() => {
  if (viewer) {
    viewer.destroy()
    viewer = null
  }
})
</script>

<style scoped>
.osd-viewer {
  width: 100%;
  height: 600px;
  background-color: #000;
  margin-bottom: 10px;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
