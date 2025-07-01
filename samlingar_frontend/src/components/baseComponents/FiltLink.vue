<template>
  <div class="grid divLink" @click="doSearch" @mouseover="onHover" @mouseleave="unHover">
    <div class="col-6" no-gutters>
      <Button text :label="$t(text)" :class="{ hover: isHover }" />
    </div>
    <div class="col-2">
      <Button
        v-if="loading"
        text
        icon="pi pi-spin pi-spinner"
        aria-label="loading"
        title="loading"
      />
    </div>
    <div class="col-4">
      <Button text :class="{ hover: isHover }">
        {{ total }}
      </Button>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'

const props = defineProps(['loading', 'text', 'total'])
const emits = defineEmits(['doSearch'])

const isHover = ref(false)

function onHover() {
  isHover.value = true
}

function unHover() {
  isHover.value = false
}

function doSearch() {
  emits('doSearch')
}
</script>
<style scoped>
.hover {
  color: #fff !important;
  text-decoration: none !important;
}

.p-button-text {
  color: #4a4949b6;
  text-decoration: underline;
}

.p-button-text:hover {
  /* color: var(--p-emerald-500) !important; */
  /* color: #fff !important; */
  text-decoration: none !important;
  background: transparent !important;
}

.divLink {
  min-width: 100% !important;
  cursor: pointer !important;
}

.divLink:hover {
  background: #0e3a12;
}
</style>
