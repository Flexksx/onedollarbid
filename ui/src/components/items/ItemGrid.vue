<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from '../../axios/axios'
import ItemBox from './ItemBox.vue'

const items = ref([])
const currentPage = ref(1)
const itemsPerPage = 8
const totalItems = ref(0) 
const nextButtonDisabled = ref(false)

const fetchItems = async (page = 1) => {
  const offset = (page - 1) * itemsPerPage
  try {
    const response = await axios.get('items', {
      params: { offset, limit: itemsPerPage }
    })
    console.log('response:', response);

    
    items.value = response.data 
    totalItems.value = response.data.length 

    console.log('totalItems:', totalItems.value);
    console.log('items:', items.value);

    
    if (response.data.length < itemsPerPage) {
      nextButtonDisabled.value = true
    } else {
      nextButtonDisabled.value = false
    }
  } catch (error) {
    console.error('Error fetching items:', error)
  }
}


const goToNextPage = () => {
  if (nextButtonDisabled.value) return
  currentPage.value++
  fetchItems(currentPage.value)
}


const goToPreviousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    fetchItems(currentPage.value)
  }
}


onMounted(() => fetchItems(currentPage.value))
</script>

<template>
  <div>
    <div class="grid-container">
      <ItemBox v-for="item in items" :key="item.id" :product="item" />
    </div>

    
    <div class="pagination">
      <button @click="goToPreviousPage" :disabled="currentPage === 1">Previous</button>
      <button @click="goToNextPage" :disabled="nextButtonDisabled">Next</button>
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
</style>

