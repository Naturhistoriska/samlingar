<template>
  <div class="grid formgrid">
    <div class="col-12 md:col-6 mb-3">
      <DatePicker
        size="small"
        ref="startPickerRef"
        v-model="startYear"
        view="year"
        dateFormat="yy"
        style="width: 100%"
        @date-select="updateStartYear"
        :placeholder="$t('search.startYear')"
        :showIcon="true"
        :manualInput="true"
      >
        <template #footer>
          <div class="p-d-flex p-jc-end p-w-full">
            <button
              type="button"
              class="p-button p-component p-button-text"
              @click="removeStartYear"
            >
              {{ $t('btnLabel.removeDates') }}
            </button>
          </div>
        </template>
      </DatePicker>
    </div>
    <div class="col-12 md:col-6 mb-3">
      <DatePicker
        size="small"
        ref="endPickerRef"
        v-model="endYear"
        view="year"
        dateFormat="yy"
        :minDate="minDate"
        style="width: 100%"
        @date-select="updateEndYear"
        :placeholder="$t('search.endYear')"
        :showIcon="true"
      >
        <template #footer>
          <div class="p-d-flex p-jc-end p-w-full">
            <button type="button" class="p-button p-component p-button-text" @click="removeEndYear">
              {{ $t('btnLabel.removeDates') }}
            </button>
          </div>
        </template>
      </DatePicker>
    </div>
    <div class="col-12">
      <p id="yearHelp" class="mt-2 text-sm text-gray-500">
        {{ $t('tip.dateRangeHelp') }}
      </p>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import DatePicker from 'primevue/datepicker'
import Button from 'primevue/button'
import { useStore } from 'vuex'

const store = useStore()

const startYear = ref()
const endYear = ref()

const startPickerRef = ref(null)
const endPickerRef = ref(null)

const minDate = computed(() => startYear.value || null)

onMounted(async () => {
  startYear.value = store.getters['startYear']
  endYear.value = store.getters['endYear']
})

const updateStartYear = () => {
  store.commit('setStartYear', startYear.value ? new Date(startYear.value) : null)
}

const updateEndYear = () => {
  store.commit('setEndYear', endYear.value ? new Date(endYear.value) : null)
}

function removeStartYear() {
  startYear.value = null
  store.commit('setStartYear', null)

  startPickerRef.value && (startPickerRef.value.overlayVisible = false)
}

function removeEndYear() {
  endYear.value = null
  store.commit('setEndYear', null)
  endPickerRef.value && (endPickerRef.value.overlayVisible = false)
}
</script>
<style scoped>
.p-button-text {
  color: #fff;
  background: #1d634a !important;
}
.p-button-text:hover {
  color: #fff !important;
  background: #0b583d !important;
}
</style>
