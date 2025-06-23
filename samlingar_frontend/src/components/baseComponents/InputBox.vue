<template>
  <div class="grid" id="inputbox">
    <div class="col-11">
      <InputGroup>
        <InputGroupAddon style="min-width: 120px; font-weight: bold">
          <small>{{ label }}: </small>
        </InputGroupAddon>
        <InputText
          v-model="value"
          :placeholder="$t(placehold)"
          size="small"
          class="w-full"
          @input="onInputAction"
        />
        <Button icon="pi pi-times" v-if="showClearField" @click="clearField" />
      </InputGroup>
    </div>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import InputGroupAddon from 'primevue/inputgroupaddon'

const store = useStore()

const emits = defineEmits(['search'])

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

  search(props.field.key, value.value)
}

function clearField() {
  showClearField.value = false
  value.value = null

  search(props.field.key, value.value)
}

function search(fieldKey, value) {
  let fields = store.getters['fields']
  const field = fields.find(({ key }) => key === fieldKey)
  field.text = value

  // fields.map((item) => (item.key === fieldKey ? { ...item, text: value } : item))

  store.commit('setFields', fields)
  emits('search')
}
</script>
<style scoped>
/* .btnStyle {
  max-width: 32px;
  min-height: 30px;
} */
</style>
