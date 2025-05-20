<script setup>
import { defineProps } from 'vue'
import RoomRedirectButton from './RoomRedirectButton.vue';
const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

</script>

<template>
  <div class="product-box">
    <h3>{{ product.name }}</h3>
    <p>Price: ${{ product.startingPrice }}</p>
    <RoomRedirectButton :product="product" />
  </div>
</template>

<style scoped>
.product-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 220px; 
  border: 1px solid #e0e0e0; 
  padding: 1.5rem; 
  border-radius: 16px; 
  background-color: #ffffff; 
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); 
  transition: transform 0.3s ease, box-shadow 0.3s ease; 
  cursor: default; 
}

.product-box:hover {
  transform: translateY(-5px); 
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12); 
}

h3 {
  margin-top: 0; 
  margin-bottom: 0.75rem; 
  font-size: 1.25rem; 
  color: #333; 
  font-weight: 600; 
}

p {
  margin-bottom: 1.5rem; 
  font-size: 1rem;
  color: #555; 
}

.auction-button {
  background-color: #28a745; 
  color: white; 
  padding: 0.75em 1.5em; 
  border: none;
  cursor: pointer;
  border-radius: 8px;
  margin-top: auto; 
  font-weight: 500;
  transition: background-color 0.2s ease;
  align-self: center; 
  width: fit-content; 
}

.auction-button:hover {
  background-color: #218838; 
  opacity: 1; 
}
</style>
