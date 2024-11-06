<script setup>
import { ref, onMounted } from 'vue'
import axios from './axios/axios'

const items = ref([])

const fetchItems = async () => {
  try {
    const response = await axios.get('items') // This will use the base URL + '/items'
    items.value = response.data
  } catch (error) {
    console.error('Error fetching items:', error)
  }
}

onMounted(fetchItems)
</script>

<template>
  <div>
    <h1>Items</h1>
    <ul>
      <li v-for="item in items" :key="item.id">{{ item.name }}</li>
    </ul>
  </div>
</template>
