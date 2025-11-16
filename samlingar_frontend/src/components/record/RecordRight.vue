<template>
  <div class="col-12 md:col-5">
    <single-map />
    <image-thumbnails v-if="showThumb" />
  </div>
</template>

<script setup>
import SingleMap from '../SingleMap.vue'
import ImageThumbnails from '../ImageThumbnails.vue'

defineProps({
  showThumb: Boolean
})
</script>
