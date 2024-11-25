<template>
  <div class="flex flex flex-wrap justify-center gap-3">
    <div class="flex items-center vertical-align-middle">
      <Checkbox v-model="options" inputId="option1" name="option1" value="exact" class="mt-1" />
      <label for="option1" class="ml-2">{{ $t('search.exact') }}</label>
    </div>
    <div class="flex items-center">
      <Checkbox v-model="options" inputId="option2" name="option2" value="contains" class="mt-1" />
      <label for="option2" class="ml-2">{{ $t('search.contains') }}</label>
    </div>
    <div class="flex items-center">
      <Checkbox
        v-model="options"
        inputId="option3"
        name="option3"
        value="startsWith"
        class="mt-1"
      />
      <label for="option3" class="ml-2">{{ $t('search.startsWith') }}</label>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'

const options = ref()
</script>
