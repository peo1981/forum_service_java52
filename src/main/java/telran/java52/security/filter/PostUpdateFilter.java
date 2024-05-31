package telran.java52.security.filter;

import java.io.IOException;

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
import telran.java52.post.dao.PostRepository;

@Component
@RequiredArgsConstructor
@Order(40)
public class PostUpdateFilter implements Filter {
	
	final PostRepository postRepository;
	
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
				System.out.println("================"+postId+"====================================");
				try {
					author = postRepository.findById(postId).orElseThrow(RuntimeException::new).getAuthor();
				} catch (Exception e) {
					response.sendError(404,"post not found!");
					e.printStackTrace();
					return;
					
				}
				if(!author.equalsIgnoreCase(request.getUserPrincipal().getName())) {
					response.sendError(403,"You dont have access to this post!!");
					System.out.println("================ PostUpdateFilter - You dont have access to this post!!\"====================================");
					return;
				}
						
			
		
		}
		
		
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		 String regexShort = "^/forum/post/[^/]+$";
		 
		boolean authorPath =(path.matches(regexShort)&& HttpMethod.PUT.matches(method))
				          ;
								
		 return authorPath;
	}

}
