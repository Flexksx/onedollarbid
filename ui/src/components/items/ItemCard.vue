<script setup>
import { defineProps } from 'vue'
import RoomRedirectButton from './RoomRedirectButton.vue';
import { useAuctionItemStore } from '../../api/stores/auctionItemStore';

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const auctionItemStore = useAuctionItemStore();

const deleteItem = async () => {
  if (confirm(`Are you sure you want to delete "${props.product.name}"?`)) {
    await auctionItemStore.deleteAuctionItem(props.product.id);
    if (auctionItemStore.error) {
      alert(`Error deleting item: ${auctionItemStore.error}`);
    } else {
      alert(`"${props.product.name}" deleted successfully.`);
    }
  }
}

</script>

<template>
  <div class="product-box">
    <div>
      <h3>{{ product.name }}</h3>
      <p>Price: ${{ product.startingPrice }}</p>
    </div>
    <div class="card-actions">
      <RoomRedirectButton :product="product" />
      <button @click="deleteItem" class="delete-button" :disabled="auctionItemStore.loading">
        {{ auctionItemStore.loading && auctionItemStore.currentItem?.id === product.id ? 'Deleting...' : 'Delete' }}
      </button>
    </div>
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

.card-actions {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-top: auto;
  width: 100%;
}

.auction-button {
  background-color: #28a745; 
  color: white; 
  padding: 0.75em 1.5em; 
  border: none;
  cursor: pointer;
  border-radius: 8px;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.auction-button:hover {
  background-color: #218838; 
  opacity: 1; 
}

.delete-button {
  background-color: #dc3545;
  color: white;
  padding: 0.75em 1.5em;
  border: none;
  cursor: pointer;
  border-radius: 8px;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.delete-button:hover {
  background-color: #c82333;
}

.delete-button:disabled {
  background-color: #e08080;
  cursor: not-allowed;
}
</style>
