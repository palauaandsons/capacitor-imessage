export interface SendMessageOptions {
  text: string;
  imageUrl?: string;
}

export interface IMessagePlugin {
  sendMessage(options: SendMessageOptions): Promise<{ status: string }>;
}
