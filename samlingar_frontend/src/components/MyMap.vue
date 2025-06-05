<template>
  <div id="myMap">
    <div id="map" style="height: 60vh" class="custom-popup"></div>
  </div>
</template>
<script setup>
import { ref, onMounted, onUpdated, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

import * as L from 'leaflet'
import 'leaflet.markercluster'

import markerIconUrl from '/node_modules/leaflet/dist/images/marker-icon.png'
import markerIconRetinaUrl from '/node_modules/leaflet/dist/images/marker-icon-2x.png'
import markerShadowUrl from '/node_modules/leaflet/dist/images/marker-shadow.png'

const store = useStore()
const initialMap = ref(null)

const router = useRouter()

const isMapFetch = ref(false)

onMounted(() => {
  console.log('onMounted')
  initMap()

  addClusterMarkers()
})

watch(
  () => store.getters['geoData'],
  (newValue, oldValue) => {
    console.log('geoData')
    addClusterMarkers()
  }
)

// watch(
// () => router.currentRoute.value.name,
// () => {
// const currentRouteName = router.currentRoute.value.name
// console.log('currentRouteName', currentRouteName)
// }
// )
//
// watch(() => isMapFetch, fetchData, { immediate: true })
// onUnmounted(() => console.log('onUnmounted'))

function addClusterMarkers() {
  console.log('addClusterMarkers')
  const latLongArray = store.getters['geoData']
  console.log('geodata', latLongArray)

  const markers = L.markerClusterGroup()

  latLongArray.forEach((lat_long) => {
    const latLong = lat_long.val
    const count = lat_long.count

    const array = JSON.parse('[' + latLong + ']')

    const latitude = array[0]
    const longitude = array[1]

    for (let i = 0; i < count; i++) {
      // const div = document.createElement('div')
      // div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

      // const button = document.createElement('button')
      // button.innerHTML = 'More details'

      // button.onclick = function () {
      //   displayDetail(latitude, longitude)
      // }
      // div.style.cssText = 'width: auto;  '
      // div.appendChild(button)

      // const marker = new L.marker([latLong])
      const marker = new L.marker([latitude, longitude])
      // .bindPopup(div)

      // const marker = L.marker(L.latLng(parseFloat(item[latitude]), parseFloat(item[longitude])))

      markers.addLayer(marker)
    }

    // console.log('lat lon', latLong, count)

    // if (latLong !== 'Not supplied') {
    //   const array = latLong.split(',')

    //   const latitude = array[0]
    //   const longitude = array[1]
    //   if (count === 1) {
    //     const div = document.createElement('div')
    //     div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

    //     const button = document.createElement('button')
    //     button.innerHTML = 'More details'

    //     button.onclick = function () {
    //       onClickDetail(coordinates)
    //     }
    //     div.style.cssText = 'width: auto;  '
    //     div.appendChild(button)

    //     const marker = new L.marker([latitude, longitude]).bindPopup(div)

    //     markers.addLayer(marker)
    //   } else {
    //     const div = document.createElement('div')
    //     div.innerHTML = `<br>Total occurrences: ${count}<br>
    //       Coordinates: ${latitude}, ${longitude}<br><br>`

    //     const button = document.createElement('button')
    //     button.innerHTML = 'More details'

    //     button.onclick = function () {
    //       onClick(coordinates, count)
    //     }
    //     div.appendChild(button)

    //     L.circle([array[0], array[1]], {
    //       color: 'red',
    //       fillColor: '#f03',
    //       fillOpacity: 0.5,
    //       radius: 60000
    //     })
    //       .bindPopup(div)
    //       .addTo(initialMap.value)
    //   }
    // }

    // if (latLong !== 'Not supplied') {
    //   const array = latLong.split(',')

    //   const count = lat_long.count
    //   const latitude = array[0]
    //   const longitude = array[1]

    //   const coordinates = array.toString()
    //   if (count === 1) {
    //     const div = document.createElement('div')
    //     div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

    //     const button = document.createElement('button')
    //     button.innerHTML = 'More details'

    //     button.onclick = function () {
    //       onClickDetail(coordinates)
    //     }
    //     div.style.cssText = 'width: auto;  '
    //     div.appendChild(button)

    //     const marker = new L.marker([latitude, longitude]).bindPopup(div)

    //     // const marker = L.marker(L.latLng(parseFloat(item[latitude]), parseFloat(item[longitude])))

    //     markers.addLayer(marker)
    //   } else {
    //     const div = document.createElement('div')
    //     div.innerHTML = `<br>Total occurrences: ${count}<br>
    //       Coordinates: ${latitude}, ${longitude}<br><br>`

    //     const button = document.createElement('button')
    //     button.innerHTML = 'More details'

    //     button.onclick = function () {
    //       onClick(coordinates, count)
    //     }
    //     div.appendChild(button)

    //     L.circle([array[0], array[1]], {
    //       color: 'red',
    //       fillColor: '#f03',
    //       fillOpacity: 0.5,
    //       radius: 60000
    //     })
    //       .bindPopup(div)
    //       .addTo(initialMap.value)
    //   }
    // }
  })
  initialMap.value.addLayer(markers)
}

function initMap() {
  console.log('initmap')
  initialMap.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView([59.0, 15.0], 6)
    .setZoom(3)
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 1,
    maxZoom: 7,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Icon.Default.prototype.options.iconUrl = markerIconUrl
  L.Icon.Default.prototype.options.iconRetinaUrl = markerIconRetinaUrl
  L.Icon.Default.prototype.options.shadowUrl = markerShadowUrl
  L.Icon.Default.imagePath = '' // necessary to avoid Leaflet adds some prefix to image path.

  isMapFetch.value = true

  // initialMap.value.on('zoomend', function (e) {
  //   // onZoomChanged()
  // })
}
</script>
