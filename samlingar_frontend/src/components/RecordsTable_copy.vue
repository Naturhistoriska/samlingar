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
        itemSize: 30,
        delay: 200,
        showLoader: true,
        loading: lazyLoading,
        numToleratedItems: 10
      }"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold"> </span>
          <div style="text-align: right" clss="grid justify-end">
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
            icon="pi pi-search"
            @click="selectRow(data)"
            severity="secondary"
            rounded
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
      <datatable-column />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

import DatatableColumn from './DatatableColumn.vue'

import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const emits = defineEmits(['search'])

const dialogVisible = ref(false)

const dt = ref()

let columns = ref([
  { field: 'collectionName', header: 'Collection Name' },
  { field: 'scientificName', header: 'Scientific Name' },
  { field: 'locality', header: 'Locality' },
  { field: 'catalogNumber', header: 'CatalogNumber' }
])

let records = ref(Array.from({ length: 10000 }))
const selectedRecord = ref()
const lazyLoading = ref(false)
const loadLazyTimeout = ref()

watch(
  async () => store.getters['results'],
  (newValue, oldValue) => {
    const total = store.getters['totalRecords']
    if (total < 10000) {
      records.value = Array.apply(null, Array(total)).map(function () {})
    }
    records.value = store.getters['results']
  }
)

onMounted(async () => {
  const fields = store.getters['fields']

  const params = new URLSearchParams({
    text: searchText.value
  })

  if (scientificName.value) {
    params.set('scientificName', scientificName.value)
    params.set('fuzzySearch', isFuzzySearch.value)
  }

  if (isType.value) {
    params.set('isType', isType.value)
  }

  if (isInSweden.value) {
    params.set('isInSweden', isInSweden.value)
  }

  if (hasImages.value) {
    params.set('hasImage', hasImages.value)
  }

  if (hasCoordinates.value) {
    params.set('hasCoordinates', hasCoordinates.value)
  }

  if (startDate.value) {
    params.set('startDate', startDate.value)
  }

  if (endDate.value) {
    params.set('endDate', endDate.value)
  }

  fields
    .filter((field) => field.text)
    .forEach((field) => {
      console.log('what...', field.value, field.text)
      params.set(field.value, field.text)
    })

  await service
    .apiSearch(params, 0, 40)
    .then((response) => {
      const loadedRecords = response.response.docs
      const total = response.response.numFound

      store.commit('setResults', loadedRecords)
      store.commit('setTotalRecords', total)

      if (total < 10000) {
        records.value = Array.apply(null, Array(total)).map(function () {})
      }
      let _virtualRecords = [...records.value]

      Array.prototype.splice.apply(_virtualRecords, [
        ...[0, loadedRecords.length],
        ...loadedRecords
      ])
      records.value = _virtualRecords

      if (total > 0) {
        const geofacet = response.facets.geo.buckets
        store.commit('setGeoData', geofacet)
      }
    })
    .catch(() => {
      console.log('error')
    })
    .finally(() => {
      return { response: [] }
    })
})

const searchText = computed(() => {
  let text = store.getters['searchText']
  return text ? text : '*'
})

const scientificName = computed(() => {
  return store.getters['scientificName']
})

const isFuzzySearch = computed(() => {
  return store.getters['isFuzzySearch']
})

const hasImages = computed(() => {
  return store.getters['filterImage']
})

const hasCoordinates = computed(() => {
  return store.getters['filterCoordinates']
})

const isInSweden = computed(() => {
  return store.getters['filterInSweden']
})

const isType = computed(() => {
  return store.getters['filterType']
})

const startDate = computed(() => {
  return store.getters['startDate']
})

const endDate = computed(() => {
  return store.getters['endDate']
})

const loadRecoudsLazy = async (event) => {
  console.log('loadRecoudsLazy')

  !lazyLoading.value && (lazyLoading.value = true)

  if (loadLazyTimeout.value) {
    clearTimeout(loadLazyTimeout.value)
  }

  let _virtualRecords = [...records.value]

  loadLazyTimeout.value = setTimeout(
    () => {
      let { first, last } = event

      const fields = store.getters['fields']

      const params = new URLSearchParams({
        text: searchText.value
      })

      if (scientificName.value) {
        params.set('scientificName', scientificName.value)
        params.set('fuzzySearch', isFuzzySearch.value)
      }

      if (isType.value) {
        params.set('isType', isType.value)
      }

      if (isInSweden.value) {
        params.set('isInSweden', isInSweden.value)
      }

      if (hasImages.value) {
        params.set('hasImages', hasImages.value)
      }

      if (hasCoordinates.value) {
        params.set('hasCoordinates', hasCoordinates.value)
      }

      if (startDate.value) {
        params.set('startDate', startDate.value)
      }

      if (endDate.value) {
        params.set('endDate', endDate.value)
      }

      fields
        .filter((field) => field.text)
        .forEach((field) => {
          console.log('what...', field.value, field.text)
          params.set(field.value, field.text)
        })

      service
        .apiSearch(params, first, last)
        .then((response) => {
          const loadedRecords = response.response.docs

          Array.prototype.splice.apply(_virtualRecords, [
            ...[first, loadedRecords.length],
            ...loadedRecords
          ])
          records.value = _virtualRecords
        })
        .catch()
        .finally(() => {
          lazyLoading.value = false
        })
    },
    Math.random() * 1000 + 250
  )
}

const exportCSV = () => {
  dt.value.exportCSV()
}

function selectRow(data) {
  selectedRecord.value = data
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${data.id}`)
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
