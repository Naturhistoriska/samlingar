<template>
  <div>
    <div
      v-if="!dataPrepared"
      :class="exportCss"
      @click="exportData"
      :style="{
        pointerEvents: isLoading ? 'none' : 'auto',
        cursor: isLoading ? 'none' : 'pointer',
        opacity: isLoading ? 0.5 : 1
      }"
    >
      <small>
        <i>{{ exportText }}</i>
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
      <small>{{ downLoadText }}</small>
      <small style="padding-left: 10px"><i class="pi pi-download"></i></small>
    </downloadexcel>
    <VueSpinnerDots v-if="isLoading" size="20" color="red" />
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue'
import downloadexcel from 'vue-json-excel3'
import { useStore } from 'vuex'

import { useI18n } from 'vue-i18n'

const { t } = useI18n()

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

const downLoadText = computed(() => {
  return isLoading.value ? t('exportData.downloading') : t('exportData.download')
})

const exportText = computed(() => {
  return isLoading.value ? t('exportData.prepareData') : t('exportData.export')
})

const exportCss = computed(() => {
  return isLoading.value ? 'fade-in-text' : 'preparaDataLink'
})

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
  /* cursor: pointer !important; */
}
.preparaDataLink:hover {
  color: var(--p-emerald-500) !important;
  font-size: 15px;
  text-decoration: none;
}

.fade-in-text {
  animation: fadeIn 2s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
