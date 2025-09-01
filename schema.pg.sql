--
-- PostgreSQL database dump
--

\restrict CGEKq3GOuJwbHchuUOkz1Kot6z78hel9CeifGTebySL1whooP6IzzanvqWAY8LS

-- Dumped from database version 16.10 (Debian 16.10-1.pgdg13+1)
-- Dumped by pg_dump version 16.10 (Debian 16.10-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: activity; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.activity (
    id bigint NOT NULL,
    author_id bigint NOT NULL,
    task_id bigint NOT NULL,
    updated timestamp without time zone,
    comment character varying(4096),
    title character varying(1024),
    description character varying(4096),
    estimate integer,
    type_code character varying(32),
    status_code character varying(32),
    priority_code character varying(32)
);


ALTER TABLE public.activity OWNER TO jira;

--
-- Name: activity_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.activity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.activity_id_seq OWNER TO jira;

--
-- Name: activity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.activity_id_seq OWNED BY public.activity.id;


--
-- Name: attachment; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.attachment (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    file_link character varying(2048) NOT NULL,
    object_id bigint NOT NULL,
    object_type smallint NOT NULL,
    user_id bigint NOT NULL,
    date_time timestamp without time zone
);


ALTER TABLE public.attachment OWNER TO jira;

--
-- Name: attachment_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.attachment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.attachment_id_seq OWNER TO jira;

--
-- Name: attachment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.attachment_id_seq OWNED BY public.attachment.id;


--
-- Name: contact; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.contact (
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    value character varying(256) NOT NULL
);


ALTER TABLE public.contact OWNER TO jira;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.databasechangelog (
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


ALTER TABLE public.databasechangelog OWNER TO jira;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO jira;

--
-- Name: mail_case; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.mail_case (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    date_time timestamp without time zone NOT NULL,
    result character varying(255) NOT NULL,
    template character varying(255) NOT NULL
);


ALTER TABLE public.mail_case OWNER TO jira;

--
-- Name: mail_case_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.mail_case_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.mail_case_id_seq OWNER TO jira;

--
-- Name: mail_case_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.mail_case_id_seq OWNED BY public.mail_case.id;


--
-- Name: profile; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.profile (
    id bigint NOT NULL,
    last_login timestamp without time zone,
    last_failed_login timestamp without time zone,
    mail_notifications bigint
);


ALTER TABLE public.profile OWNER TO jira;

--
-- Name: project; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.project (
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    title character varying(1024) NOT NULL,
    description character varying(4096) NOT NULL,
    type_code character varying(32) NOT NULL,
    startpoint timestamp without time zone,
    endpoint timestamp without time zone,
    parent_id bigint
);


ALTER TABLE public.project OWNER TO jira;

--
-- Name: project_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.project_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.project_id_seq OWNER TO jira;

--
-- Name: project_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.project_id_seq OWNED BY public.project.id;


--
-- Name: reference; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.reference (
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    ref_type smallint NOT NULL,
    endpoint timestamp without time zone,
    startpoint timestamp without time zone,
    title character varying(1024) NOT NULL,
    aux character varying
);


ALTER TABLE public.reference OWNER TO jira;

--
-- Name: reference_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.reference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reference_id_seq OWNER TO jira;

--
-- Name: reference_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.reference_id_seq OWNED BY public.reference.id;


--
-- Name: sprint; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.sprint (
    id bigint NOT NULL,
    status_code character varying(32) NOT NULL,
    startpoint timestamp without time zone,
    endpoint timestamp without time zone,
    code character varying(32) NOT NULL,
    project_id bigint NOT NULL
);


ALTER TABLE public.sprint OWNER TO jira;

--
-- Name: sprint_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.sprint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sprint_id_seq OWNER TO jira;

--
-- Name: sprint_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.sprint_id_seq OWNED BY public.sprint.id;


--
-- Name: task; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.task (
    id bigint NOT NULL,
    title character varying(1024) NOT NULL,
    type_code character varying(32) NOT NULL,
    status_code character varying(32) NOT NULL,
    project_id bigint NOT NULL,
    sprint_id bigint,
    parent_id bigint,
    startpoint timestamp without time zone,
    endpoint timestamp without time zone
);


ALTER TABLE public.task OWNER TO jira;

--
-- Name: task_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.task_id_seq OWNER TO jira;

--
-- Name: task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.task_id_seq OWNED BY public.task.id;


--
-- Name: task_tag; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.task_tag (
    task_id bigint NOT NULL,
    tag character varying(32) NOT NULL
);


ALTER TABLE public.task_tag OWNER TO jira;

--
-- Name: user_belong; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.user_belong (
    id bigint NOT NULL,
    object_id bigint NOT NULL,
    object_type smallint NOT NULL,
    user_id bigint NOT NULL,
    user_type_code character varying(32) NOT NULL,
    startpoint timestamp without time zone,
    endpoint timestamp without time zone
);


ALTER TABLE public.user_belong OWNER TO jira;

--
-- Name: user_belong_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.user_belong_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_belong_id_seq OWNER TO jira;

--
-- Name: user_belong_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.user_belong_id_seq OWNED BY public.user_belong.id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role smallint NOT NULL
);


ALTER TABLE public.user_role OWNER TO jira;

--
-- Name: users; Type: TABLE; Schema: public; Owner: jira
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    display_name character varying(32) NOT NULL,
    email character varying(128) NOT NULL,
    first_name character varying(32) NOT NULL,
    last_name character varying(32),
    password character varying(128) NOT NULL,
    endpoint timestamp without time zone,
    startpoint timestamp without time zone
);


