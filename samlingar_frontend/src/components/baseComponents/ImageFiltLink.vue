<template>
  <div
    class="grid collectionDivLink p-2 align-items-center cursor-pointer"
    @click="searchCollection"
  >
    <!-- Image section -->
    <div class="col-12 md:col-4 text-center mb-3 md:mb-0">
      <Image :src="imageSource" alt="Image" width="100%" class="collection-image" />
    </div>

    <!-- Text/Button section -->
    <div class="col-12 md:col-8 collectionTextLink text-center md:text-left">
      <Button text class="p-button-text w-full md:w-auto">
        {{ $t(text) }}
      </Button>
    </div>
  </div>
</template>
<script setup>
const props = defineProps(['imageSource', 'text'])

const emits = defineEmits(['searchCollection'])

function searchCollection() {
  emits('searchCollection')
}
</script>
<style scoped>
.p-button-text {
  font-size: 1.1rem;
  color: #000000e2;
  text-decoration: underline;
}

.p-button-text:hover {
  font-size: 1.2em;
  color: #b3b0b0 !important;
  text-decoration: none !important;
  background: transparent !important;
}

.collectionDivLink {
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}
.collectionDivLink:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.collection-image img {
  width: 100%;
  max-width: 250px; /* default desktop size */
  border-radius: 8px;
  object-fit: cover;
}

/* Mobile-first spacing and alignment */
@media (max-width: 768px) {
  .p-button-text {
    font-size: 0.675rem;
  }
}
</style>
