<template>
  <div class="justify-right" style="float: left; margin-top: 10px">
    <InputGroup>
      <InputGroupAddon style="min-width: 80px; font-weight: bold">
        <small>{{ label }}: </small>
      </InputGroupAddon>
      <InputText
        v-model="value"
        :placeholder="$t(placehold)"
        size="small"
        class="w-full"
        style="min-width: 300px; max-width: 300px"
        @input="onInputAction"
      />
      <Button
        icon="pi pi-times"
        v-if="showClearField"
        @click="clearField"
        style="background-color: #144836 !important"
      />
    </InputGroup>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import InputGroupAddon from 'primevue/inputgroupaddon'

const store = useStore()

const props = defineProps(['field'])

const value = ref()
let showClearField = ref(false)

const placehold = computed(() => {
  return 'search.' + props.field.value
})

const label = computed(() => {
  return props.field.label
})

function onInputAction() {
  showClearField.value = value.value !== undefined

  const fieldKey = props.field.key

  let fields = store.getters['fields']
  const field = fields.find(({ key }) => key === fieldKey)

  if (value.value.trim() !== '') {
    field.text = value.value
  } else {
    delete field.text
  }

  store.commit('setFields', fields)
}

function clearField() {
  showClearField.value = false
  value.value = null
}

// function search(fieldKey, value) {
//   let fields = store.getters['fields']
//   const field = fields.find(({ key }) => key === fieldKey)
//   field.text = value

//   store.commit('setFields', fields)
//   emits('search')
// }
</script>
<style scoped></style>
