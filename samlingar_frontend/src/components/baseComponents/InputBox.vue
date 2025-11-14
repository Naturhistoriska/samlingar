<template>
  <div class="input-wrapper">
    <InputGroup class="flex-inputgroup">
      <InputGroupAddon class="input-label">
        <small>{{ label }}:</small>
      </InputGroupAddon>

      <InputText
        v-model="value"
        :placeholder="$t(placehold)"
        size="small"
        class="input-field"
        @input="onInputAction"
      />

      <Button v-if="showClearField" icon="pi pi-times" class="clear-button" @click="clearField" />
    </InputGroup>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import InputGroup from 'primevue/inputgroup'
import InputGroupAddon from 'primevue/inputgroupaddon'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'

const store = useStore()
const props = defineProps({ field: Object })
const emits = defineEmits(['search'])

const value = ref('')
const showClearField = ref(false)

const placehold = computed(() => `search.${props.field.value}`)
const label = computed(() => props.field.label)

function onInputAction() {
  console.log('onInputAction')
  showClearField.value = !!value.value

  const fieldKey = props.field.key
  const fields = store.getters['fields']
  const field = fields.find((f) => f.key === fieldKey)

  if (value.value.trim() !== '') {
    field.text = value.value
  } else {
    delete field.text
  }

  store.commit('setFields', fields)
}

function clearField() {
  console.log('clearField')
  value.value = ''
  onInputAction()

  showClearField.value = false
}
</script>

<style scoped>
.input-wrapper {
  margin-top: 10px;
  width: 100%;
}

.flex-inputgroup {
  display: flex;
  flex-wrap: nowrap;
  width: 100%;
  align-items: center;
}

.input-label {
  min-width: 80px;
  font-weight: bold;
}

.input-field {
  flex: 1 1 auto;
  min-width: 150px;
  max-width: 100%;
}

.clear-button {
  background-color: #144836 !important;
  flex-shrink: 0;
  margin-left: 5px;
}

/* Mobile adjustments */
@media (max-width: 480px) {
  .flex-inputgroup {
    flex-wrap: nowrap; /* keep input + button on the same line */
  }

  .input-field {
    width: 100%;
  }
}
</style>
