package notearius;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import beans.User;
import configuration.RootConfig;
import repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
public class UserRepositoryTest {

	@Autowired 
	private UserRepository rep;
	
	User user = new User("gaben" + Math.random(), "mooly", "", "");

	
	@Test
	public void addUserTest() throws Exception {
		rep.add(user);
		User userFromDB = rep.get(user.getUsername());
		assertEquals(user, userFromDB);
	}
	
	@Test
	@Ignore
	public void updateUserTest() throws Exception {
		user.setFirstName("GABE");
		rep.update(user);
		assertEquals(user, rep.get(user.getId()));
	}
		
}
