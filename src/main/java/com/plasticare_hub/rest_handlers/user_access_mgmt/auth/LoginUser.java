package com.plasticare_hub.rest_handlers.user_access_mgmt.auth;


import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plasticare_hub.db_handlers.DatabaseConnectionsHikari;
import com.plasticare_hub.db_handlers.DatabaseOperationsHikari;
import com.plasticare_hub.db_handlers.DatabaseProcessor;
import com.plasticare_hub.utils.EndpointProps;
import com.plasticare_hub.utils.hashing.Hasher;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/uam", templatePath = "/auth/login", httpMethod = "POST", allowedRoles = {"GUEST"})
public class LoginUser implements HttpHandler
{
    @SuppressWarnings("unchecked")
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        httpServerExchange.startBlocking();
        FormParserFactory formFactory = FormParserFactory.builder().build();
        FormDataParser formDataParser = formFactory.createParser(httpServerExchange);

        if (formDataParser == null)
        {
            httpServerExchange.setStatusCode(400);
            httpServerExchange.getResponseSender().send("{\"err_status\" : \"An error occurred\"}");
            return;
        }
        FormData formData = formDataParser.parseBlocking();
        String emailAddress = formData.getFirst("email").getValue();
        String password = formData.getFirst("password").getValue();

        if (!"".equals(emailAddress) && !"".equals(password))
        {
            Connection connection = DatabaseConnectionsHikari.getDbDataSource().getConnection();
            String getPassHashQuery = """
                    SELECT password 
                    FROM system_users 
                    WHERE email = ?
                    """;
            List<Object> getPassHashParams = List.of(emailAddress);
            ResultSet resultSet = DatabaseOperationsHikari.dbQuery(connection, getPassHashQuery, getPassHashParams);
            Boolean passVerified = false;
            if (resultSet != null && resultSet.next())
            {
                String passwordHash = resultSet.getString("password");
                passVerified = Hasher.verifyPassword(password, passwordHash);
            }
            resultSet.close();
            connection.close();

            String response = "";
            if (passVerified)
            {
                String sqlQuery = """
                SELECT user_id, email, first_name, middle_name, surname, privilege, user_status
                FROM system_users
                WHERE email = ?
                    """;
                List<Object> sqlParams = List.of(emailAddress);

                String jsonString = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Map<String, Object>> map = new HashMap<>();
                map = objectMapper.readValue(jsonString, Map.class);
                Map<String, Object> userDetails = map.get("1");
                String authToken = AuthManager.evergreenAuthToken(emailAddress);

                userDetails.put("auth_token", authToken);
                response = objectMapper.writeValueAsString(map);

                httpServerExchange.setStatusCode(200);
                httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                httpServerExchange.getResponseSender().send(response);

            }
            else
            {
                httpServerExchange.setStatusCode(200);
                httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                httpServerExchange.getResponseSender().send("{\"err_status\" : \"Invalid email or password\"}");
            }

        }
        else
        {
            httpServerExchange.setStatusCode(200);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send("{\"err_status\" : \"Invalid email or password\"}");
        }
        
    }
}