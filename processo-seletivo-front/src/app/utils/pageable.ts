import { SortEnum } from "./sort";


export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: SortEnum;
}
