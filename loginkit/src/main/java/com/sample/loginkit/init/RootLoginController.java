package com.sample.loginkit.init;

import android.content.Context;

/*
Main entry point class of the Login Kit Library.
The shell app has to start from calling init() method and then it can register to all the callbacks of the Loginkit Library


 */
public class RootLoginController {

    private String TAG = RootLoginController.class.getName();

    private static volatile RootLoginController instance = new RootLoginController();
    public static CustomConfiguration customConfiguration = new CustomConfiguration();


    public RootLoginController() {
       /* if (instance != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }*/
    }

    /*
     * Double checking that only a single instance of the Root login controller gets called and is thread safe
     * */
    public static RootLoginController init() {

        if (instance == null) {

            synchronized (RootLoginController.class) {

                if (instance == null) {

                    instance = new RootLoginController();


                }
            }

        }

        return instance;
    }


    /*
    Once the root controller object is created in the shell app and its configuration has been setup , loadLoginKit() should
    be called to load the changes in which the custom configuration is saved.

     */
    public void loadLoginKit(Context context, RootLoginController rootLoginController) {

        customConfiguration = rootLoginController.customConfiguration;


    }


    /* To fetch the custom configuration
     */
   /* public static CustomConfiguration getCustomConfiguration() {
        return customConfiguration;
    }*/


}

