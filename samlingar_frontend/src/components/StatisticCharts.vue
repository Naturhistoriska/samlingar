<template>
  <div class="grid w-full align-items-stretch">
    <div class="col-12 md:col-6 p-2">
      <MonthChart />
    </div>

    <div class="col-12 md:col-6 p-2">
      <YearChart />
    </div>
  </div>
</template>
<script setup>
import MonthChart from './MonthChart.vue'
import YearChart from './YearChart.vue'
</script>
<style scoped>
/* Optional: ensure charts resize nicely */
:deep(canvas),
:deep(.chart-container) {
  width: 100% !important;
  height: auto !important;
}

/* Add a bit of spacing on small screens */
@media (max-width: 768px) {
  .col-12 {
    padding: 0.5rem 0;
  }
}
</style>
