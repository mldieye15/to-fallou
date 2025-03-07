<script setup>
import { useAnneeStore } from '@/views/apps/annees/store'
import { storeToRefs } from "pinia"
import { ref, watchEffect, computed } from 'vue'

const anneeStore = useAnneeStore()
const searchQuery = ref('')
const dateRange = ref('')
const selectedStatus = ref()
const rowPerPage = ref(10)
const currentPage = ref(1)
const totalPage = ref(1)
const totalItems = ref(0)
const selectedRows = ref([])
const selectAllItems = ref(false)
const { dataListe, headerTable, loading } = storeToRefs(anneeStore)

watchEffect(() => {
  const [start, end] = dateRange.value ? dateRange.value.split('to') : ''

  anneeStore.all({
    q: searchQuery.value,
    status: selectedStatus.value,
    perPage: rowPerPage.value,
    currentPage: currentPage.value,
    startDate: start,
    endDate: end,
  }).then(response => {
    totalPage.value = response.totalPage
    totalItems.value = response.totalItems
  }).catch(error => {
    console.log(error)
  })
})

// 👉 Fetch Items
watchEffect(() => {
  if (currentPage.value > totalPage.value)
    currentPage.value = totalPage.value
})

// 👉 Computing pagination data
const paginationData = computed(() => {
  const firstIndex = dataListe.value.length ? (currentPage.value - 1) * rowPerPage.value + 1 : 0
  const lastIndex = dataListe.value.length + (currentPage.value - 1) * rowPerPage.value
  
  return `${firstIndex}-${lastIndex} of ${totalItems.value}`
})

// 👉 Add/Remove all checkbox ids in/from array
const selectUnselectAll = () => {
  selectAllItems.value = !selectAllItems.value
  if (selectAllItems.value) {
    dataListe.value.forEach(item => {
      if (!selectedRows.value.includes(`check${item.id}`))
        selectedRows.value.push(`check${item.id}`)
    })
  } else {
    selectedRows.value = []
  }
}

// 👉 watch if checkbox array is empty all checkbox should be uncheck
watch(selectedRows, () => {
  if (!selectedRows.value.length)
    selectAllItems.value = false
}, { deep: true })

const addRemoveIndividualCheckbox = checkID => {
  if (selectedRows.value.includes(checkID)) {
    const index = selectedRows.value.indexOf(checkID)

    selectedRows.value.splice(index, 1)
  } else {
    selectedRows.value.push(checkID)
    selectAllItems.value = true
  }
}

const computedMoreList = computed(() => {
  return paramId => [
    {
      title: 'Download',
      value: 'download',
      prependIcon: 'bx-download',
    },
    {
      title: 'Edit',
      value: 'edit',
      prependIcon: 'bx-pencil',
      to: {
        name: 'apps-annees-edit-id',
        params: { id: paramId },
      },
    },
    {
      title: 'Duplicate',
      value: 'duplicate',
      prependIcon: 'bx-layer',
    },
  ]
})
</script>

