<template>
  <div class="grid" id="eventDateDiv">
    <div class="col-12">
      <div class="card flex gap-3" style="padding-bottom: 0.5em">
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="option1"
            name="dateOption"
            value="date"
            size="small"
            class="mt-1"
            @value-change="change"
          />
          <label for="option1" class="ml-2">
            <small>{{ $t('search.filterByCollectingDate') }}</small>
          </label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="option2"
            name="dateOption"
            value="year"
            class="mt-1"
            size="small"
            @value-change="change"
          />
          <label for="option2" class="ml-2">
            <small>{{ $t('search.filterByCollectingYear') }}</small>
          </label>
        </div>
      </div>

      <div class="w-full" v-if="isFilterByDate">
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
                <button
                  type="button"
                  class="p-button p-component p-button-text"
                  @click="removeDates"
                >
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

      <div class="grid formgrid" v-else>
        <div class="col-12 md:col-6 mb-3">
          <DatePicker
            size="small"
            ref="startyearpickerRef"
            v-model="startYear"
            view="year"
            dateFormat="yy"
            style="width: 100%"
            @date-select="onStartYearSelected"
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
            ref="endyearpickerRef"
            v-model="endYear"
            view="year"
            dateFormat="yy"
            :minDate="minDate"
            style="width: 100%"
            @date-select="onEndYearSelected"
            :placeholder="$t('search.endYear')"
            :showIcon="true"
          >
            <template #footer>
              <div class="p-d-flex p-jc-end p-w-full">
                <button
                  type="button"
                  class="p-button p-component p-button-text"
                  @click="removeEndYear"
                >
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

      <div class="flex grid justify-end" style="float: inline-end">
        <Button
          :label="$t('search.search')"
          @click="search"
          id="searchRecordBtn"
          class="my-custom-button"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import DatePicker from 'primevue/datepicker'

const emits = defineEmits(['dateSearch'])

const store = useStore()

const dates = ref()
const startYear = ref()
const endYear = ref()

const searchOptions = ref('date')
const datepickerRef = ref(null)
const startyearpickerRef = ref(null)
const endyearpickerRef = ref(null)

const isFilterByDate = computed(() => {
  return searchOptions.value === 'date'
})

const minDate = computed(() => {
  if (startYear.value === null) {
    return null
  }
  return startYear.value
})

watch(
  () => store.getters['dates'],
  (newValue, oldValue) => {
    if (store.getters['dates'] === null) {
      removeDates()
    }
  }
)

watch(
  () => store.getters['startYear'],
  (newValue, oldValue) => {
    if (store.getters['startYear'] === null) {
      startYear.value = null
      // store.commit('setStartYear', null)
    }
  }
)

watch(
  () => store.getters['endYear'],
  (newValue, oldValue) => {
    if (store.getters['endYear'] === null) {
      endYear.value = null
      // store.commit('setEndYear', null)
    }
  }
)

onMounted(async () => {
  console.log('onMounted')
  dates.value = store.getters['dates']

  searchOptions.value = store.getters['dateFilter']

  startYear.value = store.getters['startYear']
  endYear.value = store.getters['endYear']
  console.log('onMounted', startYear.value, endYear.value)
})

function search() {
  emits('dateSearch')
}

function change() {
  store.commit('setDateFilter', searchOptions.value)
}

function onStartYearSelected() {
  const date = new Date(startYear.value)
  console.log('date', date, startYear.value)
  // const year = date.getFullYear()
  store.commit('setStartYear', date)
}

function onEndYearSelected() {
  const date = new Date(endYear.value)
  // const year = date.getFullYear()
  store.commit('setEndYear', date)
}

function onSelect() {
  const start = new Date(dates.value[0])
  const end = new Date(dates.value[1])

  console.log('dates', dates.value)

  const utcStartDate = new Date(
    Date.UTC(start.getFullYear(), start.getMonth(), start.getDate(), 0, 0, 0)
  )
  const startDate = utcStartDate.toISOString() // Includes time and Z

  const utcEndDate = new Date(Date.UTC(end.getFullYear(), end.getMonth(), end.getDate(), 0, 0, 0))

  const endDate = utcEndDate.toISOString() // Includes time and Z

  store.commit('setStartDate', startDate)
  store.commit('setEndDate', endDate)
  store.commit('setDates', dates.value)

  if (dates.value[0] && dates.value[1]) {
    if (datepickerRef.value) {
      datepickerRef.value.overlayVisible = false
    }
  }
}

function removeStartYear() {
  startYear.value = null
  store.commit('setStartYear', null)
  if (startyearpickerRef.value) {
    startyearpickerRef.value.overlayVisible = false
  }
}

function removeEndYear() {
  endYear.value = null
  store.commit('setEndYear', null)
  if (endyearpickerRef.value) {
    endyearpickerRef.value.overlayVisible = false
  }
}

function removeDates() {
  dates.value = null

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)

  if (datepickerRef.value) {
    datepickerRef.value.overlayVisible = false
  }
}
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
.p-button-text {
  color: #144836;
}

.p-button-text:hover {
  color: #fff !important;
  background: #1d634a !important;
}
</style>
