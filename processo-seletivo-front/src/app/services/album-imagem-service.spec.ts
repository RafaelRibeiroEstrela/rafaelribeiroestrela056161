import { TestBed } from '@angular/core/testing';

import { AlbumImagemService } from './album-imagem-service';

describe('AlbumImagemService', () => {
  let service: AlbumImagemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlbumImagemService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
