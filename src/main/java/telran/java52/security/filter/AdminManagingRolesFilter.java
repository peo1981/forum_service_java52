package telran.java52.security.filter;

import java.io.IOException;
import java.util.Set;

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
import telran.java52.security.model.User;

@Component
@RequiredArgsConstructor
@Order(20)
public class AdminManagingRolesFilter implements Filter {
	
	final UserAccountRepository userAccountRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response =(HttpServletResponse)resp;
		if (checkEndPoint(request.getMethod(),request.getServletPath())) {
			try {
				User principal = (User) request.getUserPrincipal();
				Set <String> roles = principal.getRoles();
				if (!(roles.contains("ADMINISTRATOR"))) {
					throw new RuntimeException();
				}}
			   catch (Exception e) {
				   System.out.println("==========AdminManagingRolesFilter===========");
				response.sendError(403);
				return;
			} 
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		  boolean rolePath =(HttpMethod.PUT.matches(method)||HttpMethod.DELETE.matches(method))
				  &&path.matches("/account/user/.*/role/.*");
		 
		 return rolePath;
	}

}
