<template>
  <div v-if="isHeatMap">
    <large-map v-bind:entry="entryType" v-bind:from="from" />
  </div>
  <div v-else>
    <Map v-bind:entry="entryType" v-bind:from="from" />
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { entryType, previousRoute, currentRoute } from '@/router'

import Map from '../components/Map2.vue'
import LargeMap from '../components/NewMap.vue'

const store = useStore()

// let isLargeMap = ref(true)
let reloadMapData = ref(false)
const from = ref()

const isHeatMap = computed(() => {
  const total = store.getters['totalGeoData']
  console.log('is heat map', total >= 50000)
  return store.getters['totalGeoData'] >= 50000
})

// watch(
//   () => store.getters['totalRecords'],
//   () => {
//     const total = store.getters['totalRecords']
//     console.log('total', total)
//     isLargeMap.value = total > 50000
//   }
// )

// watch(
//   () => store.getters['resetMapData'],
//   () => {
//     console.log('resetMapData..')
//     reloadMapData.value = true
//     // const total = store.getters['totalGeoData'] > 50000

//     // if (total > 50000) {
//     //   reloadMapData.value = true
//     // }
//     //
//   }
// )

// onMounted(async () => {
//   console.log('switch map onMounted')

//   from.value = previousRoute.value?.fullPath
//   // const to = currentRoute.value?.fullPath
//   console.log('from', from)

//   const total = store.getters['totalGeoData']
//   console.log('total', total)
//   // isLargeMap.value = total >= 50000
// })
</script>
