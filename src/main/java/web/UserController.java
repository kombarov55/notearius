package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.User;
import repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository rep;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@CrossOrigin
	@PostMapping("/users/add")
	public User addUser(@RequestBody String json) throws Exception {
		User user = objectMapper.readValue(json, beans.User.class);
		rep.add(user);
		return user;
	}

	@CrossOrigin
	@GetMapping("/users/{username}")
	public User getUser(@PathVariable String username) {
		try {
			User user = rep.get(username);
			return user;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new User("sorry no such user", "", "", "");
		}
		
	}
		
	@CrossOrigin
	@PutMapping("/users/update")
	public void updateUser(@RequestBody String json) throws Exception {
		rep.update(objectMapper.readValue(json, beans.User.class));
	}
	
	@CrossOrigin
	@DeleteMapping("/users/delete")
	public void deleteUser() {
		//...
	}
}
