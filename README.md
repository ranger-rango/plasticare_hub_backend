# Plasticare Hub Backend

## Description  
    The backend system handles patient data storage, access and modification.

## Backend Setup Guidelines  
    Fork the backend repository, into your repo.  
    You can make it private (preferred).  
### Prerequisites  
    1. Setup gmail app password.
    2. Install postgers

### Database Setup on Render 
    1. Go to Add New, Postgres and setup the database
    2. Copy the PSQL Command and paste it into a terminal to access the database (ensure postgres is install).  
    3. Find the ddl and insert SQL files in the sql_queries/ directory.  
    4. Run the scripts in your database, with the DDL run first, then the INSERT.  

### Web Service Setup on Render  
    1. Go to Add New, Web Service then add the backend repo.

#### Setting Up conf.xml  
    1. Go to the render secret files section, and name the file conf.xml  
    2. Copy the XML configurations below into the file contents.  
    3. Configure the items in the '{}' to your specifications.  

    ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <API>
            <UNDERTOW>
                <PORT REST="{rest_api_port}" />
                <HOST REST="{rest_api_host}" />
                <BASE_PATH PORTAL="" REST="/api" />
            </UNDERTOW>
            <DB>
                <HOST>{database_host}</HOST>
                <PORT>{database_port}</PORT>
                <DATABASE_NAME TYPE="PLAINTEXT">plasticare_hub_db</DATABASE_NAME>
                <USERNAME TYPE="PLAINTEXT">{database_user}</USERNAME>
                <PASSWORD TYPE="PLAINTEXT">{database_password}</PASSWORD>
                <SHOW_SQL>false</SHOW_SQL>
            </DB>
            <SMTP>
                <APP_NAME TYPE="PLAINTEXT">{application_name}</APP_NAME>
                <DOMAIN TYPE="PLAINTEXT">{smptp_url}</DOMAIN>
                <PORT TYPE="PLAINTEXT">{smtp_server_port}</PORT>
                <EMAIL TYPE="PLAINTEXT">{youremail@example.com}</EMAIL>
                <APP_PASSWORD TYPE="PLAINTEXT">{google_app_password}</APP_PASSWORD>
            </SMTP>
            <FRONTEND>
                <DOMAIN TYPE="PLAINTEXT">{frontend_server_domain}</DOMAIN>
                <REGISTER_USER_ENDPOINT TYPE="PLAINTEXT">/auth/registration</REGISTER_USER_ENDPOINT>
            </FRONTEND>
            <DEFAULT>
                <ADMIN_EMAIL TYPE="PLAINTEXT">default.system@ssldom.com</ADMIN_EMAIL>
            </DEFAULT>
        </API>
    ```
#### Create db.properties  
    - Create a db.properties file in the render secret files section.  

    Your backend should be running at this point.  
    Cheers.