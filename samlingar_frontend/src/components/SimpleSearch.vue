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
              placeholder="Search"
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
            <Button link @click="onAdvanceSearchLinkClick">
              <small id="simpleSearchInput-help">{{ $t('search.advanceSearch') }} </small>
            </Button>
          </div>
        </div>
      </div>
      <div class="grid">
        <div class="col-8" no-gutters style="padding-top: 20px">
          <p class="m-0">{{ $t('startPage.text') }}</p>
        </div>
      </div>
      <div class="grid">
        <div class="col-5" no-gutters style="padding-top: 50px">
          <p class="m-0" style="font-weight: bold; font-size: 16px">
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
          <Button link @click="searchBotCollection">
            <small id="simpleSearchInput-help">{{ $t('startPage.botanicalCollection') }} </small>
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-5">
          <Image src="/Zoologiska.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <Button link @click="searchBotCollection">
            <small id="simpleSearchInput-help">{{ $t('startPage.zooCollection') }} </small>
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-5">
          <Image src="/Angelinoceras.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <Button link @click="searchBotCollection">
            <small id="simpleSearchInput-help">{{ $t('startPage.palaeCollection') }} </small>
          </Button>
        </div>
      </div>
      <div class="grid">
        <div class="col-5">
          <Image src="/Bergkristall.jpg" alt="Image" width="180" />
        </div>
        <div class="col-7" style="vertical-align: bottom; float: left; padding-top: 30px">
          <Button link @click="searchBotCollection">
            <small id="simpleSearchInput-help">{{ $t('startPage.geoCollection') }} </small>
          </Button>
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

function onPressEnter() {
  search()
}

function onSearchClick() {
  search()
}

function searchAll() {
  search('all')
}

function searchAllCoordinates() {
  searchWithFilter('map:*')
}

function searchInSweden() {
  searchWithFilter('inSweden:*')
}

function searchWithType() {
  searchWithFilter('isType:*')
}

function searchWithImage() {
  searchWithFilter('image:*')
}

function searchWithFilter(filter) {
  const searchText = '*:*'
  store.commit('setSearchText', searchText)
  emits('searchWithFilter', filter)
}

function searchBotCollection() {
  console.log('searchBotCollection')
}

function onAdvanceSearchLinkClick() {
  store.commit('setIsAdvanceSearch', true)
}

function search(searchType) {
  loading.value = true

  let searchText
  if (searchType === 'all') {
    searchText = '*:*'
  } else {
    searchText = 'text:' + value.value + '*'
  }

  store.commit('setSearchText', searchText)
  emits('simpleSearch')

  setTimeout(() => {
    loading.value = false
  }, 2000)
}
</script>
<style scoped>
.searchInput {
  min-width: 60%;
  text-align: left;
  float: left;
}

.advanceLink {
  float: right;
}
</style>
