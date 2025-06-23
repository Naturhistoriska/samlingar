<template>
  <Card>
    <template #title>List Results Columns</template>
    <template #content>
      <div class="grid">
        <div class="3">
          <div class="12">Taxonomy</div>
          <div class="12">
            <Checkbox
              v-model="selectedColumns"
              id="kingdom"
              name="Kingdom"
              value="kingdom"
              @change="onChang"
            />
            <label for="kingdom" class="text-sm">{{ $t('search.kingdom') }}</label>
          </div>
          <div class="12">
            <Checkbox v-model="selectedColumns" id="phylum" name="Phylum" value="phylum" />
            <label for="phylum" class="text-sm">{{ $t('search.phylum') }}</label>
          </div>
          <div class="12">
            <Checkbox v-model="selectedColumns" id="class" name="Class" value="class" />
            <label for="phylum" class="text-sm">{{ $t('search.clazz') }}</label>
          </div>
          <div class="12">
            <Checkbox v-model="selectedColumns" id="order" name="Order" value="order" />
            <label for="order" class="text-sm">{{ $t('search.order') }}</label>
          </div>
        </div>
        <div class="3"></div>
        <div class="3"></div>
        <div class="3"></div>
      </div>
    </template>
  </Card>
</template>
<script setup>
import { ref } from 'vue'
const selectedColumns = ref([])
function onChang(event) {
  console.log('onChang', selectedColumns.value)
}
</script>
