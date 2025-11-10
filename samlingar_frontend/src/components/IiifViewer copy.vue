<template>
  <div class="iiif-viewer">
    <div ref="viewer" class="osd-viewer"></div>

    <div class="thumbnail-gallery">
      <img
        v-for="(canvas, i) in canvases"
        :key="i"
        :src="getThumbnail(canvas)"
        class="thumbnail"
        @click="loadCanvas(i)"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import OpenSeadragon from 'openseadragon'

const viewer = ref(null)
const canvases = ref([])
let osdViewer = null

const iiifManifestUrl = 'https://assets.nrm.se/manifest/NHRS-HEMI000000010.json' // IIIF v3 example

onMounted(async () => {
  const manifest = await fetch(iiifManifestUrl).then((r) => r.json())
  canvases.value = manifest.items || []

  osdViewer = OpenSeadragon({
    element: viewer.value,
    prefixUrl: 'https://openseadragon.github.io/openseadragon/images/'
  })

  loadCanvas(0)
})

function loadCanvas(index) {
  const canvas = canvases.value[index]
  const body = canvas?.items?.[0]?.items?.[0]?.body
  const imageService = body?.service?.[0]?.['@id'] || body?.service?.[0]?.id

  if (!imageService) return
  osdViewer.open(`${imageService}/info.json`)
}

function getThumbnail(canvas) {
  console.log('canvas', canvas)
  const thumb =
    canvas.thumbnail?.[0]?.id ||
    canvas.thumbnail?.[0]?.['@id'] ||
    canvas.items?.[0]?.items?.[0]?.body?.id

  console.log('thumb', thumb)
  return thumb ? `${thumb}/full/!200,200/0/default.jpg` : ''
  // return thumb ? thumb + '/info.json' : ''
}

onBeforeUnmount(() => osdViewer?.destroy())
</script>

<style scoped>
.iiif-viewer {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}
.osd-viewer {
  width: 800px;
  height: 600px;
  border: 1px solid #ccc;
}
.thumbnail-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: center;
}
.thumbnail {
  width: 100px;
  height: auto;
  border: 1px solid #999;
  cursor: pointer;
  transition: transform 0.2s;
}
.thumbnail:hover {
  transform: scale(1.05);
  border-color: #007bff;
}
</style>
