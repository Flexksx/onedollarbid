<script setup>
import { onMounted } from 'vue';
import axiosInstance from '../../api/axios/axios';

const props = defineProps({
    product: {
        type: Object,
        required: true
    }
})

const redirectToAuction = async () => {
    const existingRooms = await getRooms();
    console.log("Existing rooms:", existingRooms);
    
    const productRoom = existingRooms.find(room => room.itemId === props.product.id);
    if (productRoom) {
        window.location.href = `/rooms/${productRoom.id}`;
    } else {
        const newRoom = await createRoom();
        window.location.href = `/rooms/${newRoom.id}`;
    }
}

const getRooms = () => {
    return axiosInstance.get('/rooms')
        .then(response => response.data)
        .catch(error => {
            console.log(error);
            return []; // Return an empty array if there's an error
        });
}

const createRoom = async () => {
    const requestBody = {
        itemId: props.product.id
    }
    try {
        const response = await axiosInstance.post('/rooms', requestBody);
        console.log('Room created:', response.data);
        return response.data;
    } catch (error) {
        console.log(error);
        return null;
    }
}

onMounted(async () => {
    const rooms = await getRooms();
    console.log('Rooms on mount:', rooms);
})
</script>

<template>
    <button @click="redirectToAuction" class="auction-button">Auction</button>
</template>
