<template>
  <l-map ref="mapRef" style="height: 100vh" :zoom="zoom" :center="center">
    <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
  </l-map>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet'
import L from 'leaflet'
import 'leaflet.markercluster'

const mapRef = ref(null)
const zoom = ref(5)
const center = ref([0, 0])
const clusterGroup = ref(null)

// Fetch data within map bounds
const fetchPointsInView = async (bounds) => {
  const bbox = [
    bounds.getSouthWest().lng,
    bounds.getSouthWest().lat,
    bounds.getNorthEast().lng,
    bounds.getNorthEast().lat
  ].join(',')

  console.log(bbox)

  const res = await fetch(`/api/geojson?bbox=${bbox}`)
  return await res.json()
}

const renderMarkers = (geojson) => {
  if (!clusterGroup.value) return

  clusterGroup.value.clearLayers()

  geojson.features.forEach((feature) => {
    const [lng, lat] = feature.geometry.coordinates
    const marker = L.marker([lat, lng])
    marker.bindPopup(feature.properties.name)
    clusterGroup.value.addLayer(marker)
  })
}

onMounted(async () => {
  const map = mapRef.value.leafletObject
  clusterGroup.value = L.markerClusterGroup().addTo(map)

  // Initial fetch
  const initialData = await fetchPointsInView(map.getBounds())
  renderMarkers(initialData)

  map.on('moveend', async () => {
    const data = await fetchPointsInView(map.getBounds())
    renderMarkers(data)
  })
})
</script>
