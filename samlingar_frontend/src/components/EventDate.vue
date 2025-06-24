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
    <!-- <div class="col-12">
      <div class="grid" id="dates">
        <div class="col-10">
          <div class="grid">
            <div class="col-6">
              <div class="flex flex-col gap-2 selectGroup">
                <label class="searchLabel">
                  {{ $t('search.start') }}
                </label>
                <DatePicker
                  v-model="beginDate"
                  dateFormat="yy-m-dd"
                  showIcon
                  fluid
                  size="small"
                  inputId="startDate"
                  :placeholder="$t('search.beginDateLabel')"
                  @date-select="onSelect"
                />
              </div>
            </div>
            <div class="col-6">
              <div class="flex flex-col gap-2 selectGroup">
                <label class="searchLabel">
                  {{ $t('search.end') }}
                </label>
                <DatePicker
                  v-model="endDate"
                  dateFormat="yy-m-dd"
                  showIcon
                  size="small"
                  fluid
                  inputId="endDate"
                  :placeholder="$t('search.endDateLabel')"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div> -->
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import DatePicker from 'primevue/datepicker'

const store = useStore()

const dates = ref()

const emits = defineEmits(['search'])

const displayButton = computed(() => {
  return dates.value
})

onMounted(async () => {
  console.log('event date')

  dates.value = store.getters['dates']
})

function onSelect() {
  console.log('dates : ', dates)
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
  emits('search')
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
