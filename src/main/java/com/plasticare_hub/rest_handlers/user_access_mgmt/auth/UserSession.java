package com.plasticare_hub.rest_handlers.user_access_mgmt.auth;

public record UserSession(String userId, String userRole, String tokenType) {}