<template>
  <div class="grid" id="eventDateDiv">
    <div class="col-12">
      <div class="col-12">
        <div class="card flex justify-center">
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

const displayButton = computed(() => {
  return dates.value
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
