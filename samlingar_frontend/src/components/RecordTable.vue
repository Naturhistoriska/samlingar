<template>
  <div class="card">
    <DataTable
      inputId="recordDatatable"
      :value="records"
      v-model:selection="selectedRecord"
      selectionMode="single"
      v-model:filters="filters"
      filterDisplay="row"
      dataKey="id"
      stripedRows
      class="p-datatable-sm"
      tableStyle="min-width: 50rem"
      paginator
      @row-select="onSelect"
      @page="onPage($event)"
      filterIcon="pi pi-search-plus"
      lazy
      :rows="10"
      :rowsPerPageOptions="[5, 10, 20]"
      :totalRecords="totalCount"
      :filters="filters"
      @filter="onFilter"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold"> </span>
          <div style="text-align: right" clss="grid justify-end">
            <Button type="button" :label="$t('btnLabel.columns')" @click="dialogVisible = true" />
          </div>
        </div>
      </template>
      <template #empty>{{ $t('search.noResultsFound') }}</template>
      <template #loading>{{ $t('search.loadingData') }}</template>

      <!-- <Column
        v-for="col of columns"
        :key="col.field"
        :field="col.field"
        :header="col.header"
        :style="getColumnStyle(col)"
      >
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column> -->

      <Column
        field="dataResourceName"
        header="Collection name"
        :showFilterMenu="false"
        style="min-width: 14rem; max-width: 14rem"
      >
        <template #body="{ data }">
          {{ data.dataResourceName }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <MultiSelect
            v-model="filterModel.value"
            @change="filterCallback()"
            :options="collectionOptions"
            placeholder="Select collections"
            style="min-width: 14rem; max-width: 14rem; max-height: 1.9rem"
            size="small"
            :maxSelectedLabels="1"
            :showClear="true"
          >
            <template #option="slotProps">
              <div class="flex items-center">
                <span>
                  <small>{{ slotProps.option }}</small>
                </span>
              </div>
            </template>
          </MultiSelect>
        </template>
        <template #filtericon>
          <i :class="filters.dataResourceName?.value ? 'pi pi-filter-fill' : 'pi pi-filter'" />
        </template>
        <template #filterclearicon>
          <!-- render nothing -->
        </template>
      </Column>

      <Column
        field="scientificName"
        header="Scientific Name"
        style="min-width: 12rem"
        filterField="scientificName"
        :filter="true"
        :filterMatchModeOptions="nameFilterMatchModes"
      >
        <template #body="{ data }">
          {{ data.scientificName }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            v-model="filterModel.value"
            type="text"
            size="small"
            class="small-placeholder"
            @input="filterCallback()"
            placeholder="Filter by scientificName"
          />
        </template>
        <template #filtericon>
          <i :class="filters.scientificName?.value ? 'pi pi-filter-fill' : 'pi pi-filter'" />
        </template>
        <template #filterclearicon>
          <!-- render nothing -->
        </template>
      </Column>

      <Column
        field="catalogNumber"
        header="CatalogNumber"
        :showFilterMenu="false"
        style="min-width: 8rem; max-width: 8rem"
      >
        <template #body="{ data }">
          {{ data.catalogNumber }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <!-- <InputText
            v-model="filterModel.value"
            type="text"
            size="small"
            class="small-placeholder"
            @input="filterCallback()"
            placeholder="Filter by catalogNumber"
          />
          <i
            v-if="filterModel.value"
            class="pi pi-times absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 cursor-pointer"
            @click="filterModel.value = ''"
          /> -->

          <InputGroup>
            <InputText
              v-model="filterModel.value"
              type="text"
              size="small"
              class="small-placeholder"
              @input="filterCallback()"
              placeholder="Filter by catalogNumber"
            />
            <Button
              v-if="filterModel.value"
              icon="pi pi-times"
              @click="onFilterChange(filterModel, filterCallback)"
            />
          </InputGroup>
        </template>
        <template #filtericon>
          <i :class="filters.catalogNumber?.value ? 'pi pi-filter-fill' : 'pi pi-filter'" />
        </template>
        <template #filterclearicon>
          <!-- render nothing -->
        </template>
      </Column>

      <Column field="locality" header="Locality" :showFilterMenu="false" style="min-width: 12rem">
        <template #body="{ data }">
          {{ data.locality }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputGroup>
            <InputText
              v-model="filterModel.value"
              type="text"
              size="small"
              class="small-placeholder"
              @input="filterCallback()"
              placeholder="Filter by locality"
            />
            <Button
              v-if="filterModel.value"
              icon="pi pi-times"
              @click="onFilterChange(filterModel, filterCallback)"
            />
          </InputGroup>
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
      <datatable-column />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { FilterMatchMode } from '@primevue/core/api'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'
import DatatableColumn from './DatatableColumn.vue'

const store = useStore()
const router = useRouter()

const service = new Service()

const emits = defineEmits(['search'])

const dialogVisible = ref(false)

let columns = ref([])

const defaultColumns = ref([
  // { field: 'dataResourceName', header: 'Collection Name', minWidth: '150px', maxWidth: '150px' },
  // { field: 'scientificName', header: 'Scientific Name', minWidth: '150px', maxWidth: '150px' },
  { field: 'locality', header: 'Locality', minWidth: '250px', maxWidth: '250px' }
  // { field: 'catalogNumber', header: 'CatalogNumber', minWidth: '150px', maxWidth: '150px' }
])

const nameFilterMatchModes = [
  { label: 'Starts With', value: 'startsWith' },
  { label: 'Contains', value: 'contains' },
  { label: 'Equals', value: 'equals' }
]

const selectedRecord = ref()
const records = ref()
const collectionOptions = ref()
let filterArray = ref([])

const loading = ref(false)

const filters = ref({
  dataResourceName: { value: null, matchMode: FilterMatchMode.EQUALS },
  scientificName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  catalogNumber: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  locality: { value: null, matchMode: FilterMatchMode.CONTAINS }
})

const oldFilters = ref()

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

watch(
  () => store.getters['results'],
  (newValue, oldValue) => {
    records.value = newValue
  }
)

watch(
  () => store.getters['collections'],
  (newValue, oldValue) => {
    const data = newValue
    if (data) {
      collectionOptions.value = data.map((item) => item.val)
    }
  }
)

onMounted(async () => {
  records.value = store.getters['results']
  const collections = store.getters['collections']

  collectionOptions.value = collections ? collections.map((item) => item.val) : []
  columns.value = defaultColumns.value
})

function getColumnStyle(col) {
  return {
    minWidth: col.minWidth,
    maxWidth: col.maxWidth,
    width: col.width || 'auto',
    overflow: 'visible',
    whiteSpace: 'wrap'
  }
}

function onFilterChange(filterModel, filterCallback) {
  filterModel.value = null
  filterCallback()
}

function onFilter(event) {
  filterArray.value = []

  const { filters: filterMeta, first, rows } = event

  const dataResource = filterMeta.dataResourceName
  const scientificName = filterMeta.scientificName
  const catalogNumber = filterMeta.catalogNumber
  const locality = filterMeta.locality

  let filterSearch = false

  if (oldFilters.value) {
    console.log('here....')
    if (oldFilters.value.dataResourceName.value !== filterMeta.dataResourceName.value) {
      filterSearch = true
    }

    if (oldFilters.value.scientificName.value !== filterMeta.scientificName.value) {
      if (!scientificName.value || scientificName.value.length >= 3) {
        filterSearch = true
      }
    }

    if (oldFilters.value.catalogNumber.value !== filterMeta.catalogNumber.value) {
      if (!catalogNumber.value || catalogNumber.value.length >= 3) {
        filterSearch = true
      }
    }
  } else {
    console.log('here1....')
    if (dataResource.value) {
      buildFilter(dataResource, 'dataResourceName', true)
      filterSearch = true
    }

    console.log('scientificName', scientificName.value)
    if (scientificName.value && scientificName.value.length >= 3) {
      console.log('scientificName 1...', scientificName.value)
      filterSearch = true
    }

    if (catalogNumber.value && catalogNumber.value.length >= 3) {
      console.log('why...', !catalogNumber.value)

      console.log('why.1..', catalogNumber.value.length >= 3)
      filterSearch = true
    }
  }

  if (dataResource.value) {
    buildFilter(dataResource, 'dataResourceName', true)
  }

  if (scientificName.value) {
    buildFilter(scientificName, 'scientificName', false)
  }

  if (catalogNumber.value) {
    buildFilter(catalogNumber, 'catalogNumber', false)
  }

  if (filterSearch) {
    console.log('start search...')
    oldFilters.value = event.filters
    loadRecordsLazy(first, rows)
  }
}

function buildFilter(data, filterKey, isArray) {
  let value

  if (isArray) {
    const string = data.value.map((val) => `'${val}'`).join(' ')
    value = `(${string})`
  } else {
    value = data.value
  }
  const matchMode = data.matchMode

  const json = {
    key: filterKey,
    value,
    matchMode
  }
  filterArray.value.push(json)
  console.log('filterArray', filterArray.value)
}

async function loadRecordsLazy(first, rows) {
  loading.value = true

  let params = buildParams()

  await service
    .apiSearch(params, first, rows)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const geofacet = response.facets.map.buckets

        store.commit('setGeoData', geofacet)
      } else {
        store.commit('setGeoData', null)
      }

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

function buildParams() {
  const fields = store.getters['fields']

  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']
  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  // const collectionGroup = store.getters['collectionGroup']

  const params = new URLSearchParams({
    text: searchText
  })

  if (isType) {
    params.set('typeStatus', '*')
  }

  if (isInSweden) {
    params.set('country', 'Sweden')
  }

  if (hasImages) {
    params.set('hasImage', hasImages)
  }

  if (hasCoordinates) {
    params.set('lat_long', '*')
  }

  if (startDate) {
    params.set('startDate', startDate)
  }

  if (endDate) {
    params.set('endDate', endDate)
  }

  const hasDataResource = filterArray.value.some((obj) => obj.key === 'dataResourceName')
  let dataResource
  if (hasDataResource) {
    dataResource = filterArray.value.filter((item) => item.key === 'dataResourceName')[0].value
  } else {
    dataResource = store.getters['dataResource']
  }
  if (dataResource) {
    let newValue = dataResource.replace(/'/g, '"')
    params.set('dataResourceName', newValue)
  }

  let scientificName
  let searchMode
  let isFuzzy
  const hasScientificName = filterArray.value.some((obj) => obj.key === 'scientificName')
  if (hasScientificName) {
    const scientificNameData = filterArray.value.filter((item) => item.key === 'scientificName')[0]
    scientificName = scientificNameData.value
    searchMode = scientificNameData.matchMode
    isFuzzy = searchMode !== 'equals'
  } else {
    scientificName = store.getters['scientificName']
    if (scientificName) {
      isFuzzy = store.getters['isFuzzySearch']
      searchMode = store.getters['searchMode']
    }
  }
  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('searchMode', searchMode)
    params.set('fuzzySearch', isFuzzy)
  }

  const hasCatalogNumber = filterArray.value.some((obj) => obj.key === 'catalogNumber')
  if (hasCatalogNumber) {
    const catalogNumber = filterArray.value.filter((item) => item.key === 'catalogNumber')[0].value
    params.set('catalogNumber', catalogNumber + '*')
  } else {
    fields
      .filter((field) => field.value === 'catalogNumber')
      .forEach((field) => {
        if (field.text) {
          params.set(field.value, field.text)
        }
      })
  }

  if (fields) {
    fields
      .filter((field) => field.text && field.value !== 'catalogNumber')
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }
  return params
}

function onSelect() {
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${selectedRecord.value.id}`)
}

function selectRow(data) {
  selectedRecord.value = data
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${data.id}`)
}

const onPage = async (event) => {
  const { first, rows } = event
  emits('search', first, rows, false)
}
</script>
<style scoped>
.small-placeholder::placeholder {
  font-size: 0.8rem; /* or use 12px, etc. */
}

/* Hide the clear filter icon in DataTable header */
.p-column-filter-clear-icon {
  display: none !important;
}

.p-datatable {
  font-size: 12px !important;
  padding-top: 0 !important;
}

.p-button-text {
  text-decoration: underline;
}

::v-deep .p-select {
  background: #ffffff !important;
}
</style>
