import { defineStore } from "pinia";
import BidService, { BidCreationPayload } from "../services/BidService";
import { Bid } from "../models/Bid";

interface BidState {
  bids: Bid[];
  currentBid: Bid | null; // If needed for individual bid focus
  loading: boolean;
  error: string | null;
}

export const useBidStore = defineStore("bid", {
  state: (): BidState => ({
    bids: [],
    currentBid: null,
    loading: false,
    error: null,
  }),
  actions: {
    async fetchAllBids() {
      this.loading = true;
      this.error = null;
      try {
        this.bids = await BidService.getAllBids();
      } catch (e: any) {
        this.error = e.message || "Failed to fetch bids";
      } finally {
        this.loading = false;
      }
    },
    async fetchBidById(id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.currentBid = await BidService.getBidById(id);
      } catch (e: any) {
        this.error = e.message || `Failed to fetch bid ${id}`;
        this.currentBid = null;
      } finally {
        this.loading = false;
      }
    },
    async createBid(bidData: BidCreationPayload) {
      this.loading = true;
      this.error = null;
      try {
        const newBid = await BidService.createBid(bidData);
        this.bids.push(newBid); // Add to local state
        // Consider if bids are room-specific and how to update that view
      } catch (e: any) {
        this.error = e.message || "Failed to create bid";
      } finally {
        this.loading = false;
      }
    },
    async deleteBid(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await BidService.deleteBid(id);
        this.bids = this.bids.filter((bid) => bid.id !== id);
        if (this.currentBid?.id === id) {
          this.currentBid = null;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to delete bid ${id}`;
      } finally {
        this.loading = false;
      }
    },
  },
});
