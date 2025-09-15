<template>
  <div v-if="isLargeMap">
    <large-map />
  </div>
  <div v-else>
    <Map v-bind:entry="entryType" v-bind:from="from" />
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { entryType, previousRoute, currentRoute } from '@/router'

import Map from '../components/Map2.vue'
import LargeMap from '../components/NewMap.vue'

const store = useStore()

let isLargeMap = ref(true)
const from = ref()

watch(
  () => store.getters['totalRecords'],
  () => {
    const total = store.getters['totalRecords']
    console.log('total', total)
    isLargeMap.value = total > 50000
  }
)

onMounted(async () => {
  console.log('entry', entryType.value, previousRoute.value)

  from.value = previousRoute.value?.fullPath
  const to = currentRoute.value?.fullPath

  console.log('from', from)

  const total = store.getters['totalRecords']
  console.log('total', total)
  isLargeMap.value = total > 50000
})
</script>
