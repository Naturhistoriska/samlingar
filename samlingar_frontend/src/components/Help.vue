<template>
  <div class="search-help">
    <Panel :header="$t('help.howToSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.howToSearchText') }}</p>
    </Panel>
    <Divider />
    <Panel :header="$t('help.basicSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.basicSearchText') }}</p>
      <p style="padding-left: 1rem">
        <b>{{ $t('help.example') }}</b>
        &nbsp; {{ $t('help.bySearching') }} <i>aves västergötland</i>
        {{ $t('help.returnMatchingTerms') }}
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
        <li>
          {{ $t('help.checkbox') }} (e.g.
          <em>Has images, Have coordinates, Is typestatus etc...</em>)
        </li>
        <li>{{ $t('help.scientificName') }} (e.g. <em>Pica pica</em>)</li>
        <li>{{ $t('help.collectionName') }} (e.g. Bird Collection, Fungi Collection ect...)</li>
        <li>{{ $t('help.collectedDate') }} (e.g. 1990-01-01, 1920-12-31 or 1900, 1920)</li>
        <li>{{ $t('help.predefinedFilters') }}</li>
      </ul>
      <Divider />
      <p style="padding-left: 1rem">{{ $t('help.advanceSearchFeatures') }}:</p>
      <ul>
        <li>
          {{ $t('help.checkbox') }}
          <p>{{ $t('help.checkboxSearchText') }}</p>
          <ul>
            <li>{{ $t('help.checkboxHasImages') }}</li>
            <li>{{ $t('help.checkboxHaveCoordinates') }}</li>
            <li>{{ $t('help.checkboxIsType') }}</li>
            <li>{{ $t('help.checkboxInSweden') }}</li>
            <li>{{ $t('help.checkboxNordic') }}</li>
          </ul>
        </li>
        <li style="padding-top: 2em">
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

          <DataTable
            :value="searchExamples"
            :size="size"
            stripedRows
            class="p-datatable-sm"
            responsiveLayout="scroll"
          >
            <Column field="fieldName" :header="$t('help.fieldName')" />
            <Column field="searchMode" :header="$t('help.searchMode')" />
            <Column field="description" :header="$t('help.description')" />
          </DataTable>
        </li>
      </ul>
    </Panel>
    <Divider />
    <Panel :header="$t('help.moreHelp')" toggleable>
      <p style="padding-left: 1rem">
        {{ t('help.text') }}
      </p>
      <p style="padding-left: 1rem">
        <strong>{{ t('help.email') }}:</strong>
        <a :href="`mailto:${email}`">{{ email }}</a>
      </p>
    </Panel>
  </div>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const size = ref('small')

const searchExamples = ref([
  {
    fieldName: t('help.phylum'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.class'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.order'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.family'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.genus'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.subgenus'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.species'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.commonName'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.synonyms'),
    searchMode: t('help.synonymsSearchMode'),
    description: t('help.synonymsSearchDesc')
  },
  {
    fieldName: t('help.taxonRank'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.catalogNumber'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.typeStatus'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.collectedBy'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.country'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.state'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },

  {
    fieldName: t('help.county'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
  },
  {
    fieldName: t('help.locality'),
    searchMode: t('help.autocomplete'),
    description: t('help.autocompleteDesc')
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
