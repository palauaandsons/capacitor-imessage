import { WebPlugin } from '@capacitor/core';

import type { IMessagePlugin } from './definitions';

export class IMessageWeb extends WebPlugin implements IMessagePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
