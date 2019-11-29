package com.sample.loginkit.models;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void getAccessToken() throws NoSuchFieldException, IllegalAccessException  {
        final Login pojo = new Login();
        final Field field = pojo.getClass().getDeclaredField("accessToken");
        field.setAccessible(true);
        field.set(pojo, "testvalue");

        //when
        final String result = pojo.getAccessToken();

        //then
        assertEquals("field wasn't retrieved properly", result, "testvalue");
       // return result;
    }

    @Test
    public void getRefreshToken() throws NoSuchFieldException, IllegalAccessException{

        final Login pojo = new Login();
        final Field field = pojo.getClass().getDeclaredField("accessToken");
        field.setAccessible(true);
        field.set(pojo, "testvalue");

        //when
        final String result = pojo.getAccessToken();

        //then
        assertEquals("field wasn't retrieved properly", result, "testvalue");
    }
}