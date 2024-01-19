import dayjs from 'dayjs';
import { IQueue } from 'app/shared/model/queue.model';

export interface IWaiting {
  id?: number;
  name?: string;
  phone?: string;
  timeArrived?: dayjs.Dayjs | null;
  timeSummoned?: dayjs.Dayjs | null;
  timeDone?: dayjs.Dayjs | null;
  queue?: IQueue | null;
}

export const defaultValue: Readonly<IWaiting> = {};
