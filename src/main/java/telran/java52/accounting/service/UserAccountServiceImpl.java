package telran.java52.accounting.service;



import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserAccountRepository;
import telran.java52.accounting.dto.RolesDto;
import telran.java52.accounting.dto.UserDto;
import telran.java52.accounting.dto.UserEditDto;
import telran.java52.accounting.dto.UserRegisterDto;
import telran.java52.accounting.dto.exception.IncorrectRoleException;
import telran.java52.accounting.dto.exception.UserExistsException;
import telran.java52.accounting.model.Role;
import telran.java52.accounting.model.UserAccount;
import telran.java52.post.dto.exceptions.PostNotFoundException;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {
	
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto registration(UserRegisterDto userRegisterDto) {
		if(userAccountRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsException();
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
		userAccount.setPassword(password);
		userAccount=userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class); 
	}

	@Override
	public UserDto login(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).
                orElseThrow(PostNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).
				                 orElseThrow(PostNotFoundException::new);
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}
		
	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login).
                orElseThrow(PostNotFoundException::new);
		String firstName = userEditDto.getFirstName();
		if (firstName!=null) {
			userAccount.setFirstName(firstName);
		}
		String lastName = userEditDto.getLastName();
		if (lastName!=null) {
			userAccount.setLastName(lastName);
		}
		userAccountRepository.save(userAccount);
		return  modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login).
                orElseThrow(PostNotFoundException::new);
		try {
			boolean done= isAddRole? userAccount.addRole(role):userAccount.removeRole(role);
			if(done) {
				userAccountRepository.save(userAccount);
			}
		} catch (Exception e) {
			throw new IncorrectRoleException();
			
		}
		return modelMapper.map(userAccount, RolesDto.class);
	}

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).
                orElseThrow(PostNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
		
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login).
                orElseThrow(PostNotFoundException::new);
		String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);

	}

	@Override
	public void run(String... args) throws Exception {
		if(!userAccountRepository.existsById("admin")){
			String password  = BCrypt.hashpw("admin", BCrypt.gensalt());
			UserAccount userAccount =new UserAccount("admin", "", "", password);
			userAccount.addRole(Role.MODERATOR.name());
			userAccount.addRole(Role.ADMINISTRATOR.name());
			userAccountRepository.save(userAccount); 
		}
		
	}

}
