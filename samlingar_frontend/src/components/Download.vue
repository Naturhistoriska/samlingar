<template>
  <div>
    <div v-if="!dataPrepared" class="preparaDataLink" @click="exportData">
      <small>{{ $t('exportData.export') }}</small>
      <small style="padding-left: 10px"><i class="pi pi-file-export"></i></small>
    </div>
    <downloadexcel
      v-else
      @click="downloadFile"
      class="btn btn-default"
      :data="json_data"
      :fields="json_fields"
      type="csv"
      name="data.csv"
      :before-finish="finishDownload"
      :before-generate="startDownload"
      style="color: var(--p-emerald-500); font-size: 14px; cursor: pointer; padding-left: 8px"
    >
      <small>Download Data</small>
      <small style="padding-left: 10px"><i class="pi pi-download"></i></small>
    </downloadexcel>

    <ProgressSpinner
      v-if="isLoading"
      aria-label="Loading"
      style="width: 50px; height: 50px; position: fixed; padding-left: 20%; z-index: 200"
      strokeWidth="8"
      fill="transparent"
    />
  </div>
</template>
<script setup>
import { computed, ref, toRaw, isProxy, watch } from 'vue'
import downloadexcel from 'vue-json-excel3'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['exportData'])

let json_data = ref()
let dataPrepared = ref(false)
const isLoading = ref(false)

watch(
  () => store.getters['exportData'],
  () => {
    console.log('data changed')
    json_data.value = store.getters['exportData']
    setTimeout(() => {
      dataPrepared.value = true
      isLoading.value = false
    }, 2000)
  }
)

let json_fields = {
  'Collection Name': 'collectionName',
  'Catalogue Number': 'catalogNumber',
  Locality: 'locality',
  District: 'district',
  'State Province': 'state',
  Country: 'country',
  Continent: 'continent',
  Latitude: 'latitudeText',
  Longitude: 'longitudeText',
  'Station field number': 'stationFieldNumber',
  Determiner: 'determiner',
  Family: 'family',
  Genus: 'genus',
  Species: 'species',
  'High classification': 'higherTx',
  Prepration: 'prepration',
  Preservation: 'preservation',
  'Vernacular Name': 'commonName',
  'Cataloged date': 'catalogedDate',
  'Event date': 'startDate',
  Collectors: 'collector',
  Author: 'author'
}

function startDownload() {
  console.log('start...')
}

function finishDownload() {
  console.log('done....')
  dataPrepared.value = false
  isLoading.value = false
}

function exportData() {
  console.log('exportData')
  isLoading.value = true
  emits('exportData')
}

function downloadFile() {
  console.log('downloadFile')
  isLoading.value = true
}
</script>
<style scoped>
.preparaDataLink {
  font-size: 14px;
  text-decoration: underline;
  cursor: pointer !important;
}
.preparaDataLink:hover {
  color: var(--p-emerald-500) !important;
  font-size: 15px;
  text-decoration: none;
}
</style>
