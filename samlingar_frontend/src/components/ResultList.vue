<template>
  <div>
    <div id="resultList">
      <template v-for="(result, index) in results" :key="result.id">
        <Result v-bind:result="result" />
        <Divider
          v-if="index + 1 < results.length"
          :key="`devider-${index}`"
          style="padding-bottom: 20px"
        />
      </template>
    </div>

    <Paginator
      v-if="results.length > 0"
      v-model:first="first"
      :rows="10"
      :totalRecords="totalRecords"
      :rowsPerPageOptions="[10, 20, 30]"
      @page="onPage($event)"
    ></Paginator>
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue'
import { useStore } from 'vuex'
// import ImageGalleria from './ImageGalleria.vue'
import Result from './Result.vue'

const first = ref(0)
const store = useStore()
const emits = defineEmits(['search'])

watch(
  () => store.getters['startRecord'],
  () => {
    const startRecord = store.getters['startRecord']
    console.log('resetPaging change...', startRecord)
    if (startRecord === 0) {
      first.value = 0
    }
  }
)

const results = computed(() => {
  return store.getters['results']
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

// const showGalleria = computed(() => {
// return store.getters['openGalleria']
// })
//
function onPage(event) {
  const start = event.first
  const numPerPage = event.rows

  store.commit('setNumPerPage', numPerPage)
  store.commit('setStartRecord', start)

  emits('search')
}
</script>
<style scoped>
.p-paginator-page,
.p-paginator-next,
.p-paginator-last,
.p-paginator-first,
.p-paginator-prev {
  /* ... */
  background: red !important;
  padding: 0;
  /* ... */
}

.custom-paginator .p-paginator {
  font-size: 36px;
  background-color: #333;
}
.p-paginator {
  width: 18rem;
  background: #000 !important;
}

:root {
  --p-paginator-padding: 0.5rem 1rem;
  --p-paginator-gap: 0.25rem;
  --p-paginator-border-radius: var(--p-content-border-radius);
  --p-paginator-background: red !important;
  --p-paginator-color: var(--p-content-color);
  --p-paginator-transition-duration: var(--p-transition-duration);
  --p-paginator-nav-button-background: transparent;
  --p-paginator-nav-button-hover-background: var(--p-content-hover-background);
  --p-paginator-nav-button-selected-background: var(--p-highlight-background);
  --p-paginator-nav-button-color: var(--p-text-muted-color);
  --p-paginator-nav-button-hover-color: var(--p-text-hover-muted-color);
  --p-paginator-nav-button-selected-color: var(--p-highlight-color);
  --p-paginator-nav-button-width: 2.5rem;
  --p-paginator-nav-button-height: 2.5rem;
  --p-paginator-nav-button-border-radius: 50%;
  --p-paginator-nav-button-focus-ring-width: var(--p-focus-ring-width);
  --p-paginator-nav-button-focus-ring-style: var(--p-focus-ring-style);
  --p-paginator-nav-button-focus-ring-color: var(--p-focus-ring-color);
  --p-paginator-nav-button-focus-ring-offset: var(--p-focus-ring-offset);
  --p-paginator-nav-button-focus-ring-shadow: var(--p-focus-ring-shadow);
  --p-paginator-current-page-report-color: var(--p-text-muted-color);
  --p-paginator-jump-to-page-input-max-width: 2.5rem;
}

.p-paginator {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  background: red;
  color: var(--p-paginator-color);
  padding: var(--p-paginator-padding);
  border-radius: var(--p-paginator-border-radius);
  gap: var(--p-paginator-gap);
}

.customStyle {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  background: transparent;
  color: #fff;
  padding: var(--p-paginator-padding);
  border-radius: var(--p-paginator-border-radius);
  gap: var(--p-paginator-gap);
}
/* style="
        display: flex;
        align-items: center;
        justify-content: center;
        flex-wrap: wrap;
        background: transparent;
        color: #fff;
        padding: var(--p-paginator-padding);
        border-radius: var(--p-paginator-border-radius);
        gap: var(--p-paginator-gap);
      " */
/* .p-paginator-page,
.p-paginator-next,
.p-paginator-last,
.p-paginator-first,
.p-paginator-prev {
  color: red !important;
}
.p-paginator {
  background: transparent !important;
} */
</style>
