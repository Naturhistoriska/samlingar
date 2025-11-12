<template>
  <div class="card table-wrapper" id="recordsLabelsDiv">
    <DataView
      :value="records"
      layout="grid"
      paginator
      :rows="rows"
      :paginator="true"
      :lazy="true"
      @page="onPage($event)"
      :totalRecords="totalCount"
      responsiveLayout="scroll"
      class="responsive-table"
    >
      <template #grid="slotProps">
        <div class="grid">
          <div
            v-for="(item, index) in slotProps.items"
            :key="index"
            class="col-12 sm:col-6 md:col-4 lg:col-3 p-2"
          >
            <Card v-if="item" class="h-full shadow-1 hover:shadow-3 transition-shadow">
              <!-- Title -->
              <template #title>
                <div class="flex justify-content-between align-items-start">
                  <small class="font-semibold line-height-2 text-sm md:text-base">
                    {{ getTaxon(item) }}
                  </small>
                  <Button text size="small" @click="view(item)" class="p-0 text-xs md:text-sm">
                    {{ $t('records.view') }}
                  </Button>
                </div>
              </template>

              <!-- Subtitle -->
              <template #subtitle>
                <small class="text-color-secondary text-xs md:text-sm">
                  {{ item.collectionName }}
                </small>
              </template>

              <!-- Content -->
              <template #content>
                <div class="text-xs md:text-sm text-color-secondary">
                  <div>{{ item.catalogNumber }}</div>
                  <div>
                    {{ item.locality }}<br />
                    {{ item.continent }} {{ item.country }} {{ item.stateProvince }}
                    {{ item.county }}
                  </div>
                  <div v-if="item.decimalLatitude != null">
                    Lat: {{ item.decimalLatitude }}<br />
                    Lon: {{ item.decimalLongitude }}
                  </div>
                </div>
              </template>
            </Card>

            <!-- <div
              class="flex flex-row justify-between items-start gap-3"
              style="max-width: 350px; min-width: 350px"
            >
              <card style="height: 150px; width: 350px" v-if="item != undefined">
                <template #title>
                  <div class="grid">
                    <div class="col-10" no-gutters>
                      <small>{{ getTaxon(item) }}</small>
                    </div>
                    <div class="col-2" no-gutters>
                      <Button variant="link" @click="view(item)">
                        <small>{{ $t('records.view') }}</small>
                      </Button>
                    </div>
                  </div>
                </template>
                <template #subtitle>
                  <small>{{ item.collectionName }}</small>
                </template>
                <template #content>
                  <div style="font-size: 0.7em">
                    {{ item.catalogNumber }}
                  </div>
                  <div style="font-size: 0.7em">
                    {{ item.locality }}
                    <br />
                    {{ item.continent }} {{ item.country }} {{ item.stateProvince }}
                    {{ item.county }}
                  </div>
                  <div v-if="item.decimalLatitude != null" style="font-size: 0.7em">
                    Lat: {{ item.decimalLatitude }} Lon: {{ item.decimalLongitude }}
                  </div>
                </template>
              </card>
            </div> -->
          </div>
        </div>
      </template>
    </DataView>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

const service = new Service()

const store = useStore()
const router = useRouter()

// const emits = defineEmits(['search'])

let records = ref(Array.from({ length: 50 }))

let windowWidth = ref(window.innerWidth)

const loading = ref(false)

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

const rows = computed(() => {
  const width = windowWidth.value
  console.log('width 1', width)

  if (width < 1000) {
    return 20
  } else if (width < 1300) {
    return 30
  } else if (width < 1800) {
    return 40
  } else {
    return 50
  }
})

watch(
  () => store.getters['results'],
  (newValue, oldValue) => {
    records.value = store.getters['results']
  }
)

onMounted(async () => {
  console.log('RecordLabel')
  window.addEventListener('resize', onWidthChange)
  records.value = store.getters['results']
})

const onWidthChange = () => {
  windowWidth.value = window.innerWidth
  // emits('search', 0, rows.value, false)
}

const onPage = async (event) => {
  const { first, rows } = event
  const params = store.getters['searchParams']
  loadRecordsLazy(params, first, rows)
}

function getTaxon(data) {
  const { collectionCode, genus, scientificName, species, taxonRank } = data

  if (collectionCode === 'pz' || collectionCode === 'pb') {
    return taxonRank === 'species' ? genus + ' ' + species : scientificName
  } else if (collectionCode === 'PI' || collectionCode === 'HE') {
    return taxonRank === 'Species' ? genus + ' ' + species : scientificName
  } else if (collectionCode === 'vp') {
    if (species) {
      return genus ? genus + ' ' + species : species
    }
  } else {
    return scientificName
  }
}

async function loadRecordsLazy(params, first, rows) {
  loading.value = true

  console.log('params', params.toString())
  await service
    .apiSearch(params, first, rows)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)

      store.commit('setTotalRecords', total)

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      loading.value = false
      store.commit('setSearchParams', params)
    })
}

function view(data) {
  store.commit('setSelectedRecord', data)
  router.push(`/record/${data.id}`)
}
</script>
<style scoped>
.responsive-table {
  font-size: 0.85rem;
}

::v-deep(.p-card-title) {
  font-size: 15px;
  font-weight: bold;
  font-style: italic;
  margin-top: 0 !important;
  margin-bottom: 0 !important;
}

::v-deep(.p-card-subtitle) {
  margin-top: 0 !important;
}

:deep(.p-paginator) {
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.5rem 0;
  background: var(--surface-card, #fff);
  border-top: 1px solid var(--surface-border, #ddd);
  z-index: 20;
}

/* ✅ Row select font size (like before) */
:deep(.p-paginator .p-dropdown) {
  font-size: 0.85rem;
  height: 2rem;
  min-width: 4.5rem;
}
:deep(.p-paginator .p-dropdown .p-dropdown-label) {
  font-size: 0.85rem;
  padding: 0.25rem 0.5rem;
}

/* ✅ Sticky paginator only on mobile */
@media (max-width: 768px) {
  :deep(.p-paginator) {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    border-radius: 0;
    justify-content: space-around;
    padding: 0.5rem;
    box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.1);
  }

  /* Reduce paginator size for small screens */
  :deep(.p-paginator .p-paginator-page),
  :deep(.p-paginator .p-paginator-next),
  :deep(.p-paginator .p-paginator-prev) {
    min-width: 1.8rem;
    height: 1.8rem;
    font-size: 0.8rem;
    backdrop-filter: blur(6px);
    background: rgba(255, 255, 255, 0.9);
  }

  /* Compact dropdown */
  :deep(.p-paginator .p-dropdown) {
    font-size: 0.75rem;
    min-width: 3.5rem;
  }

  /* Optional: hide report text to save space */
  :deep(.p-paginator-current) {
    display: none;
  }

  /* Add padding-bottom to table wrapper to avoid content overlap */
  .table-wrapper {
    padding-bottom: 4rem; /* height of sticky paginator area */
  }
}
@media (prefers-color-scheme: dark) {
  :deep(.p-paginator) {
    background: rgba(30, 30, 30, 0.95);
    border-top: 1px solid #444;
  }
}
</style>
