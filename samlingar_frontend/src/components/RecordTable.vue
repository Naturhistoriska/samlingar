<template>
  <div class="card table-wrapper" :class="{ 'hide-paginator': hidePaginator }">
    <!-- Loading overlay -->
    <div v-if="loading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>

    <DataTable
      class="p-datatable-sm responsive-table"
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
      v-model:filters="filters"
      :first="firstPage"
      :rows="20"
      :rowsPerPageOptions="[10, 20, 50]"
      :totalRecords="totalCount"
      :filters="filters"
      :value="records"
      @filter="onFilter"
      @page="onPage($event)"
      @row-select="onRowSelect"
      @sort="onSort"
      responsiveLayout="scroll"
    >
      <template #empty>{{ $t('search.noResultsFound') }}</template>
      <template #loading>{{ $t('search.loadingData') }}</template>
      <!-- Collection -->
      <Column
        field="collectionName"
        :header="$t('labels.collectionName')"
        :showFilterMenu="false"
        style="min-width: 8rem"
      >
        <template #body="{ data }">
          <small>{{ data.collectionName }}</small>
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <MultiSelect
            v-model="filterModel.value"
            @change="filterCallback()"
            :options="collectionOptions"
            :placeholder="$t('search.filterCollections')"
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
      </Column>
      <!-- Scientific Name -->
      <Column
        field="scientificName"
        :header="$t('labels.scientificName')"
        style="min-width: 10rem"
        filterField="scientificName"
        sortable
        :filter="true"
        :filterMatchModeOptions="nameFilterMatchModes"
      >
        <template #body="{ data }">
          <div class="text-xs md:text-sm">
            {{ getTaxon(data) }}<br />
            {{ buildClassification(data) }}
          </div>
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            v-model="filterModel.value"
            type="text"
            size="small"
            class="small-placeholder w-full"
            @input="onScientificNameFilterInput($event, filterModel)"
            :placeholder="$t('search.filterScientificname')"
          />
        </template>
      </Column>

      <!-- Catalog Number -->
      <Column
        field="catalogNumber"
        :header="$t('labels.catalogNumber')"
        :showFilterMenu="false"
        sortable
        style="min-width: 5rem"
      >
        <template #body="{ data }">
          <small>{{ data.catalogNumber }}</small>
        </template>
        <template #filter="{ filterModel }">
          <InputText
            v-model="filterModel.value"
            type="text"
            size="small"
            class="small-placeholder w-full"
            @input="onCatalogNumberFilterInput($event, filterModel)"
            :placeholder="$t('search.filterCatalogNumber')"
          />
        </template>
      </Column>
      <!-- Locality -->
      <Column
        field="locality"
        :header="$t('labels.locality')"
        sortable
        :showFilterMenu="false"
        style="min-width: 12rem"
      >
        <template #body="{ data }">
          <div class="text-xs md:text-sm">
            {{ data.locality }}<br />
            {{ data.continent }} {{ data.country }} {{ data.stateProvince }} {{ data.county }}
          </div>
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            type="text"
            size="small"
            class="small-placeholder w-full"
            v-model="filterModel.value"
            @input="onLocalityFilterInput($event, filterModel, filterCallback)"
            :placeholder="$t('search.filterLocality')"
          />
        </template>
      </Column>

      <Column style="min-width: 2rem; text-align: center">
        <template #body="{ data }">
          <Button
            text
            icon="pi pi-external-link"
            @click="selectRow(data)"
            severity="secondary"
            class="p-button-sm"
          ></Button>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<script setup>
import { onBeforeUnmount, computed, onMounted, ref, watch } from 'vue'
// import { useToast } from 'primevue/usetoast'
import { FilterMatchMode } from '@primevue/core/api'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

const store = useStore()
const router = useRouter()

const service = new Service()
// const toast = useToast()

// const emits = defineEmits(['search'])

let columns = ref([])
const dt = ref()
const hidePaginator = ref(false)
let lastScroll = 0

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
// const expandedRows = ref({})
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
  () => store.getters['pageNum'],
  (newValue, oldValue) => {
    if (newValue === 0) {
      firstPage.value = 0
    }
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
    const data = newValue
    if (data) {
      collectionOptions.value = data.map((item) => item.val)
    } else {
      collectionOptions.value = null
    }
    clearFilters()
  }
)

const handleScroll = () => {
  const current = window.scrollY
  hidePaginator.value = current > lastScroll && current > 100
  lastScroll = current
}

