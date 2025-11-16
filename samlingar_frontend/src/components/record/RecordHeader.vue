<template>
  <div class="grid">
    <p class="m-0 titleStyle col-12">{{ $t('records.recordTitle') }}</p>

    <div class="classificationText col-12">{{ classification }}</div>

    <div class="scientificNameText col-12">
      <i>{{ name }}</i>
    </div>
  </div>
</template>

<script setup>
defineProps({
  classification: String,
  name: String
})
</script>

<style scoped>
.titleStyle {
  font-weight: bold;
  font-size: 2em;
  color: #777676;
}
.classificationText {
  font-size: 1em;
  color: #6fade6;
}
.scientificNameText {
  font-size: 1.5em;
  font-weight: bold;
  color: #6fade6;
}

@media (max-width: 768px) {
  .titleStyle {
    font-size: 1.2rem;
  }

  .classificationText {
    font-size: 0.7em;
  }

  .scientificNameText {
    font-size: 0.8em;
    font-weight: bold;
    color: #6fade6;
  }
}
</style>
