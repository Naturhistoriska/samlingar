<template>
  <div class="card">
    <DataTable
      :value="records"
      ref="dt"
      stripedRows
      resizableColumns
      columnResizeMode="fit"
      :size="size"
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
            {{ $t('results.searchResults') }} [{{ $t('results.num_results', totalCount) }}]
          </span>
          <div style="text-align: right" class="grid">
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
import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()

onMounted(async () => {
  console.log('onMounted')

  count.value = 100
  fetchData(0, 100)
})

let count = ref(0)
let records = ref(Array.from({ length: 100000 }))
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
  () => store.getters['searchText'],
  (newValue, oldValue) => {
    console.log('watch')

    count.value = 100
    fetchData(0, 100)
  }
)

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

const loadRecoudsLazy = (event) => {
  console.log('loadRecoudsLazy')

  console.log('event', event)
  console.log('count.value', count.value)

  !lazyLoading.value && (lazyLoading.value = true)

  if (loadLazyTimeout.value) {
    clearTimeout(loadLazyTimeout.value)
  }

  loadLazyTimeout.value = setTimeout(
    () => {
      let { first, last } = event
      console.log(first, last, totalCount.value)
      if (last >= 100 && totalCount.value > 100) {
        fetchData(count.value, 100)
        count.value += 100
      }
      lazyLoading.value = false
    },
    Math.random() * 1000 + 250
  )
}

const exportCSV = () => {
  dt.value.exportCSV()
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