ALTER TABLE public.users OWNER TO jira;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: jira
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO jira;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jira
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: activity id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.activity ALTER COLUMN id SET DEFAULT nextval('public.activity_id_seq'::regclass);


--
-- Name: attachment id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.attachment ALTER COLUMN id SET DEFAULT nextval('public.attachment_id_seq'::regclass);


--
-- Name: mail_case id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.mail_case ALTER COLUMN id SET DEFAULT nextval('public.mail_case_id_seq'::regclass);


--
-- Name: project id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.project ALTER COLUMN id SET DEFAULT nextval('public.project_id_seq'::regclass);


--
-- Name: reference id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.reference ALTER COLUMN id SET DEFAULT nextval('public.reference_id_seq'::regclass);


--
-- Name: sprint id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.sprint ALTER COLUMN id SET DEFAULT nextval('public.sprint_id_seq'::regclass);


--
-- Name: task id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task ALTER COLUMN id SET DEFAULT nextval('public.task_id_seq'::regclass);


--
-- Name: user_belong id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.user_belong ALTER COLUMN id SET DEFAULT nextval('public.user_belong_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: activity activity_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT activity_pkey PRIMARY KEY (id);


--
-- Name: attachment attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.attachment
    ADD CONSTRAINT attachment_pkey PRIMARY KEY (id);


--
-- Name: contact contact_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id, code);


--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: mail_case mail_case_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.mail_case
    ADD CONSTRAINT mail_case_pkey PRIMARY KEY (id);


--
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (id);


--
-- Name: project project_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- Name: reference reference_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.reference
    ADD CONSTRAINT reference_pkey PRIMARY KEY (id);


--
-- Name: sprint sprint_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.sprint
    ADD CONSTRAINT sprint_pkey PRIMARY KEY (id);


--
-- Name: task task_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- Name: project uk_project_code; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT uk_project_code UNIQUE (code);


--
-- Name: reference uk_reference_ref_type_code; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.reference
    ADD CONSTRAINT uk_reference_ref_type_code UNIQUE (ref_type, code);


--
-- Name: task_tag uk_task_tag; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task_tag
    ADD CONSTRAINT uk_task_tag UNIQUE (task_id, tag);


--
-- Name: user_role uk_user_role; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT uk_user_role UNIQUE (user_id, role);


--
-- Name: users uk_users_display_name; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_users_display_name UNIQUE (display_name);


--
-- Name: users uk_users_email; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_users_email UNIQUE (email);


--
-- Name: user_belong user_belong_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.user_belong
    ADD CONSTRAINT user_belong_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: ix_user_belong_user_id; Type: INDEX; Schema: public; Owner: jira
--

CREATE INDEX ix_user_belong_user_id ON public.user_belong USING btree (user_id);


--
-- Name: uk_sprint_project_code; Type: INDEX; Schema: public; Owner: jira
--

CREATE UNIQUE INDEX uk_sprint_project_code ON public.sprint USING btree (project_id, code);


--
-- Name: uk_user_belong; Type: INDEX; Schema: public; Owner: jira
--

CREATE UNIQUE INDEX uk_user_belong ON public.user_belong USING btree (object_id, object_type, user_id, user_type_code) WHERE (endpoint IS NULL);


--
-- Name: activity fk_activity_task; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT fk_activity_task FOREIGN KEY (task_id) REFERENCES public.task(id) ON DELETE CASCADE;


--
-- Name: activity fk_activity_users; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT fk_activity_users FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: attachment fk_attachment; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.attachment
    ADD CONSTRAINT fk_attachment FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: contact fk_contact_profile; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.contact
    ADD CONSTRAINT fk_contact_profile FOREIGN KEY (id) REFERENCES public.profile(id) ON DELETE CASCADE;


--
-- Name: profile fk_profile_users; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT fk_profile_users FOREIGN KEY (id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: project fk_project_parent; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT fk_project_parent FOREIGN KEY (parent_id) REFERENCES public.project(id) ON DELETE CASCADE;


--
-- Name: sprint fk_sprint_project; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.sprint
    ADD CONSTRAINT fk_sprint_project FOREIGN KEY (project_id) REFERENCES public.project(id) ON DELETE CASCADE;


--
-- Name: task fk_task_parent_task; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT fk_task_parent_task FOREIGN KEY (parent_id) REFERENCES public.task(id) ON DELETE CASCADE;


--
-- Name: task fk_task_project; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES public.project(id) ON DELETE CASCADE;


--
-- Name: task fk_task_sprint; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT fk_task_sprint FOREIGN KEY (sprint_id) REFERENCES public.sprint(id) ON DELETE SET NULL;


--
-- Name: task_tag fk_task_tag; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.task_tag
    ADD CONSTRAINT fk_task_tag FOREIGN KEY (task_id) REFERENCES public.task(id) ON DELETE CASCADE;


--
-- Name: user_belong fk_user_belong; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.user_belong
    ADD CONSTRAINT fk_user_belong FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_role fk_user_role; Type: FK CONSTRAINT; Schema: public; Owner: jira
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict CGEKq3GOuJwbHchuUOkz1Kot6z78hel9CeifGTebySL1whooP6IzzanvqWAY8LS

