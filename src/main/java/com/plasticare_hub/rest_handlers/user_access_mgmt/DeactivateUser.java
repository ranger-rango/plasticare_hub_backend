package com.plasticare_hub.rest_handlers.user_access_mgmt;


import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import com.plasticare_hub.db_handlers.DatabaseConnectionsHikari;
import com.plasticare_hub.db_handlers.DatabaseOperationsHikari;
import com.plasticare_hub.db_handlers.DatabaseResultsProcessors;
import com.plasticare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/uam", templatePath = "/user/deactivate/{userId}", httpMethod = "POST", allowedRoles = {"ADMIN"})
public class DeactivateUser implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String userId = httpServerExchange.getQueryParameters().get("userId").getFirst();
        String userStatus = "INACTIVE";

        Connection connection = DatabaseConnectionsHikari.getDbDataSource().getConnection();
        String sqlQuery = """
            UPDATE system_users
            SET user_status = ?
            set password = 'NULL'
            WHERE user_id = ?
            """;
        
        List<Object> sqlParams = List.of(userStatus, userId);
        ResultSet resultSet = DatabaseOperationsHikari.dbQuery(connection, sqlQuery, sqlParams);
        String response = "";
        if (resultSet != null)
        {
            response = DatabaseResultsProcessors.processResultsToJson(resultSet, connection);
        }
        else
        {
            response = "{\"status\": \"success\"}";
        }

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}