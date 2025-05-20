<script setup>
import { ref } from 'vue'
import { useAuctionItemStore } from '../../../../api/stores/auctionItemStore';

const auctionItemStore = useAuctionItemStore();
const isOverlayVisible = ref(false)
const itemName = ref('')
const startingPrice = ref(0)

const toggleOverlay = () => {
    isOverlayVisible.value = !isOverlayVisible.value
    if (!isOverlayVisible.value) {
        
        itemName.value = '';
        startingPrice.value = 0;
        auctionItemStore.error = null; 
    }
}

const submitForm = async () => {
    if (!itemName.value || startingPrice.value <= 0) {
        alert('Please fill out all fields correctly. Starting price must be greater than 0.')
        return
    }
    const newItem = {
        name: itemName.value,
        startingPrice: parseFloat(startingPrice.value) 
    }

    await auctionItemStore.createAuctionItem(newItem);

    if (!auctionItemStore.error) {
        alert('Item created successfully!')
        toggleOverlay() 
        
        
    } else {
        
        alert(`Error creating item: ${auctionItemStore.error}`)
    }
}

</script>
<template>
    <div>
        
        <div class="button-wrapper">
            <button @click="toggleOverlay" class="add-item-button">
                <span class="plus-icon">+</span> Add New Item
            </button>
        </div>

        
        <div v-if="isOverlayVisible" class="overlay" @click.self="toggleOverlay"> {
            <div class="overlay-content">
                <h2>Create New Item</h2>

                
                <form @submit.prevent="submitForm">
                    <div class="form-group">
                        <label for="itemName">Product Name</label>
                        <input type="text" id="itemName" placeholder="Enter product name" v-model="itemName" required />
                    </div>

                    <div class="form-group">
                        <label for="startingPrice">Starting Price ($)</label>
                        <input type="number" id="startingPrice" placeholder="Enter starting price" v-model="startingPrice" min="0.01" step="0.01" required />
                    </div>
                    
                    <div v-if="auctionItemStore.error" class="error-message">
                        {{ auctionItemStore.error }}
                    </div>

                    <div class="form-actions">
                        <button type="button" class="close-btn" @click="toggleOverlay">Cancel</button>
                        <button type="submit" class="submit-btn" :disabled="auctionItemStore.loading">
                            {{ auctionItemStore.loading ? 'Creating...' : 'Create Item' }}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>


<style scoped>
.button-wrapper {
    display: flex;
    justify-content: center;
    margin-bottom: 1.5rem; 
}

.add-item-button {
    background-color: #007bff; 
    color: white;
    padding: 12px 24px; 
    border: none;
    border-radius: 8px; 
    cursor: pointer;
    font-size: 1rem; 
    font-weight: 500;
    display: flex;
    align-items: center;
    gap: 8px; 
    transition: background-color 0.2s ease-in-out, transform 0.1s ease;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-item-button:hover {
    background-color: #0056b3; 
    transform: translateY(-1px);
}

.add-item-button:active {
    transform: translateY(0px);
}

.plus-icon {
    font-size: 1.2rem;
    font-weight: bold;
}


.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6); 
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    padding: 20px; 
}


.overlay-content {
    background-color: #ffffff;
    padding: 2rem; 
    border-radius: 12px; 
    width: 100%;
    max-width: 480px; 
    text-align: left; 
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    animation: fadeInScale 0.3s ease-out;
}

@keyframes fadeInScale {
    from {
        opacity: 0;
        transform: scale(0.95);
    }
    to {
        opacity: 1;
        transform: scale(1);
    }
}

.overlay-content h2 {
    margin-top: 0;
    margin-bottom: 1.5rem;
    color: #333;
    font-size: 1.75rem;
    text-align: center;
}


form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem; 
}

.form-group {
    display: flex;
    flex-direction: column;
}

.form-group label {
    margin-bottom: 0.5rem;
    font-weight: 500;
    color: #555;
}

input[type="text"],
input[type="number"] {
    padding: 12px; 
    border: 1px solid #ccc;
    border-radius: 6px; 
    font-size: 1rem;
    transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

input[type="text"]:focus,
input[type="number"]:focus {
    border-color: #007bff;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
    outline: none;
}

.form-actions {
    display: flex;
    justify-content: flex-end; 
    gap: 1rem; 
    margin-top: 1rem;
}

button { 
    padding: 10px 20px;
    border-radius: 6px;
    font-size: 0.95rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s ease, opacity 0.2s ease;
    border: none;
}

button.submit-btn {
    background-color: #28a745; 
    color: white;
}

button.submit-btn:hover {
    background-color: #218838; 
}

button.submit-btn:disabled {
    background-color: #a0a0a0;
    cursor: not-allowed;
    opacity: 0.7;
}

button.close-btn {
    background-color: #6c757d; 
    color: white;
}
button.close-btn:hover {
    background-color: #5a6268; 
}

.error-message {
    color: #dc3545; 
    background-color: #f8d7da; 
    border: 1px solid #f5c6cb; 
    padding: 0.75rem 1.25rem;
    margin-bottom: 1rem;
    border-radius: 0.25rem;
    font-size: 0.9rem;
}





button {
    margin-top: 5px; 
}

.file-upload-buttons { 
    margin-top: 20px;
}

input[type="file"] { 
    display: none;
}
</style>

