package com.saferent1.mapper;

import com.saferent1.domain.User;
import com.saferent1.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")  //Mitt mål är att kalla någon klass i Spring
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    List<UserDTO> map(List<User> userList);

}
