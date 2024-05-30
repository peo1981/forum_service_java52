package telran.java52.accounting.service;

import telran.java52.accounting.dto.RolesDto;
import telran.java52.accounting.dto.UserDto;
import telran.java52.accounting.dto.UserEditDto;
import telran.java52.accounting.dto.UserRegisterDto;

public interface UserAccountService {

	UserDto registration (UserRegisterDto userRegisterDto);
	UserDto login (String login);
	UserDto removeUser (String login);
	UserDto updateUser (String login, UserEditDto userEditDto);
	RolesDto changeRolesList (String login, String role, boolean isAddRole);
	UserDto getUser (String login);
	void changePassword(String login, String newPassword);
}
