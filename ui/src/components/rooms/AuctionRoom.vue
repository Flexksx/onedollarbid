<script setup>
import { ref, onMounted } from 'vue'
import axios from '../../axios/axios' // Adjust based on your actual axios path

const product = ref({})
const bids = ref([])
const roomId = ref('')
const bidAmount = ref(0)

</script>

<template>
    <div class="auction-room">
      <h2>Auction Room for {{ product.name }}</h2>
      <p>Starting Price: ${{ product.startingPrice }}</p>
      <h3>Current Bids</h3>
    </div>
  </template>
  
  
  <style scoped>
  .auction-room {
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
    max-width: 600px;
    margin: 0 auto;
  }
  
  button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    cursor: pointer;
    border-radius: 8px;
  }
  
  button:hover {
    opacity: 0.8;
  }
  
  h2, h3, h4 {
    margin-bottom: 1rem;
  }
  
  ul {
    list-style-type: none;
    padding-left: 0;
  }
  </style>
  