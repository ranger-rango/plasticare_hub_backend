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

@EndpointProps(prefixPath = "/uam", templatePath = "/users", httpMethod = "GET")
public class GetUsers implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        Connection connection = DatabaseConnectionsHikari.getDbDataSource().getConnection();
        String sqlQuery = """
        SELECT user_id, email, first_name, middle_name, surname, privilege, user_status
        FROM system_users
        ORDER BY user_status
            """;
        List<Object> sqlParams = List.of();
        ResultSet resultSet = DatabaseOperationsHikari.dbQuery(connection, sqlQuery, sqlParams);
        String response = "";
        if (resultSet != null)
        {
            response = DatabaseResultsProcessors.processResultsToJson(resultSet, connection);
        }
        else
        {
            response = "{\"status\" : \"error\"}";
        }

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}