<template>
  <div class="col-12 md:col-7">
    <record-dataset :code="code" />
    <record-event :code="code" />
    <location :code="code" />

    <geological-context v-if="isPalCollection" />

    <record-identification :code="code" />
    <taxonomy :code="code" />
  </div>
</template>

<script setup>
import Location from './Location.vue'
import GeologicalContext from './GeologicalContext.vue'
import RecordDataset from './RecordDataset.vue'
import RecordEvent from './RecordEvent.vue'
import RecordIdentification from './RecordIdentification.vue'
import Taxonomy from './Taxonomy.vue'

defineProps({
  code: String,
  isPalCollection: Boolean
})
</script>
