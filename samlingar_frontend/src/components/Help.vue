<!-- SearchHelp.vue -->
<template>
  <div class="search-help">
    <Panel :header="$t('help.howToSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.howToSearchText') }}</p>
    </Panel>
    <Divider />
    <Panel :header="$t('help.basicSearch')" toggleable>
      <p style="padding-left: 1rem">{{ $t('help.basicSearchText') }}</p>

      <p style="padding-left: 1rem">
        <strong>Example:</strong> By searching: <code>aves västergötland</code> will return all
        specimens matching both terms.
      </p>
    </Panel>
    <Divider />
    <Panel :header="$t('help.filterSearch')" toggleable>
      <p style="padding-left: 1rem">You can search using:</p>
      <ul>
        <li>Scientific name (e.g. <em>Pica pica</em>)</li>
        <li>Common name (e.g. lion)</li>
        <li>Collector name (e.g. David Livingstone)</li>
        <li>Catalog number or Collection ID</li>
        <li>Location (e.g. Falu gruva)</li>
        <li>Collection date (e.g. 1901, 1920)</li>
      </ul>
    </Panel>
    <!--



    <Divider />
    <Panel header="  Advanced Search Features" toggleable class="mt-3">
      <p>You can use the following operators to refine your search:</p>
      <DataTable :value="searchOperators" class="p-datatable-sm" responsiveLayout="scroll">
        <Column field="feature" header="Feature" />
        <Column field="syntax" header="Syntax / Example" />
        <Column field="description" header="Description" />
      </DataTable>
    </Panel>
    <Divider />
    <Panel header="🧭 Searchable Fields" toggleable class="mt-3">
      <ul>
        <li>Scientific name</li>
        <li>Common name</li>
        <li>Taxonomic rank (Family, Genus)</li>
        <li>Collector</li>
        <li>Collection or Accession number</li>
        <li>Location (Country, Site, Coordinates)</li>
        <li>Collection date</li>
        <li>Preservation type (e.g. fossil, pinned specimen)</li>
        <li>Institutional notes or remarks</li>
      </ul>
    </Panel>
    <Divider />
    <Panel header="📘 Example Searches" toggleable class="mt-3">
      <DataTable :value="searchExamples" class="p-datatable-sm" responsiveLayout="scroll">
        <Column field="goal" header="What You Want to Find" />
        <Column field="query" header="Try This Search" />
      </DataTable>
    </Panel>
    <Divider />
    <Panel header="💡 Search Tips" toggleable class="mt-3">
      <ul>
        <li>Search is <strong>not case-sensitive</strong></li>
        <li>Use <strong>quotes</strong> to search exact phrases</li>
        <li>Wildcards (<code>*</code>) match multiple terms</li>
        <li>Filters can be applied after your search to narrow results</li>
      </ul>
    </Panel>
    <Divider />
    <Panel header="❓ Still Need Help?" toggleable class="mt-3">
      <p>
        If you're unsure how to search or can't find what you're looking for, contact our
        collections team:
      </p>
      <p>
        <strong>Email:</strong>
        <a href="mailto:collections@yourmuseum.org">collections@yourmuseum.org</a>
      </p>
    </Panel> -->
  </div>
</template>
<script setup>
import { ref } from 'vue'
const searchOperators = ref([
  { feature: 'Exact phrase', syntax: '"Panthera leo"', description: 'Finds the exact phrase' },
  { feature: 'AND', syntax: 'mammal AND fossil', description: 'Matches both terms' },
  { feature: 'OR', syntax: 'beetle OR butterfly', description: 'Matches either term' },
  { feature: 'NOT', syntax: 'bird NOT fossil', description: 'Excludes matching term' },
  { feature: 'Wildcard', syntax: 'felis*', description: 'Matches anything starting with "felis"' },
  {
    feature: 'Field search',
    syntax: 'collector:"Darwin"',
    description: 'Searches a specific field'
  }
])
const searchExamples = ref([
  { goal: 'All butterfly specimens from Peru', query: 'butterfly AND Peru' },
  { goal: 'Fossils collected by Darwin', query: 'fossil AND collector:"Charles Darwin"' },
  { goal: 'Amphibians collected before 1900', query: 'amphibian AND date:<1900' },
  { goal: 'Specimens from Galapagos', query: 'Galapagos' }
])
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
