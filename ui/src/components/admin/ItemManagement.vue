<script setup>
import { ref, computed } from 'vue';
import { useAuctionItemStore } from '../../api/stores/auctionItemStore';

const itemStore = useAuctionItemStore();
const searchQuery = ref('');
const selectedItem = ref(null);
const isModalOpen = ref(false);
const isAddModalOpen = ref(false);

// Form data for editing
const itemName = ref('');
const startingPrice = ref(0);
const isSold = ref(false);
const soldPrice = ref(null);

// Form data for adding
const newItemName = ref('');
const newStartingPrice = ref(0);

// Filtered items based on search query
const filteredItems = computed(() => {
  if (!searchQuery.value) return itemStore.items;
  
  const query = searchQuery.value.toLowerCase();
  return itemStore.items.filter(item => 
    item.name.toLowerCase().includes(query)
  );
});

// Select an item for editing
const selectItem = (item) => {
  selectedItem.value = { ...item };
  itemName.value = item.name;
  startingPrice.value = item.startingPrice;
  isSold.value = item.sold || false;
  soldPrice.value = item.soldPrice || null;
  isModalOpen.value = true;
};

// Close the edit modal
const closeModal = () => {
  isModalOpen.value = false;
  selectedItem.value = null;
  resetForm();
};

// Close the add modal
const closeAddModal = () => {
  isAddModalOpen.value = false;
  resetNewForm();
};

// Reset the edit form
const resetForm = () => {
  itemName.value = '';
  startingPrice.value = 0;
  isSold.value = false;
  soldPrice.value = null;
};

// Reset the add form
const resetNewForm = () => {
  newItemName.value = '';
  newStartingPrice.value = 0;
};

// Save item changes
const saveItem = async () => {
  if (!selectedItem.value) return;
  
  try {
    await itemStore.updateAuctionItem(selectedItem.value.id, {
      name: itemName.value,
      startingPrice: parseFloat(startingPrice.value),
      sold: isSold.value,
      soldPrice: isSold.value ? parseFloat(soldPrice.value) : null
    });
    closeModal();
  } catch (error) {
    console.error('Failed to update item:', error);
  }
};

// Add new item
const addItem = async () => {
  if (!newItemName.value || parseFloat(newStartingPrice.value) <= 0) {
    alert('Please fill all required fields with valid values.');
    return;
  }
  
  try {
    await itemStore.createAuctionItem({
      name: newItemName.value,
      startingPrice: parseFloat(newStartingPrice.value)
    });
    closeAddModal();
  } catch (error) {
    console.error('Failed to add item:', error);
  }
};

// Delete an item
const deleteItem = async (itemId) => {
  if (confirm('Are you sure you want to delete this item? This action cannot be undone.')) {
    try {
      await itemStore.deleteAuctionItem(itemId);
    } catch (error) {
      console.error('Failed to delete item:', error);
    }
  }
};

// Open add modal
const openAddModal = () => {
  resetNewForm();
  isAddModalOpen.value = true;
};
</script>

<template>
  <div class="item-management">
    <h2>Item Management</h2>
    
    <!-- Add Button and Search -->
    <div class="controls">
      <button @click="openAddModal" class="add-btn">Add New Item</button>
      <input 
        type="text" 
        v-model="searchQuery" 
        placeholder="Search items..." 
        class="search-input"
      />
    </div>
    
    <!-- Items Table -->
    <div class="table-container">
      <table v-if="filteredItems.length > 0" class="items-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in filteredItems" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>${{ item.startingPrice }}</td>
            <td>
              <span :class="{ 'sold': item.sold, 'available': !item.sold }">
                {{ item.sold ? 'Sold' : 'Available' }}
              </span>
            </td>
            <td class="actions">
              <button @click="selectItem(item)" class="edit-btn">Edit</button>
              <button @click="deleteItem(item.id)" class="delete-btn">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-else class="no-items">
        <p>No items found matching your search criteria.</p>
      </div>
    </div>
    
    <!-- Edit Item Modal -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>Edit Item</h3>
        
        <div class="form-group">
          <label for="itemName">Item Name</label>
          <input 
            type="text" 
            id="itemName" 
            v-model="itemName" 
            placeholder="Enter item name"
          />
        </div>
        
        <div class="form-group">
          <label for="startingPrice">Starting Price ($)</label>
          <input 
            type="number" 
            id="startingPrice" 
            v-model="startingPrice" 
            min="0.01" 
            step="0.01"
          />
        </div>
        
        <div class="form-group checkbox">
          <input 
            type="checkbox" 
            id="isSold" 
            v-model="isSold"
          />
          <label for="isSold">Item is Sold</label>
        </div>
        
        <div v-if="isSold" class="form-group">
          <label for="soldPrice">Sold Price ($)</label>
          <input 
            type="number" 
            id="soldPrice" 
            v-model="soldPrice" 
            min="0.01" 
            step="0.01"
          />
        </div>
        
        <div class="modal-actions">
          <button @click="closeModal" class="cancel-btn">Cancel</button>
          <button @click="saveItem" class="save-btn">Save Changes</button>
        </div>
      </div>
    </div>
    
    <!-- Add Item Modal -->
    <div v-if="isAddModalOpen" class="modal-overlay" @click.self="closeAddModal">
      <div class="modal-content">
        <h3>Add New Item</h3>
        
        <div class="form-group">
          <label for="newItemName">Item Name</label>
          <input 
            type="text" 
            id="newItemName" 
            v-model="newItemName" 
            placeholder="Enter item name"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="newStartingPrice">Starting Price ($)</label>
          <input 
            type="number" 
            id="newStartingPrice" 
            v-model="newStartingPrice" 
            min="0.01" 
            step="0.01"
            required
          />
        </div>
        
        <div class="modal-actions">
          <button @click="closeAddModal" class="cancel-btn">Cancel</button>
          <button @click="addItem" class="save-btn">Add Item</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.item-management {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.add-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.search-input {
  width: 60%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.table-container {
  overflow-x: auto;
}

.items-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.items-table th,
.items-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.items-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.items-table tr:hover {
  background-color: #f9f9f9;
}

.sold {
  color: #f44336;
  font-weight: 500;
}

.available {
  color: #4CAF50;
  font-weight: 500;
}

.actions {
  display: flex;
  gap: 10px;
}

.edit-btn, .delete-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.edit-btn {
  background-color: #2196F3;
  color: white;
}

.delete-btn {
  background-color: #f44336;
  color: white;
}

.no-items {
  text-align: center;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-group.checkbox {
  display: flex;
  align-items: center;
}

.form-group.checkbox label {
  margin-bottom: 0;
  margin-left: 8px;
}

.form-group input[type="text"],
.form-group input[type="number"] {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn, .save-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f2f2f2;
  color: #333;
}

.save-btn {
  background-color: #4CAF50;
  color: white;
}
</style>
