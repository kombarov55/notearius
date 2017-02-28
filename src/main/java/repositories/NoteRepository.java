package repositories;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import beans.DTO;
import beans.Note;

@Repository
public class NoteRepository {

	@Autowired
	private SessionFactory sf;
	
	private Stack<BlockingQueue> callbacks = new Stack<BlockingQueue>();

	public void add(Note note) throws Exception {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			session.save(note);
			session.getTransaction().commit();
		}
		notifyCallbacks("add", note);
	}


	public void addAll(List<Note> notes) throws HibernateException {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			notes.forEach((note) -> {
				session.save(note);
			});
			session.getTransaction().commit();
		}
	}

	public Note get(int id) throws HibernateException {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			Note ret = session.load(Note.class, id);
			session.getTransaction().commit();
			return ret;
		}
	}
	
	public List<Note> lookup(Note prototype) throws HibernateException {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			List<Note> ret = session.createQuery(""
					+ "FROM Note "
					+ "WHERE content=:content "
					+ "AND heading=:heading")
					.setString("content", prototype.getContent())
					.setString("heading", prototype.getHeading())
					.list();
			session.getTransaction().commit();
			return ret;
		}
	}

	public List<Note> getAll() throws HibernateException {
		List<Note> notes;
		Session session = sf.openSession();
		session.beginTransaction();
		notes = session.createQuery("FROM Note").list();
		session.getTransaction().commit();
		return notes;
	}

	public void update(Note note) throws Exception {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			session.update(note);
			session.getTransaction().commit();
		}
		notifyCallbacks("update", note);
	}

	public void delete(int id) throws Exception {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			session.delete(session.get(Note.class, id));
			session.getTransaction().commit();
		}
		notifyCallbacks("delete", new Note(id, "", ""));
	}
	
	public void delete(Note note) throws Exception {
		delete(note.getId());
	}
	
	public void addCallback(BlockingQueue bq) {
		callbacks.push(bq);
	}
	
	private void notifyCallbacks(String status, Note note) throws Exception {
		while (!callbacks.isEmpty()) 
			callbacks.pop().put(new DTO(status, note));
	}
	
}
