<template>
  <div>
    <div id="map" style="height: 60vh" class="custom-popup"></div>

    <ProgressSpinner
      v-if="isLoading"
      aria-label="Loading"
      style="width: 50px; height: 50px; position: relative; padding-left: 80%"
      strokeWidth="8"
      fill="transparent"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import moment from 'moment'
import Geohash from 'latlon-geohash'

// import 'leaflet/dist/leaflet.css'
// import 'leaflet.markercluster'

// import ProgressSpinner from 'primevue/progressspinner'

import * as L from 'leaflet'
import 'leaflet.markercluster'

import markerIconUrl from '/node_modules/leaflet/dist/images/marker-icon.png'
import markerIconRetinaUrl from '/node_modules/leaflet/dist/images/marker-icon-2x.png'
import markerShadowUrl from '/node_modules/leaflet/dist/images/marker-shadow.png'
// import { inferRuntimeType } from 'vue/compiler-sfc'

const emits = defineEmits(['searchDetial', 'resetView', 'search'])

const store = useStore()
const initialMap = ref(null)

const isLoading = ref(false)

let markers = ref()
// let radius = ref(8000)

onMounted(() => {
  initMap()

  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    // addSingleMarker()
    addSamlingarSinglemMarker()
  } else {
    isLoading.value = true
    // addSamlingarMarks()
    // drawPolygon()
    // addClusterMarkers()
  }
})

function initMap() {
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

  initialMap.value.on('zoomend', function (e) {
    onZoomChanged()
  })
  // L.Marker.prototype._animateZoom = function (opt) {
  //   if (!this._map) {
  //     return
  //   }
  //   const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
  //   this._setPos(pos)
  // }

  // L.circle.prototype._animateZoom = function (opt) {
  //   if (!this._map) {
  //     return
  //   }
  //   const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
  //   this._setPos(pos)
  // }
}

// watch(
//   () => store.getters['results'],
//   () => {
//     console.log('results changed')
//     initialMap.value.eachLayer((layer) => {
//       if (layer instanceof L.Marker) {
//         layer.remove()
//       } else if (layer instanceof L.Circle) {
//         layer.remove()
//       } else {
//         layer.remove()
//       }
//     })
//     resetMap()
//     addClusterMarkers()
//   }
// )

watch(
  () => store.getters['geoData'],
  () => {
    console.log('geoData changed')
    initialMap.value.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        layer.remove()
      } else if (layer instanceof L.Circle) {
        layer.remove()
      } else {
        layer.remove()
      }
    })
    resetMap()
    addSamlingarMarks()

    // drawPolygon()
  }
)

// watch(
//   () => isLoading,
//   (newValue, oldValue) => {
//     console.log('showloading changed...', isLoading)
//     isLoading.value = newValue
//   }
// )

function onZoomChanged() {
  const currentZoom = initialMap.value.getZoom()

  const isDetailView = store.getters['showDetail']
  const total = store.getters['totalGeoData']

  if (!isDetailView && total > 300000) {
    if (!isDetailView) {
      initialMap.value.eachLayer((layer) => {
        if (layer instanceof L.Marker) {
          layer.remove()
        } else if (layer instanceof L.Circle) {
          layer.remove()
        } else {
          layer.remove()
        }
      })
      resetMap()
      addSamlingarMarks()
    }
  }
}

function resetMap() {
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 1,
    maxZoom: 7,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Marker.prototype._animateZoom = function (opt) {
    if (!this._map) {
      return
    }
    const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
    this._setPos(pos)
  }

  L.circle.prototype._animateZoom = function (opt) {
    if (!this._map) {
      return
    }
    const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
    this._setPos(pos)
  }
}

function onCircleClick(marker, geohash) {
  console.log('onCircleClick', marker, geohash)
  // initialMap.value.removeLayer(marker)

  // markers.forEach(function (marker) {
  //   if (marker._id == geohash) {
  //     map.removeLayer(marker)
  //   }
  // else {
  //   new_markers.push(marker)
  // }
  // })
  // markers = new_markers
  // emits('resetView', geohash)
}

