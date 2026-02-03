<template>
  <div class="grid" id="eventDateDiv">
    <div class="col-12">
      <div class="card flex gap-3" style="padding-bottom: 0.5em">
        <!-- eslint-disable-next-line vuejs-accessibility/label-has-for -->
        <label class="flex items-center cursor-pointer" for="option1">
          <RadioButton
            v-model="searchOptions"
            inputId="option1"
            name="dateOption"
            value="date"
            size="small"
            class="mt-1"
            @value-change="change"
          />
          <span class="ml-2">
            <small>{{ $t('search.filterByCollectingDate') }}</small>
          </span>
        </label>
        <!-- eslint-disable-next-line vuejs-accessibility/label-has-for -->
        <label class="flex items-center cursor-pointer" for="option2">
          <RadioButton
            v-model="searchOptions"
            inputId="option2"
            name="dateOption"
            value="year"
            class="mt-1"
            size="small"
            @value-change="change"
          />
          <span class="ml-2">
            <small>{{ $t('search.filterByCollectingYear') }}</small>
          </span>
        </label>
      </div>

      <div class="w-full" v-if="isFilterByDate">
        <date-picker />
      </div>

      <div class="grid formgrid" v-else>
        <date-range />
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
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import DateRange from './EventDateRange.vue'
import DatePicker from './EventDatePicker.vue'
import Button from 'primevue/button'

const emits = defineEmits(['dateSearch'])

const store = useStore()

const searchOptions = ref('date')

const isFilterByDate = computed(() => {
  return searchOptions.value === 'date'
})

onMounted(async () => {
  searchOptions.value = store.getters['dateFilter']
})

function search() {
  emits('dateSearch')
}

function change() {
  store.commit('setDateFilter', searchOptions.value)
}
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
</style>
