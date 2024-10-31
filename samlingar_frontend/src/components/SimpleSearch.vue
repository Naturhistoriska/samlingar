<template>
  <div class="grid">
    <div class="col-7" no-gutters style="padding-top: 20px">
      <div class="grid">
        <div class="flex-col gap-2 searchInput">
          <InputGroup>
            <InputText
              id="simpleSearchInput1"
              v-model="value"
              @keydown.enter="onPressEnter"
              :placeholder="$t('search.freeTextSearch')"
              aria-describedby="simpleSearchInput-help"
            />
            <Button
              icon="pi pi-search"
              style="max-width: 30px; max-height: 30px"
              :loading="loading"
              @click="onSearchClick"
            />
          </InputGroup>

          <div class="advanceLink">
            <Button link @click="onAdvanceSearchLinkClick" disabled>
              <small id="simpleSearchInput-help"
                >{{ $t('search.advanceSearch') }} (Under development)
              </small>
            </Button>
          </div>
        </div>
      </div>
      <div class="grid">
        <div class="col-10" no-gutters style="padding-top: 20px; font-size: 16px">
          <p class="m-0">{{ $t('startPage.text') }}</p>
        </div>
      </div>
      <div class="grid">
        <div class="col-5" no-gutters style="padding-top: 50px">
          <p class="m-0" style="font-weight: bold; font-size: 18px">
            {{ $t('startPage.commonSearch') }}
          </p>
        </div>
      </div>

      <div class="grid">
        <div class="col-6" no-gutters style="float: left; text-align: left">
          <Button text :label="$t('startPage.allSpecimens')" @click="searchAll" />
        </div>
        <div class="col-6" style="float: left; text-align: left">
          <Button text @click="searchAll">
            {{ totalCount }}
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-6" no-gutters style="float: left; text-align: left">
          <Button
            text
            :label="$t('startPage.specimensWithCoordinates')"
            @click="searchAllCoordinates"
          />
        </div>
        <div class="col-6">
          <Button text @click="searchAllCoordinates">
            {{ coordinatesCount }}
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-6" no-gutters style="float: left; text-align: left">
          <Button text :label="$t('startPage.specimensFromSweden')" @click="searchInSweden" />
        </div>
        <div class="col-6">
          <Button text @click="searchInSweden">
            {{ inSwedenCount }}
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-6" no-gutters style="float: left; text-align: left">
          <Button text :label="$t('startPage.specimensWithImages')" @click="searchWithImage" />
        </div>
        <div class="col-6">
          <Button text @click="searchWithImage">
            {{ imageCount }}
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-6" no-gutters style="float: left; text-align: left">
          <Button text :label="$t('startPage.specimensWithType')" @click="searchWithType" />
        </div>
        <div class="col-6">
          <Button text @click="searchWithType">
            {{ isTypeCount }}
          </Button>
        </div>
      </div>
    </div>
    <div class="col-5" no-gutters style="padding-top: 20px; float: right">
      <div class="grid">
        <div class="col-5">
          <Image src="/paucidentatus.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <div class="grid">
            <Button link @click="searchBotCollection">
              <small>{{ $t('startPage.botanicalCollection') }}</small>
            </Button>
          </div>
          <div class="grid">
            <Button link @click="searchBotCollection">
              <small>[{{ botanyCount }}]</small>
            </Button>
          </div>
        </div>
      </div>
      <div class="grid">
        <div class="col-5">
          <Image src="/Zoologiska.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <div class="grid">
            <Button link @click="searchZooCollection">
              <small>{{ $t('startPage.zooCollection') }} </small>
            </Button>
          </div>
          <div class="grid">
            <Button link @click="searchZooCollection">
              <small>[{{ zooCount }}]</small>
            </Button>
          </div>
        </div>
      </div>
      <div class="grid">
        <div class="col-5">
          <Image src="/Angelinoceras.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <div class="grid">
            <Button link @click="searchPalaeCollection">
              <small>{{ $t('startPage.palaeCollection') }} </small>
            </Button>
          </div>
          <div class="grid">
            <Button link @click="searchPalaeCollection">
              <small>[{{ paleaCount }}]</small>
            </Button>
          </div>
        </div>
      </div>
      <div class="grid">
        <div class="col-5">
          <Image src="/Bergkristall.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <div class="grid">
            <Button link @click="searchGeoCollection">
              <small>{{ $t('startPage.geoCollection') }} </small>
            </Button>
          </div>
          <div class="grid">
            <Button link @click="searchGeoCollection">
              <small>[{{ geoCount }}]</small>
            </Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['searchWithFilter', 'simpleSearch'])

const value = ref()
let loading = ref(false)

const botanyCount = computed(() => {
  return store.getters['botanyCollectionTotal']
})

const geoCount = computed(() => {
  return store.getters['geoCollectionTotal']
})

const paleaCount = computed(() => {
  return store.getters['paleaCollectionTotal']
})

const zooCount = computed(() => {
  return store.getters['zooCollectionTotal']
})

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

function searchAllCoordinates() {
  store.commit('setSearchText', '%2Bmap:*')

  store.commit('setFilterCoordinates', true)
  emits('simpleSearch')
}

function searchInSweden() {
  store.commit('setSearchText', '%2BinSweden:*')
  store.commit('setFilterInSweden', true)
  emits('simpleSearch')
}

function searchWithType() {
  store.commit('setSearchText', '%2BisType:*')
  store.commit('setFilterType', true)
  emits('simpleSearch')
}

function searchWithImage() {
  store.commit('setSearchText', '%2Bimage:*')
  store.commit('setFilterImage', true)
  emits('simpleSearch')
}

function searchPalaeCollection() {
  const searchText = '%2BcollectionId:p*'

  store.commit('setSearchText', searchText)
  emits('simpleSearch')
}

function searchGeoCollection() {
  const searchText = '%2BcollectionId:(557057 753664 786432)'

  store.commit('setSearchText', searchText)
  emits('simpleSearch')
}

function searchZooCollection() {
  const searchText = '%2BcollectionId:(e* 262144 655361 163840 ma fish herps va)'

  store.commit('setSearchText', searchText)
  emits('simpleSearch')
}

function searchBotCollection() {
  console.log('searchBotCollection')

  const searchText = '%2BcollectionId:(vp fungi mosses algae)'
  store.commit('setSearchText', searchText)
  emits('simpleSearch')
}

function onPressEnter() {
  // const searchText = '%2Btext:' + value.value + '*'

  const searchText = '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
  search(searchText)
}

function onSearchClick() {
  // const searchText = '%2Btext:' + value.value + '*'
  const searchText = '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
  search(searchText)
}

function searchAll() {
  search('*:*')
}

function search(value) {
  loading.value = true

  store.commit('setSearchText', value)
  emits('simpleSearch')

  setTimeout(() => {
    loading.value = false
  }, 2000)
}

function onAdvanceSearchLinkClick() {
  store.commit('setIsAdvanceSearch', true)
}
</script>
<style scoped>
.searchInput {
  min-width: 80%;
  text-align: left;
  float: left;
}

.advanceLink {
  float: right;
}

.p-button-link {
  color: #0dff5c;
  font-size: 18px;
}

.p-button-text {
  color: #0dff5c;
}
</style>
