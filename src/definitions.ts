export interface SendMessageOptions {
  text: string;
  imageUrl?: string;
}

export interface IMessagePlugin {
  isMessagingAvailable(): Promise<{ available: boolean }>;

  sendMessage(options: SendMessageOptions): Promise<{ status: string }>;
}
