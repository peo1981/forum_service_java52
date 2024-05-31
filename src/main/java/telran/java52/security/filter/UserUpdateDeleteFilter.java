package telran.java52.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserAccountRepository;
import telran.java52.accounting.model.Role;

@Component
@RequiredArgsConstructor
@Order(30)
public class UserUpdateDeleteFilter implements Filter {
	
	final UserAccountRepository userAccountRepository;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response =(HttpServletResponse)resp;
		String method =request.getMethod();
		
		if (checkEndPoint(method,request.getServletPath())) {
			try {
				Principal principal = request.getUserPrincipal();
				String login =principal.getName();
				boolean admin = userAccountRepository.findById(login)
						.orElseThrow(RuntimeException::new).getRoles().contains(Role.ADMINISTRATOR);
				boolean delete =HttpMethod.DELETE.matches(method);
				boolean owner =request.getServletPath().matches("/account/user/" + Pattern.quote(login));
				if (
						(!(owner||(delete&&admin)))
						
					)
						 {
					throw new RuntimeException();
				}}
			   catch (Exception e) {
				response.sendError(403);
				System.out.println("================ UserUpdateDeleteFilter - You dont have access to this post!!\"====================================");
				return;
			}} 
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		 
		 boolean userRemovePath =(path.matches("^/account/user/[^/]+$")&&
				 (HttpMethod.DELETE.matches(method)||HttpMethod.PUT.matches(method)));
		 return userRemovePath;
	} 
	}

