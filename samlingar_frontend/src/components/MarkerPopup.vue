<script setup>
defineProps({
  markers: Array
})
</script>

<template>
  <div class="p-2">
    <h4 class="font-bold mb-2">Markers at same location</h4>
    <ul class="list-disc pl-4">
      <li v-for="(m, i) in markers" :key="i">Marker #{{ i + 1 }}</li>
    </ul>
  </div>
</template>
