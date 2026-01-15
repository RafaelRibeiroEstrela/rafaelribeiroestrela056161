create table tb_artistas(
                            id bigserial,
                            nome varchar(255) not null,
                            constraint tb_artistas_id_pk primary key(id)
)

create table tb_albuns(
                          id bigserial,
                          nome varchar(255) not null,
                          constraint tb_albuns_id_pk primary key(id)
)

create table tb_albuns_artistas(
                                   id_artista bigint not null,
                                   id_album bigint not null,
                                   constraint tb_albuns_artistas_id_artista_fk foreign key(id_artista) references tb_artistas,
                                   constraint tb_albuns_artistas_id_album_fk foreign key(id_album) references tb_albuns
)

create table tb_regionais(
                             id bigserial,
                             id_externo bigint not null,
                             nome varchar(255) not null,
                             ativo boolean not null,
                             constraint tb_regionais_id_pk primary key(id)
)

create table tb_roles(
                         id bigserial,
                         authority varchar(255) not null,
                         constraint tb_roles_id_pk primary key(id),
                         constraint tb_roles_authority_un unique(authority)
)

create table tb_users(
                         id bigserial,
                         username varchar(255) not null,
                         password varchar(255) not null,
                         constraint tb_users_id_pk primary key(id),
                         constraint tb_users_username_un unique(username)
)