package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.UserMapper;
import com.dwbook.phonebook.representations.User;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface UserDAO {

    @SqlUpdate("insert into users (username, password) values (:username, :password)")
    void createUser(@Bind("username") String username, @Bind("password") String password);

    @Mapper(UserMapper.class)
    @SqlQuery("select * from users where username = :username")
    User getUserByUsername(@Bind("username") String username);


}
