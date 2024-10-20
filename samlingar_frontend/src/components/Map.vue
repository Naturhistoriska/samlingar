<template>
  <div>
    <div id="map" style="height: 60vh"></div>
    <!-- <div v-if="isLoading"> -->
    <!-- <ProgressSpinner style="width: 50px; height: 50px" animationDuration=".5s" /> -->
    <!-- </div> -->
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
// import moment from 'moment'

// import 'leaflet/dist/leaflet.css'
// import 'leaflet.markercluster'

// import ProgressSpinner from 'primevue/progressspinner'

import * as L from 'leaflet'
import 'leaflet.markercluster'
import { inferRuntimeType } from 'vue/compiler-sfc'

const emits = defineEmits(['resetView', 'search'])

const store = useStore()
const initialMap = ref(null)

const isLoading = ref(true)

onMounted(() => {
  initMap()
  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    addSingleMarker()
  } else {
    addClusterMarkers()
    // addGroupMark()
  }
})

function initMap() {
  initialMap.value = L.map('map').setView([59.0, 15.0], 6)
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 3,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Marker.prototype._animateZoom = function (opt) {
    if (!this._map) {
      return
    }
    const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
    this._setPos(pos)
  }
}

watch(
  () => store.getters['results'],
  () => {
    initialMap.value.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        layer.remove()
      } else if (layer instanceof L.Circle) {
        layer.remove()
      }
    })
    addClusterMarkers()
    // addGroupMark()
  }
)

watch(
  () => isLoading,
  (newValue, oldValue) => {
    console.log('showloading changed...', isLoading)
    isLoading.value = newValue
  }
)

function addClusterMarkers() {
  console.log('addClusterMarkers')
  // const isAdvanceSearch = store.getters['isAdvanceSearch']
  const latLongArray = store.getters['latLong']

  // L.marker([51.5, -0.09]).addTo(initialMap.value)

  // const circle = L.circle([51.508, -0.11], {
  //   color: 'red',
  //   fillColor: '#f03',
  //   fillOpacity: 0.5,
  //   radius: 1000
  // }).addTo(initialMap.value)
  const markers = L.markerClusterGroup()

  latLongArray.forEach((lat_long) => {
    const latLong = lat_long.label

    if (latLong !== 'Not supplied') {
      const array = latLong.split(',')

      const count = lat_long.count
      const latitude = array[0]
      const longitude = array[1]

      // for (let i = 0; i < count; i++) {
      //   const each_marker = new L.marker([latitude, longitude])
      //   markers.addLayer(each_marker)
      // }

      if (count === 1) {
        const each_marker = new L.marker([latitude, longitude])
        markers.addLayer(each_marker)
        // const div = document.createElement('div')
        // div.innerHTML = `<br>Total occurrences: ${count}<br>
        //   Coordinates: ${latitude}, ${longitude}<br><br>`

        // const each_marker = new L.marker([latitude, longitude])
        // markers.addLayer(each_marker)
        // for (let i = 0; i < count; i++) {
        //   const each_marker = new L.marker([latitude, longitude])
        //   markers.addLayer(each_marker)
        // }

        // L.marker([latitude, longitude]).addTo(initialMap.value)
      } else {
        // const color = getColor
        // const r = getRadious(count)
        const div = document.createElement('div')
        div.innerHTML = `<br>Total occurrences: ${count}<br>
          Coordinates: ${latitude}, ${longitude}<br><br>`

        const button = document.createElement('button')
        button.innerHTML = 'More details'

        button.onclick = function () {
          onClick()
        }

        const radious = count * 20

        div.appendChild(button)

        L.circle([array[0], array[1]], {
          color: 'red',
          fillColor: '#f03',
          fillOpacity: 0.5,
          radius: 60000
        })
          .bindPopup(div)
          // .bindPopup(
          //   `Total occurrences: ${count} <br>
          // Coordinates: ${latitude}, ${longitude}
          // <br><br><center><button id="getres"  ">More details</button>
          // `
          // )
          // .on('click', onClick)
          .addTo(initialMap.value)
      }
    }
  })
  initialMap.value.addLayer(markers)

  // const latLong = lat_Long.label
  // console.log('latLong: ', latLong)

  // const circle = L.circle([51.508, -0.11], {
  //   color: 'red',
  //   fillColor: '#f03',
  //   fillOpacity: 0.5,
  //   radius: 500
  // }).addTo(map);

  // isLoading.value = true
  // if (isAdvanceSearch) {
  //   // do advance search
  // } else {
  //   // emits('search')
  // }

  // const records = store.getters['mapRecords']
  // const markers = L.markerClusterGroup()

  // const layerGroup = new L.LayerGroup()

  // records.forEach((record) => {
  //   const {
  //     collectionName,
  //     collectors,
  //     decimalLatitude,
  //     decimalLongitude,
  //     eventDate,
  //     raw_catalogNumber,
  //     scientificName
  //   } = record

  // const date = moment(eventDate).format('yyyy-MM-DD h:mm:ss')
  // const each_marker = new L.marker([decimalLatitude, decimalLongitude]).bindPopup(
  //   `<strong> Catalogue number: ${raw_catalogNumber}  </strong>
  //     <br> Collection: ${collectionName}
  //     <br>ScientificName: ${scientificName}
  //     <br>Collectors: ${collectors}
  //     <br>Event date: ${date}`
  // )
  // markers.addLayer(each_marker)

  // if (decimalLatitude !== undefined && decimalLongitude !== undefined) {
  // const each_marker = new L.marker([decimalLatitude, decimalLongitude]).addTo(layerGroup)
  // const each_marker = new L.marker([decimalLatitude, decimalLongitude]).bindPopup(
  // `<strong> Catalogue number: ${raw_catalogNumber}  </strong>
  // <br> Collection: ${collectionName}
  // <br>ScientificName: ${scientificName}
  // <br>Collectors: ${collectors}
  // <br>Event date: ${date}`
  // )

  // markers.addLayer(layerGroup)
  // }
  // })
  // initialMap.value.addLayer(markers)

  // sleep(1000).then(() => {
  //   isLoading.value = false
  // })
  // isLoading.value = false
}

