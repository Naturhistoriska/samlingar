<template>
  <div class="grid">
    <div class="col-6" no-gutters style="padding-top: 20px">
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
        <div class="col-5" no-gutters>
          <p class="m-0">{{ $t('startPage.allSpecimens') }}</p>
        </div>
        <div class="col-7">
          {{ totalCount }}
        </div>
      </div>
      <div class="grid">
        <div class="col-5" no-gutters>
          <p class="m-0">{{ $t('startPage.specimensWithCoordinates') }}</p>
        </div>
        <div class="col-7">
          {{ coordinatesCount }}
        </div>
      </div>
      <div class="grid">
        <div class="col-5" no-gutters>
          <p class="m-0">{{ $t('startPage.specimensFromSweden') }}</p>
        </div>
        <div class="col-7">
          {{ inSwedenCount }}
        </div>
      </div>
      <div class="grid">
        <div class="col-5" no-gutters>
          <p class="m-0">{{ $t('startPage.specimensWithImages') }}</p>
        </div>
        <div class="col-7">
          {{ imageCount }}
        </div>
      </div>
      <div class="grid">
        <div class="col-5" no-gutters>
          <p class="m-0">{{ $t('startPage.specimensWithType') }}</p>
        </div>
        <div class="col-7">
          {{ isTypeCount }}
        </div>
      </div>
    </div>
    <div class="col-6" no-gutters style="padding-top: 20px; float: right">
      <div class="grid">
        <div class="col-4"><br /></div>
        <div class="col-8">
          <div class="grid">
            <div class="col-4">
              <Image src="/paucidentatus.jpg" alt="Image" width="100" />
            </div>
            <div class="col-8" style="vertical-align: bottom; float: left">
              <Button link @click="searchBotCollection">
                <small id="simpleSearchInput-help"
                  >{{ $t('startPage.botanicalCollection') }}
                </small>
              </Button>
            </div>
          </div>

          <div style="padding-top: 20px">
            <Image src="/Zoologiska.jpg" alt="Image" width="100" />
          </div>
          <div style="padding-top: 20px">
            <Image src="/Angelinoceras.jpg" alt="Image" width="100" />
          </div>
          <div style="padding-top: 20px">
            <Image src="/Bergkristall.jpg" alt="Image" width="100" />
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- <div>
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

    <div><Image src="/paucidentatus.jpg" alt="Image" width="250" /></div>
    <div><Image src="/Angelinoceras.jpg" alt="Image" width="250" /></div>
    <div><Image src="/Bergkristall.jpg" alt="Image" width="250" /></div>
    <div><Image src="/Zoologiska.jpg" alt="Image" width="250" /></div>

    <div class="grid">
      <div class="col-4" no-gutters style="padding-top: 20px">
        <p class="m-0">{{ $t('startPage.text') }}</p>
      </div>
    </div>

    <div class="grid">
      <div class="col-5" no-gutters style="padding-top: 30px">
        <p class="m-0">{{ $t('startPage.commonSearch') }}</p>
      </div>
    </div>
  </div> -->
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['simpleSearch'])

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

function searchBotCollection() {
  console.log('searchBotCollection')
}

function onAdvanceSearchLinkClick() {
  store.commit('setIsAdvanceSearch', true)
}

function search() {
  loading.value = true

  const searchText = value.value + '*'
  store.commit('setSearchText', searchText)
  emits('simpleSearch')

  setTimeout(() => {
    loading.value = false
  }, 2000)

  // const searchText = value.value + '*'

  // loading.value = true

  // service
  //   .quickSearch(searchText, 1, 10)
  //   .then((response) => {
  //     const total = response.totalRecords
  //     const results = response.occurrences

  //     let facetResults = response.facetResults
  //     let collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
  //     const collections = collectionFacet.fieldResult

  //     const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
  //     const occurrenceYears = yearFacet.fieldResult

  //     store.commit('setResults', results)
  //     store.commit('setTotalRecords', total)

  //     store.commit('setCollections', collections)
  //     store.commit('setOccurrenceYears', occurrenceYears)

  //     store.commit('setSearchText', searchText)
  //     store.commit('setSelectedCollection', null)

  //     store.commit('setYear', null)
  //     store.commit('setShowDetail', false)
  //     store.commit('setShowResults', true)

  //     setTimeout(() => {
  //       loading.value = false
  //     }, 2000)
  //   })
  //   .catch()
  //   .finally(() => {})
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
