package repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import beans.User;

@Repository
public class UserRepository {

	@Autowired
	private SessionFactory sf;

	public void add(User user) throws Exception {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		}
	}
	
	public void update(User user) throws Exception {
		//...
	}
	
	public User get(int id) {
		try (Session session = sf.openSession()) {
			User user;
			session.beginTransaction();
			user = session.get(User.class, id);
			session.getTransaction().commit();
			return user;
		}
	}
	
	public User get(String username) throws Exception {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			User user = (User) session
					.createQuery("from User where username=:username")
					.setString("username",  username)
					.list().get(0);
			session.getTransaction().commit();
			return user;
		}
	}
	
	public void delete(User user) throws Exception {
		//...
	}
	
}