function onClickDetail(latitude, longitude) {
  console.log('onClickDetail', latitude, longitude)
  emits('searchDetial', latitude, longitude)
}

// From detail single marker
function displayDetail(id) {
  console.log('displayDetail', id)
  emits('searchDetial', id)
}

function addSamlingarSinglemMarker() {
  console.log('addSamlingarSinglemMarker')
  const record = store.getters['selectedResult']
  const {
    id,
    catalogNumber,
    collectionName,
    country,
    latitude,
    latitudeText,
    locality,
    longitude,
    longitudeText,
    species,
    state,
    startDate,
    txFullName
  } = record

  const scientificName = txFullName ? txFullName : species

  if (latitude && longitude) {
    const div = document.createElement('div')

    div.innerHTML = `<strong> Catalogue number: ${catalogNumber}  </strong>
      <br> <strong>Collection</strong>: ${collectionName}
      <br><strong>Scientific Name</strong>: ${scientificName}
      <br>
      <br><strong>Locality</strong>:<br> ${locality}, ${state}, ${country}
      <br>
      <br><strong>GPS-coordinate</strong>: <br>
      ${latitudeText} -- ${longitudeText}
      <br>
      <br><strong>Event date</strong>: ${startDate}
      <br>
      <br>`

    const button = document.createElement('button')
    button.innerHTML = 'More details'

    button.onclick = function () {
      displayDetail(id)
    }
    div.style.cssText = ' overflow-wrap: break-word;   '
    div.appendChild(button)
    const marker = new L.marker([latitude, longitude]).bindPopup(div)
    marker.addTo(initialMap.value)
  }
}

// var HTML = `<table  width=100%>
//     <tr>
//       <td align = left > <strong> Catalogue number:</strong></td>
//       <td align = left > ${catalogNumber} </td>
//     </tr>
//     <tr>
//       <td align = left > <strong> Scientific Name:</strong></td>
//       <td align = left > ${scientificName} </td>
//     </tr>
//     <tr>
//       <td align = left > <strong> Collection:</strong></td>
//       <td align = left > ${collectionName} </td>
//     </tr>
//     <tr>
//       <td v-align = top vertical-align=top> <strong> Locality:</strong></td>
//       <td align = left vertical-align=top> ${locality} </td>
//     </tr>
//     <tr>
//       <td align = left > <strong> Collection:</strong></td>
//       <td align = left > ${collectionName} </td>
//     </tr>
//   </table>`

// div.innerHTML = `<strong> Catalogue number: ${catalogNumber}  </strong>
// <br> <strong>Collection</strong>: ${collectionName}
// <br><strong>Scientific Name</strong>: ${scientificName}
// <br><strong>Locality</strong>:<br> ${locality},
// <br><strong>State/Province</strong>: ${state},
// <br><strong>Country</strong>: ${country}
// <br><strong>GPS-coordinate</strong>: ${latitudeText} -- ${longitudeText}
// <br><strong>Event date</strong>: ${startDate}
// <br>
// <br>`

