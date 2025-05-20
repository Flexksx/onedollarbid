export interface AuctionItem {
  id: number;
  name: string;
  startingPrice: number;
  soldPrice?: number | null;
  sold?: boolean | null;
}
