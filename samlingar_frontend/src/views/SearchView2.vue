<template>
  <div>
    <div class="grid">
      <div class="col-4" no-gutters>
        <search-records />
      </div>
      <div class="col-8" no-gutters>
        <Map />
      </div>
    </div>
  </div>
</template>
<script setup>
import Map from '../components/Map.vue'
import SearchRecords from '../components/SearchRecords.vue'

function handleSearch(hasImage, hasMap, start, numPerPage) {}
</script>
