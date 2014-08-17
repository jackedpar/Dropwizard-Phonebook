package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Contact;
import com.dwbook.phonebook.representations.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {
   public User map(int index, ResultSet r, StatementContext ctx)
   throws SQLException {
      return new User(r.getString("username"), r.getString("password"));
   }
}