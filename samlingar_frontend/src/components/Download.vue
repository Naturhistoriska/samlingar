<template>
  <div v-if="isLoading" class="spinner-overlay">
    <div class="spinner"></div>
  </div>
  <div class="grid">
    <div
      class="col-7"
      no-gutters
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
    <div class="col-2">
      <RadioButton
        v-model="exportFormat"
        inputId="excel"
        name="csv"
        value="excel"
        size="small"
        @click="onClick"
      />
      <label for="excel" class="text-sm customRadio" style="padding-left: 1em">Excel</label>
    </div>
    <div class="col-2">
      <RadioButton
        v-model="exportFormat"
        inputId="csv"
        name="csv"
        value="csv"
        size="small"
        @click="onClick"
      />
      <label for="csv" class="text-sm customRadio" style="padding-left: 1em">CSV</label>
    </div>

    <VueSpinnerDots v-if="isLoading" size="20" color="red" />
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue'

import { Parser } from '@json2csv/plainjs'
import * as XLSX from 'xlsx'

import Service from '../Service'
import { useStore } from 'vuex'

import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const store = useStore()
const service = new Service()

const emits = defineEmits(['exportData'])

let json_data = ref()
const isLoading = ref(false)

const exportFormat = ref('excel')

watch(
  () => store.getters['exportData'],
  () => {
    json_data.value = store.getters['exportData']
    setTimeout(() => {
      isLoading.value = false
    }, 2000)
  }
)

const exportText = computed(() => {
  return isLoading.value ? t('exportData.prepareData') : t('exportData.export')
})

const exportCss = computed(() => {
  return isLoading.value ? 'fade-in-text' : 'preparaDataLink'
})

function exportData() {
  isLoading.value = true
  const totalRecords = store.getters['totalRecords']

  const params = store.getters['searchParams']

  service
    .apiPreparaExport(params, totalRecords)
    .then((response) => {
      const jsonData = response

      if (exportFormat.value === 'csv') {
        downloadCSV(jsonData)
      } else {
        downExcel(jsonData)
      }

      setTimeout(() => {
        isLoading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {
      isLoading.value = false
    })
}

function downExcel(jsonData) {
  const worksheet = XLSX.utils.json_to_sheet(jsonData)

  const sheetName = 'Sheet1'
  const fileName = 'data.xlsx'
  // Create a new workbook and append the worksheet
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, sheetName)

  // Write the Excel file and trigger download
  XLSX.writeFile(workbook, fileName)
}

async function downloadCSV(jsonData) {
  try {
    const parser = new Parser()
    const csv = parser.parse(jsonData)

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)

    const link = document.createElement('a')
    link.href = url
    link.download = 'export.csv'
    document.body.appendChild(link)
    link.click()

    // ♻️ Clean up
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
  } catch (error) {
    console.error('CSV download error:', error)
  }
  isLoading.value = false
}

function onClick() {
  console.log('onClick', exportFormat.value)
}
</script>
<style scoped>
.customRadio {
  color: #144836 !important;
}
.preparaDataLink {
  font-size: 14px;
  text-decoration: underline;
  color: #144836;
  /* cursor: pointer !important; */
}
.preparaDataLink:hover {
  color: #1d634a;
  font-size: 14px;
  text-decoration: none;
}

.fade-in-text {
  animation: fadeIn 2s ease-in-out;
}

/* Change the dot inside the selected radio */
:deep(.p-radiobutton-icon) {
  background-color: #144836 !important;
  color: #144836 !important;
}

/* Change the color of the selected radio button */
:deep(.p-radiobutton-box.p-highlight) {
  background-color: #144836 !important;
}

/* Optional: unselected state styling */
/* :deep(.p-radiobutton-box) { */
/* border: 2px solid #144836 !important; */
/* transition: all 0.3s ease !important; */
/* } */

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
  z-index: 9999;
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
