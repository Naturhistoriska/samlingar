<template>
  <Card>

    <template #content>
      <div class="grid">
       
         <div class="col">
          <div class>Taxonomy</div>
          <div>
            <Checkbox v-model="selectedColumns" id="Kingdom" name="Kingdom" value="Kingdom" @change="onChang"/>
            <label for="kingdom" class="text-sm">{{ $t('columns.kingdom') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="order" name="Order" value="order" @change="onChang"/>
            <label for="order" class="text-sm">{{ $t('columns.order') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="phylum" name="Phylum" value="phylum" @change="onChang"/>
            <label for="phylum" class="text-sm">{{ $t('columns.phylum') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="class" name="Class" value="class" @change="onChang"/>
            <label for="clazz" class="text-sm">{{ $t('columns.clazz') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="Family" name="Family" value="family" @change="onChang"/>
            <label for="family" class="text-sm">{{ $t('columns.family') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="scientificname" name="scientificname" value="scientificname" @change="onChang"/>
            <label for="scientificname" class="text-sm">{{ $t('columns.scientificname') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="vernacularName" name="vernacularName" value="vernacularName" @change="onChang"/>
            <label for="vernacularName" class="text-sm">{{ $t('columns.vernacularName') }}</label>
          </div>
        </div>

       
        <div class="col">
          <div>Specimen</div>
          <div>
            <Checkbox v-model="selectedColumns" id="collectionName" name="collectionName" value="collectionName" @change="onChang"/>
            <label for="collectionName" class="text-sm">{{ $t('columns.collectionName') }}</label>
          </div>   
          <div>
            <Checkbox v-model="selectedColumns" id="CatalogNumber" name="CatalogNumber" value="CatalogNumber" @change="onChang"/>
            <label for="CatalogNumber" class="text-sm">{{ $t('columns.catalogNumber') }}</label>
          </div>   
          <div>
            <Checkbox v-model="selectedColumns" id="CreatedDate" name="CreatedDate" value="CreatedDate" @change="onChang"/>
            <label for="CreatedDate" class="text-sm">{{ $t('columns.createdDate') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="typeStatus" name="typeStatus" value="typeStatus" @change="onChang"/>
            <label for="typeStatus" class="text-sm">{{ $t('columns.typeStatus') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="originalname" name="originalname" value="originalname" @change="onChang"/>
            <label for="originalname" class="text-sm">{{ $t('columns.originalName') }}</label>
          </div>

          <div>
            <Checkbox v-model="selectedColumns" id="IdentifiedBy" name="IdentifiedBy" value="IdentifiedBy" @change="onChang"/>
            <label for="IdentifiedBy" class="text-sm">{{ $t('columns.identifiedBy') }}</label>
          </div>
        </div>

         <div class="col">
          <div>Geography</div>
          <div>
            <Checkbox v-model="selectedColumns" id="Continent" name="Continent" value="Continent" @change="onChang"/>
            <label for="Continent" class="text-sm">{{ $t('columns.continent') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="Country" name="Country" value="Country" @change="onChang"/>
            <label for="Country" class="text-sm">{{ $t('columns.country') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="Habitat" name="Habitat" value="Habitat" @change="onChang" />
            <label for="Habitat" class="text-sm">{{ $t('columns.habitat') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="Island" name="Island" value="Island" @change="onChang"/>
            <label for="Island" class="text-sm">{{ $t('columns.island') }}</label>
          </div>     
          <div>
            <Checkbox v-model="selectedColumns" id="Locality" name="Locality" value="Locality" @change="onChang" />
            <label for="Locality" class="text-sm">{{ $t('columns.locality') }}</label>
          </div>
          <div>
            <Checkbox v-model="selectedColumns" id="ExpeditionName" name="ExpeditionName" value="ExpeditionName" @change="onChang"/>
            <label for="ExpeditionName" class="text-sm">{{ $t('columns.expeditionName') }}</label>
          </div>

        </div>
      </div>
    </template>
  </Card>
</template>
<script setup>
import { ref } from 'vue'
const selectedColumns = ref([])
function onChang(event) {
  console.log('onChang', selectedColumns.value)
}
</script>
