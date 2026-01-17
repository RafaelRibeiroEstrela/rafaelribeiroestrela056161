create table tb_albuns_imagens(
                                  id bigserial,
                                  id_album bigint not null,
                                  storage_key varchar(255) not null,
                                  file_name varchar(255) not null,
                                  file_content_type varchar(255) not null,
                                  file_hash varchar(255) not null,
                                  constraint tb_albuns_imagens_id_pk primary key(id),
                                  constraint tb_albuns_imagens_id_album_fk foreign key(id_album) references tb_albuns(id)
);
