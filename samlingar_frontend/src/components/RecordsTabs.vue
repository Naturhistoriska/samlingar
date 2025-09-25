<template>
  <div class="card">
    <Tabs v-model:value="value">
      <TabList class="my-tablist">
        <Tab value="0" class="my-tab">{{ $t('records.list') }}</Tab>
        <Tab value="1" class="my-tab">{{ $t('records.labels') }}</Tab>
        <!-- <Tab value="2">{{ $t('records.media') }}</Tab> -->
        <Tab value="2" disabled>
          <span class="text-900 font-bold">
            [{{ $t('results.searchResults') }} {{ $t('results.num_results', totalCount) }}]
          </span>
        </Tab>
        <Tab :value="value" style="border-color: transparent !important">
          <div style="float: left; text-align: left; min-width: 370px">
            <Download @exportData="preparaDataExport" />
          </div>
        </Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0" v-if="isListView">
          <RecordTable />
        </TabPanel>
        <TabPanel value="1" v-if="isLableView">
          <RecordLabels />
        </TabPanel>
        <!-- <TabPanel value="2">
          <RecordMedia v-if="isMediaView" />
        </TabPanel> -->
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
// import RecordMedia from './RecordMedia.vue'

const store = useStore()

const emits = defineEmits(['exportData', 'search'])

let value = ref('0')

const isListView = computed(() => {
  return value.value == 0
})

const isLableView = computed(() => {
  return value.value == 1
})

// const isMediaView = computed(() => {
//   return value.value == 2
// })

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

function preparaDataExport() {
  emits('exportData')
}

// function download() {
//   emits('download')
// }

// function handleSearch(start, numPerPage, saveData) {
//   emits('search', start, numPerPage, saveData)
// }

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
<style scoped>
/* a {
  cursor: pointer;
  text-decoration: none;
  color: #fff !important;
}

a:hover {
  color: rgb(166, 158, 158) !important;
} */
/* Base (inactive) tab style */
:deep(.my-tablist .my-tab) {
  padding: 0.75rem 1.25rem;
  cursor: pointer;
  color: #989797 !important; /* inactive text color */
  /* background-color: #f0f0f0 !important;   */
  /* border-bottom: 2px solid transparent; */
  /* transition: all 0.3s ease; */
}

/* Hover state (inactive) */
:deep(.my-tablist .my-tab:hover) {
  /* background-color: #e0e0e0 !important; */
  color: #444444 !important;
}

/* Active tab style - when its value matches active val */
:deep(.my-tablist .my-tab[aria-selected='true']) {
  /* PrimeVue Tabs uses aria-selected on active Tab */
  color: #1d634a !important;
  /* background-color: #007bff !important; */
  /* border-bottom-color: #0056b3 !important;
  font-weight: bold; */
}
</style>
