<template>
  <div id="container" class="container container--fluid">
    <h2>{{ $t('about.header') }}</h2>
    <br />
    <h3>{{ $t('about.header2') }}</h3>

    <br />
    <template v-for="(collection, index) in collections" :key="index">
      {{ $t('collectionLabel.' + collection) }}<br />
    </template>
  </div>
</template>
<script setup>
import { computed } from 'vue'

const collections = computed(() => {
  const collectionList = import.meta.env.VITE_SUPPORTED_DATASET
  return collectionList.split(':')
})
</script>
