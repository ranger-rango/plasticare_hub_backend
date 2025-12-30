package com.platicare_hub.rest_handlers.consultation;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/consultation", templatePath = "/{id}", httpMethod = "POST", allowedRoles = {"ADMIN", "OPERATOR"})
public class UpdateProcedureStatus implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String consultationId = httpServerExchange.getQueryParameters().get("id").getFirst();
        String sqlQuery = """
                UPDATE consultation_info
                SET consultation_status = 'INACTIVE'
                WHERE consultation_id = ?
                """;
        List<Object> sqlParams = List.of(consultationId);
        String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}
