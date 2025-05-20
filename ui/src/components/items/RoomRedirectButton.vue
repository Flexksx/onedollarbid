<script setup>
import { onMounted } from 'vue';
import { useAuctionRoomStore } from '../../api/stores/auctionRoomStore';

const props = defineProps({
    product: {
        type: Object,
        required: true
    }
})

const auctionRoomStore = useAuctionRoomStore();

const redirectToAuction = async () => {
    // Ensure rooms are fetched before trying to find one
    if (auctionRoomStore.rooms.length === 0 && !auctionRoomStore.loading) {
        await auctionRoomStore.fetchAllAuctionRooms();
    }
    // If still loading after fetch attempt, or if there was an error, handle appropriately
    if (auctionRoomStore.loading) {
        console.log("Still loading rooms, please wait...");
        // Optionally, disable button or show a message
        return;
    }
    if (auctionRoomStore.error) {
        console.error("Error fetching rooms:", auctionRoomStore.error);
        // Optionally, show an error message to the user
        return;
    }

    console.log("Existing rooms from store:", auctionRoomStore.rooms);
    
    const productRoom = auctionRoomStore.rooms.find(room => room.itemId === props.product.id);
    if (productRoom) {
        window.location.href = `/rooms/${productRoom.id}`;
    } else {
        try {
            const newRoomPayload = { itemId: props.product.id };
            // The createAuctionRoom action in the store should update currentRoom and push to rooms array
            await auctionRoomStore.createAuctionRoom(newRoomPayload);
            if (auctionRoomStore.currentRoom && auctionRoomStore.currentRoom.itemId === props.product.id) {
                window.location.href = `/rooms/${auctionRoomStore.currentRoom.id}`;
            } else if (auctionRoomStore.error) {
                 console.error('Error creating room:', auctionRoomStore.error);
                 // Handle error, e.g., show a message to the user
            } else {
                // Fallback if currentRoom is not set as expected, try to find it again
                // This might indicate a need to refresh the list or ensure createAuctionRoom returns the new room for immediate use
                await auctionRoomStore.fetchAllAuctionRooms(); // Refresh to be sure
                const newlyCreatedRoom = auctionRoomStore.rooms.find(room => room.itemId === props.product.id);
                if (newlyCreatedRoom) {
                    window.location.href = `/rooms/${newlyCreatedRoom.id}`;
                } else {
                    console.error('Failed to find or create room for product:', props.product.id);
                }
            }
        } catch (error) {
            console.error('Error creating room:', error);
            // Handle error, e.g., show a message to the user
        }
    }
}

onMounted(async () => {
    // Optionally pre-fetch rooms if not already loaded
    // if (auctionRoomStore.rooms.length === 0) {
    //     await auctionRoomStore.fetchAllAuctionRooms();
    //     console.log('Rooms on mount from store:', auctionRoomStore.rooms);
    // }
    // For this component, fetching on demand in redirectToAuction might be sufficient
    // to ensure the latest data when the button is clicked.
})
</script>

<template>
    <button @click="redirectToAuction" class="auction-button" :disabled="auctionRoomStore.loading">
        {{ auctionRoomStore.loading ? 'Loading...' : 'Auction' }}
    </button>
</template>
