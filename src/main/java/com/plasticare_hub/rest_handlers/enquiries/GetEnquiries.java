package com.plasticare_hub.rest_handlers.enquiries;

import java.util.List;

import com.plasticare_hub.db_handlers.DatabaseProcessor;
import com.plasticare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/free-enquiries", templatePath = "", httpMethod = "GET", allowedRoles = {"ADMIN", "OPERATOR"})
public class GetEnquiries implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String sqlQuery = """
                SELECT * 
                FROM enquiry_info
                WHERE enquiry_status = 'ACTIVE'
                -- ORDER BY enquiry_status
                """;
        List<Object> sqlParams = List.of();
        String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}
