<template>
  <div class="w-full">
    <div class="block">
      <DatePicker
        v-model="dates"
        size="small"
        ref="datepickerRef"
        inputId="dateRange"
        aria-label="Select a date"
        aria-required="true"
        aria-describedby="dateHelp"
        selectionMode="range"
        showIcon
        @date-select="onSelect"
        dateFormat="yy-mm-dd"
        :manualInput="true"
        class="w-full sm:w-[30rem]"
      >
        <template #footer>
          <div class="p-d-flex p-jc-end p-w-full" style="float: right">
            <button type="button" class="p-button p-component p-button-text" @click="removeDates">
              {{ $t('btnLabel.removeDates') }}
            </button>
          </div>
        </template>
      </DatePicker>
    </div>

    <p id="dateHelp" class="mt-2 text-sm text-gray-500">
      {{ $t('tip.dateRangeHelp') }}
    </p>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import DatePicker from 'primevue/datepicker'

const store = useStore()

const dates = ref()
const datepickerRef = ref(null)

onMounted(async () => {
  dates.value = store.getters['dates']
})

const toUtcIso = (date) => {
  if (!date) return null
  return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())).toISOString()
}

function onSelect() {
  const [start, end] = dates.value

  const startIso = start ? toUtcIso(new Date(start)) : null
  const endIso = end ? toUtcIso(new Date(end)) : null

  console.log('endIso', endIso)

  store.commit('setStartDate', startIso)
  store.commit('setEndDate', endIso)
  store.commit('setDates', dates.value)

  if (dates.value[0] && dates.value[1]) {
    closeCalendar()
  }
}

function removeDates() {
  dates.value = null

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)
  closeCalendar()
}

function closeCalendar() {
  datepickerRef.value && (datepickerRef.value.overlayVisible = false)
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
