package com.platicare_hub.rest_handlers.user_access_mgmt.auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
// import java.util.Map;

// import com.fasterxml.jackson.databind.ObjectMapper;
import com.platicare_hub.db_handlers.DatabaseConnectionsHikari;
import com.platicare_hub.db_handlers.DatabaseOperationsHikari;
// import com.monitoringsystem.db_handlers.DatabaseResultsProcessors;

public class UserRepository
{
    public static String[] lookupUserAttrByApiKey(String authToken)
    {
        String[] userAttr = new String[2];
        // ObjectMapper objectMapper = new ObjectMapper();
        try (Connection connection = DatabaseConnectionsHikari.getDbDataSource().getConnection())
        {
            String sqlQuery = """
                    SELECT su.user_id, su.privilege
                    FROM system_users su
                    JOIN user_auth_tokens uat ON uat.email = su.email
                    WHERE token = ?
                    """;
            List<Object> sqlParams = List.of(authToken);
            ResultSet resultSet = DatabaseOperationsHikari.dbQuery(connection, sqlQuery, sqlParams);
            if (resultSet != null && resultSet.next())
            {
                userAttr[0] = resultSet.getString("user_id");
                userAttr[1] = resultSet.getString("privilege");

                // String jsonString = DatabaseResultsProcessors.processResultsToJson(resultSet);
                // Map<String, Map<String, Object>> resultMap = objectMapper.readValue(jsonString, Map.class);
                // userId = String.valueOf(resultMap.get("1").get("auth_token"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return userAttr;
    }
}