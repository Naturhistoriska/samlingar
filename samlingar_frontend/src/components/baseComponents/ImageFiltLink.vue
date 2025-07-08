<template>
  <div
    class="grid collectionDivLink"
    @click="searchCollection"
    :style="group === 'geo' ? 'pointer-events: none; opacity: 0.5' : ''"
  >
    <div class="col-4">
      <Image :src="imageSource" alt="Image" width="180" />
    </div>
    <div class="col-8 collectionTextLink">
      <div class="grid">
        <Button text>
          <small>{{ $t(text) }}</small>
        </Button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const props = defineProps(['group', 'imageSource', 'text'])

const emits = defineEmits(['searchCollection'])

const isDisabled = ref(true)

function searchCollection() {
  emits('searchCollection')
}
</script>
<style scoped>
.p-button-text {
  font-size: 16px;
  color: #4a4949b6;
  text-decoration: underline;
}

.p-button-text:hover {
  font-size: 16px;
  color: #b3b0b0 !important;
  text-decoration: none !important;
  background: transparent !important;
}

.collectionDivLink {
  min-width: 100%;
  cursor: pointer;
}
.collectionTextLink {
  float: left;
  padding-top: 50px;
}
</style>
