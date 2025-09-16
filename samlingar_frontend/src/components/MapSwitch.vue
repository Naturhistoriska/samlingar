<template>
  <div v-if="isHeatMap">
    <large-map />
  </div>
  <div v-else>
    <Map v-bind:entry="entryType" v-bind:from="from" v-bind:reloadData="reloadMapData" />
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { entryType, previousRoute, currentRoute } from '@/router'

import Map from '../components/Map2.vue'
import LargeMap from '../components/NewMap.vue'

const store = useStore()

let isLargeMap = ref(true)
let reloadMapData = ref(false)
const from = ref()

const isHeatMap = computed(() => {
  return store.getters['totalRecords'] > 50000
})

// watch(
//   () => store.getters['totalRecords'],
//   () => {
//     const total = store.getters['totalRecords']
//     console.log('total', total)
//     isLargeMap.value = total > 50000
//   }
// )

watch(
  () => store.getters['searchParams'],
  () => {
    console.log('map data changed..')
    const total = store.getters['totalRecords'] > 50000

    if (total > 50000) {
      reloadMapData.value = true
    }
    //
  }
)

onMounted(async () => {
  console.log('entry', entryType.value, previousRoute.value)

  from.value = previousRoute.value?.fullPath
  // const to = currentRoute.value?.fullPath

  console.log('from', from)

  const total = store.getters['totalRecords']
  console.log('total', total)
  isLargeMap.value = total > 50000
})
</script>
