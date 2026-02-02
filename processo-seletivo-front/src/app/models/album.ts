import { AlbumImagem } from "./album-imagem";

export interface Album {
  id?: number;
  nome: string;
  capas?: AlbumImagem[];
}
