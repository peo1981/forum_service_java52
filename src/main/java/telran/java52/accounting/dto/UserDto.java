package telran.java52.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	
	 String login; //  "JavaFan",
     String firstName; // "John",
     String lastName; //"Smith"
     @Singular
     Set <String> roles;
}
