package ru.pudgy.vertex;


public class TestDataUtil {

    public static final String TEST_EMAIL = "test@mail.ru";
    public static final String TEST_PASSWD = "qwe";
    public static final String TEST_PASS_HASH = "$2a$10$1RLCpfTVi0/AjP.KnOs6yO/PWany.zMjxHURHR0.3Iv/4edUEWdCK";


    public static String authPayload() {
        return String.format("{ \"username\": \"%s\",  \"password\": \"%s\" }",TEST_EMAIL, TEST_PASSWD);
    }









}
