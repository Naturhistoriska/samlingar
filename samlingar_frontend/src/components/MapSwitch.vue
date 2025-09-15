<template>
  <div v-if="isLargeMap">
    <large-map />
  </div>
  <div v-else>
    <Map />
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useStore } from 'vuex'

import Map from '../components/Map2.vue'
import LargeMap from '../components/NewMap.vue'

const store = useStore()

let isLargeMap = ref(true)

watch(
  () => store.getters['totalRecords'],
  () => {
    const total = store.getters['totalRecords']
    console.log('total', total)
    isLargeMap.value = total > 50000
  }
)
</script>
