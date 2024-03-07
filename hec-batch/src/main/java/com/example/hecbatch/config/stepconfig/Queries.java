package com.example.hecbatch.config.stepconfig;

public class Queries {

    //유저 대상 쿼리
    public static final String USER_DELETE_READ_QUERY = "select * from users where is_deleted = true";
    public static final String USER_DELETE_WRITE_FIRST_QUERY = "delete from bank_accounts where user_id = :userId";
    public static final String USER_DELETE_WRITE_SECOND_QUERY = "delete from users where user_id = :userId";



    //계좌 대상 쿼리
    public static final String BANK_ACCOUNT_DELETE_READ_QUERY = "select * from bank_accounts where is_deleted = true";
    public static final String BANK_ACCOUNT_DELETE_WRITE_QUERY = "delete from bank_accounts where account_id =:accountId";
}
