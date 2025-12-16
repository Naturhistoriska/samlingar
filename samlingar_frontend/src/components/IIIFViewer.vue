<template>
  <iframe :src="iiifManifestUrl" width="100%" height="600" frameborder="0" allowfullscreen></iframe>
</template>

<script setup>
import { computed, nextTick, ref, onMounted, onBeforeUnmount } from 'vue'

const iiifManifestUrl = 'https://assets.nrm.se/viewer/uv.html/NHRS-TOBI000002720'
const currentLabel = ref('')
</script>
