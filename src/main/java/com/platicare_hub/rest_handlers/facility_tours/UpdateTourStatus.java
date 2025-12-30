package com.platicare_hub.rest_handlers.facility_tours;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/facility-tour", templatePath = "/{id}", httpMethod = "POST", allowedRoles = {"ADMIN", "OPERATOR"})
public class UpdateTourStatus implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String tourId = httpServerExchange.getQueryParameters().get("id").getFirst();
        String sqlQuery = """
                UPDATE facility_tour_info
                SET status = 'INACTIVE'
                WHERE tour_id = ?
                """;
        List<Object> sqlParams = List.of(tourId);
        String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}