onMounted(async () => {
  window.addEventListener('scroll', handleScroll, { passive: true })
  records.value = store.getters['results']
  const collections = store.getters['selectedCollectionGroup']

  collectionOptions.value = collections ? collections.map((item) => item.val) : []
  columns.value = defaultColumns.value

  const currentPage = store.getters['pageNum']
  console.log('currentPage', currentPage)
  if (currentPage > 0) {
    const rowsPerPage = store.getters['rowsPerPage']
    firstPage.value = currentPage * rowsPerPage
  }
})

onBeforeUnmount(() => window.removeEventListener('scroll', handleScroll))

function getTaxon(data) {
  const { collectionCode, genus, scientificName, species, taxonRank } = data

  if (collectionCode === 'pz' || collectionCode === 'pb') {
    return taxonRank === 'species' ? genus + ' ' + species : scientificName
  } else if (collectionCode == 'PI' || collectionCode === 'HE' || collectionCode === 'vp') {
    return taxonRank === 'Species' ? genus + ' ' + species : scientificName
  } else {
    return scientificName
  }
}

function buildClassification(data) {
  const { kingdom, phylum, clazz, order, family, genus, subgenus } = data

  const higherClassification = new Array(kingdom, phylum, clazz, order, family, genus, subgenus)
  return higherClassification.filter((str) => str !== undefined).join(' > ')
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

function onLocalityFilterInput(event, filterModel, filterCallback) {
  console.log('onLocalityFilterInput')
  const value = event.target.value

  if (value.length >= 2 || value.length === 0) {
    dt.value.filter(filters.value.locality.value, 'locality', 'contains')
    filters.value.locality.value = value
  }
  // else {
  //   if (value.length === 0) {
  //     dt.value.filter(filters.value.locality.value, 'locality', 'contains')
  //     filters.value.locality.value = value
  //   }
  // }
}

function onCatalogNumberFilterInput(event, filterModel) {
  const value = event.target.value

  if (value.length >= 2 || value.length === 0) {
    dt.value.filter(filters.value.catalogNumber.value, 'catalogNumber', 'startsWith')
    filters.value.catalogNumber.value = value
  }
  // else {
  //   if (value.length === 0) {
  //     dt.value.filter(filters.value.catalogNumber.value, 'catalogNumber', 'startsWith')
  //     filters.value.catalogNumber.value = value
  //   }
  // }
}

function onScientificNameFilterInput(event, filterModel) {
  const value = event.target.value
  if (value.length >= 2 || value.length === 0) {
    dt.value.filter(filters.value.scientificName.value, 'scientificName', 'equals')
    filters.value.scientificName.value = value
  }
  // else {
  //   if () {
  //     dt.value.filter(filters.value.scientificName.value, 'catalogNumber', 'equals')
  //     filters.value.scientificName.value = value
  //   }
  // }
}

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
  const fullTextSearchMode = store.getters['fullTextSearchMode']

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']
  const startYear = store.getters['startYear']
  const endYear = store.getters['endYear']
  const dateFilter = store.getters['dateFilter']

  // const collectionGroup = store.getters['collectionGroup']

  const params = new URLSearchParams({
    catchall: searchText,
    mode: fullTextSearchMode
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

  if (dateFilter === 'date') {
    if (startDate) {
      params.set('startDate', startDate)
    }
    if (endDate) {
      params.set('endDate', endDate)
    }
  } else {
    if (startYear && endYear) {
      const yearQuery = `[${startYear.getFullYear()} TO ${endYear.getFullYear()}]`
      params.set('year', yearQuery)
    }
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
  const hasScientificName = filterArray.value.some((obj) => obj.key === 'scientificName')
  if (hasScientificName) {
    const scientificNameData = filterArray.value.filter((item) => item.key === 'scientificName')[0]
    scientificName = scientificNameData.value
    searchMode = scientificNameData.matchMode
  } else {
    scientificName = store.getters['scientificName']
    if (scientificName) {
      searchMode = store.getters['searchMode']
    }
  }
  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('searchMode', searchMode)
  }

  const hasCatalogNumber = filterArray.value.some((obj) => obj.key === 'catalogNumber')
  if (hasCatalogNumber) {
    const catalogNumber =
      filterArray.value.filter((item) => item.key === 'catalogNumber')[0].value + '*'

    // const firstUpCatalogNumber = catalogNumber.charAt(0).toUpperCase() + catalogNumber.slice(1)
    // const upcaseCatalogNumber = catalogNumber.toUpperCase()
    // const value = `(${firstUpCatalogNumber} ${upcaseCatalogNumber} ${catalogNumber})`

    params.set('copy_catalogNumber', catalogNumber)
  }

  // else {
  // fields
  // .filter((field) => field.value === 'catalogNumber')
  // .forEach((field) => {
  // if (field.text) {
  // params.set(field.value, field.text)
  // }
  // })
  // }

  const hasLocality = filterArray.value.some((obj) => obj.key === 'locality')
  if (hasLocality) {
    const locality = filterArray.value.filter((item) => item.key === 'locality')[0].value

    params.set('location', locality)
  }
  // else {
  //   fields
  //     .filter((field) => field.value === 'locality')
  //     .forEach((field) => {
  //       if (field.text) {
  //         params.set(field.value, field.text)
  //       }
  //     })
  // }

  if (fields) {
    fields
      .filter(
        (field) => field.text
        // (field) => field.text && field.value !== 'catalogNumber' && field.value !== 'locality'
      )
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }
  return params
}

function onRowSelect(event) {
  selectedRecord.value = event.data
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${selectedRecord.value.id}`)
}

function selectRow(data) {
  // selectedRecord.value = data
  // store.commit('setSelectedRecord', selectedRecord)

  // router.push(`/record/${data.id}`)

  const url = `/record/${data.id}` // Example URL
  window.open(url, '_blank')
}

const onPage = async (event) => {
  const { first, rows } = event
  firstPage.value = event.first

  store.commit('setPageNum', event.page)
  store.commit('setRowsPerPage', event.rows)

  const params = store.getters['searchParams']
  loadRecordsLazy(params, first, rows)
}

// function onFilterChange(filterModel, filterCallback) {
//   filterModel.value = null
//   filterCallback()
// }

// function exportCSV() {
//   console.log('exportCSV')
//   // dt.value.exportCSV()

//   const exportFields = [
//     'scientificName',
//     'catalogNumber',
//     'locality',
//     'decimalLatitude',
//     'decimalLongitude',
//     'stateProvince',
//     'country'
//   ]
//   const exportHeaders = [
//     'Scientific Name',
//     'Catalog Number',
//     'Locality',
//     'DecimalLatitude',
//     'DecimalLongitude',
//     'Province',
//     'Country'
//   ]

//   const csvRows = [exportHeaders.join(',')]

//   records.value.forEach((record) => {
//     const row = exportFields.map((field) => {
//       let value = record[field] ?? ''
//       // Escape quotes and wrap in double quotes
//       return `"${String(value).replace(/"/g, '""')}"`
//     })
//     csvRows.push(row.join(','))
//   })

//   const csvContent = csvRows.join('\n')
//   const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })

//   const link = document.createElement('a')
//   link.href = URL.createObjectURL(blob)
//   link.download = 'records.csv'
//   document.body.appendChild(link)
//   link.click()
//   document.body.removeChild(link)
// }
</script>
<style scoped>
.responsive-table {
  font-size: 0.85rem;
}

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

:deep(.p-paginator) {
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.4rem;
  padding: 0.5rem 0;
  background: var(--surface-card, #fff);
  border-top: 1px solid var(--surface-border, #ddd);
  transition:
    transform 0.35s ease,
    opacity 0.25s ease;
  z-index: 20;
}

:deep(.p-paginator .p-dropdown),
:deep(.p-paginator .p-dropdown-label),
:deep(.p-paginator .p-dropdown-item) {
  font-size: 0.85rem;
}

/* :deep(.p-paginator .p-dropdown) {
  font-size: 0.85rem;
  height: 2rem;
  min-width: 4.5rem;
}
:deep(.p-paginator .p-dropdown .p-dropdown-label) {
  font-size: 0.85rem;
  padding: 0.25rem 0.5rem;
} */

@media (max-width: 768px) {
  :deep(.p-paginator) {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    background: rgba(255, 255, 255, 0.95);
    border-top: 1px solid var(--surface-border, #ddd);
    backdrop-filter: blur(6px);
    box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.1);
  }

  /* Add space below table to prevent overlap */
  .table-wrapper {
    padding-bottom: 4rem;
  }

  /* Hide paginator smoothly when scrolling down */
  .hide-paginator :deep(.p-paginator) {
    transform: translateY(100%);
    opacity: 0;
    pointer-events: none;
  }

  /* Compact paginator buttons */
  :deep(.p-paginator .p-paginator-page),
  :deep(.p-paginator .p-paginator-prev),
  :deep(.p-paginator .p-paginator-next) {
    min-width: 1.8rem;
    height: 1.8rem;
    font-size: 0.8rem;
  }

  /* Compact dropdown */
  /* Smaller filter inputs on phones */
  :deep(.p-inputtext),
  :deep(.p-multiselect),
  :deep(.p-dropdown) {
    font-size: 0.8rem;
  }

  /* Optional: hide report text to save space */
  :deep(.p-paginator-current) {
    display: none;
  }

  /* Add padding-bottom to table wrapper to avoid content overlap */
  .table-wrapper {
    padding-bottom: 4rem; /* height of sticky paginator area */
  }
}
</style>
