<template>
  <div class="justify-right" style="float: left; margin-top: 10px">
    <FloatLabel variant="on">
      <InputGroup>
        <AutoComplete
          dropdown
          forceSelection
          v-model="values"
          size="small"
          fluid
          :inputId="inputId"
          :suggestions="items"
          :minLength="3"
          :multiple="multiple"
          @complete="apiAutoComplete"
          @option-select="onItemSelect"
          @option-unselect="onItemRemove"
          :inputStyle="acwidth"
        />
        <label :for="inputId">{{ $t(placehold) }}</label>
      </InputGroup>
    </FloatLabel>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import Service from '../../Service'

const service = new Service()

const store = useStore()

const props = defineProps(['field'])

const acwidth = ref()
const items = ref([])
const values = ref([])

const placehold = computed(() => {
  return 'search.' + props.field.value
})

const inputId = computed(() => {
  return props.field.label
})

const multiple = computed(() => {
  return props.field.multiple
})

onMounted(() => {
  acwidth.value = { width: '400px' }

  const input = props.field.text
  if (input) {
    values.value = input.match(/"([^"]+)"/g)?.map((s) => s.replace(/"/g, '')) || []
  }
})

const apiAutoComplete = (event) => {
  const field = props.field.value
  let value = event.query

  // if (field === 'typeStatus') {
  //   value = value.toUpperCase()
  // }

  service
    .apiAutoCompleteSearch(value, field)
    .then((response) => {
      const facets = response.facets.facet
      if (facets) {
        items.value = facets.buckets.map((a) => a.val)
      }
    })
    .catch()
    .finally(() => {})
}

function onItemRemove(event) {
  itemsChanged()
}

function onItemSelect() {
  itemsChanged()
  items.value = []
}

function itemsChanged() {
  const fieldKey = props.field.key

  let fields = store.getters['fields']
  const field = fields.find(({ key }) => key === fieldKey)

  let value
  if (multiple.value) {
    value = values.value
      .map((str) => `"${str}"`) // Wrap each string in single quotes
      .join(' ')
  } else {
    value = values.value
  }

  if (value.trim() !== '') {
    field.text = multiple.value ? `(${value})` : value
  } else {
    delete field.text
  }

  store.commit('setFields', fields)
}
</script>

<style scoped>
.p-floatlabel label {
  color: #c0bdbd !important;
}
.p-button-text {
  color: #fff !important;
  background: var(--p-emerald-500) !important;
  border-start-end-radius: var(--p-inputgroup-addon-border-radius) !important;
  border-end-end-radius: var(--p-inputgroup-addon-border-radius) !important;
  border: 1px solid var(--p-button-primary-border-color) !important;
  text-decoration: none !important;
}
.p-button-text:hover {
  /* color: #fff !important; */
  text-decoration: none !important;
  background: var(--p-emerald-500) !important;
  border-start-end-radius: var(--p-inputgroup-addon-border-radius);
  border-end-end-radius: var(--p-inputgroup-addon-border-radius);
  border: 1px solid var(--p-button-primary-border-color);
}
</style>
