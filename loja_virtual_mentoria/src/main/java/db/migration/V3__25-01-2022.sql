ALTER TABLE IF EXISTS pessoa_fisica
    ADD COLUMN tipo_pessoa character varying(10) COLLATE pg_catalog."default" NOT NULL;

ALTER TABLE IF EXISTS public.pessoa_juridica
    ADD COLUMN nome character varying(10) COLLATE pg_catalog."default" NOT NULL;