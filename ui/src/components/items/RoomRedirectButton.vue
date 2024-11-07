<script setup>

const props = defineProps({
    product: {
        type: Object,
        required: true
    }
})

const redirectToRoom = () => {
    window.location.href = `/rooms/${props.product.id}`;
}

</script>