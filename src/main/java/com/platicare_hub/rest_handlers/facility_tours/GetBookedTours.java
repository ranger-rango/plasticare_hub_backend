package com.platicare_hub.rest_handlers.facility_tours;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/facility-tours", templatePath = "", httpMethod = "GET", allowedRoles = {"ADMIN", "OPERATOR"})
public class GetBookedTours implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String sqlQuery = """
                SELECT *
                FROM facility_tour_info
                where status = 'ACTIVE'
                ORDER BY status
                """;
        List<Object> sqlParams = List.of();
        String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}
