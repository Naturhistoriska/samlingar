<template>
  <div class="card" id="recordsLabelsDiv">
    <DataView
      :value="records"
      layout="grid"
      paginator
      :rows="rows"
      :paginator="true"
      :lazy="true"
      @page="onPage($event)"
      :totalRecords="totalCount"
    >
      <template #grid="slotProps">
        <div class="grid grid-cols-12">
          <div
            v-for="(item, index) in slotProps.items"
            :key="index"
            class="col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-6 p-2"
          >
            <div
              class="flex flex-row justify-between items-start gap-3"
              style="max-width: 350px; min-width: 350px"
            >
              <card style="height: 150px; width: 350px" v-if="item != undefined">
                <template #title>
                  <div class="grid">
                    <div class="col-10" no-gutters>
                      <small>{{ item.scientificName }}</small>
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
                    {{ item.locality }} {{ item.country }} {{ item.continent }}
                  </div>
                  <div v-if="item.decimalLatitude != null" style="font-size: 0.7em">
                    Lat: {{ item.decimalLatitude }} Lon: {{ item.decimalLongitude }}
                  </div>
                </template>
              </card>
            </div>
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

const store = useStore()
const router = useRouter()

const emits = defineEmits(['search'])

let records = ref(Array.from({ length: 50 }))

let windowWidth = ref(window.innerWidth)

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
  emits('search', 0, rows.value, false)
})

const onWidthChange = () => {
  windowWidth.value = window.innerWidth
  emits('search', 0, rows.value, false)
}

const onPage = async (event) => {
  const { first, rows } = event
  emits('search', first, rows, false)
}

function view(data) {
  store.commit('setSelectedRecord', data)
  router.push(`/record/${data.id}`)
}
</script>
<style scoped></style>
