import { Pageable } from './pageable';
import { SortEnum } from './sort';

export interface Page<T> {
  content: T[];
  pageable: Pageable;

  totalPages: number;
  totalElements: number;

  last: boolean;
  first: boolean;

  size: number;
  number: number; // página atual (0-based)

  sort: SortEnum;
  numberOfElements: number;
  empty: boolean;
}
