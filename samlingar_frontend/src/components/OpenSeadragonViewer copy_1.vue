<template>
  <div ref="osdContainer" class="osd-viewer"></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import OpenSeadragon from 'openseadragon'

const props = defineProps({
  /**
   * Can be:
   * - A raw image URL (e.g., "https://example.com/image.jpg")
   * - A IIIF image info.json URL (e.g., "https://example.com/iiif/imageID/info.json")
   * - Or a full OpenSeadragon tileSource object
   */
  source: {
    type: [String, Object],
    required: true
  },
  prefixUrl: {
    type: String,
    default: 'https://openseadragon.github.io/openseadragon/images/'
  },
  showNavigator: {
    type: Boolean,
    default: true
  },
  navigatorPosition: {
    type: String,
    default: 'BOTTOM_RIGHT'
  }
})

const osdContainer = ref(null)
let viewer = null

// 🧠 Detects if a string is a IIIF info.json URL
const isIiif = (src) => typeof src === 'string' && src.includes('/info.json')

// 🧩 Initialize viewer
const initViewer = (tileSource) => {
  if (viewer) {
    viewer.destroy()
    viewer = null
  }

  viewer = OpenSeadragon({
    element: osdContainer.value,
    prefixUrl: props.prefixUrl,
    showNavigator: props.showNavigator,
    navigatorPosition: props.navigatorPosition,
    tileSources: tileSource
  })
}

// ⚙️ Prepare source for OpenSeadragon
const loadSource = async () => {
  let tileSource

  if (typeof props.source === 'object') {
    // Directly a tileSource object
    tileSource = props.source
  } else if (isIiif(props.source)) {
    // IIIF: fetch info.json
    try {
      const response = await fetch(props.source)
      if (!response.ok) throw new Error('Failed to load IIIF info.json')
      tileSource = await response.json()
    } catch (err) {
      console.error('Error loading IIIF source:', err)
      return
    }
  } else {
    // Raw image
    tileSource = {
      type: 'image',
      url: props.source
    }
  }

  initViewer(tileSource)
}

// 🏁 Initialize on mount
onMounted(loadSource)

// 🔄 Reload when source changes
watch(
  () => props.source,
  () => {
    loadSource()
  }
)

// 🧹 Cleanup
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
}
</style>
