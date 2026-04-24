package com.plasticare_hub.utils;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.plasticare_hub.db_handlers.DatabaseConnectionsHikari;
import com.plasticare_hub.db_handlers.DatabaseOperationsHikari;

record XmlConfig (XPath xpath, Document document) {}

public class Constants
{
    public static String DEFAULT_ADMIN_AUTH_TOKEN;
    public static String DEFAULT_ADMIN_EMAIL;
    public static final String UDERTOW_PORT = getUndertowPort();
    public static final String UNDERTOW_HOST = getUndertowHost();
    public static final String UNDERTOW_BASE_PATH_REST = getUndertowBasePathRest();
    public static final String DB_HOST = getDbHost();
    public static final String DB_PORT = getDbPort();
    public static final String DATABASE_NAME = getDatabaseName();
    public static final String DB_USERNAME = getDbUserName();
    public static final String DB_PASSWORD = getDbPassword();
    public static final String SMTP_SERVER = getSmtpServer();
    public static final String SMTP_SERVER_PORT = getSmtpServerPort();
    public static final String SMTP_SERVER_USERNAME = getSmtpServerUsername();
    public static final String SMTP_SERVER_PASSWORD = getSmtpServerPassword();
    public static final String FRONTEND_DOMAIN = getFrontEndDomain();
    public static final String FRONTEND_REG_USER_ENDPOINT = getFrontEndRegistrationEndpoint();

    public static void init()
    {
        DEFAULT_ADMIN_AUTH_TOKEN = getDefaultAdminAuthToken();
    }

    public static XmlConfig readXmlConfigFile() throws IOException, ParserConfigurationException, SAXException
    {
        Path configPath = Paths.get("conf.xml");
        String xmlString = new String(Files.readAllBytes(configPath));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        Document doc = builder.parse(is);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        return new XmlConfig(xPath, doc);
    }

    public static String getDatabaseName()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//DB/DATABASE_NAME");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDbUserName()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//DB/USERNAME");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDbPassword()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//DB/PASSWORD");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDbHost()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//DB/HOST");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDbPort()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//DB/PORT");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUndertowHost()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//UNDERTOW/HOST/@REST");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUndertowPort()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//UNDERTOW/PORT/@REST");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUndertowBasePathRest()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//UNDERTOW/BASE_PATH/@REST");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSmtpServer()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//SMTP/DOMAIN");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSmtpServerPort()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//SMTP/PORT");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSmtpServerUsername()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//SMTP/EMAIL");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSmtpServerPassword()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//SMTP/APP_PASSWORD");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFrontEndDomain()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//FRONTEND/DOMAIN");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFrontEndRegistrationEndpoint()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//FRONTEND/REGISTER_USER_ENDPOINT");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            return nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDefaultAdminAuthToken()
    {
        try
        {
            XmlConfig xmlConfig = readXmlConfigFile();
            XPathExpression expr = xmlConfig.xpath().compile("//DEFAULT/ADMIN_EMAIL");
            NodeList nodeList = (NodeList) expr.evaluate(xmlConfig.document(), XPathConstants.NODESET);
            DEFAULT_ADMIN_EMAIL = nodeList.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String sqlQuery = """
                SELECT token
                FROM user_auth_tokens
                WHERE email = ?
                """;
        List<Object> sqlParams = List.of(DEFAULT_ADMIN_EMAIL);
        String defaultAdminAuthToken = "";
        Connection connection = null;
        try
        {
            connection = DatabaseConnectionsHikari.getDbDataSource().getConnection();
            ResultSet resultSet = DatabaseOperationsHikari.dbQuery(connection, sqlQuery, sqlParams);
            if (resultSet != null && resultSet.next())
            {
                defaultAdminAuthToken = resultSet.getString("token");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return defaultAdminAuthToken;
    }


}