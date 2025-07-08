<template>
  <div>
    <div v-if="!dataPrepared" class="preparaDataLink" @click="exportData">
      <small>
        <i>{{ $t('exportData.export') }}</i>
      </small>
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

    <!-- <ProgressSpinner
      v-if="isLoading"
      aria-label="Loading"
      style="width: 50px; height: 50px; position: fixed; padding-left: 20%; z-index: 200"
      strokeWidth="8"
      fill="transparent"
    /> -->
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import downloadexcel from 'vue-json-excel3'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['exportData', 'download'])

let json_data = ref()
let dataPrepared = ref(false)
const isLoading = ref(false)

watch(
  () => store.getters['exportData'],
  () => {
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
  'State Province': 'stateProvince',
  Country: 'country',
  Continent: 'continent',
  Latitude: 'decimalLatitude',
  Longitude: 'decimalLongitude',
  'Station field number': 'fieldNumber',
  Family: 'family',
  Genus: 'genus',
  Species: 'species',
  Prepration: 'preparations',
  Preservation: 'dynamicProperties_preservation',
  'Vernacular Name': 'vernacularName',
  'Event date': 'eventDate',
  'Recorded by': 'recordedBy',
  'Expedition name': 'dynamicProperties_expeditionNam'
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
  emits('download')
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
