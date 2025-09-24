<template>
  <div class="card">
    <div v-if="loading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>
    <DataTable
      class="p-datatable-sm"
      dataKey="id"
      filterDisplay="row"
      lazy
      paginator
      ref="dt"
      selectionMode="single"
      sortMode="multiple"
      stripedRows
      tableStyle="min-width: 50rem"
      v-model:selection="selectedRecord"
      v-model:expandedRows="expandedRows"
      v-model:filters="filters"
      :first="firstPage"
      :rows="20"
      :rowsPerPageOptions="[10, 20, 50]"
      :totalRecords="totalCount"
      :filters="filters"
      :value="records"
      @filter="onFilter"
      @page="onPage($event)"
      @rowExpand="onRowExpand"
      @rowCollapse="onRowCollapse"
      @sort="onSort"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold"> </span>

          <div class="text-end pb-4">
            <Button
              icon="pi pi-external-link"
              :label="$t('exportData.exportToCsv')"
              @click="exportCSV($event)"
              class="my-custom-button"
            />
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
      <Column expander style="width: 2rem" />
      <Column
        field="collectionName"
        header="Collection name"
        :showFilterMenu="false"
        style="min-width: 8rem; max-width: 8rem"
      >
        <template #body="{ data }">
          {{ data.collectionName }}
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
        <!-- <template #filtericon>
          <i :class="filters.dataResourceName?.value ? 'pi pi-filter-fill' : 'pi pi-filter'" />
        </template>
        <template #filterclearicon>
        </template> -->
      </Column>

      <Column
        field="scientificName"
        header="Scientific Name"
        style="min-width: 10rem; max-width: 10rem"
        filterField="scientificName"
        sortable
        :filter="true"
        :filterMatchModeOptions="nameFilterMatchModes"
      >
        <template #body="{ data }">
          {{ data.scientificName }}
          <br />
          {{ buildClassification(data) }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            v-model="filterModel.value"
            type="text"
            size="small"
            class="small-placeholder w-full"
            @input="onScientificNameFilterInput($event, filterModel)"
            placeholder="Filter by scientificName"
          />
        </template>
      </Column>

      <Column
        field="catalogNumber"
        header="CatalogNumber"
        :showFilterMenu="false"
        sortable
        style="min-width: 6 rem; max-width: 8rem"
      >
        <template #body="{ data }">
          {{ data.catalogNumber }}
        </template>
        <template #filter="{ filterModel }">
          <InputText
            v-model="filterModel.value"
            type="text"
            size="small"
            class="small-placeholder w-full"
            @input="onCatalogNumberFilterInput($event, filterModel)"
            placeholder="Filter by catalogNumber"
            style="font-size: 0.8rem"
          />
        </template>
      </Column>

      <Column
        field="locality"
        header="Locality"
        sortable
        :showFilterMenu="false"
        style="min-width: 12rem; max-width: 12rem"
      >
        <template #body="{ data }">
          {{ data.locality }}<br />
          {{ data.continent }} {{ data.country }}
          {{ data.stateProvince }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            type="text"
            size="small"
            class="small-placeholder w-full"
            v-model="filterModel.value"
            @input="onLocalityFilterInput($event, filterModel, filterCallback)"
            placeholder="Search..."
          />
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
      <template #expansion="slotProps">
        <div class="p-2">
          <b>
            {{ slotProps.data.scientificName }}
          </b>
          <span v-if="slotProps.data.scientificNameAuthorship"
            >({{ slotProps.data.scientificNameAuthorship }})</span
          >
          <br />
          {{ buildClassification(slotProps.data) }}<br />
          {{ slotProps.data.locality }}
          {{ buildHighGeographigher(slotProps.data) }}<br />
          {{ slotProps.data.decimalLatitude }}, {{ slotProps.data.decimalLongitude }}<br />
        </div>
      </template>
    </DataTable>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useToast } from 'primevue/usetoast'
import { FilterMatchMode } from '@primevue/core/api'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

const store = useStore()
const router = useRouter()

const service = new Service()
const toast = useToast()

// const emits = defineEmits(['search'])

let columns = ref([])
const dt = ref()
const showColumn = ref(false)
// const params = ref()

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
const expandedRows = ref({})
const records = ref()
const collectionOptions = ref()
let filterArray = ref([])

let firstPage = ref(0)
const loading = ref(false)

const filters = ref({
  collectionName: { value: null, matchMode: FilterMatchMode.EQUALS },
  scientificName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  catalogNumber: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  locality: { value: null, matchMode: FilterMatchMode.CONTAINS }
})

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

watch(
  () => filterArray.value,
  (newValue, oldValue) => {
    console.log('filters', newValue, oldValue)
  }
)

watch(
  () => store.getters['results'],
  (newValue, oldValue) => {
    records.value = newValue
  }
)

watch(
  () => store.getters['selectedCollectionGroup'],
  (newValue, oldValue) => {
    console.log('why...', newValue)
    const data = newValue
    if (data) {
      collectionOptions.value = data.map((item) => item.val)
    } else {
      collectionOptions.value = null
    }
    clearFilters()
  }
)

onMounted(async () => {
  console.log('record table onMounted')
  records.value = store.getters['results']
  const collections = store.getters['selectedCollectionGroup']

  collectionOptions.value = collections ? collections.map((item) => item.val) : []
  columns.value = defaultColumns.value
})

function exportCSV() {
  console.log('exportCSV')
  // dt.value.exportCSV()

  const exportFields = [
    'scientificName',
    'catalogNumber',
    'locality',
    'decimalLatitude',
    'decimalLongitude',
    'stateProvince',
    'country'
  ]
  const exportHeaders = [
    'Scientific Name',
    'Catalog Number',
    'Locality',
    'DecimalLatitude',
    'DecimalLongitude',
    'Province',
    'Country'
  ]

  const csvRows = [exportHeaders.join(',')]

  records.value.forEach((record) => {
    const row = exportFields.map((field) => {
      let value = record[field] ?? ''
      // Escape quotes and wrap in double quotes
      return `"${String(value).replace(/"/g, '""')}"`
    })
    csvRows.push(row.join(','))
  })

  const csvContent = csvRows.join('\n')
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })

  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = 'records.csv'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

