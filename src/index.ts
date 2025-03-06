import { registerPlugin } from '@capacitor/core';

import type { IMessagePlugin } from './definitions';

const IMessage = registerPlugin<IMessagePlugin>('IMessage', {
  web: () => import('./web').then((m) => new m.IMessageWeb()),
});

export * from './definitions';
export { IMessage };