function getRadius(d) {
  if (d === 1) {
    return 90000
  }
  if (d === 2) {
    return 70000
  }
  if (d === 3) {
    return 50000
  }
  if (d === 4) {
    return 40000
  }
  if (d === 5) {
    return 20000
  }
  if (d === 6) {
    return 12000
  }
  if (d === 7) {
    return 10000
  }
}
function addSamlingarMarks() {
  console.log('addSamlingarMarks')

  const geoArray = store.getters['geoData']
  markers = L.markerClusterGroup({
    spiderfyOnMaxZoom: false,
    chunkedLoading: true
  })

  const currentZoom = initialMap.value.getZoom()

  const radius = getRadius(currentZoom)

  geoArray.forEach((geo) => {
    const { count, val } = geo

    console.log('geohash', val.substring(2))

    const latlon = Geohash.decode(val.substring(2))
    console.log('latlon', latlon.lat, latlon.lon)

    const latitude = latlon.lat
    const longitude = latlon.lon

    if (count === 1) {
      const div = document.createElement('div')
      div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

      const button = document.createElement('button')
      button.innerHTML = 'More details'

      button.onclick = function () {
        displayDetail(latitude, longitude)
      }
      div.style.cssText = 'width: auto;  '
      div.appendChild(button)

      const marker = new L.marker([latitude, longitude]).bindPopup(div)

      markers.addLayer(marker)
    } else {
      const marker = L.circle([latlon.lat, latlon.lon], {
        id: val,
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5,
        radius
      })

      const div = document.createElement('div')
      div.innerHTML = `<br>Total occurrences: ${count}<br><br><br> `

      const button = document.createElement('button')
      button.innerHTML = 'Click to open for detail'

      button.onclick = function () {
        onCircleClick(marker, val)
      }
      div.appendChild(button)

      marker.bindPopup(div).addTo(initialMap.value)
    }
  })
}

