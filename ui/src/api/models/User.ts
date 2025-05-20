import { Bid } from "./Bid";

export interface User {
  id: number;
  username: string;
  roles?: string[];
  bids?: Bid[];
}
