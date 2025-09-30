<template>
  <div class="flex flex-col gap-2" style="width: 100%">
    <InputGroup>
      <InputText
        id="inputWithIcon"
        v-model="value"
        @keydown.enter="onClick"
        :placeholder="placehold"
        :size="size"
        class="w-full"
      />
      <Button :icon="icon" @click="onClick" class="btnStyle" />
    </InputGroup>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const emits = defineEmits(['doAction'])
const props = defineProps(['icon', 'placehold', 'size'])
const value = ref()

function onClick() {
  emits('doAction', value.value)
}
</script>
<style scoped>
.btnStyle {
  background: #144836 !important;
}
</style>
