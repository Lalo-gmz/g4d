PGDMP     !                    {            g4d_v2    15.1    15.1 [    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    26794    g4d_v2    DATABASE     y   CREATE DATABASE g4d_v2 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Spain.1252';
    DROP DATABASE g4d_v2;
                postgres    false            �            1259    26895    atributo    TABLE     u   CREATE TABLE public.atributo (
    id bigint NOT NULL,
    nombre character varying(255),
    para_gitlab boolean
);
    DROP TABLE public.atributo;
       public         heap    postgres    false            �            1259    26912    atributo_funcionalidad    TABLE     �   CREATE TABLE public.atributo_funcionalidad (
    id bigint NOT NULL,
    marcado boolean,
    valor character varying(255),
    funcionalidad_id bigint,
    atributo_id bigint,
    para_gitlab boolean
);
 *   DROP TABLE public.atributo_funcionalidad;
       public         heap    postgres    false            �            1259    26837    bitacora    TABLE     �   CREATE TABLE public.bitacora (
    id bigint NOT NULL,
    accion character varying(255) NOT NULL,
    creado timestamp without time zone NOT NULL,
    user_id bigint,
    funcionalidad_id bigint
);
    DROP TABLE public.bitacora;
       public         heap    postgres    false            �            1259    26917    captura    TABLE     �   CREATE TABLE public.captura (
    id bigint NOT NULL,
    fecha timestamp without time zone,
    proyecto_id bigint,
    funcionalidades text
);
    DROP TABLE public.captura;
       public         heap    postgres    false            �            1259    26890 
   comentario    TABLE     �   CREATE TABLE public.comentario (
    id bigint NOT NULL,
    mensaje character varying(255) NOT NULL,
    creado timestamp without time zone,
    modificado timestamp without time zone,
    funcionalidad_id bigint,
    user_id bigint
);
    DROP TABLE public.comentario;
       public         heap    postgres    false            �            1259    26900    configuracion    TABLE     �   CREATE TABLE public.configuracion (
    id bigint NOT NULL,
    clave character varying(255),
    valor character varying(255),
    proyecto_id bigint
);
 !   DROP TABLE public.configuracion;
       public         heap    postgres    false            �            1259    26800    databasechangelog    TABLE     Y  CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);
 %   DROP TABLE public.databasechangelog;
       public         heap    postgres    false            �            1259    26795    databasechangeloglock    TABLE     �   CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);
 )   DROP TABLE public.databasechangeloglock;
       public         heap    postgres    false            �            1259    26861    estatus_funcionalidad    TABLE     i   CREATE TABLE public.estatus_funcionalidad (
    id bigint NOT NULL,
    nombre character varying(255)
);
 )   DROP TABLE public.estatus_funcionalidad;
       public         heap    postgres    false            �            1259    26866    etiqueta    TABLE     �   CREATE TABLE public.etiqueta (
    id bigint NOT NULL,
    nombre character varying(255) NOT NULL,
    color character varying(255),
    funcionalidad_id bigint
);
    DROP TABLE public.etiqueta;
       public         heap    postgres    false            �            1259    26873    funcionalidad    TABLE     �  CREATE TABLE public.funcionalidad (
    id bigint NOT NULL,
    nombre character varying(255),
    descripcion character varying(255),
    url_git_lab character varying(255),
    fecha_entrega date,
    creado timestamp without time zone,
    modificado timestamp without time zone,
    iteracion_id bigint,
    estatus_funcionalidad character varying(255),
    prioridad character varying(255),
    enlace_git_lab character varying(300)
);
 !   DROP TABLE public.funcionalidad;
       public         heap    postgres    false            �            1259    26842 	   iteracion    TABLE     �   CREATE TABLE public.iteracion (
    id bigint NOT NULL,
    nombre character varying(255) NOT NULL,
    inicio date,
    fin date,
    proyecto_id bigint,
    id_gitlab character varying(255)
);
    DROP TABLE public.iteracion;
       public         heap    postgres    false            �            1259    26817    jhi_authority    TABLE     O   CREATE TABLE public.jhi_authority (
    name character varying(50) NOT NULL
);
 !   DROP TABLE public.jhi_authority;
       public         heap    postgres    false            �            1259    26806    jhi_user    TABLE     �  CREATE TABLE public.jhi_user (
    id bigint NOT NULL,
    login character varying(50) NOT NULL,
    password_hash character varying(60) NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(191),
    image_url character varying(256),
    activated boolean NOT NULL,
    lang_key character varying(10),
    activation_key character varying(20),
    reset_key character varying(20),
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone,
    reset_date timestamp without time zone,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone
);
    DROP TABLE public.jhi_user;
       public         heap    postgres    false            �            1259    26822    jhi_user_authority    TABLE     {   CREATE TABLE public.jhi_user_authority (
    user_id bigint NOT NULL,
    authority_name character varying(50) NOT NULL
);
 &   DROP TABLE public.jhi_user_authority;
       public         heap    postgres    false            �            1259    26907    participacion_proyecto    TABLE     �   CREATE TABLE public.participacion_proyecto (
    id bigint NOT NULL,
    es_admin boolean,
    usuario_id bigint,
    proyecto_id bigint
);
 *   DROP TABLE public.participacion_proyecto;
       public         heap    postgres    false            �            1259    26885 	   prioridad    TABLE     }   CREATE TABLE public.prioridad (
    id bigint NOT NULL,
    nombre character varying(255),
    prioridad_numerica integer
);
    DROP TABLE public.prioridad;
       public         heap    postgres    false            �            1259    26847    proyecto    TABLE     6  CREATE TABLE public.proyecto (
    id bigint NOT NULL,
    nombre character varying(255),
    id_proyecto_git_lab character varying(255) NOT NULL,
    creado timestamp without time zone,
    modificado timestamp without time zone,
    abierto boolean DEFAULT true,
    enlace_git_lab character varying(300)
);
    DROP TABLE public.proyecto;
       public         heap    postgres    false            �            1259    26880    rel_funcionalidad__user    TABLE     s   CREATE TABLE public.rel_funcionalidad__user (
    user_id bigint NOT NULL,
    funcionalidad_id bigint NOT NULL
);
 +   DROP TABLE public.rel_funcionalidad__user;
       public         heap    postgres    false            �            1259    26856    rel_proyecto__participantes    TABLE     {   CREATE TABLE public.rel_proyecto__participantes (
    participantes_id bigint NOT NULL,
    proyecto_id bigint NOT NULL
);
 /   DROP TABLE public.rel_proyecto__participantes;
       public         heap    postgres    false            �            1259    27035    script    TABLE     �   CREATE TABLE public.script (
    id bigint NOT NULL,
    descripcion text NOT NULL,
    nombre character varying(120) NOT NULL,
    nombre_boton character varying(300),
    proyecto_id bigint NOT NULL,
    orden bigint DEFAULT 1
);
    DROP TABLE public.script;
       public         heap    postgres    false            �            1259    26805    sequence_generator    SEQUENCE        CREATE SEQUENCE public.sequence_generator
    START WITH 1050
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.sequence_generator;
       public          postgres    false            �          0    26895    atributo 
   TABLE DATA           ;   COPY public.atributo (id, nombre, para_gitlab) FROM stdin;
    public          postgres    false    230   {       �          0    26912    atributo_funcionalidad 
   TABLE DATA           p   COPY public.atributo_funcionalidad (id, marcado, valor, funcionalidad_id, atributo_id, para_gitlab) FROM stdin;
    public          postgres    false    233   �{       �          0    26837    bitacora 
   TABLE DATA           Q   COPY public.bitacora (id, accion, creado, user_id, funcionalidad_id) FROM stdin;
    public          postgres    false    220   �{       �          0    26917    captura 
   TABLE DATA           J   COPY public.captura (id, fecha, proyecto_id, funcionalidades) FROM stdin;
    public          postgres    false    234   ��       �          0    26890 
   comentario 
   TABLE DATA           `   COPY public.comentario (id, mensaje, creado, modificado, funcionalidad_id, user_id) FROM stdin;
    public          postgres    false    229   ��       �          0    26900    configuracion 
   TABLE DATA           F   COPY public.configuracion (id, clave, valor, proyecto_id) FROM stdin;
    public          postgres    false    231   c�       �          0    26800    databasechangelog 
   TABLE DATA           �   COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
    public          postgres    false    215   T�       �          0    26795    databasechangeloglock 
   TABLE DATA           R   COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
    public          postgres    false    214   Q�       �          0    26861    estatus_funcionalidad 
   TABLE DATA           ;   COPY public.estatus_funcionalidad (id, nombre) FROM stdin;
    public          postgres    false    224   ��       �          0    26866    etiqueta 
   TABLE DATA           G   COPY public.etiqueta (id, nombre, color, funcionalidad_id) FROM stdin;
    public          postgres    false    225   o�       �          0    26873    funcionalidad 
   TABLE DATA           �   COPY public.funcionalidad (id, nombre, descripcion, url_git_lab, fecha_entrega, creado, modificado, iteracion_id, estatus_funcionalidad, prioridad, enlace_git_lab) FROM stdin;
    public          postgres    false    226   q�       �          0    26842 	   iteracion 
   TABLE DATA           T   COPY public.iteracion (id, nombre, inicio, fin, proyecto_id, id_gitlab) FROM stdin;
    public          postgres    false    221   ��       �          0    26817    jhi_authority 
   TABLE DATA           -   COPY public.jhi_authority (name) FROM stdin;
    public          postgres    false    218   �       �          0    26806    jhi_user 
   TABLE DATA           �   COPY public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) FROM stdin;
    public          postgres    false    217   =�       �          0    26822    jhi_user_authority 
   TABLE DATA           E   COPY public.jhi_user_authority (user_id, authority_name) FROM stdin;
    public          postgres    false    219   ��       �          0    26907    participacion_proyecto 
   TABLE DATA           W   COPY public.participacion_proyecto (id, es_admin, usuario_id, proyecto_id) FROM stdin;
    public          postgres    false    232   ɫ       �          0    26885 	   prioridad 
   TABLE DATA           C   COPY public.prioridad (id, nombre, prioridad_numerica) FROM stdin;
    public          postgres    false    228   O�       �          0    26847    proyecto 
   TABLE DATA           p   COPY public.proyecto (id, nombre, id_proyecto_git_lab, creado, modificado, abierto, enlace_git_lab) FROM stdin;
    public          postgres    false    222   *�       �          0    26880    rel_funcionalidad__user 
   TABLE DATA           L   COPY public.rel_funcionalidad__user (user_id, funcionalidad_id) FROM stdin;
    public          postgres    false    227   Ű       �          0    26856    rel_proyecto__participantes 
   TABLE DATA           T   COPY public.rel_proyecto__participantes (participantes_id, proyecto_id) FROM stdin;
    public          postgres    false    223   ��       �          0    27035    script 
   TABLE DATA           [   COPY public.script (id, descripcion, nombre, nombre_boton, proyecto_id, orden) FROM stdin;
    public          postgres    false    235   ݱ       �           0    0    sequence_generator    SEQUENCE SET     C   SELECT pg_catalog.setval('public.sequence_generator', 8750, true);
          public          postgres    false    216            �           2606    26916 2   atributo_funcionalidad atributo_funcionalidad_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.atributo_funcionalidad
    ADD CONSTRAINT atributo_funcionalidad_pkey PRIMARY KEY (id);
 \   ALTER TABLE ONLY public.atributo_funcionalidad DROP CONSTRAINT atributo_funcionalidad_pkey;
       public            postgres    false    233            �           2606    26899    atributo atributo_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.atributo
    ADD CONSTRAINT atributo_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.atributo DROP CONSTRAINT atributo_pkey;
       public            postgres    false    230            �           2606    26841    bitacora bitacora_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.bitacora
    ADD CONSTRAINT bitacora_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.bitacora DROP CONSTRAINT bitacora_pkey;
       public            postgres    false    220            �           2606    26923    captura captura_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.captura
    ADD CONSTRAINT captura_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.captura DROP CONSTRAINT captura_pkey;
       public            postgres    false    234            �           2606    26894    comentario comentario_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.comentario
    ADD CONSTRAINT comentario_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.comentario DROP CONSTRAINT comentario_pkey;
       public            postgres    false    229            �           2606    26906     configuracion configuracion_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.configuracion
    ADD CONSTRAINT configuracion_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.configuracion DROP CONSTRAINT configuracion_pkey;
       public            postgres    false    231            �           2606    26799 0   databasechangeloglock databasechangeloglock_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.databasechangeloglock DROP CONSTRAINT databasechangeloglock_pkey;
       public            postgres    false    214            �           2606    26865 0   estatus_funcionalidad estatus_funcionalidad_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.estatus_funcionalidad
    ADD CONSTRAINT estatus_funcionalidad_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.estatus_funcionalidad DROP CONSTRAINT estatus_funcionalidad_pkey;
       public            postgres    false    224            �           2606    26872    etiqueta etiqueta_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.etiqueta
    ADD CONSTRAINT etiqueta_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.etiqueta DROP CONSTRAINT etiqueta_pkey;
       public            postgres    false    225            �           2606    26879     funcionalidad funcionalidad_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.funcionalidad
    ADD CONSTRAINT funcionalidad_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.funcionalidad DROP CONSTRAINT funcionalidad_pkey;
       public            postgres    false    226            �           2606    26846    iteracion iteracion_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.iteracion
    ADD CONSTRAINT iteracion_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.iteracion DROP CONSTRAINT iteracion_pkey;
       public            postgres    false    221            �           2606    26821     jhi_authority jhi_authority_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.jhi_authority
    ADD CONSTRAINT jhi_authority_pkey PRIMARY KEY (name);
 J   ALTER TABLE ONLY public.jhi_authority DROP CONSTRAINT jhi_authority_pkey;
       public            postgres    false    218            �           2606    26826 *   jhi_user_authority jhi_user_authority_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT jhi_user_authority_pkey PRIMARY KEY (user_id, authority_name);
 T   ALTER TABLE ONLY public.jhi_user_authority DROP CONSTRAINT jhi_user_authority_pkey;
       public            postgres    false    219    219            �           2606    26812    jhi_user jhi_user_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT jhi_user_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.jhi_user DROP CONSTRAINT jhi_user_pkey;
       public            postgres    false    217            �           2606    26911 2   participacion_proyecto participacion_proyecto_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.participacion_proyecto
    ADD CONSTRAINT participacion_proyecto_pkey PRIMARY KEY (id);
 \   ALTER TABLE ONLY public.participacion_proyecto DROP CONSTRAINT participacion_proyecto_pkey;
       public            postgres    false    232            �           2606    26889    prioridad prioridad_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.prioridad
    ADD CONSTRAINT prioridad_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.prioridad DROP CONSTRAINT prioridad_pkey;
       public            postgres    false    228            �           2606    26853    proyecto proyecto_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.proyecto
    ADD CONSTRAINT proyecto_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.proyecto DROP CONSTRAINT proyecto_pkey;
       public            postgres    false    222            �           2606    26884 4   rel_funcionalidad__user rel_funcionalidad__user_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.rel_funcionalidad__user
    ADD CONSTRAINT rel_funcionalidad__user_pkey PRIMARY KEY (funcionalidad_id, user_id);
 ^   ALTER TABLE ONLY public.rel_funcionalidad__user DROP CONSTRAINT rel_funcionalidad__user_pkey;
       public            postgres    false    227    227            �           2606    26860 <   rel_proyecto__participantes rel_proyecto__participantes_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.rel_proyecto__participantes
    ADD CONSTRAINT rel_proyecto__participantes_pkey PRIMARY KEY (proyecto_id, participantes_id);
 f   ALTER TABLE ONLY public.rel_proyecto__participantes DROP CONSTRAINT rel_proyecto__participantes_pkey;
       public            postgres    false    223    223            �           2606    27041    script script_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.script
    ADD CONSTRAINT script_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.script DROP CONSTRAINT script_pkey;
       public            postgres    false    235            �           2606    26855 )   proyecto ux_proyecto__id_proyecto_git_lab 
   CONSTRAINT     s   ALTER TABLE ONLY public.proyecto
    ADD CONSTRAINT ux_proyecto__id_proyecto_git_lab UNIQUE (id_proyecto_git_lab);
 S   ALTER TABLE ONLY public.proyecto DROP CONSTRAINT ux_proyecto__id_proyecto_git_lab;
       public            postgres    false    222            �           2606    26816    jhi_user ux_user_email 
   CONSTRAINT     R   ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT ux_user_email UNIQUE (email);
 @   ALTER TABLE ONLY public.jhi_user DROP CONSTRAINT ux_user_email;
       public            postgres    false    217            �           2606    26814    jhi_user ux_user_login 
   CONSTRAINT     R   ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT ux_user_login UNIQUE (login);
 @   ALTER TABLE ONLY public.jhi_user DROP CONSTRAINT ux_user_login;
       public            postgres    false    217            �           2606    27009 =   atributo_funcionalidad fk_atributo_funcionalidad__atributo_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.atributo_funcionalidad
    ADD CONSTRAINT fk_atributo_funcionalidad__atributo_id FOREIGN KEY (atributo_id) REFERENCES public.atributo(id);
 g   ALTER TABLE ONLY public.atributo_funcionalidad DROP CONSTRAINT fk_atributo_funcionalidad__atributo_id;
       public          postgres    false    233    3290    230            �           2606    27004 B   atributo_funcionalidad fk_atributo_funcionalidad__funcionalidad_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.atributo_funcionalidad
    ADD CONSTRAINT fk_atributo_funcionalidad__funcionalidad_id FOREIGN KEY (funcionalidad_id) REFERENCES public.funcionalidad(id);
 l   ALTER TABLE ONLY public.atributo_funcionalidad DROP CONSTRAINT fk_atributo_funcionalidad__funcionalidad_id;
       public          postgres    false    233    226    3282            �           2606    26827 $   jhi_user_authority fk_authority_name    FK CONSTRAINT     �   ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES public.jhi_authority(name);
 N   ALTER TABLE ONLY public.jhi_user_authority DROP CONSTRAINT fk_authority_name;
       public          postgres    false    3264    219    218            �           2606    26929 &   bitacora fk_bitacora__funcionalidad_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.bitacora
    ADD CONSTRAINT fk_bitacora__funcionalidad_id FOREIGN KEY (funcionalidad_id) REFERENCES public.funcionalidad(id);
 P   ALTER TABLE ONLY public.bitacora DROP CONSTRAINT fk_bitacora__funcionalidad_id;
       public          postgres    false    220    3282    226            �           2606    26924    bitacora fk_bitacora__user_id    FK CONSTRAINT        ALTER TABLE ONLY public.bitacora
    ADD CONSTRAINT fk_bitacora__user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);
 G   ALTER TABLE ONLY public.bitacora DROP CONSTRAINT fk_bitacora__user_id;
       public          postgres    false    220    3258    217            �           2606    27025    captura fk_captura__proyecto_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.captura
    ADD CONSTRAINT fk_captura__proyecto_id FOREIGN KEY (proyecto_id) REFERENCES public.proyecto(id) NOT VALID;
 I   ALTER TABLE ONLY public.captura DROP CONSTRAINT fk_captura__proyecto_id;
       public          postgres    false    222    234    3272            �           2606    26979 *   comentario fk_comentario__funcionalidad_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.comentario
    ADD CONSTRAINT fk_comentario__funcionalidad_id FOREIGN KEY (funcionalidad_id) REFERENCES public.funcionalidad(id);
 T   ALTER TABLE ONLY public.comentario DROP CONSTRAINT fk_comentario__funcionalidad_id;
       public          postgres    false    229    3282    226            �           2606    26984 !   comentario fk_comentario__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.comentario
    ADD CONSTRAINT fk_comentario__user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);
 K   ALTER TABLE ONLY public.comentario DROP CONSTRAINT fk_comentario__user_id;
       public          postgres    false    229    3258    217            �           2606    26989 +   configuracion fk_configuracion__proyecto_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.configuracion
    ADD CONSTRAINT fk_configuracion__proyecto_id FOREIGN KEY (proyecto_id) REFERENCES public.proyecto(id);
 U   ALTER TABLE ONLY public.configuracion DROP CONSTRAINT fk_configuracion__proyecto_id;
       public          postgres    false    231    3272    222            �           2606    26949 &   etiqueta fk_etiqueta__funcionalidad_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.etiqueta
    ADD CONSTRAINT fk_etiqueta__funcionalidad_id FOREIGN KEY (funcionalidad_id) REFERENCES public.funcionalidad(id);
 P   ALTER TABLE ONLY public.etiqueta DROP CONSTRAINT fk_etiqueta__funcionalidad_id;
       public          postgres    false    226    225    3282            �           2606    26969 ,   funcionalidad fk_funcionalidad__iteracion_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.funcionalidad
    ADD CONSTRAINT fk_funcionalidad__iteracion_id FOREIGN KEY (iteracion_id) REFERENCES public.iteracion(id);
 V   ALTER TABLE ONLY public.funcionalidad DROP CONSTRAINT fk_funcionalidad__iteracion_id;
       public          postgres    false    226    3270    221            �           2606    26934 #   iteracion fk_iteracion__proyecto_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.iteracion
    ADD CONSTRAINT fk_iteracion__proyecto_id FOREIGN KEY (proyecto_id) REFERENCES public.proyecto(id);
 M   ALTER TABLE ONLY public.iteracion DROP CONSTRAINT fk_iteracion__proyecto_id;
       public          postgres    false    221    3272    222            �           2606    26999 =   participacion_proyecto fk_participacion_proyecto__proyecto_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.participacion_proyecto
    ADD CONSTRAINT fk_participacion_proyecto__proyecto_id FOREIGN KEY (proyecto_id) REFERENCES public.proyecto(id);
 g   ALTER TABLE ONLY public.participacion_proyecto DROP CONSTRAINT fk_participacion_proyecto__proyecto_id;
       public          postgres    false    222    232    3272            �           2606    26994 <   participacion_proyecto fk_participacion_proyecto__usuario_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.participacion_proyecto
    ADD CONSTRAINT fk_participacion_proyecto__usuario_id FOREIGN KEY (usuario_id) REFERENCES public.jhi_user(id);
 f   ALTER TABLE ONLY public.participacion_proyecto DROP CONSTRAINT fk_participacion_proyecto__usuario_id;
       public          postgres    false    232    3258    217            �           2606    26954 D   rel_funcionalidad__user fk_rel_funcionalidad__user__funcionalidad_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.rel_funcionalidad__user
    ADD CONSTRAINT fk_rel_funcionalidad__user__funcionalidad_id FOREIGN KEY (funcionalidad_id) REFERENCES public.funcionalidad(id);
 n   ALTER TABLE ONLY public.rel_funcionalidad__user DROP CONSTRAINT fk_rel_funcionalidad__user__funcionalidad_id;
       public          postgres    false    226    3282    227            �           2606    26959 ;   rel_funcionalidad__user fk_rel_funcionalidad__user__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.rel_funcionalidad__user
    ADD CONSTRAINT fk_rel_funcionalidad__user__user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);
 e   ALTER TABLE ONLY public.rel_funcionalidad__user DROP CONSTRAINT fk_rel_funcionalidad__user__user_id;
       public          postgres    false    217    3258    227            �           2606    26944 L   rel_proyecto__participantes fk_rel_proyecto__participantes__participantes_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.rel_proyecto__participantes
    ADD CONSTRAINT fk_rel_proyecto__participantes__participantes_id FOREIGN KEY (participantes_id) REFERENCES public.jhi_user(id);
 v   ALTER TABLE ONLY public.rel_proyecto__participantes DROP CONSTRAINT fk_rel_proyecto__participantes__participantes_id;
       public          postgres    false    223    3258    217            �           2606    26939 G   rel_proyecto__participantes fk_rel_proyecto__participantes__proyecto_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.rel_proyecto__participantes
    ADD CONSTRAINT fk_rel_proyecto__participantes__proyecto_id FOREIGN KEY (proyecto_id) REFERENCES public.proyecto(id);
 q   ALTER TABLE ONLY public.rel_proyecto__participantes DROP CONSTRAINT fk_rel_proyecto__participantes__proyecto_id;
       public          postgres    false    223    3272    222            �           2606    27042    script fk_script__proyecto_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.script
    ADD CONSTRAINT fk_script__proyecto_id FOREIGN KEY (proyecto_id) REFERENCES public.proyecto(id) NOT VALID;
 G   ALTER TABLE ONLY public.script DROP CONSTRAINT fk_script__proyecto_id;
       public          postgres    false    3272    222    235            �           2606    26832    jhi_user_authority fk_user_id    FK CONSTRAINT        ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);
 G   ALTER TABLE ONLY public.jhi_user_authority DROP CONSTRAINT fk_user_id;
       public          postgres    false    3258    217    219            �   �   x�=�An�@EמS����m�h�V�`��J���d�ɢ�� 9E.���������6��G)*��B�_	>�ϰ����4�/pR Ճ�aK��T�Z+^��'���Pi˥�	���r���u����N�p(��1ǡKi��oSo�~�4z��Q�8���V��1׆,y"�]ѝι�K�      �      x������ � �      �   �  x������6��z�8Cΐ�mw�C�4$�[�±ݬ�]{a;E�w꩏��(ٲD	��Y"��$/�����zX�������~Xm
4hoޠ+M��k���G�-�X=-w������c_����|�DSc�ɵ���f�o�+�s��V�BS�7���?���_���VR��zھl,W۟�����a�����PmB�.֛��q{*��t5_���:_��e��c�ڿ�6��v��j(�&7�>��zY�ٝ�6��������a���9>m���5��u��j{X}�A���H����~}^�W\��2�����|���g_�`����x�{�T�AX�:c\�8Z��:	�T0X�֙��E�`���N[kct�s�Z7m�i��N-�X&'�7���%�p����XO��W�-� .&��i�ajm�f�:�m,���j�U�L����8�amLe"Dh���q6�dAr�i���q6��F9}_��8�[���I�46���҈R��jl��-Y+�c(Y�����6���g��8[�N
ַc��|6��FK6�%6?Z�be�cP�/֔/���&[g""%�j3��-D��U���l�ԭK֩��v�1Ĵ,�Hm&�΢� 1YV���l���ܼ�Lng�R�>٠6������![G���e�M�y�������}~�}��s�,??�]��Kck�5�&Jg������vrǕ���ui��a�D}@m\m�E,�i�l��ɢ�a��m$�k���l�2�֩�M�Zόɒ�a��-I��dY��l�֓����˦oeYF�lP;,����(vԥ�VzCLs�.-6���]�X�"��jn�.ݳ,���jn�.ݷH�q��ۨK��Υ���6��}ˁ������t�F�}��6j�=���As�龵����ۨM�-Iy�c��Fm�o=˒o��i٬�r��fm�F+i8im�~������d�P�n�Q� J',ZiO]�����ش�V�����uj�m����|����Yw~�W�f��8ge���F��Ks���;[c�hl<[�jn<��G��A�&�|�X�J9��t����l�h&�j�c�,��U�mƐ���uj��3��6[;c�j���Y�-��^m�v�6���N�=��J��|�t6Ps����3��[�v:+C�d5�|팭]0�j�i���/:�
]l.!,w��lCK���]�m>��uƨ֎�k����r��V-�R�M�E��Wc�,rl�k�d��Hr���\j�-�值�Z�q���O���>ʁ���a�;��>��)z�7rE�i8�G�Z�냶��__��u�աY�jq�Z�d��f�E�v�ڳ�jݜuڊ��Ҝ���%��6�j�۬W�f��8g��撍b�OړVN�b�rX�fs�X�fs�X�fs�X�&sҎ��Tg���dnj�7�vXs��ܮ��S�9��{�ȴ6,�T���jy��      �   �  x��]�r�F]�_��Z����f"�N��qEV6qj
&a	I( �WJ3��|�ln�� H�!��M%�I4����n �^jMؓ�o��j-�F?&���S�O��)F�4O�;��_~4�O%��ǣI���_�y��7g�^>?{�����ř�´�D��(C��/.����-�?I�?D٫��:��G�u�I4�g�����$�J�VJND�P#�!���x}����%��q.�ʵ��a�����v�id����؍$N���Ż�w��;�|a��IP�8Gi��9^� ǙG]��l�>��T�7�|zvF�q�כ#?Ȓ��$��0[?��,���!ʂ~���,�nAƳW���G+,��Ɲ�x���0����b$!|�_77G�obK�J�w�-O�4V&֧�H�������/	��̮t��4�0)Z7��w��^�+���۸ř�˼�y�1J3�����e:q��'�#�4��W�ҕ����JW������j��7��b^���a�wU�:
��|>�4<�?�8�/�%L�u����,H�����弭���"Z�t� 9�Ŀi�G^�G�PM8L��J;<N���'���>�ӿ_�{���/�&a�Ck�T���2�=�!h����,���e1�g��yP��W���9����/�����K=��&�U�����W�8L��`e�#�м��S<���vP����J�͉���9̢Q��ޏѪ �E�x��z������`eNc��������-�v�zVf=�b�nU�U�fb�
zU�kU}2U�jA�#��Kz.W�|�U�,����B x���^��w�M&��~��4��B�����L�ϱ�z�z96J0�M�JPQ[�R��j���т�l���|��u����.�w�o��{B��de�jW��,���ԷB�_yOY88�ߑ~I�p4��|���e�����ƋG?�b�W2~0��@I��	F.Fg�=�S@��]�9��h8��a����|?������]/��n�a��Qp^&���*	�0��f=�΃���=aP%�a�k�dn�a��˕��Ձ�=(���g�.��5��V�ܼ��A��e� ��n~_�&u�ݢp��J����G�!����(�����HP�Ჟr�z�@`�h�"3;�r`fp�3p�u�A(��2�� �L�C5���׳���߹���[���gW�ߋ��@�f��v7�׭f-.f�ap%���sep��)l7[̌��jpl�l�6b{����mY�m����;�
��%l�{ö���j��-�zlK*����.a[��u�5b��Sl��VD~�`ۦ���l�ɍQi�۵�k�ödT���mؒk�D����=���;FI��mf�=Omڔ7o*�7���Vυ��y�z}�l�WӼW۸)#uM׹SȲ;e�����2��R=j���ť����fO�%���������iP���7�t��Y߿�	�q�]^�{���.˭�=�'��*{����G��A�_��o�g��ҏ��Fqv�����j��)��:L�݋�mJ��&��l�
��n��U�Ge/�K��?���B�"	��:Zu��MWǸ��ۚݶ���en [��[X��V;�[��O=w𨐺�*y(p3�z�qn�&��#�V�ew�o.I�-%��SJO��	�9��$9Ô�qn,բ�;��)I��_��Xv(a��A'�m?��$�Ir�$w�Ir��~/�j�<I��Y�X�1I��1�b�"�^�v�2��ܝ7&�!�;nL�+����� �$�Ir]O�C�����ۏۘ$W`��V#�;�mL��b[Hf1�S��$9�vW��Ir����$�ۂR�WC�1I��0I��0I��pET�UL�+2@c�Q��p�C��g�Kr��p��Sf\��{�&�#�m���r��Y�!�g$��$y�u:y�V/�2��y�x���,�&���(�񍯼pe=����5���:����D��סclNB�p�9	�#�ya�^B��a��~�+�����`��Y�[Ǹ��=E���1h��{CT�b����]5E"��a�P�te��N))mS��"�c�҆��+t���e�����U��.�'p�����Q||��_k�A\�1<9>��t�'��u��r����x�o8�xQ�.�8�C{
��Y�5�\�,���x�U��~�c����ﷄm�4'�ּ�i��$�6��uV⹄��ć"alu��~!!��,a��P�P���k��y���r�=컉k&^�l�s�*�E���Glq�h���C�z�ٻ>/ގx�f���>����Pī�
Fs�rO�G�B�z�99a.^�XZ/�L�֙���U��x)�᳴m4����{t��Io�F�jF�u~�9]ˎ�C��j��t5RW����j,��� ]7��[��nF�u�9]�&�C��i���[�0i�{tݜ9�:]M3���(��Zv)
]��t�RU���骑����f�䜮J��w��mDW��vyK�
���5���j-ç��+��m��9X��fUQ��`�곪',%Rm�*(�-�������U�63�ZSQEiك!쪫h'�5��R$l���G�a�iT����\��v�I��r����v��M�#m��yD՚G��G�{d'�5��2M$��H*�<KeI����e����-kf#Q�ٟ�p�?�j�g��WI�ͷ�����2�Iƣ��Ѵ�t�f)����E��E�t~��Кnv~	m��]'���� b5i�fN�y�T������ni�Z�����r��
;d,/3�T��1����&�6����j�_�p�_t+��%�G5]�;�����,�z�����n1�� V@�q�`�FX^숕�1#�VZ,v�}�'�G��/s�n��H�ڦ��bG{\�(/����P���Qe�#�Ŏ��ؑ�0`�P�P���k�bG�`��=.vT���»A(^�B��(v�%;��bG�xQ�y�����k�bG���;�u��B�$l���=�nS쨅�X�h�Ŏr�2�r`q��u�bG-�����QAWWxo�{tݦ�Qt�bG�.vT��ZI1��{tݢؑ���Ŏ���Q�Wά��p���M��6��Ŏv_� ��B�ݦ�v�bG�����QAX+�F�v��[;j��X�h�Ŏr�
w�	�1�G�-��BX,v��bGa��X�M$X��>d����Q.c�Y�P�:��-���';��bGc%0�:�X,v����q�����c�f\K�`���g쎋��d�s"4��u��{^��}��ӧ�^��J      �   �  x�}�Mn�0���)x	3���5�"��Eѥ7���tlQ���=W�Ћ�Rd�� �o�<���������1��(�J��t$�g�9���W�ޜ��!L��]�дm��3:L����S�r�2���b���bC���*%�����4��{H��&J<rR�ZŚ�W8�f�ͿNM��8�ʘ蔙'��k�4��{;6�%=��i��-��7�VN�^�)�!&�k�B����ο���Eʍ0����N�P�����j镃D�*�li���6=Q�������g��.�b{��E�J��2N��"��~��%J�K��)]YKB�G%��?X(�$�6�*�8Z&Y�@�Z��%�9zƵ񒖰B��]$�'��t����������M�#�ߍ�鶞��h��B����J�^x��(�.b��      �   �   x�]�MN�0FדS� �?K7�QI uXu3u��±#;�P��)�q�	]�7��, -^�\�Rء�� [�6O�AȬ�(rh��N�u�:�X�Q���*�_�_�����B&�{�I��7}<"Ӷ�om�24��0�w�>�3�O��l~v�֓�0kL�����qg��Qx<�����zR{�k���	�,Ų�cA Њ+�?�,�fX���'�m/�$��Oc�      �   �  x��Zmo������3��5�~h�P8h����[��,������&v,:��.�\���\�g8��P ���|Z�����n[�_m�ׁ�U�Dۏy�������vݯi�:�O��n�{��$H����
�5��`oPX�\'�w�~�����v�upQ��St)t���ĿEGJ���}�>�3�q�ۘ��~�������>�m�S��w�o�t�F�>�R����ڴ��@
�M'O����|B$
F�!OT����@a�����/y���!���~N���n��?�4��t$��~������ϗ�������-�}���=����`�gf����R~|���o^^��ܜ%m�/H��#��<��I+�N���e���)�����J�O�+�
ĨL�$0ŬD]2�dL�)���Z?]����m.t�����1�e6���Q�ܴ{92o�e���'�G!M�N�C��HaE�9�J��\�A�n�����wc��y�g`�05¿�\���v"[�!�
b!��ytI��������Ox��a��T�f*��JYәS��H*I&��A�c���(b�r�E2m����~���c��������}5�$|0de e�D��)�`�S���~���y޼�]Z�u��;�}ت������8�	����U�N���fG�hЅ�zL~}�X�}y׫�����u\�Ѷχ�|uf�2������`xR�+�l)�E6Xv2
D2��z�f;qc~q�����j��=������p�i�N���
iAu*��e�*K!Š}�✜��u�U9]y>Ů��g��;߉J�j>�H�S��A�8DA)�(9ގG؋�/��[��O��<!���@�ӎ	��/gJ�8�RhmX�fIQ��h�e�����s�9^�SKg��J$*�R��Y�<���8�2_e�qT���D%B�5ę�k��D���j(#��ĩ�M
3!��\wi]0y<�������Ȧ�F������B8E�PqI$K@d�� "K�����
�꾜<�OG&,�X�!l4�wj����5:c�"&�)�R	\�X��NbsdE�r�S���P��NTBV����2�P 'S�F`�h;)d��Ϫhfu9P�(8��:�ء�C���e&�J���鈙�5�̋l�6e���?�?��Q|�+%���#'ш�8�>rP'`���m��au����8{�#�fGj��RJ
~tj����a�`ٜs���*�F��<i�EW�W��_��:�T�{�+
YI̤5_I�p��1fC,e���M�_l>��L�b��s����pd�e��
�J�J!*�^����c /R�Z�TO=v�v�Г1Ƴ��d%-A��ra���'5qt ��0�����t����fR�[���d%2�PƄd��Y���)�F.Ǐ�܋��4��I���C�aX�b#�8�\A�J�� 9�%��.!8�	L�t�lL���Z�2����3M���NV�-a�20ÞN
R"�Y�5`T�]��Z�h�C�<�jA �NV��8暵k�2��1B)mAG�g_х������3M ��u�Rz�c[.9s�6�trA���KR�\Hoo�`K3�t���]<sub�d��n����tkup&��c�֮=���ץΫ��y�/��5�1���M�֢ﰒj1�$�+��	1$?{��(��0���x�5�����o:��Ь�!�a�ـ����(r��.s(�{.�e���w�p�6v����kM�Z��W�\�~�DO���ν���d�U�Z���kZ(4�^�;��BtX�Q�#�q+�@h]X���H39�v�6f�oW�S�l�>j0��6�E`����X�])��X
CȆe@��E=��:0ϴ�ˡ1˞���sOM��}[·P駩���,�X�a%䝳:��Q!��-'��C̟g��F��i�����`ϧM�S{�I���êPH\8g:��v�IoBV�&�YQ�w��f���m2���l{K��>�4�3:RN������,�J ��s��Yh$���?���c�j�>�O(c�71K�,iY4p��q9�s��N�aU�!e�%��#؜b@��Ĥ�!g�y��qk��Z [t�Ӗ@��8�I�%AU��)JP��hh�+�z�&XP,3�;�1������_�������E��E,?��Oy]V�;���*����.�5R��m0h�D1syo�~ut��-�[�k�vZ�WA�B��0Vu
����KI��L"�,xM�U�S.�-c�����������i��/�y���� 0�y      �   A   x�3�,�4202�50"+cc+S=K#3NǼ��CK#=C3=C=s#M�=... d��      �   �   x�M�=jAFk�s���/�"aɦL#��k�X"#�a}�\#�/��@H�������v���l�+�����c5\�F<���͟��0�(A��g���E�Կ�w)�*��{�R3������JܚS��>@O�jn�AR�zs���܀��-���x����i�~NGq�En%7i�q \^��d�zV��W����Rb      �   �   x�U�MN1FמS��~�rh��EYt$Ģ�4��PO<r2�`���bd@����|����@�J��	�����Z^�K�l`�\]A�̂;c�v,5�)��G�C�%���&���SAr�T�9���0(�F�d�����3�`�#1&�c��`]X�t��W�ck��1�#�+z�a;r�=��_�3+����+F�����zV�탗��45P?�;)*�1�r$lً;}���eUU_��q(      �     x��\Mr��^ç�DT��olR��q��<�eg��M��e�$�@����2��r_,�I��nPE�U.��*�h
�����E^fú(��t�"�K7�&y�0��	e'L4~$ )����5jR����ެ��`�M��K޸�]2t37�=���x�:��r�hJE
�u[���:+ݰοd�p�?W'n::�e����W�E���M�R�@X�T*UD$��x�ȗ���nL��O_Ur9��dur{~{�	ДI�g��7C[ 2�g^��ڑ��cW��;rU�irS��?���M���W���:�9�$ŗ�,�~4�I�JHE�n�๵�U2,���� n~��sW~M���O�����^���}]f7��aRqo����� ��T��F�>����u>t�F^ϑ[�6��O��ٸ���U����?�*&7<�R�)K,�m�1(7����v�MQ�l���&�v#�/�r���m��Ȭ���I�n��l��9/�x��.�\���U�ܕ�YۊX۔X�)�@���;;��ܑ�����|X��⛛|��gɤ@9ͿeHe>���˼rsr�(���]�8A.=�m�V(M^�ZϠ$$�+�Q|�QF�WsW���@�!���OȨ�WyM�$�	
\��&��%q�|�W�<�l��r6�+7�W�K6&�ē��i"w7u�:���Y�@%JǗb<���J1�"�w�O22͐�:A���	�''Ň�ˆx�$���Dv��h��
�5�ɦ�G�4��a�K:�	S^ �T�l���n�������l\��e�h�B�����zq��Dv���b����a@+�J ���<�
ɹ�
�ܼ�ıT���� ��Z�Hr�Z���pƴ������g�7�qn����68{����L�8��5LD8{X���:�+�d��#wMf��t-ŕS�8S��L2�Au-�9�-�t8Ӓt-��iqfzÙ�D8�xX�0h�E��i�R%ä=,E���VTK�C��V�i
0������_ߜ]]^�]����_�:K.^ݞ���i��E2k}���#����F�/`6�1���i0�}u�����[�H^����� I�;����ZL�E3��$�2�d�����e�p���e�R��$FFTn����F3��)l��������y��x�9�~y�k��\���԰�x��]s�#�p�9�0B�p��.>��.��-��Y���Q����ֿ���4X��Yƅ�OY[�P��eUM�J�,�y�5 ��_������<.I���R�DhDh��ջ�gPh'|��vKĕSf$�jY7^�&�(��{�L� b�5	�B�tM~�k`�*���Y䃆qi�ˊ�Hv�Y�A�~�Ş�YgR4��ZP�Y_9ad��M�{RZ�&Ȟ�h+N�!{,�ޑg��G�e6̞:�SA�<�h�g�V���`lG�y{�O���T`�&����G�(��A��)N�f�{y�k�����:�ׄʔ�����_����@���FM7��n��O���[�m8���@ ��)2z��Y��[�����u�F�8
���$T�a��Im Z*�]�t��W�]xyT�}TGn�GE�)B8z�\H�:#2T�#/�4����a7��vDd&�ޑg�M�8`a��T�LD�H��w��&}�J��h�Z�����μ�7j�!L�TJXO�R��t�|���0��nt�;�[��lex�3y�`%[Ѐ�����t��oBx�Oᯉ|dB#~��J�\'��%�26��zA2�;1")�t��tAƧ��U���]^�݇������h�OOG٤8���W�~���(;��ӓӼ��Yu*=����=�Ӄ	A"Pl��'li�E�ly�u9����.�{�	��p�J�Q���dq�DEӉ�IL.}���g^}-��3/����K���i�������q�O����l��\,�Dld]x耇kw�q�%_l#�ZW���ǲ?���3P�aS~�p��o@�)$�pf��^��n����� Vs� ,c&Ȟ 4*�).� {�l�6���?�JX�'�4�){�\	L��ʹ��ja������h�v-���n]���*�ͻ{�XKzܰ��_�y=�����°'�ql/�+����״R�HW3����]�q�x��D�=��˞�I�63ȕ�y�������~"�<���.�{�vΧ��i��0�uY�O�B�Lr��sр�\3�fef�	1�?a���"Z>�M9�>���8�i���������U \��t����U��"��~�
��z�+j�Lp��#�RX諸2�^����)n)K�1�Έz�+j�g�a�S�@���b��q���!����b�m��,�r�)�xd�@����K .����=4����Ɲ1Ɯ,�����E>Q��|�͇��]�̊��@}��K�^�r�hӅ��$Z?�l�H~�p����T��%W����ʣo�Hx�Ѷ�3�w1j��np/��M�JE�i��
i��3{xq��c;�Q<�������W��x���Q�&�g��Q���s��~���t�꤆HP$WA����]*R����,���gTk���|p�c��U�0?sa"�yϨ�)K*x`��Q���y�g��B��+� 4<�Mm0��UpXF��z9���@�S���K�Z��m���m�|W��m�͸�9*�ߓ�DǊ�/R5߂Y������M�lGAd�<d˸�Zڂ-���Y��aX��l�V��&�ki�b��a�86{�	b��a�-V� � �=<2� 2^~�d�t-m7����R��K9�-<Ϥ�6j�<�f���<ÞFHߊf`)�U�/�NlFi/;^'�edv]	�X��;����bڿ7��^*c1��;���M]~�n���hJ�o�o`km5[����)�Ya�|���S���2�M/[�w
V،
��T[�w�~~�N���l'�����
� ��Go��N��ʰ�Gc��j��^������|S���M�,�Y)@� ?���^N�5k
Z��,��p�����0J{9=�d�FeO�����6x����wy\      �   j  x�}�An� E��\ �a�q��Uٴ��e6�Յ�ܧG�z�\�8�S�v,�@�7�a H֥�'��1�EL���Ê(ʎ��BjkT5�B�ʤ���C��������F������ZZ�W�Qx[Mt}�'m�
��W�X�r�R�r6f���[|�΋��f"{��lQ�J�g�v�V�wa���ON����ݞ��P���f�/�c@�u8�ta�k���u����Í6x�q���&-��7h�;���a�UO[�M�_0��vmmx5&����>�c}c���9���ի�e ��f;8笙��i㯷�\  X��	a�[�%�@;���e����Aȷ�-O�%!ጒ��aBO�� Ä� ��.��G�      �      x���q�wt����
1C�]��b���� b��      �   C  x���Ko�@��ï�­3��+�X�(>�R�^E�#3���Hۅ]5����=��I.^X$%`o��Al��>�yN�8�LW� �﯋��~S'���Ѷ"��<cS!���"a5�jB�T���$��a5 5�8ݰ��Q�,8џJ���A����J9�֊ńj��ʱ��
�P���g��'�ݎw��#��c�/a	�0���
����Sx8�c(��vL�(ڦja�#�����&�\ߡ�&5�4���gVxI�G����6k��O��0w����;z%)ۂ4���,q��/h4�)�P�XF����8�����;C�� �)�_      �   )   x�3���q�wt����2�pB�]����؆F���=... ���      �   v   x�M��� �v1��p�Hy����@�N�3�B�A���ѳ��}�:Z�;���- �*	�Y4(�[Mj�R�F��b��o���[���ۤ}y��r��{o���ʤ�~	�)�r�3+      �   �   x��1N1E�S�����%	�CP�Ll�2�bG^�q8�4�{�=o�?�I���iBgޛl������U����G�,i3���R�~��ԕs���n�2X0f��8�Q��*9�X�3U��GxN;��1��!��*����H~6Hp�����h!tx}��:c\�!��G�w=���7�#B=�k�Jd��������F�      �   �  x���ˎ�6���S���U��s2��@'�&@V���Q#�EO;}����b9�X#��( �Ʊ�����I1�luU�#>��5�>�1��?��v���lx�j�O.Z���_�r����1�zxV5�i��@}z����T6�ַgWW��?��4�2��&�JS�Q-�&j	���4��qj^��u4�.WG��� >��ֺ�����
�4[��\��.%*�s����s���{�[�U�X�m7�W/�D	员�ɳʣGS~�������l���������{��ah��[�����B�i��\5&4<���{�>�{���=ZW�������[ە`c0M�g���`��1�y6�4:���}�[-�`�7]���G���IMVP�%X�F,%h���%��'Ք�~��ǳ+kM��|��]O
�ͯ*�lE��l?�l�kw:G|g8`�!��.1�Jʦ���$4��6�K*�j<a�Ď��p��R���!�gH���s���u�qe���p�	��}�X����X+4%i�
	�H�W�[�z|�v?03����wx�j��aL��t0B7L������� ��+ 3u�IͲ��]̐��N�b�d\i��Rl���ɆF�)� 3�P��ۇp�p^2&(���Ċ�R�<-T���W�z4Y�abw��É>2������ԸJ3�X>�p�O�`Ȼ�}b~��.!���)aK�Ԇ�-���1�J��(�g���77�:6搖��9
84m�n*�L\��\4�A3���T!��_7T�,ۈ/s5נI��r�����E����ʡ��;T�9?���䪡�&X�����P��!�U�3�P����-�o�<%�Pٿi��rqq�����7�,t���$y*)e��/����?����|k1�      �   �   x�=�K�!�]��(�w���c,�f�*�T?~�5�Z}t�"�za-ԍ&p`�NB��&`�@�I�s}E ��U۫:��a'a��2x3n�
��&z�ւ!�*�la"x�PU0=e���r�r�H��Q�ɮ�0���n�x�.��T��68�|sA��͠��^�^�# �b�,K�F-���`~���T�����<�?�Vs"      �      x�3�4�2bCNS#C�=... ��      �   �   x�͑�j�0�g�)��!�ȴ�!�ݚ�:�d���l$yIȻ��C����︪T��g���r֙ޡ�\�).�Ac��ظ��0��q��M�UY�BV�z#Ex
����ل�Aqm��st��ɿj3d4ҙ\D���/��0dWX�YC�z2�>��W�msL�ErJ���1bj#9���NX�n������/ǀ�����ߏ
g�h/���S�g����R�o�Y��     