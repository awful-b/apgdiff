GRANT EXECUTE ON function f_name_1 (in arg_1 INTEGER, out arg_2 DATE), f_name_2 () TO user_1, PUBLIC, GROUP user_2 WITH GRANT OPTION;
GRANT SELECT(field_1, "field_2"), UPDATE (field_1, field_2), DELETE ON all tables in schema public, the_schema TO user_1;
GRANT SELECT(field_1, field_2), UPDATE (field_1, field_2), DELETE ON TABLE test_table TO user_1;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: test_table.field_1; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL(field_1) ON TABLE test_table FROM PUBLIC;
REVOKE ALL(field_1) ON TABLE test_table FROM postgres;
GRANT SELECT(field_1) ON TABLE test_table TO user_1;


--
-- Name: test_table.field_2; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL(field_2) ON TABLE test_table FROM PUBLIC;
REVOKE ALL(field_2) ON TABLE test_table FROM postgres;
GRANT SELECT(field_2) ON TABLE test_table TO user_1;


--
-- Name: test_table.field_3; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL(field_3) ON TABLE test_table FROM PUBLIC;
REVOKE ALL(field_3) ON TABLE test_table FROM postgres;
GRANT UPDATE(field_3) ON TABLE test_table TO user_1;


--
-- PostgreSQL database dump complete
--

