<template>
  <div class="grid collectionDivLink" @click="searchCollection">
    <div class="col-4">
      <Image :src="imageSource" alt="Image" width="180" />
    </div>
    <div class="col-8 collectionTextLink">
      <div class="grid">
        <Button link>
          <small>{{ $t(text) }}</small>
        </Button>
      </div>
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
.collectionDivLink {
  min-width: 100%;
  cursor: pointer;
}
.collectionTextLink {
  float: left;
  padding-top: 50px;
}
.p-button-link {
  font-size: 18px;
}
</style>
