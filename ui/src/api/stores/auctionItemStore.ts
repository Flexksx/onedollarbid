import { defineStore } from "pinia";
import AuctionItemService, {
  AuctionItemCreationPayload,
} from "../services/AuctionItemService";
import { AuctionItem } from "../models/AuctionItem";

interface AuctionItemState {
  items: AuctionItem[];
  currentItem: AuctionItem | null;
  loading: boolean;
  error: string | null;
}

export const useAuctionItemStore = defineStore("auctionItem", {
  state: (): AuctionItemState => ({
    items: [],
    currentItem: null,
    loading: false,
    error: null,
  }),
  actions: {
    async fetchAllAuctionItems() {
      this.loading = true;
      this.error = null;
      try {
        this.items = await AuctionItemService.getAllAuctionItems();
      } catch (e: any) {
        this.error = e.message || "Failed to fetch auction items";
      } finally {
        this.loading = false;
      }
    },
    async fetchAuctionItemsPaginated(offset: number, limit: number) {
      this.loading = true;
      this.error = null;
      try {
        this.items = await AuctionItemService.getAuctionItemsPaginated(
          offset,
          limit
        );
      } catch (e: any) {
        this.error = e.message || "Failed to fetch paginated auction items";
      } finally {
        this.loading = false;
      }
    },
    async fetchAuctionItemById(id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.currentItem = await AuctionItemService.getAuctionItemById(id);
      } catch (e: any) {
        this.error = e.message || `Failed to fetch auction item ${id}`;
        this.currentItem = null;
      } finally {
        this.loading = false;
      }
    },
    async createAuctionItem(itemData: AuctionItemCreationPayload) {
      this.loading = true;
      this.error = null;
      try {
        const newItem = await AuctionItemService.createAuctionItem(itemData);
        this.items.push(newItem);
        // Optionally, refresh the whole list or handle pagination if active
      } catch (e: any) {
        this.error = e.message || "Failed to create auction item";
      } finally {
        this.loading = false;
      }
    },
    async updateAuctionItem(id: number, itemData: Partial<AuctionItem>) {
      this.loading = true;
      this.error = null;
      try {
        const updatedItem = await AuctionItemService.updateAuctionItem(
          id,
          itemData
        );
        const index = this.items.findIndex((item) => item.id === id);
        if (index !== -1) {
          this.items[index] = updatedItem;
        }
        if (this.currentItem?.id === id) {
          this.currentItem = updatedItem;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to update auction item ${id}`;
      } finally {
        this.loading = false;
      }
    },
    async deleteAuctionItem(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await AuctionItemService.deleteAuctionItem(id);
        this.items = this.items.filter((item) => item.id !== id);
        if (this.currentItem?.id === id) {
          this.currentItem = null;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to delete auction item ${id}`;
      } finally {
        this.loading = false;
      }
    },
    async saveAllAuctionItems(itemsData: AuctionItemCreationPayload[]) {
      this.loading = true;
      this.error = null;
      try {
        const newItems =
          await AuctionItemService.saveAllAuctionItems(itemsData);
        this.items = [...this.items, ...newItems]; // Or replace, depending on desired behavior
      } catch (e: any) {
        this.error = e.message || "Failed to save all auction items";
      } finally {
        this.loading = false;
      }
    },
  },
});
