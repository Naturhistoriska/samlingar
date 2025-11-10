<template>
  <div
    class="flex align-items-center justify-content-between divLink p-2 cursor-pointer"
    @click="doSearch"
    @mouseover="onHover"
    @mouseleave="unHover"
  >
    <div class="col-12 md:col-8 mb-2 md:mb-0">
      <Button text :label="$t(text)" :class="{ hover: isHover }" />
    </div>

    <div class="col-12 md:col-8 mb-2 md:mb-0">
      <Button text :class="{ hover: isHover }">
        {{ total }}
      </Button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps(['text', 'total'])
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
  text-decoration: none !important;
  background: transparent !important;
}

/* Optional: Add some hover effects */
.divLink .hover {
  background: #0e3a12;
  transition: background-color 0.3s ease;
}

/* Mobile-first: Stack buttons vertically */
@media (max-width: 768px) {
  .divLink .col-12 {
    padding-left: 0;
    padding-right: 0;
  }

  /* Make buttons take full width on mobile */
  .divLink .w-full {
    width: 100%;
  }
}
</style>
