package com.plasticare_hub;

import com.plasticare_hub.db_handlers.DatabaseWriteConfig;
import com.plasticare_hub.rest.RestApiServer;
import com.plasticare_hub.utils.Constants;

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
