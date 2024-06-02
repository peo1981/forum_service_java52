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
import telran.java52.post.dao.PostRepository;
import telran.java52.security.model.User;

@Component
@RequiredArgsConstructor
@Order(50)
public class DletePostFilter implements Filter {
	
	final PostRepository postRepository;
	final UserAccountRepository userAccountRepository;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response =(HttpServletResponse)resp;
		String method =request.getMethod();
		String path =request.getServletPath();

		if (checkEndPoint(method,path)) {
			String postId = path.split("/")[3];
			
			String author;
			
			try {
				author = postRepository.findById(postId).orElseThrow(RuntimeException::new).getAuthor();
			} catch (Exception e) {
				response.sendError(404,"post not found!");
				e.printStackTrace();
				return;
			}
			User principal = (User) request.getUserPrincipal();
			Set <String> roles = principal.getRoles();
			boolean moderator = roles.contains("MODERATOR");
			if(!author.equalsIgnoreCase(request.getUserPrincipal().getName())&&!moderator) {
				response.sendError(403,"You dont have access to this post!!");
				System.out.println("================\"DeletePost_Filter_  You dont have access to delete this post!!\"====================================");
				return;
			}
					
		
	
	}
		
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		 String regexShort = "^/forum/post/[^/]+$";
		 
		boolean authorPath =(path.matches(regexShort)&& HttpMethod.DELETE.matches(method))
				          ;
								
		 return authorPath;
	}
}
