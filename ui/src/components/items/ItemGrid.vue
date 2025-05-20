<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import ItemCard from './ItemCard.vue'
import { useAuctionItemStore } from '../../api/stores/auctionItemStore'

const auctionItemStore = useAuctionItemStore();

const currentPage = ref(1)
const itemsPerPage = 8
const nextButtonDisabled = ref(false)

const items = computed(() => auctionItemStore.items);

const fetchItems = async (page = 1) => {
  const offset = (page - 1) * itemsPerPage;
  await auctionItemStore.fetchAuctionItemsPaginated(offset, itemsPerPage);
}

watch(() => auctionItemStore.items, (newItems) => {
  if (newItems.length < itemsPerPage) {
    nextButtonDisabled.value = true;
  } else {
    nextButtonDisabled.value = false;
  }
}, { immediate: true }); 

const goToNextPage = () => {
  if (nextButtonDisabled.value || auctionItemStore.loading) return
  currentPage.value++
  fetchItems(currentPage.value)
}

const goToPreviousPage = () => {
  if (currentPage.value > 1 && !auctionItemStore.loading) {
    currentPage.value--
    fetchItems(currentPage.value)
  }
}

onMounted(() => {
  fetchItems(currentPage.value)
})
</script>

<template>
  <div>
    <div v-if="auctionItemStore.loading" class="loading-indicator">Loading items...</div>
    <div v-else-if="auctionItemStore.error" class="error-message">
      Error fetching items: {{ auctionItemStore.error }}
    </div>
    <div v-else class="grid-container">
      <ItemCard v-for="item in items" :key="item.id" :product="item" />
    </div>

    <div class="pagination">
      <button @click="goToPreviousPage" :disabled="currentPage === 1 || auctionItemStore.loading">Previous</button>
      <button @click="goToNextPage" :disabled="nextButtonDisabled || auctionItemStore.loading">Next</button>
    </div>
  </div>
</template>

<style scoped>

.grid-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 columns */
  gap: 1rem; /* spacing between items */
  padding: 1rem;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 1rem;
}

.pagination button {
  background-color: #007BFF; /* Blue background */
  color: white; /* White text */
  border: none;
  padding: 0.5rem 1rem; /* padding inside buttons */
  font-size: 1rem; /* font size */
  cursor: pointer; /* pointer cursor on hover */
  border-radius: 5px; /* rounded corners */
  transition: background-color 0.3s ease, transform 0.2s ease; /* smooth transitions */
  margin: 0 0.5rem; /* spacing between buttons */
}

.pagination button:hover {
  background-color: #0056b3; /* Darker blue on hover */
  transform: scale(1.05); /* Slight zoom effect */
}

.pagination button:disabled {
  background-color: #ccc; /* Disabled background */
  cursor: not-allowed; /* Not allowed cursor when disabled */
}

.pagination button:focus {
  outline: none; /* Remove default focus outline */
}

.loading-indicator, .error-message {
  text-align: center;
  padding: 20px;
  font-size: 1.2em;
}

.error-message {
  color: red;
}
</style>

