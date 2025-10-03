<template>
  <div class="search-help">
    <Panel :header="$t('help.howToSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.howToSearchText') }}</p>
    </Panel>
    <Divider />
    <Panel :header="$t('help.basicSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.basicSearchText') }}</p>
      <p style="padding-left: 1rem">
        <b>{{ $t('help.example') }}</b> {{ $t('help.bySearching') }}
        <i>"aves västergötland"</i> &nbsp;{{ $t('help.returnMatchingTerms') }}
      </p>
      <p style="padding-left: 1rem">{{ $t('help.fullTextSearchOptions') }}:</p>
      <ul>
        <li>
          {{ $t('help.exact') }}
          <p>{{ $t('help.example') }}</p>
          <ul>
            <li>{{ $t('help.exactExampleSecifitcName') }}</li>
            <li>{{ $t('help.exactExampleLocality') }}</li>
          </ul>
        </li>
        <li style="padding-top: 2em">
          {{ $t('help.contains') }}
          <p>{{ $t('help.example') }}</p>
          <p>{{ $t('help.containsExample') }}</p>
        </li>
        <li>
          {{ $t('help.startWith') }}
          <p>{{ $t('help.example') }}</p>
          <p>{{ $t('help.startWithExample') }}</p>
        </li>
      </ul>
    </Panel>
    <Divider />
    <Panel :header="$t('help.filterSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.advanceSearchSubtitle') }}:</p>
      <ul>
        <li>{{ $t('help.scientificName') }} (e.g. <em>Pica pica</em>)</li>
        <li>{{ $t('help.collectionName') }} (e.g. Bird Collection, Fungi Collection ect...)</li>
        <li>{{ $t('help.collectedDate') }} (e.g. 1990-01-01, 1920-12-31 or 1900, 1920)</li>
        <li>{{ $t('help.predefinedFilters') }}</li>
      </ul>
      <Divider />
      <p style="padding-left: 1rem">{{ $t('help.advanceSearchFeatures') }}:</p>
      <ul>
        <li>
          {{ $t('help.scientificNameSearch') }}:
          <p>{{ $t('help.scientificNameSearchText') }}:</p>
          <ul>
            <li>{{ $t('help.scienticNameSearchExact') }}</li>
            <li>{{ $t('help.scienticNameSearchContains') }}</li>
            <li>{{ $t('help.scienticNameSearchStartWith') }}</li>
          </ul>
        </li>
        <li style="padding-top: 2em">
          {{ $t('help.collectionNameSearch') }}:
          <p>{{ $t('help.collectionNameSearchText') }}</p>
          <ul>
            <li>{{ $t('help.collectionNameSearchHelp1') }}</li>
            <li>{{ $t('help.collectionNameSearchHelp2') }}</li>
            <li>{{ $t('help.collectionNameSearchHelp3') }}</li>
          </ul>
        </li>
        <li style="padding-top: 2em">
          {{ $t('help.collectedDateSearch') }}
          <p>{{ $t('help.collectedDateSearchText') }}</p>
          <ul>
            <li>{{ $t('help.dateRange') }} - {{ $t('help.collectedDateRangeSearch') }}</li>
            <li>{{ $t('help.yearRange') }} - {{ $t('help.collectedYearRangeSearch') }}</li>
          </ul>
        </li>
        <li style="padding-top: 2em">
          {{ $t('help.predefinedFilterSearch') }}:
          <p>{{ $t('help.predefinedFiltersText') }}</p>

          <DataTable :value="searchExamples" class="p-datatable-sm" responsiveLayout="scroll">
            <Column field="fieldName" header="Field name" />
            <Column field="searchMode" header="Search mode" />
            <Column field="description" header="Description" />
          </DataTable>
        </li>
      </ul>
    </Panel>
    <Divider />
    <Panel header="Still Need Help?" toggleable>
      <p style="padding-left: 1rem">
        If you're unsure how to search or can't find what you're looking for, contact us:
      </p>
      <p style="padding-left: 1rem">
        <strong>Email: </strong>
        <a :href="`mailto:${email}`">{{ email }}</a>
      </p>
    </Panel>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'

const searchExamples = ref([
  {
    fieldName: 'Phylum',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Class',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Order',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Family',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Genus',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Subgenus',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Species',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Common name',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Synonyms',
    searchMode: 'Start with text search',
    description: 'Search records where a field begins with the specified text.'
  },
  {
    fieldName: 'Taxon rank',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Catalog number',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Type status',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Collected by',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Country',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'State/Province',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },

  {
    fieldName: 'County/Parish',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  },
  {
    fieldName: 'Locality',
    searchMode: 'Autocomplet',
    description: 'Type at least 3 letters to see suggestions'
  }
])

const email = computed(() => {
  return import.meta.env.VITE_CONTACT_EMAIL
})
</script>

<style scoped>
/* Remove border and shadow */
::v-deep(.p-panel) {
  border: none;
  box-shadow: none;
  padding: 0px;
}

/* Remove header completely */
::v-deep(.p-panel-header) {
  font-weight: bold;
  font-size: 1rem;
  padding: 0;
}

/* Remove content padding */
::v-deep(.p-panel-content) {
  padding: 0;
}
</style>
