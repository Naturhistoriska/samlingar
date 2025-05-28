<template>
  <div class="card">
    <DataTable
      :value="records"
      v-model:selection="selectedRecord"
      selectionMode="single"
      dataKey="id"
      ref="dt"
      stripedRows
      resizableColumns
      columnResizeMode="fit"
      class="p-datatable-sm"
      scrollable
      scrollHeight="400px"
      tableStyle="min-width: 50rem"
      :virtualScrollerOptions="{
        lazy: true,
        onLazyLoad: loadRecoudsLazy,
        itemSize: 44,
        delay: 200,
        showLoader: true,
        loading: lazyLoading,
        numToleratedItems: 10
      }"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold">
            {{ $t('results.searchResults') }}
            [{{ $t('results.num_results', totalCount) }}]
          </span>
          <div style="text-align: right" clss="grid">
            <Button
              icon="pi pi-external-link"
              :label="$t('exportData.exportDatatable')"
              @click="exportCSV($event)"
            />
            <Button type="button" :label="$t('btnLabel.columns')" @click="dialogVisible = true" />
          </div>
        </div>
      </template>

      <Column v-for="col of columns" :key="col.field" :field="col.field" :header="col.header">
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column>
      <Column class="w-24 !text-end">
        <template #body="{ data }">
          <Button
            text
            @click="selectRow(data)"
            severity="secondary"
            :label="$t('records.view')"
          ></Button>
        </template>
      </Column>
    </DataTable>
    <Dialog
      v-model:visible="dialogVisible"
      header="Flex Scroll"
      :style="{ width: '75vw' }"
      maximizable
      modal
      :contentStyle="{ height: '300px' }"
    >
      test
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const emits = defineEmits(['search'])

// onBeforeRouteUpdate((to, from) => {
//   console.log('onBeforeRouteUpdate', to, from)
//   const { name } = to
//   if (name === 'Home') {
//     // store.commit('setShowResults', false)
//   }
// })

// onMounted(async () => {
// console.log('onMounted')

// count.value = 100
// fetchData(0, 100)
// })

let count = ref(30)
let records = ref(Array.from({ length: 100000 }))
const selectedRecord = ref()
const lazyLoading = ref(false)
const loadLazyTimeout = ref()
const size = ref('small')

const dialogVisible = ref(false)

const dt = ref()

let columns = ref([
  { field: 'collectionName', header: 'Collection Name' },
  { field: 'scientificName', header: 'Scientific Name' },
  { field: 'locality', header: 'Locality' },
  { field: 'catalogNumber', header: 'CatalogNumber' }
])

watch(
  () => store.getters['results'],
  (newValue, oldValue) => {
    records.value = store.getters['results']
    console.log('watched...')
  }
)

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

const loadRecoudsLazy = (event) => {
  console.log('loadRecoudsLazy')

  console.log('event', event)
  console.log('count.value', count.value)

  const results = store.getters['results']
  console.log('results', results)

  records.value = results

  !lazyLoading.value && (lazyLoading.value = true)

  if (loadLazyTimeout.value) {
    clearTimeout(loadLazyTimeout.value)
  }

  loadLazyTimeout.value = setTimeout(
    () => {
      let { first, last } = event
      console.log(first, last, totalCount.value)

      if (last >= count.value && totalCount.value > count.value) {
        emits('search')
        // fetchData(count.value, 100)
        count.value += 30
      }
      lazyLoading.value = false
    },
    Math.random() * 1000 + 250
  )
}

const exportCSV = () => {
  dt.value.exportCSV()
}

function selectRow(data) {
  selectedRecord.value = data
  // store.commit('setShowDetail', true)
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${data.id}`)
}

function fetchData(start, numPerPage) {
  console.log('fatchData')

  const searchText = store.getters['searchText']
  const value = searchText == null ? '*' : searchText

  service
    .apiFreeTextSearch(value, start, numPerPage)
    .then((response) => {
      const results = response.response.docs
      const total = response.response.numFound

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      records.value = results
    })
    .catch()
    .finally(() => {})
}
</script>
<style scoped>
.p-button-text:hover {
  color: var(--p-emerald-500) !important;
  text-decoration: none !important;
  background: transparent !important;
}

.p-datatable {
  font-size: 12px !important;
  padding-top: 0 !important;
}

.p-button-text {
  text-decoration: underline;
}
</style>
