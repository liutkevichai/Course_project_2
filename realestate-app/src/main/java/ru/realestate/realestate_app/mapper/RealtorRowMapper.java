package ru.realestate.realestate_app.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import ru.realestate.realestate_app.model.Realtor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RealtorRowMapper implements RowMapper<Realtor> {
    
    @Override
    public Realtor mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Realtor realtor = new Realtor();
        
        realtor.setIdRealtor(rs.getLong("id_realtor"));
        realtor.setFirstName(rs.getString("first_name"));
        realtor.setLastName(rs.getString("last_name"));
        realtor.setMiddleName(rs.getString("middle_name"));
        realtor.setPhone(rs.getString("phone"));
        realtor.setEmail(rs.getString("email"));
        realtor.setExperienceYears(rs.getInt("experience_years"));
        
        return realtor;
    }
} 