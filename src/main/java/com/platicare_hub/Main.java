package com.platicare_hub;

import com.platicare_hub.db_handlers.DatabaseWriteConfig;
import com.platicare_hub.rest.RestApiServer;
import com.platicare_hub.utils.Constants;

public class Main
{
    static 
    {
        DatabaseWriteConfig.writeDbProperties();
        Constants.init();
    }

    public static void main(String[] args)
    {
        RestApiServer.restApiServerStart();
    }
}
