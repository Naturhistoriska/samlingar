<template>
  <v-select
    :items="supportedCollections"
    :label="$t('search.selectSpeciesGroup')"
    :clearable="true"
    density="compact"
    max-width="500"
    @update:modelValue="setSelected"
  />
</template>
<script>
export default {
  name: 'SpeciesGroup',
  data() {
    return {
      // selected: ''
    }
  },
  computed: {
    supportedCollections() {
      return import.meta.env.VITE_SUPPORTED_COLLECTIONS.split(',')
    }
  },
  methods: {
    setSelected(value) {
      console.log('selected...' + value)
      this.$emit('species-group', value)
    }
  }
}
</script>
