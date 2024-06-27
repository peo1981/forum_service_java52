package telran.java52.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.post.dao.PostRepository;
import telran.java52.post.model.Post;

@Service("webSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {
	
	final PostRepository postRepository;
	
	public boolean checkPostAuthor (String postId, String username) {
		Post post =postRepository.findById(postId).orElse(null);
		return post!=null && post.getAuthor().equalsIgnoreCase(username);
		
	}

}
