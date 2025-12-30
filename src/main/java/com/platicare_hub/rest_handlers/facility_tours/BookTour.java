package com.platicare_hub.rest_handlers.facility_tours;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.utils.EndpointProps;
import com.platicare_hub.utils.TokenGenerator;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/facility-tours", templatePath = "/book", httpMethod = "POST", allowedRoles = {"GUEST"})
public class BookTour implements HttpHandler
{
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        FormParserFactory formParserFactory = FormParserFactory.builder().build();
        FormDataParser formDataParser = formParserFactory.createParser(httpServerExchange);
        if (formDataParser == null)
        {
            httpServerExchange.setStatusCode(400);
            httpServerExchange.getResponseSender().send("{\"status\": \"An Error Occured\"}");
            return;
        }
        FormData formData = formDataParser.parseBlocking();
        String tourId = TokenGenerator.generateSecureToken();
        String fullName = formData.getFirst("name").getValue();
        String email = formData.getFirst("email").getValue();
        String phone = formData.getFirst("phone").getValue();
        String preferredDate = formData.getFirst("preferred_date").getValue();
        String preferredTime = formData.getFirst("preferred_time").getValue();
        String message = formData.getFirst("message").getValue();
        String status = "ACTIVE";

        if (!"".equals(fullName) && !"".equals(email) && !"".equals(phone) && !"".equals(preferredDate) && !"".equals(preferredTime))
        {
            String sqlQuery = """
                    INSERT INTO facility_tour_info (tour_id, full_name, email, phone, preferred_date, preferred_time, message, status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            List<Object> sqlParams = List.of(tourId, fullName, email, phone, preferredDate, preferredTime, message, status);
            String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

            httpServerExchange.setStatusCode(200);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send(response);

        }
    }
}
