<template>
  <component :is="Component" />
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'

import Map from '../components/Map2.vue'
import LargeMap from '../components/NewMap.vue'

const store = useStore()

const Component = computed(() => (store.getters['totalGeoData'] >= 50000 ? LargeMap : Map))
</script>
