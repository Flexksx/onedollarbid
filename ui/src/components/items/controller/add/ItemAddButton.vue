<script setup>
import { ref } from 'vue'
import axios from '../../../../api/axios/axios'

const isOverlayVisible = ref(false)
const itemName = ref('')
const startingPrice = ref(0)

const toggleOverlay = () => {
    isOverlayVisible.value = !isOverlayVisible.value
}

// Handle form submission for creating an item
const submitForm = async () => {
    if (!itemName.value || !startingPrice.value) {
        alert('Please fill out all fields.')
        return
    }
    const newItem = {
        name: itemName.value,
        startingPrice: startingPrice.value
    }

    try {
        await axios.post('/items', newItem, {
            headers: {
                'Content-Type': 'application/json' // Ensure JSON content type is set
            }
        })
        alert('Item created successfully!')
        toggleOverlay() // Close the overlay after submission
    } catch (error) {
        console.error('Error creating item:', error)
        alert('There was an error creating the item.')
    }
}

</script>
<template>
    <div>
        <!-- Wrapper that centers the "Add Item" button -->
        <div class="button-wrapper">
            <button @click="toggleOverlay">Add Item</button>
        </div>

        <!-- Overlay that appears when 'Add Item' button is clicked -->
        <div v-if="isOverlayVisible" class="overlay">
            <div class="overlay-content">
                <h2>Create New Item</h2>

                <!-- Form to create a new item -->
                <form @submit.prevent="submitForm">
                    <label for="itemName">Product Name</label>
                    <input type="text" id="itemName" placeholder="Enter product name" v-model="itemName" />

                    <label for="startingPrice">Starting Price</label>
                    <input type="number" id="startingPrice" placeholder="Enter starting price" v-model="startingPrice" />

                    <button type="submit">Create Item</button>
                </form>
                <button class="close-btn" @click="toggleOverlay">Close</button>
            </div>
        </div>
    </div>
</template>


<style scoped>

/* Overlay Style */
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

/* Content inside the overlay */
.overlay-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    max-width: 100%;
    text-align: center;
}

/* Form styling */
form {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

input {
    padding: 10px;
    margin: 5px 0;
    border: 1px solid #ccc;
    border-radius: 4px;
}

button.close-btn {
    background-color: red;
    margin-top: 20px;
}

button:hover {
    opacity: 0.8;
}

button.close-btn:hover {
    opacity: 0.6;
}

.file-upload-buttons {
    margin-top: 20px;
}

input[type="file"] {
    display: none;
}

button {
    margin-top: 5px;
}

.button-wrapper {
    display: flex;
    justify-content: center; /* Center horizontally */
    align-items: center;     /* Center vertically */
}

/* Add Item Button */
button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 200px;
    text-align: center; /* Ensures text is centered in the button */
}
</style>
