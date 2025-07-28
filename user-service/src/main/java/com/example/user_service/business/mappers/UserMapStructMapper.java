package com.example.user_service.business.mappers;


import com.example.user_service.business.repository.model.UserDAO;
import com.example.user_service.model.User;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapStructMapper {

    UserDAO userToDAO(User user);

    User userDAOToUser(UserDAO userDAO);

    List<User> userDAOToUserList(List<UserDAO> daoList);

    List<UserDAO> userToDAOList(List<User> userList);


}


