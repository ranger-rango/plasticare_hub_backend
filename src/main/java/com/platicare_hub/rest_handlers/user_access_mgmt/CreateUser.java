package com.platicare_hub.rest_handlers.user_access_mgmt;

import java.util.List;

import com.platicare_hub.db_handlers.DatabaseProcessor;
import com.platicare_hub.rest_handlers.user_access_mgmt.auth.AuthManager;
import com.platicare_hub.utils.Constants;
import com.platicare_hub.utils.EndpointProps;
import com.platicare_hub.utils.TokenGenerator;
// import com.platicare_hub.utils.notification_manager.Mailer;
import com.platicare_hub.utils.notification_manager.Mailer;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;

@EndpointProps(prefixPath = "/uam", templatePath = "/user", httpMethod = "POST")
public class CreateUser implements HttpHandler
{
    private static String frontendDomain = Constants.FRONTEND_DOMAIN;
    private static String frontendRegUserEndpoint = Constants.FRONTEND_REG_USER_ENDPOINT;
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception
    {
        FormParserFactory formFactory = FormParserFactory.builder().build();
        FormDataParser formDataParser = formFactory.createParser(httpServerExchange);

        if (formDataParser == null)
        {
            httpServerExchange.setStatusCode(400);
            httpServerExchange.getResponseSender().send("{\"err_status\" : \"User creation failed\"}");
            return;
        }
        FormData formData = formDataParser.parseBlocking();
        String userId = TokenGenerator.generateSecureToken();
        String emailAddress = formData.getFirst("email").getValue();
        String firstName = formData.getFirst("first_name").getValue();
        String middleName = formData.getFirst("middle_name").getValue();
        String surname = formData.getFirst("surname").getValue();
        String privilege = formData.getFirst("privilege").getValue();
        String status = "PENDING";
        String password = "NULL";
        if (!"".equals(emailAddress) && !"".equals(firstName) && !"".equals(middleName) && !"".equals(surname) && !"".equals(privilege))
        {

            String sqlQuery = """
                INSERT INTO system_users (user_id, email, first_name, middle_name, surname, privilege, user_status, password) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
            
            List<Object> sqlParams = List.of(userId, emailAddress, firstName, middleName, surname, privilege, status, password);
            String response = DatabaseProcessor.dbProcessHandler(sqlQuery, sqlParams);
            if (response.contains("success"))
            {
                String regToken = TokenGenerator.generateSecureToken();
                String registrationUrl = String.format("%s%s/%s", frontendDomain, frontendRegUserEndpoint, regToken);
                Mailer.sendRegistrationUrl("user-registration-notification.html", emailAddress, firstName, registrationUrl);
                AuthManager.insertToken(emailAddress, regToken, "REG_TOKEN");
            }

            httpServerExchange.setStatusCode(200);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send(response);
        }
        else
        {
            httpServerExchange.setStatusCode(200);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send("{\"err_status\" : \"User creation failed\"}");
        }

    }

}