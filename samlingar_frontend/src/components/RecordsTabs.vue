<template>
  <div class="card">
    <Tabs v-model:value="value">
      <TabList>
        <Tab value="0">{{ $t('records.list') }}</Tab>
        <Tab value="1">{{ $t('records.labels') }}</Tab>
        <Tab value="2">{{ $t('records.media') }}</Tab>
        <Tab value="3" disabled>
          <span class="text-900 font-bold">
            [{{ $t('results.searchResults') }} {{ $t('results.num_results', totalCount) }}]
          </span>
        </Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0" v-if="isListView">
          <RecordTable @search="handleSearch" />
        </TabPanel>
        <TabPanel value="1" v-if="isLableView">
          <RecordLabels />
        </TabPanel>
        <TabPanel value="2">
          <RecordMedia v-if="isMediaView" />
        </TabPanel>
      </TabPanels>
    </Tabs>
  </div>
</template>
<script setup>
import { computed, defineAsyncComponent, ref } from 'vue'
import { useStore } from 'vuex'
import RecordLabels from './RecordLabels.vue'
import RecordTable from './RecordTable.vue'
import RecordMedia from './RecordMedia.vue'

const store = useStore()

const emits = defineEmits(['fetchMedia', 'freeTextSearch'])

const value = ref('0')

const isListView = computed(() => {
  return value.value == 0
})

const isLableView = computed(() => {
  return value.value == 1
})

const isMediaView = computed(() => {
  return value.value == 2
})

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

function fetchList() {
  console.log('fetchList', value.value)
}

function fetchLabel() {
  console.log('fetchLabel', value.value)
}

function fetchImageData() {
  console.log('fetchImageData', value.value)

  const filtMedia = store.getters['filterImage']
  console.log('filtImages', filtMedia)
  // const records = store.getters['results']
  // const filtImages = store.getters['filterImage']

  // console.log('filtImages', filtImages)
  emits('fetchMedia')
}

function handleSearch() {
  console.log('handleSearch')
}

function handleFreeTextSearch(searchText, start, numPerPage) {
  console.log('handleFreeTextSearch', searchText, start, numPerPage)
  emits('freeTextSearch', searchText, start, numPerPage)
}
</script>
