<template>
  <card>
    <template #title>{{ $t(dataGroup) }} </template>
    <template #content>
      <Accordion multiple class="p-accordion-header-variant-a">
        <AccordionPanel v-for="tab in tabs" :key="tab" :value="tab" style="font-size: 12px">
          <AccordionHeader style="background: transparent">
            {{ tab }}
          </AccordionHeader>
          <AccordionContent style="background: transparent" :unstyled="true">
            <CollectionMonthChart v-bind:collection="tab" />
            <CollectionYearChart />
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </template>
  </card>
</template>
<script setup>
import { computed } from 'vue'
import Accordion from 'primevue/accordion'
import AccordionPanel from 'primevue/accordionpanel'
import AccordionHeader from 'primevue/accordionheader'
import AccordionContent from 'primevue/accordioncontent'
import CollectionMonthChart from '../components/CollectionMonthChart.vue'
import CollectionYearChart from '../components/CollectionYearChart.vue'

const props = defineProps(['dataset', 'dataGroup'])
const tabs = computed(() => {
  const data = props.dataset
  return data.split(':')
})
</script>
<style scoped>
.p-accordionpanel:not(.p-active).p-accordionpanel > .p-accordionheader {
  background: var(--p-accordion-header-active-background);
  color: #cbcad1;
  font-size: 1rem;
}
</style>
