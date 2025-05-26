<template>
  <div class="grid divLink" @click="doSearch">
    <div class="col-6" no-gutters style="float: left; text-align: left">
      <Button text :label="$t(text)" />
    </div>
    <div class="col-2">
      <Button
        v-if="loading"
        text
        icon="pi pi-spin pi-spinner"
        aria-label="loading"
        title="loading"
      />
    </div>

    <div class="col-4" style="float: left; text-align: left">
      <Button text>
        {{ total }}
      </Button>
    </div>
  </div>
</template>
<script setup>
const props = defineProps(['loading', 'text', 'total'])
const emits = defineEmits(['doSearch'])

function doSearch() {
  emits('doSearch')
}
</script>
<style scoped>
.p-button-text:hover {
  color: var(--p-emerald-500) !important;
  text-decoration: none !important;
  background: transparent !important;
}

.p-button-text {
  text-decoration: underline;
}

.divLink {
  min-width: 100% !important;
  cursor: pointer !important;
}
.divLink {
  min-width: 100% !important;
  cursor: pointer !important;
}
.divLink:hover {
  background: #0e3a12;
  color: var(--p-emerald-500) !important;
}
</style>