<template>
  <section v-if="dataListe">
    <!-- 👉 Filters  -->
    <VCard
      title="Filters"
      class="mb-6"
    >
      <VCardText>
        <VRow>
          <!-- 👉 Status filter -->
          <VCol
            cols="12"
            md="6"
          >
            <VSelect
              v-model="selectedStatus"
              label="Select Status"
              clearable
              clear-icon="bx-x"
              :items="['Downloaded', 'Draft', 'Sent', 'Paid', 'Partial Payment', 'Past Due']"
            />
          </VCol>

          <!-- 👉 DateRange filter -->
          <VCol
            cols="12"
            md="6"
          >
            <AppDateTimePicker
              v-model="dateRange"
              label="Date"
              clear-icon="bx-x"
              clearable
              :config="{ mode: 'range' }"
            />
          </VCol>
        </VRow>
      </VCardText>
    </VCard>

    <VCard id="item-list">
      <VCardText class="d-flex align-center flex-wrap gap-4">
        <!-- 👉 Actions  -->
        <div class="me-3">
          <VSelect
            density="compact"
            label="Actions"
            :items="['Delete', 'Edit', 'Send']"
            class="item-list-actions"
            :disabled="!selectedRows.length"
          />
        </div>

        <VSpacer />

        <div class="d-flex align-center flex-wrap gap-6">
          <!-- 👉 Search  -->
          <div class="item-list-search">
            <VTextField
              v-model="searchQuery"
              placeholder="Search Item"
              density="compact"
            />
          </div>

          <!-- 👉 Create item -->
          <VBtn :to="{ name: 'apps-annees-add' }">
            Create item
          </VBtn>
        </div>
      </VCardText>

      <VDivider />

      <!-- SECTION Table -->
      <VTable class="text-no-wrap">
        <!-- 👉 Table head -->
        <thead>
          <tr>
            <!-- 👉 Check/Uncheck all checkbox -->
            <th
              scope="col"
              class="text-center"
            >
              <div style="inline-size: 1rem;">
                <VCheckbox
                  :model-value="selectAllItems"
                  :indeterminate="(dataListe.length !== selectedRows.length) && !!selectedRows.length"
                  @click="selectUnselectAll"
                />
              </div>
            </th>
            <th scope="col">
              LIBELLE
            </th>
            <th scope="col">
              ACTIONS
            </th>
          </tr>
        </thead>

        <!-- 👉 Table Body -->
        <tbody>
          <tr
            v-for="item in dataListe"
            :key="item.id"
          >
            <!-- 👉 Individual checkbox -->
            <td>
              <div style="inline-size: 1rem;">
                <VCheckbox
                  :id="`check${item.id}`"
                  :model-value="selectedRows.includes(`check${item.id}`)"
                  @click="addRemoveIndividualCheckbox(`check${item.id}`)"
                />
              </div>
            </td>

            <!-- 👉 Libelle -->
            <td>
              <RouterLink :to="{ name: 'apps-annees-preview-id', params: { id: item.id } }">
                {{ item.libelle }}
              </RouterLink>
            </td>

            <!-- 👉 Actions -->
            <td>
              <VMenu>
                <template #activator="{ on, attrs }">
                  <VBtn
                    icon
                    v-bind="attrs"
                    v-on="on"
                  >
                    <VIcon icon="bx-dots-horizontal" />
                  </VBtn>
                </template>
                <VList>
                  <VListItem
                    v-for="action in computedMoreList(item.id)"
                    :key="action.value"
                    :title="action.title"
                    @click="performAction(action, item)"
                  >
                    <VListItemIcon>
                      <VIcon :icon="action.prependIcon" />
                    </VListItemIcon>
                    <VListItemContent>
                      {{ action.title }}
                    </VListItemContent>
                  </VListItem>
                </VList>
              </VMenu>
            </td>
          </tr>
        </tbody>

        <!-- 👉 table footer  -->
        <tfoot v-show="!dataListe.length">
          <tr>
            <td
              colspan="8"
              class="text-center text-body-1"
            >
              No data available
            </td>
          </tr>
        </tfoot>
      </VTable>
      <!-- !SECTION -->

      <VDivider />

      <!-- SECTION Pagination -->
      <VCardText class="d-flex flex-wrap justify-end gap-4 pa-2">
        <!-- 👉 Rows per page -->
        <div
          class="d-flex align-center"
          style="inline-size: 171px;"
        >
          <span class="text-no-wrap text-sm me-3">Rows per page:</span>
          <VSelect
            v-model="rowPerPage"
            density="compact"
            class="small-select"
            variant="plain"
            :items="[10, 20, 30, 50]"
          />
        </div>

        <!-- 👉 Pagination and pagination meta -->
        <div class="d-flex align-center">
          <h6 class="text-sm font-weight-regular">
            {{ paginationData }}
          </h6>
        </div>
        <VPagination
          v-model="currentPage"
          size="small"
          :total-visible="1"
          :length="totalPage"
          @next="selectedRows = []"
          @prev="selectedRows = []"
        />
      </VCardText>
      <!-- !SECTION -->
    </VCard>
  </section>
</template>

<style lang="scss" scoped>
.item-list-search {
  max-inline-size: 220px;
}
</style>
