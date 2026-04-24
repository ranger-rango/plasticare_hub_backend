package com.plasticare_hub.rest_handlers.consultation;

import java.util.List;

import com.plasticare_hub.db_handlers.DatabaseProcessor;
import com.plasticare_hub.utils.EndpointProps;
import com.plasticare_hub.utils.TokenGenerator;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/consultation", templatePath = "/book", httpMethod = "POST", allowedRoles = {"GUEST"})
public class BookConsultation implements HttpHandler
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
        String consultationId = TokenGenerator.generateSecureToken();
        String fullName = formData.getFirst("name").getValue();
        String email = formData.getFirst("email").getValue();
        String phone = formData.getFirst("phone").getValue();
        String interestedProcedureId = formData.getFirst("interested_procedure_id").getValue();
        String preferredDoctorId = formData.getFirst("preferred_doctor_id").getValue();
        String preferredDate = formData.getFirst("preferred_date").getValue();
        String preferredTime = formData.getFirst("preferred_time").getValue();
        String message = formData.getFirst("message").getValue();
        String consultationStatus = "ACTIVE";

        if (!"".equals(fullName) && !"".equals(email) && !"".equals(phone) && !"".equals(interestedProcedureId) && !"".equals(preferredDoctorId) && !"".equals(preferredDate) && !"".equals(preferredTime))
        {
            String sqlQuery = """
                    INSERT INTO consultation_info (consultation_id, full_name, email, phone, interested_procedure_id, preferred_doctor_id, preferred_date, preferred_time, message, consultation_status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            List<Object> sqlParams = List.of(consultationId, fullName, email, phone, interestedProcedureId, preferredDoctorId, preferredDate, preferredTime, message, consultationStatus);
            String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

            httpServerExchange.setStatusCode(200);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send(response);

        }
    }
}
