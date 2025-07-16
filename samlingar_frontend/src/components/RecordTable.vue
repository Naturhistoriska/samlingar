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
      tableStyle="min-width: 50rem"
      paginator
      lazy
      @row-select="onSelect"
      @page="onPage($event)"
      :rows="10"
      :rowsPerPageOptions="[5, 10, 20]"
      :totalRecords="totalCount"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold"> </span>
          <div style="text-align: right" clss="grid justify-end">
            <!-- <Button
              icon="pi pi-external-link"
              :label="$t('exportData.exportDatatable')"
              @click="exportCSV($event)"
            /> -->
            <Button type="button" :label="$t('btnLabel.columns')" @click="dialogVisible = true" />
          </div>
        </div>
      </template>

      <Column
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
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import DatatableColumn from './DatatableColumn.vue'

const store = useStore()
const router = useRouter()

const emits = defineEmits(['search'])

const dialogVisible = ref(false)

const dt = ref()

let columns = ref([])

const defaultColumns = ref([
  { field: 'dataResourceName', header: 'Collection Name', minWidth: '150px', maxWidth: '150px' },
  { field: 'scientificName', header: 'Scientific Name', minWidth: '150px', maxWidth: '150px' },
  { field: 'locality', header: 'Locality', minWidth: '250px', maxWidth: '250px' },
  { field: 'catalogNumber', header: 'CatalogNumber', minWidth: '150px', maxWidth: '150px' }
])

const selectedRecord = ref()
const records = ref()

watch(
  () => store.getters['results'],
  (newValue, oldValue) => {
    records.value = newValue
  }
)

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

onMounted(async () => {
  records.value = store.getters['results']
  columns.value = defaultColumns.value
})

// const exportCSV = () => {
//   dt.value.exportCSV()
// }

function getColumnStyle(col) {
  return {
    minWidth: col.minWidth,
    maxWidth: col.maxWidth,
    width: col.width || 'auto',
    overflow: 'visible',
    whiteSpace: 'wrap'
  }
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

::v-deep .p-select {
  background: #ffffff !important;
}
</style>
