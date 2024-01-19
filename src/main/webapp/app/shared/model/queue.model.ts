import { IWaiting } from 'app/shared/model/waiting.model';
import { IUser } from 'app/shared/model/user.model';

export interface IQueue {
  id?: number;
  name?: string;
  waitings?: IWaiting[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IQueue> = {};
