export interface Seller {
  id: number;
  username: string;
  email: string;
  blocked: boolean;
  accountStatus?: string; // Add this to accept backend property
}
