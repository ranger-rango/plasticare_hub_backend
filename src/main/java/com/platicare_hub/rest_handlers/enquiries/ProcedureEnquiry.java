package com.platicare_hub.rest_handlers.enquiries;

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

@EndpointProps(prefixPath = "/free-enquiry", templatePath = "", httpMethod = "POST", allowedRoles = {"GUEST"})
public class ProcedureEnquiry implements HttpHandler
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
        String enquiryId = TokenGenerator.generateSecureToken();
        String fullName = formData.getFirst("name").getValue();
        String email = formData.getFirst("email").getValue();
        String phone = formData.getFirst("phone").getValue();
        String interestedProcedureId = formData.getFirst("interested_procedure_id").getValue();
        String typeOfInfo = formData.getFirst("info_type").getValue();
        String message = formData.getFirst("message").getValue();
        String enquiryStatus = "ACTIVE";

        if (!"".equals(fullName) && !"".equals(email) && !"".equals(phone) && !"".equals(interestedProcedureId) && !"".equals(typeOfInfo))
        {
            String sqlQuery = """
                    INSERT INTO enquiry_info (enquiry_id, full_name, email, phone, interested_procedure_id, type_of_information, message, enquiry_status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            List<Object> sqlParams = List.of(enquiryId, fullName, email, phone, interestedProcedureId, typeOfInfo, message, enquiryStatus);
            String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);

            httpServerExchange.setStatusCode(200);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send(response);

        }
    }
}
