<template>
  <div class="grid">
    <ul>
      <li v-for="record in records">
        {{ record.associatedMedia }}
        <img
          v-lazy="{
            src: 'https://media-service.nrm.se/images?id=Ar0011433_1_thumb.jpg&dataset=pal'
          }"
        />
      </li>
    </ul>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
const store = useStore()

onMounted(() => {
  console.log('media onMounted')
})

const records = computed(() => {
  return store.getters['results']
})
</script>
<style scoped></style>
