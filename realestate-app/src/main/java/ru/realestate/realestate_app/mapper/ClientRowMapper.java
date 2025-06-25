package ru.realestate.realestate_app.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import ru.realestate.realestate_app.model.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRowMapper implements RowMapper<Client> {
    
    @Override
    public Client mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
        
        client.setIdClient(rs.getLong("id_client"));
        client.setFirstName(rs.getString("first_name"));
        client.setLastName(rs.getString("last_name"));
        client.setMiddleName(rs.getString("middle_name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        
        return client;
    }
} 