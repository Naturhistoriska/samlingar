<template>
  <div class="grid" no-gutters>
    <div class="grid collectionDivLink" @click="searchBotCollection">
      <div class="col-4">
        <Image src="/paucidentatus.jpg" alt="Image" width="180" />
      </div>
      <div class="col-8 collectionTextLink">
        <div class="grid">
          <Button link>
            <small>{{ $t('startPage.botanicalCollection') }}</small>
          </Button>
        </div>
      </div>
    </div>
    <div class="grid collectionDivLink" @click="searchZooCollection">
      <div class="col-4">
        <Image src="/Zoologiska.jpg" alt="Image" width="180" />
      </div>
      <div class="col-8 collectionTextLink">
        <div class="grid">
          <Button link>
            <small>{{ $t('startPage.zooCollection') }} </small>
          </Button>
        </div>
      </div>
    </div>
    <div class="grid collectionDivLink" @click="searchPalaeCollection">
      <div class="col-4">
        <Image src="/Angelinoceras.jpg" alt="Image" width="180" />
      </div>
      <div class="col-8 collectionTextLink">
        <div class="grid">
          <Button link>
            <small>{{ $t('startPage.palaeCollection') }} </small>
          </Button>
        </div>
      </div>
    </div>
    <div class="grid collectionDivLink" @click="searchGeoCollection">
      <div class="col-4">
        <Image src="/Bergkristall.jpg" alt="Image" width="180" />
      </div>
      <div class="col-8 collectionTextLink">
        <div class="grid">
          <Button link>
            <small>{{ $t('startPage.geoCollection') }} </small>
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
const emits = defineEmits([
  'searchBotanyCollections',
  'searchZooCollections',
  'searchPaleaCollections',
  'searchGeoCollections'
])

function searchBotCollection() {
  emits('searchBotanyCollections')
}

function searchZooCollection() {
  emits('searchZooCollections')
}
function searchPalaeCollection() {
  emits('searchPaleaCollections')
}
function searchGeoCollection() {
  emits('searchGeoCollections')
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
