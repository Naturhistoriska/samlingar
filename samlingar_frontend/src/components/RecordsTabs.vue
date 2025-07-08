<template>
  <div class="card">
    <Tabs v-model:value="value">
      <TabList>
        <Tab value="0">{{ $t('records.list') }}</Tab>
        <Tab value="1">{{ $t('records.labels') }}</Tab>
        <Tab value="2" disabled>{{ $t('records.media') }}</Tab>
        <Tab value="3" disabled>
          <span class="text-900 font-bold">
            [{{ $t('results.searchResults') }} {{ $t('results.num_results', totalCount) }}]
          </span>
        </Tab>
        <Tab :value="value" style="border-color: transparent !important">
          <div class="col-12" style="float: left; text-align: left">
            <Download @exportData="preparaDataExport" />
          </div>
          <!-- <i class="pi pi-file-export" style="color: slateblue"></i> -->
        </Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0" v-if="isListView">
          <RecordTable @search="handleSearch" />
        </TabPanel>
        <TabPanel value="1" v-if="isLableView">
          <RecordLabels @search="handleSearch" />
        </TabPanel>
        <TabPanel value="2">
          <RecordMedia v-if="isMediaView" />
        </TabPanel>
      </TabPanels>
    </Tabs>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import Download from './Download.vue'
import RecordLabels from './RecordLabels.vue'
import RecordTable from './RecordTable.vue'
import RecordMedia from './RecordMedia.vue'

const store = useStore()

const emits = defineEmits(['exportData', 'search'])

let value = ref('0')

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

function preparaDataExport() {
  emits('exportData')
}

function handleSearch(start, numPerPage, saveData) {
  emits('search', start, numPerPage, saveData)
}

// function fetchList() {
//   console.log('fetchList', value.value)
// }

// function fetchLabel() {
//   console.log('fetchLabel', value.value)
// }

// function fetchImageData() {
//   console.log('fetchImageData', value.value)

//   const filtMedia = store.getters['filterImage']
//   console.log('filtImages', filtMedia)
//   // const records = store.getters['results']
//   // const filtImages = store.getters['filterImage']

//   // console.log('filtImages', filtImages)
//   emits('fetchMedia')
// }

// function handleFreeTextSearch(searchText, start, numPerPage) {
//   console.log('handleFreeTextSearch', searchText, start, numPerPage)
//   emits('freeTextSearch', searchText, start, numPerPage)
// }
</script>
