export interface IMessagePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
