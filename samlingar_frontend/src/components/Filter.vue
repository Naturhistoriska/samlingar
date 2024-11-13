<template>
  <div class="grid">
    <div class="grid">
      <div class="col-12" no-gutters style="padding-top: 2em; padding-right: 2em; font-size: 16px">
        <p class="m-0">{{ $t('startPage.text') }}</p>
      </div>
    </div>
    <div class="grid" style="min-width: 100%">
      <div class="col-12 w-full" no-gutters style="padding-top: 50px">
        <p class="m-0" style="font-weight: bold; font-size: 18px">
          {{ $t('startPage.commonSearch') }}
        </p>
      </div>
    </div>

    <div class="grid divLink" @click="searchAll">
      <div class="col-6" no-gutters style="float: left; text-align: left">
        <Button text :label="$t('startPage.allSpecimens')" />
      </div>
      <div class="col-6" style="float: left; text-align: left">
        <Button text>
          {{ totalCount }}
        </Button>
      </div>
    </div>
    <div class="grid divLink" @click="searchAllCoordinates">
      <div class="col-6" no-gutters style="float: left; text-align: left">
        <Button text :label="$t('startPage.specimensWithCoordinates')" />
      </div>
      <div class="col-6">
        <Button text>
          {{ coordinatesCount }}
        </Button>
      </div>
    </div>
    <div class="grid divLink" @click="searchInSweden">
      <div class="col-6" no-gutters style="float: left; text-align: left">
        <Button text :label="$t('startPage.specimensFromSweden')" />
      </div>
      <div class="col-6">
        <Button text>
          {{ inSwedenCount }}
        </Button>
      </div>
    </div>
    <div class="grid divLink" @click="searchWithImage">
      <div class="col-6" no-gutters style="float: left; text-align: left">
        <Button text :label="$t('startPage.specimensWithImages')" />
      </div>
      <div class="col-6">
        <Button text>
          {{ imageCount }}
        </Button>
      </div>
    </div>
    <div class="grid divLink" @click="searchWithType">
      <div class="col-6" no-gutters style="float: left; text-align: left">
        <Button text :label="$t('startPage.specimensWithType')" />
      </div>
      <div class="col-6">
        <Button text>
          {{ isTypeCount }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'

// const emits = defineEmits(['advanceSearch', 'searchWithFilter', 'simpleSearch'])
const emits = defineEmits([
  'filterWithCoordinates',
  'filterWithImages',
  'filterWithInSweden',
  'filterWithInType',
  'simpleSearch'
])

const store = useStore()

const coordinatesCount = computed(() => {
  return store.getters['hasCoordinatesCount']
})

const imageCount = computed(() => {
  return store.getters['imageCount']
})

const isTypeCount = computed(() => {
  return store.getters['isTypeCount']
})

const totalCount = computed(() => {
  return store.getters['totalCount']
})

const inSwedenCount = computed(() => {
  return store.getters['inSwedenCount']
})

function searchAll() {
  store.commit('setSearchText', '*:*')
  emits('simpleSearch')
}

function searchAllCoordinates() {
  emits('filterWithCoordinates')
}

function searchInSweden() {
  emits('filterWithInSweden')
}

function searchWithImage() {
  emits('filterWithImages')
}

function searchWithType() {
  emits('filterWithInType')
}
</script>
<style scoped>
.p-button-text:hover {
  color: #9dabf3 !important;
  text-decoration: none !important;
  background: transparent !important;
}
.divLink {
  min-width: 100% !important;
  cursor: pointer !important;
}
</style>
