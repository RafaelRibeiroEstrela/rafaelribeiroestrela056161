export interface AlbumImagem {
  id?: number;
  albumId: number;
  fileName: string;
  fileContentType: string;
  fileHash: string;
  fileContent?: string;
  storageKey: string;
  linkPreAssinado: string;
}
