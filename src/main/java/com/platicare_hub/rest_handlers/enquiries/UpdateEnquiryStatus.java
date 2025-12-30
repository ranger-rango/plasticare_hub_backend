package com.platicare_hub.rest_handlers.enquiries;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.utils.EndpointProps;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/free-enquiry", templatePath = "/{id}", httpMethod = "POST", allowedRoles = {"ADMIN", "OPERATOR"})
public class UpdateEnquiryStatus implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        String enquiryId = httpServerExchange.getQueryParameters().get("id").getFirst();
        String sqlQuery = """
                UPDATE enquiry_info
                SET enquiry_status = 'INACTIVE'
                WHERE enquiry_id = ?
                """;
        List<Object> sqlParams = List.of(enquiryId);
        String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

        httpServerExchange.setStatusCode(200);
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        httpServerExchange.getResponseSender().send(response);
    }
}
