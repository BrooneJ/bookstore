package com.group.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepository {

    public boolean isUserNotExist(JdbcTemplate jdbcTemplate, long id) {
        String readSql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, id).isEmpty();
    }

    public void updateUserName(JdbcTemplate jdbcTemplate, long id, String name) {
        String sql = "UPDATE user SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, id);
    }
}
