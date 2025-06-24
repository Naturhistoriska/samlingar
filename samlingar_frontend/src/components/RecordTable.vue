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
      @page="onPage($event)"
      :rows="10"
      :rowsPerPageOptions="[5, 10, 20]"
      :totalRecords="totalCount"
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

let columns = ref([
  { field: 'collectionName', header: 'Collection Name' },
  { field: 'scientificName', header: 'Scientific Name' },
  { field: 'locality', header: 'Locality' },
  { field: 'catalogNumber', header: 'CatalogNumber' }
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
  console.log('total count', store.getters['totalRecords'])
  return store.getters['totalRecords']
})

onMounted(async () => {
  // emits('search', 0, 20, true)
  records.value = store.getters['results']
})

const exportCSV = () => {
  dt.value.exportCSV()
}

function selectRow(data) {
  selectedRecord.value = data
  store.commit('setSelectedRecord', selectedRecord)

  router.push(`/record/${data.id}`)
}

const onPage = async (event) => {
  const { first, rows } = event
  console.log('onPage', first, rows)
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