function addSamlingarMarks1() {
  console.log('addSamlingarMarks')

  const geoArray = store.getters['geoData']
  const total = store.getters['totalGeoData']

  console.log('totalGeoData', total)

  markers = L.markerClusterGroup({
    spiderfyOnMaxZoom: false,
    chunkedLoading: true
  })
  // .on('click', function (a) {
  // console.log('marker ' + a.layer)
  // })

  const currentZoom = initialMap.value.getZoom()

  const radius = getRadius(currentZoom)

  console.log('onZoomChanged: current zoom', radius)

  geoArray.forEach((geo) => {
    const { count, latitude, longitude, geohash } = geo

    if (total <= 300000) {
      for (let i = 0; i < count; i++) {
        const div = document.createElement('div')
        div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

        const button = document.createElement('button')
        button.innerHTML = 'More details'

        button.onclick = function () {
          displayDetail(latitude, longitude)
        }
        div.style.cssText = 'width: auto;  '
        div.appendChild(button)

        const marker = new L.marker([latitude, longitude])
        //   .on('click', function (a) {
        //   console.log('marker ' + a.layer)
        // })
        // .bindPopup(div)

        // const marker = L.marker(L.latLng(parseFloat(item[latitude]), parseFloat(item[longitude])))

        markers.addLayer(marker)
      }
    } else {
      if (count === 1) {
        const div = document.createElement('div')
        div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

        const button = document.createElement('button')
        button.innerHTML = 'More details'

        button.onclick = function () {
          displayDetail(latitude, longitude)
        }
        div.style.cssText = 'width: auto;  '
        div.appendChild(button)

        const marker = new L.marker([latitude, longitude]).bindPopup(div)

        markers.addLayer(marker)
      } else {
        const marker = L.circle([latitude, longitude], {
          id: geohash,
          color: 'red',
          fillColor: '#f03',
          fillOpacity: 0.5,
          radius
        })

        const div = document.createElement('div')
        div.innerHTML = `<br>Total occurrences: ${count}<br><br><br> `

        const button = document.createElement('button')
        button.innerHTML = 'Click to open for detail'

        button.onclick = function () {
          onCircleClick(marker, geohash)
        }
        div.appendChild(button)

        marker.bindPopup(div).addTo(initialMap.value)

        // L.circle([latitude, longitude], {
        //   id: geohash,
        //   color: 'red',
        //   fillColor: '#f03',
        //   fillOpacity: 0.5,
        //   radius: 90000
        // })
        //   .bindPopup(div)
        //   .addTo(initialMap.value)
      }
    }
  })
  initialMap.value.addLayer(markers)

  isLoading.value = false

  // for (let i = 0; i < count; i++) {
  //   const div = document.createElement('div')
  //   div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

  //   const button = document.createElement('button')
  //   button.innerHTML = 'More details'

  //   button.onclick = function () {
  //     displayDetail(latitude, longitude)
  //   }
  //   div.style.cssText = 'width: auto;  '
  //   div.appendChild(button)

  //   const marker = new L.marker([latitude, longitude]).bindPopup(div)

  //   // const marker = L.marker(L.latLng(parseFloat(item[latitude]), parseFloat(item[longitude])))

  //   markers.addLayer(marker)
  // }
}

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

      const coordinates = array.toString()
      if (count === 1) {
        const div = document.createElement('div')
        div.innerHTML = `Coordinates: ${latitude}, ${longitude}<br><br>`

        const button = document.createElement('button')
        button.innerHTML = 'More details'

        button.onclick = function () {
          onClickDetail(coordinates)
        }
        div.style.cssText = 'width: auto;  '
        div.appendChild(button)

        const marker = new L.marker([latitude, longitude]).bindPopup(div)

        // const marker = L.marker(L.latLng(parseFloat(item[latitude]), parseFloat(item[longitude])))

        markers.addLayer(marker)
      } else {
        const div = document.createElement('div')
        div.innerHTML = `<br>Total occurrences: ${count}<br>
          Coordinates: ${latitude}, ${longitude}<br><br>`

        const button = document.createElement('button')
        button.innerHTML = 'More details'

        button.onclick = function () {
          onClick(coordinates, count)
        }
        div.appendChild(button)

        L.circle([array[0], array[1]], {
          color: 'red',
          fillColor: '#f03',
          fillOpacity: 0.5,
          radius: 60000
        })
          .bindPopup(div)
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

function drawPolygon() {
  console.log('drawPolygon')

  const polygon = L.polygon([
    [61.875, 22.5],
    [61.875, 33.75],
    [56.25, 33.75],
    [50.625, 33.7],
    [50.625, 22.5],
    [50.625, 11.25],
    [56.25, 11.25],
    [61.875, 11.25]
  ])
  polygon.addTo(initialMap.value)

  L.circle([51.508, -0.11], {
    color: 'red',
    fillColor: '#f03',
    fillOpacity: 0.5,
    radius: 60000
  }).addTo(initialMap.value)
}

function addGroupMark() {
  const records = store.getters['mapRecords']
  console.log('recoudslength', records.length)
  const markers = L.markerClusterGroup()

  records.forEach((record) => {
    const {
      collectionName,
      collectors,
      decimalLatitude,
      decimalLongitude,
      eventDate,
      raw_catalogNumber,
      scientificName
    } = record
    const date = moment(eventDate).format('yyyy-MM-DD h:mm:ss')
    // const each_marker = new L.marker([decimalLatitude, decimalLongitude])
    // const each_marker = L.marker(L.latLng(parseFloat(item[latitude]), parseFloat(item[longitude])))
    const each_marker = new L.marker([decimalLatitude, decimalLongitude]).bindPopup(
      `<strong> Catalogue number: ${raw_catalogNumber}  </strong>
      <br><strong>Collection</strong>: ${collectionName}
      <br><strong>ScientificName</strong>: ${scientificName}
      <br><strong>Collectors</strong>: ${collectors}
      <br><strong>Event date</strong>: ${date}
      <br><strong>Coordinates</strong>: ${decimalLatitude} -- ${decimalLongitude}`
    )
    markers.addLayer(each_marker)
  })
  initialMap.value.addLayer(markers)
}

function onClick(value, total) {
  console.log('onClick', value, total)
  emits('resetView', value, total)
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

.leaflet-popup-content-wrapper {
  padding: 10px;
  text-align: center;
  background-color: yellow !important;
}

.custom-popup .leaflet-popup-content-wrapper {
  background: #2c3e50;
  color: #fff;
  font-size: 16px;
  line-height: 24px;
}
.custom-popup .leaflet-popup-content-wrapper a {
  color: rgba(255, 255, 255, 0.5);
}
.custom-popup .leaflet-popup-tip-container {
  width: 300px;
  height: 15px;
}
.custom-popup .leaflet-popup-tip {
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  border-top: 15px solid #2c3e50;
}

.custom-popup .leaflet-popup-content-wrapper,
.custom-popup .leaflet-popup-tip {
  background-color: #f4913b;
}

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
