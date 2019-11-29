package com.sample.loginkit.init;

import com.sample.loginkit.utils.StringConstants;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.HashMap;


/*
* Shell app can use this class to setup configuration parameters in the login library in the following simple way :
*       RootLoginController rootLoginController = RootLoginController.init();
        rootLoginController.customConfiguration.isLoggingEnable = true
        rootLoginController.customConfiguration.HTTPErrorMapping.Error_400="Please try again after sometime";
        rootLoginController.customConfiguration.domainURL = StringConstants.DOMAIN_URL;
        rootLoginController.loadLoginKit(this, rootLoginController);
* */


public class CustomConfiguration {


    public String domainURL = "";
    public int retryCount = 2;
    public boolean isLoggingEnable = true;
    public UIComponents uiComponents = new UIComponents();
    public HTTPErrorMapping httpErrorMapping = new HTTPErrorMapping();


/*For configuring UI Components */

    public class UIComponents {

        public double buttonCornerRadius = 0.0;
        public String buttonBackGroundColorHex = "#ffffff";
        public String borderColor = "#9999999";
        public String buttonTextColor = "#ffffff";
        public String backgroundColor = "#ffffff";
        public String textFieldColor = "#27292b";
        public String placeHolderColorHex = "#060606";
        public String textFieldUnderLineColor = "#27292b";
        public String abelTextColor = "#27292b";


    }

    /* For customizing error responses rather than fixed server response

    */

    public class HTTPErrorMapping {

        public String Error_400 = "Bad Request";
        public String Error_401 = StringConstants.Error_401;
        public String Error_404 = StringConstants.Error_404;
        public String Error_500 = StringConstants.Error_500;
        public String Error_503 = StringConstants.Error_503;
        public String Error_504 = StringConstants.Error_504;
        public String Error_Unknown = StringConstants.Error_Unknown;


    }



}
