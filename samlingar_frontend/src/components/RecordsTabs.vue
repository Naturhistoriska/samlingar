<template>
  <div class="card">
    <Tabs value="0">
      <TabList>
        <Tab value="0">{{ $t('records.list') }}</Tab>
        <Tab value="1">{{ $t('records.labels') }}</Tab>
        <Tab value="2">{{ $t('records.media') }}</Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0">
          <RecordTable />
        </TabPanel>
        <TabPanel value="1">
          <RecordLabels />
        </TabPanel>
        <TabPanel value="2">
          <RecordMedia />
        </TabPanel>
      </TabPanels>
    </Tabs>
  </div>
</template>
<script setup>
import RecordLabels from './RecordLabels.vue'
import RecordTable from './RecordTable.vue'
import RecordMedia from './RecordMedia.vue'

const emits = defineEmits(['freeTextSearch'])

function handleFreeTextSearch(searchText, start, numPerPage) {
  console.log('handleFreeTextSearch', searchText, start, numPerPage)
  emits('freeTextSearch', searchText, start, numPerPage)
}
</script>