function addClusterMarkers1() {
  const latLongArray = store.getters['latLong']

  console.log('length..', latLongArray.length)

  const markers = L.markerClusterGroup()

  const layerGroup = new L.LayerGroup()

  latLongArray.forEach((lat_long) => {
    const latLong = lat_long.label

    if (latLong !== 'Not supplied') {
      const array = latLong.split(',')

      const count = lat_long.count
      const latitude = array[0]
      const longitude = array[1]

      const div = document.createElement('div')
      div.innerHTML = `<br>Total occurrences: ${count}<br>
          Coordinates: ${latitude}, ${longitude}<br><br>`

      const each_marker = new L.marker([latitude, longitude])
      markers.addLayer(each_marker)
    }
  })
  initialMap.value.addLayer(markers)
}

// function sleep(ms) {
//   return new Promise((resolve) => setTimeout(resolve, ms))
// }

//// Adding some color
// function getColor(d) {
// return d > 1400
// ? '#8c2d04'
// : d > 700
// ? '#cc4c02'
// : d > 400
// ? '#ec7014'
// : d > 100
// ? '#fe9929'
// : d > 50
// ? '#fec44f'
// : d > 25
// ? '#fee391'
// : '#ffffd4'
// }

// function getRadious(d) {
//   return d > 1400
//     ? 40000
//     : d > 700
//       ? 35000
//       : d > 400
//         ? 30000
//         : d > 100
//           ? 25000
//           : d > 50
//             ? 20000
//             : d > 25
//               ? 15000
//               : 10000
// }

function onClick() {
  console.log('onClick.......')
  emits('resetView')
}

function addSingleMarker() {
  const record = store.getters['selectedResult']

  const { attribution, classification, event, location } = record.processed

  const catalogNumber = record.raw.occurrence.catalogNumber

  const collectionName = attribution.collectionName

  const latitude = location.decimalLatitude
  const longitude = location.decimalLongitude
  const locality = location.locality
  const country = location.country
  const stateProvince = location.stateProvince
  const eventDate = event.eventDate

  const scientificName = classification.scientificName

  const div = document.createElement('div')
  div.innerHTML = `<strong> Catalogue number: ${catalogNumber}  </strong>
      <br> <strong>Collection</strong>: ${collectionName}
      <br><strong>Scientific Name</strong>: ${scientificName}
      <br><strong>Locality</strong>: ${locality},
      <br><strong>State/Province</strong>: ${stateProvince},
      <br><strong>Country</strong>: ${country}
      <br><strong>GPS-coordinate</strong>: ${latitude} -- ${longitude}
      <br><strong>Event date</strong>: ${eventDate}
      <br>
      <br>`

  const button = document.createElement('button')
  button.innerHTML = 'More details'

  button.onclick = function () {
    onClick()
  }
  div.style.cssText = 'width: auto;  '
  div.appendChild(button)
  const marker = new L.marker([latitude, longitude]).bindPopup(div)
  marker.addTo(initialMap.value)
}
</script>
<style scoped>
/* @import 'leaflet/dist/leaflet.css';
#leaflet-map {
  height: 100vh;
  width: 100%;
  overflow: hidden;
} */

.p-progressspinner {
  position: fixed;
  z-index: 999;
  height: 2em;
  width: 2em;
  overflow: show;
  margin: auto;
  top: -200px;
  left: 0;
  bottom: 0;
  right: 0;
}
</style>
