<template>
  <div class="grid">
    <div>
      Free text search
      <small>(Search species, genus, family, collectors, location, etc.)</small>
    </div>
    <div class="flex flex-col gap-2" style="width: 100%">
      <div class="flex-col gap-2 searchInput">
        <InputGroup>
          <InputText
            id="simpleSearchInput1"
            v-model="value"
            @keydown.enter="onPressEnter"
            :placeholder="$t('search.freeTextSearch')"
            aria-describedby="simpleSearchInput-help"
            class="w-full"
          />
          <Button
            icon="pi pi-search"
            style="max-width: 30px; max-height: 30px"
            :loading="loading"
            @click="onSearchClick"
          />
        </InputGroup>

        <div class="advanceLink">
          <Button text @click="onAdvanceSearchLinkClick">
            <small id="simpleSearchInput-help"
              >{{ $t('search.advanceSearch') }} (Under development)
            </small>
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const store = useStore()
const router = useRouter()

const loading = ref(false)
const value = ref()

function onAdvanceSearchLinkClick() {
  store.commit('setIsAdvanceSearch', true)
  router.push('/advanceSearch')
}

function onPressEnter() {
  console.log('onPressEnter')
}

function onSearchClick() {
  console.log('onSearchClick')
}
</script>
<style scoped>
.searchInput {
  min-width: 90%;
  text-align: left;
  float: left;
}
.advanceLink {
  float: right;
}
</style>
