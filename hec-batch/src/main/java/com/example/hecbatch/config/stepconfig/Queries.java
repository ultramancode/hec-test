package com.example.hecbatch.config.stepconfig;

public class Queries {

    public static final String USER_DELETE_READ_QUERY = "select * from users where is_deleted = true";
    public static final String USER_DELETE_WRITE_QUERY = "delete from users where user_id =:userId";
}
