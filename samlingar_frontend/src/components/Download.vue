<template>
  <div>
    <Button link @click="exportData" v-if="!dataPrepared" style="width: 420px">
      <small>{{ $t('exportData.export') }}</small>
    </Button>
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
      style="color: #0dff5c; font-size: 14px; cursor: pointer; padding-left: 10px"
    >
      <small>Download Data</small>
    </downloadexcel>
    <ProgressSpinner
      v-if="isLoading"
      aria-label="Loading"
      style="width: 50px; height: 50px; position: relative; padding-left: 80%"
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
