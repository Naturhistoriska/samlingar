<template>
  <div class="grid" id="dates">
    <div class="col-10">
      <div class="grid">
        <div class="col-6">
          <div class="flex flex-col gap-2 selectGroup">
            <label class="searchLabel">
              {{ $t('search.beginDateLabel') }}
            </label>
            <DatePicker
              v-model="beginDate"
              dateFormat="yy-m-dd"
              showIcon
              fluid
              inputId="startDate"
              :placeholder="$t('search.beginDateLabel')"
              @change="onBeginDateChange"
              @select="onBeginDateChange"
              @input="onBeginDateChange"
            />
          </div>
        </div>
        <div class="col-6">
          <div class="flex flex-col gap-2 selectGroup">
            <label class="searchLabel">
              {{ $t('search.endDateLabel') }}
            </label>
            <DatePicker
              v-model="endDate"
              dateFormat="yy-m-dd"
              showIcon
              fluid
              inputId="endDate"
              :placeholder="$t('search.endDateLabel')"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import DatePicker from 'primevue/datepicker'
import { ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()
let beginDate = ref()
let endDate = ref()

function onBeginDateChange(e) {
  console.log(e)
  console.log('onBeginDateChange', beginDate.value)
}
</script>
<style scoped>
.selectGroup {
  max-width: 770px;
  padding-bottom: 1rem;
}
.searchLabel {
  padding-right: 2rem;
  padding-top: 0.3rem;
  text-align: right;
  min-width: 150px;
}
</style>
