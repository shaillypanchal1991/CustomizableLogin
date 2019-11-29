package com.sample.loginkit.init;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RootLoginControllerTest {

    @Test
    public void loadLoginKit() {


        RootLoginController rootLoginController= new RootLoginController();
        assertEquals("https://usermgt-staging.shared-svc.bellmedia.ca", rootLoginController.customConfiguration.domainURL);
        assertNotEquals(null, rootLoginController.customConfiguration.domainURL);
        assertNotEquals("", rootLoginController.customConfiguration.domainURL);
    }
}