function clearFilters() {
  for (const key in filters.value) {
    filters.value[key].value = null
  }
}

function onSort(event) {
  console.log('Multi-sort event:', event)

  const sortMeta = event.multiSortMeta
  const sortString = sortMeta
    .map(({ field, order }) => `${field} ${order === 1 ? 'asc' : 'desc'}`)
    .join(', ')

  firstPage.value = 0
  let params = store.getters['searchParams']
  if (params === null) {
    params = buildParams()
  }
  params.set('sort', sortString)

  loadRecordsLazy(params, 0, 20)
}

function buildClassification(data) {
  const { kingdom, phylum, clazz, order, family, genus, subgenus } = data

  const higherClassification = new Array(kingdom, phylum, clazz, order, family, genus, subgenus)
  return higherClassification.filter((str) => str !== undefined).join(' > ')
}

function buildHighGeographigher(data) {
  const { continent, country, county, municipality, stateProvince } = data

  const highGeographigher = new Array(continent, country, stateProvince, municipality, county)
  return highGeographigher.filter((str) => str !== undefined).join(', ')
}

function onLocalityFilterInput(event, filterModel, filterCallback) {
  const value = event.target.value

  if (value.length >= 3 || value.length === 0) {
    filters.value.locality.value = value
    dt.value.filter(filters.value.locality.value, 'locality', 'contains')
  }
}

function onCatalogNumberFilterInput(event, filterModel) {
  const value = event.target.value

  if (value.length >= 3 || value.length === 0) {
    filters.value.catalogNumber.value = value
    dt.value.filter(filters.value.catalogNumber.value, 'catalogNumber', 'startsWith')
  }
}

function onScientificNameFilterInput(event, filterModel) {
  const value = event.target.value
  if (value.length >= 3 || value.length === 0) {
    filters.value.scientificName.value = value
    dt.value.filter(filters.value.scientificName.value, 'catalogNumber', 'equals')
  }
}

// function onFilterChange(filterModel, filterCallback) {
//   filterModel.value = null
//   filterCallback()
// }

