<template>
  <component :is="Component" />
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'

import Map from './Map.vue'
import LargeMap from './LargeMap.vue'

const store = useStore()

const Component = computed(() => (store.getters['totalGeoData'] >= 50000 ? LargeMap : Map))
</script>
