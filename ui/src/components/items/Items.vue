<script setup>
import { ref, onMounted } from 'vue'
// Import the ItemGrid component
import ItemGrid from './ItemGrid.vue'
import ItemAddMenu from './controller/ItemAddMenu.vue';
</script>

<template>
    <div>
        <h1>Items</h1>
        <ItemAddMenu />
        <ItemGrid />
    </div>
</template>

<style scoped>

h1 {
    text-align: center;
    margin-top: 1rem;
}

</style>
