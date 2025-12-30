package com.platicare_hub.rest_handlers.consultation;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/consultations", templatePath = "", httpMethod = "GET", allowedRoles = {"ADMIN", "OPERATOR"})
public class GetBookedConsultations implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String sqlQuery = """
                SELECT * 
                FROM consultation_info
                WHERE consultation_status = 'ACTIVE'
                -- ORDER BY consultation_status
                """;
        List<Object> sqlParams = List.of();
        String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}
