<template>
  <div class="grid" style="min-width: 100%">
    <div class="col-6" no-gutters>
      <MonthChart />
    </div>
    <div class="col-6" no-gutters>
      <YearChart />
    </div>
  </div>
</template>
<script setup>
import MonthChart from './MonthChart.vue'
import YearChart from './YearChart.vue'
</script>
