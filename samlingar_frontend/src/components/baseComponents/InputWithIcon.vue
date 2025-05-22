<template>
  <div class="flex flex-col gap-2" style="width: 100%">
    <InputGroup>
      <InputText
        id="inputWithIcon"
        v-model="value"
        @keydown.enter="onClick"
        :placeholder="$t(placehold)"
        :size="size"
        class="w-full"
      />
      <Button :icon="icon" @click="onClick" :loading="loading" />
    </InputGroup>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
const emits = defineEmits(['doAction'])

const props = defineProps(['icon', 'loading', 'placehold', 'size'])

const value = ref()

function onClick() {
  emits('doAction', value.value)
}
</script>
<style scoped>
/* .btnStyle {
  max-width: 32px;
  min-height: 30px;
} */
</style>
