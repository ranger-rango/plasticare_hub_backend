
CREATE DATABASE plasticare_hub_db IF NOT EXISTS;

\connect plasticare_hub_db;

CREATE OR REPLACE FUNCTION update_date_modified()
RETURNS TRIGGER AS $$
BEGIN
    IF ROW (NEW.*) IS DISTINCT FROM ROW (OLD.*) THEN
        NEW.date_modified = NOW();
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';


CREATE TABLE consultation_info (
    consultation_id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(12) NOT NULL,
    interested_procedure_id VARCHAR(10) NOT NULL,
    preferred_doctor_id VARCHAR(10) NOT NULL,
    preferred_date VARCHAR(10) NOT NULL,
    preferred_time VARCHAR(5) NOT NULL,
    message TEXT,
    consultation_status VARCHAR(8) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_modified TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TRIGGER update_consultation_info_date_modified BEFORE UPDATE ON consultation_info
FOR EACH ROW EXECUTE PROCEDURE update_date_modified();

CREATE TABLE enquiry_info (
    enquiry_id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    interested_procedure_id VARCHAR(10) NOT NULL,
    type_of_information VARCHAR(255) NOT NULL,
    message TEXT,
    enquiry_status VARCHAR(8) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_modified TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TRIGGER update_enquiry_info_date_modified BEFORE UPDATE ON enquiry_info
FOR EACH ROW EXECUTE PROCEDURE update_date_modified();

CREATE TABLE facility_tour_info (
    tour_id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    preferred_date VARCHAR(10) NOT NULL,
    preferred_time VARCHAR(5) NOT NULL,
    message TEXT,
    status VARCHAR(8) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_modified TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TRIGGER update_facility_tour_info_date_modified BEFORE UPDATE ON facility_tour_info
FOR EACH ROW EXECUTE PROCEDURE update_date_modified();


CREATE TABLE system_users (
    user_id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(40) NOT NULL,
    middle_name VARCHAR(40) NOT NULL,
    surname VARCHAR(40) NOT NULL,
    privilege VARCHAR(15) NOT NULL,
    user_status VARCHAR(15) NOT NULL,
    passwrd TEXT NOT NULL,
    date_created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_modified TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TRIGGER update_system_users_date_modified BEFORE UPDATE ON system_users
FOR EACH ROW EXECUTE PROCEDURE update_date_modified();

CREATE TABLE user_auth_tokens (
    email VARCHAR(100) NOT NULL REFERENCES system_users(email) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_modified TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TRIGGER update_user_auth_tokens_date_modified BEFORE UPDATE ON user_auth_tokens
FOR EACH ROW EXECUTE PROCEDURE update_date_modified();

CREATE TABLE user_registration_tokens (
    email VARCHAR(100) NOT NULL REFERENCES system_users(email) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_modified TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TRIGGER update_user_registration_tokens_date_modified BEFORE UPDATE ON user_registration_tokens
FOR EACH ROW EXECUTE PROCEDURE update_date_modified();
