<template>
  <div class="grid" id="eventDateDiv">
    <div class="col-12">
      <div class="card flex gap-3" style="padding-bottom: 0.5em">
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="option1"
            name="option1"
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
            name="option2"
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

      <div class="card flex justify-center" v-if="isFilterByDate">
        <DatePicker
          v-model="dates"
          size="small"
          selectionMode="range"
          showIcon
          @date-select="onSelect"
          dateFormat="yy-mm-dd"
          :manualInput="false"
          class="w-full sm:w-[30rem]"
        />
        <Button
          icon="pi pi-times"
          @click="removeDates()"
          variant="text"
          rounded
          :aria-label="$t('btnLabel.removeDates')"
          v-if="displayButton"
        />
      </div>

      <div class="card flex justify-center" v-else>
        <div class="col-6">
          <DatePicker
            size="small"
            v-model="startYear"
            view="year"
            dateFormat="yyyy"
            style="min-width: 200px"
            :placeholder="$t('search.startYear')"
          />
        </div>
        <div class="col-6">
          <DatePicker
            size="small"
            v-model="endYear"
            view="year"
            dateFormat="yyyy"
            style="min-width: 200px"
            :placeholder="$t('search.endYear')"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import DatePicker from 'primevue/datepicker'

const store = useStore()

const dates = ref()
const startYear = ref()
const endYear = ref()

const searchOptions = ref('date')

const displayButton = computed(() => {
  return dates.value
})

const isFilterByDate = computed(() => {
  return searchOptions.value === 'date'
})

watch(
  () => store.getters['dates'],
  (newValue, oldValue) => {
    if (store.getters['dates'] === null) {
      removeDates()
    }
  }
)

onMounted(async () => {
  dates.value = store.getters['dates']
})

function change() {}

function onSelect() {
  const start = new Date(dates.value[0])
  const end = new Date(dates.value[1])

  const utcStartDate = new Date(
    Date.UTC(start.getFullYear(), start.getMonth(), start.getDate(), 0, 0, 0)
  )
  const startDate = utcStartDate.toISOString() // Includes time and Z

  const utcEndDate = new Date(Date.UTC(end.getFullYear(), end.getMonth(), end.getDate(), 0, 0, 0))

  const endDate = utcEndDate.toISOString() // Includes time and Z

  store.commit('setStartDate', startDate)
  store.commit('setEndDate', endDate)
  store.commit('setDates', startDate + ' - ' + endDate)
}

function removeDates() {
  dates.value = null

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)
}
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
</style>
