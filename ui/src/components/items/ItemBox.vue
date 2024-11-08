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
  height: 200px; /* Adjust the height as needed */
  border: 1px solid #ccc;
  padding: 1rem;
  border-radius: 12px;
  background-color: #f9f9f9;
}

h3 {
  margin-bottom: 0.5rem;
}

p {
  margin-bottom: 1rem;
}

.auction-button {
  background-color: white;
  color: green;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-color: green;
  border: 2px solid green;
  border-radius: 8px;
  margin-top: auto; /* Ensures the button is pushed to the bottom */
}

.auction-button:hover {
  opacity: 0.8;
}
</style>
