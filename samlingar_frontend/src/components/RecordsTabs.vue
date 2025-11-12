<template>
  <div class="card">
    <Tabs v-model:value="value" class="responsive-tabs">
      <div
        class="flex flex-column md:flex-row justify-content-between align-items-start md:align-items-center flex-wrap gap-2"
      >
        <TabList class="my-tablist flex flex-wrap md:flex-nowrap overflow-x-auto">
          <Tab value="0" class="my-tab text-sm md:text-base">{{ $t('records.list') }}</Tab>
          <Tab value="1" class="my-tab text-sm md:text-base">{{ $t('records.labels') }}</Tab>
          <!-- <Tab value="2" class="my-tab">{{ $t('records.media') }}</Tab> -->
          <Tab value="2" disabled class="my-tab flex align-items-center text-xs md:text-sm">
            <span class="text-900 font-bold whitespace-nowrap">
              [{{ $t('results.searchResults') }} {{ $t('results.num_results', totalCount) }}]
            </span>
          </Tab>

          <!-- Download  -->
          <!-- <Tab
            :value="value"
            style="border-color: transparent !important"
            class="flex-grow-1 flex justify-content-end md:justify-content-start"
          >
            <div
              class="download-container w-full md:w-auto text-left pt-2 md:pt-0"
              style="min-width: 220px"
            >
              <Download @exportData="preparaDataExport" />
            </div>
          </Tab> -->
        </TabList>

        <!-- Download area (moves below on mobile) -->
        <div class="download-container w-full md:w-auto text-left md:text-right">
          <Download @exportData="preparaDataExport" />
        </div>
      </div>

      <!-- Panels -->
      <TabPanels class="mt-3">
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
/* :deep(.my-tablist .my-tab) {
  padding: 0.75rem 1.25rem;
  cursor: pointer;
  color: #989797 !important;
}

:deep(.my-tablist .my-tab:hover) {
  color: #444444 !important;
}

:deep(.my-tablist .my-tab[aria-selected='true']) {
  color: #1d634a !important;
} */

.download-container {
  min-width: 30rem;
}

.my-tablist {
  scrollbar-width: thin;
  scroll-behavior: smooth;
}
.my-tablist::-webkit-scrollbar {
  height: 6px;
}
.my-tablist::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 3px;
}

@media (max-width: 768px) {
  .download-container {
    min-width: 20rem;
  }
}
</style>
