package com.platicare_hub.db_handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class DatabaseProcessor
{
    public static String dbProcessHandler(String sqlQuery, List<Object> sqlParams) throws Exception
    {
        Connection connection = DatabaseConnectionsHikari.getDbDataSource().getConnection();
        ResultSet resultSet = DatabaseOperationsHikari.dbQuery(connection, sqlQuery, sqlParams);
        String response = "";
        if (resultSet == null)
        {
            connection.close();
            response = "{\"status\": \"success\"}";
        }
        else if (resultSet != null)
        {
            response = DatabaseResultsProcessors.processResultsToJson(resultSet, connection);
        }

        return response;
    }
}
