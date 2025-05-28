<template>
  <div class="card flex justify-center">
    <Breadcrumb :home="home" :model="items">
      <template #item="{ item, props }">
        <router-link v-if="item.route" v-slot="{ href, navigate }" :to="item.route" custom>
          <a :href="href" v-bind="props.action" @click="navigate">
            <span :class="[item.icon, 'text-color']" />
            <span class="text-primary font-semibold">{{ item.label }}</span>
          </a>
        </router-link>
        <a v-else :href="item.url" :target="item.target" v-bind="props.action">
          <span class="text-surface-700 dark:text-surface-0">{{ item.label }}</span>
        </a>
      </template>
    </Breadcrumb>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(async () => {
  console.log('onMounted')

  const currentRouteName = router.currentRoute.value.name
  console.log('currentRouteName', currentRouteName)

  items.value = [{ label: currentRouteName }]
})

const home = ref({
  icon: 'pi pi-home',
  label: 'Home',
  route: '/'
})
const items = ref([])
// const items = ref([
//   { label: 'Search', route: '' },
//   { label: 'InputText', route: '/inputtext' }
// ])
</script>
