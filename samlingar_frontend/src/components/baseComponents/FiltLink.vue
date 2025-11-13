<template>
  <div
    class="flex align-items-center justify-content-between divLink p-2 cursor-pointer"
    @click="doSearch"
    @mouseover="onHover"
    @mouseleave="unHover"
  >
    <!-- Left Button -->
    <Button text :label="$t(text)" :class="['text-btn', 'left-btn', { hover: isHover }]" />

    <!-- Right Button -->
    <Button text :class="['text-btn', 'right-btn', { hover: isHover }]">
      {{ total }}
    </Button>
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
.divLink {
  width: 100%;
  transition: background-color 0.3s ease;
}

/* Optional hover effect for the container */
.divLink:hover {
  background: #0e3a12;
}

/* PrimeVue Button styling */
.text-btn {
  color: #4a4949b6;
  text-decoration: underline;
  background: transparent;
  border: none;
  flex: 1; /* Makes both buttons share equal space */
  text-align: left;
}

/* When hovered */
.hover {
  color: #fff !important;
  background: #0e3a12 !important;
  text-decoration: none !important;
}

/* Keep underline off on hover */
.p-button-text:hover {
  text-decoration: none !important;
  background: transparent !important;
}

/* Space between the two buttons */
.text-btn + .text-btn {
  margin-left: 1rem;
}

/* Alignment control */
.left-btn {
  text-align: left;
  justify-content: flex-start;
}

.right-btn {
  text-align: right;
  justify-content: flex-end;
  padding-right: 5em;
}

/* Mobile-friendly adjustments */
@media (max-width: 768px) {
  .divLink {
    flex-wrap: nowrap; /* keep buttons on same line */
  }

  .text-btn {
    font-size: 0.7rem;
    padding: 0.5rem;
    flex: 1; /* each button takes half width */
  }

  .text-btn + .text-btn {
    margin-left: 0.5rem;
  }
}
</style>
