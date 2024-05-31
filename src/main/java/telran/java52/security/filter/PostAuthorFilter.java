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


@Component
@RequiredArgsConstructor
@Order(40)
public class PostAuthorFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response =(HttpServletResponse)resp;
		String method =request.getMethod();
		if (checkEndPoint(method, request.getServletPath())) {
			try {
				Principal principal = request.getUserPrincipal();
				String login =principal.getName();
				String quotedLogin = Pattern.quote(login);
		        String regex = "^/forum/post(?:/[^/]+/comment)?" + "/" + quotedLogin + "$";
				if (		
					  !request.getServletPath().matches(regex)
					)
						 {
					throw new RuntimeException();
				}}
			   catch (Exception e) {
				response.sendError(403, "Not avalible!!!");
				System.out.println("==========PostAuthorFilter===========");
				return;
			}} 
	
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method,String path) {
		  String regexShort = "^/forum/post/[^/]+$";
		  String regexLong = "^/forum/post/[^/]+/comment/[^/]+$";
		boolean authorPath =(path.matches(regexShort)&& HttpMethod.POST.matches(method))
				          ||(path.matches(regexLong)&& HttpMethod.PUT.matches(method));
								
		 return authorPath;
	} 
}
