<template>
  <div class="grid" id="inputbox">
    <div class="col-11">
      <InputGroup>
        <InputGroupAddon style="min-width: auto">
          <b> {{ label }}: </b>
        </InputGroupAddon>
        <InputText
          id="inputWithIcon"
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
import InputGroupAddon from 'primevue/inputgroupaddon'

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
  console.log('onInputAction', value.value)

  showClearField.value = value.value !== undefined
  console.log('showClearField', showClearField)
  emits('search', props.field.key, value.value)
}

function clearField() {
  showClearField.value = false
  value.value = null
}
</script>
<style scoped>
/* .btnStyle {
  max-width: 32px;
  min-height: 30px;
} */
</style>