function onFilter(event) {
  console.log('event', event)
  filterArray.value = []

  const { filters: filterMeta, first, rows } = event

  const collectionName = filterMeta.collectionName
  const scientificName = filterMeta.scientificName
  const catalogNumber = filterMeta.catalogNumber
  const locality = filterMeta.locality

  if (collectionName.value && collectionName.value.length > 0) {
    buildFilter(collectionName, 'collectionName', true)
  }

  if (scientificName.value) {
    buildFilter(scientificName, 'scientificName', false)
  }

  if (catalogNumber.value) {
    buildFilter(catalogNumber, 'catalogNumber', false)
  }

  if (locality.value) {
    buildFilter(locality, 'locality', false)
  }

  const params = buildParams()
  store.commit('setSearchParams', params)
  loadRecordsLazy(params, first, rows)
}

function buildFilter(data, filterKey, isArray) {
  let value
  console.log('data', data.value.length)
  if (isArray && data.value.length > 0) {
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

async function loadRecordsLazy(params, first, rows) {
  loading.value = true

  console.log('params', params.toString())
  await service
    .apiSearch(params, first, rows)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const totalGeoData = response.facets.coordinates.count
        store.commit('setTotalGeoData', totalGeoData)
      } else {
        store.commit('setTotalGeoData', 0)
      }

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      loading.value = false
      store.commit('setResetMapData', true)
      store.commit('setSearchParams', params)
    })
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
    catchall: searchText
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
    params.set('geo', '*')
  }

  if (startDate) {
    params.set('startDate', startDate)
  }

  if (endDate) {
    params.set('endDate', endDate)
  }

  const hasCollection = filterArray.value.some((obj) => obj.key === 'collectionName')
  let dataResource
  if (hasCollection) {
    dataResource = filterArray.value.filter((item) => item.key === 'collectionName')[0].value
    const newValue = dataResource.replace(/'/g, '"')
    params.set('collectionName', newValue)
  } else {
    const selectedCollection = store.getters['selectedCollection']
    if (selectedCollection !== null) {
      const newValue = selectedCollection.replace(/'/g, '"')
      params.set('collectionCode', newValue)
    }
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
    const catalogNumber =
      filterArray.value.filter((item) => item.key === 'catalogNumber')[0].value + '*'

    const firstUpCatalogNumber = catalogNumber.charAt(0).toUpperCase() + catalogNumber.slice(1)
    const upcaseCatalogNumber = catalogNumber.toUpperCase()
    const value = `(${firstUpCatalogNumber} ${upcaseCatalogNumber} ${catalogNumber})`

    params.set('catalogNumber', value)
  } else {
    fields
      .filter((field) => field.value === 'catalogNumber')
      .forEach((field) => {
        if (field.text) {
          params.set(field.value, field.text)
        }
      })
  }

  const hasLocality = filterArray.value.some((obj) => obj.key === 'locality')
  if (hasLocality) {
    const locality = filterArray.value.filter((item) => item.key === 'locality')[0].value

    params.set('locality', locality)
  } else {
    fields
      .filter((field) => field.value === 'locality')
      .forEach((field) => {
        if (field.text) {
          params.set(field.value, field.text)
        }
      })
  }

  if (fields) {
    fields
      .filter(
        (field) => field.text && field.value !== 'catalogNumber' && field.value !== 'locality'
      )
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }
  return params
}

// function onSelect() {
//   store.commit('setSelectedRecord', selectedRecord)

//   router.push(`/record/${selectedRecord.value.id}`)
// }

function selectRow(data) {
  selectedRecord.value = data
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${data.id}`)
}

const onPage = async (event) => {
  const { first, rows } = event
  firstPage.value = event.first
  const params = store.getters['searchParams']

  loadRecordsLazy(params, first, rows)
}

const onRowExpand = (event) => {
  toast.add({ severity: 'info', summary: 'Record Expanded', detail: event.data.name, life: 3000 })
}
const onRowCollapse = (event) => {
  toast.add({
    severity: 'success',
    summary: 'Product Collapsed',
    detail: event.data.name,
    life: 3000
  })
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

.spinner-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.spinner {
  border: 6px solid #f3f3f3;
  border-top: 6px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
