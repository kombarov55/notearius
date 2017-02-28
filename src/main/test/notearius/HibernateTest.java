package notearius;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import beans.Note;
import configuration.RootConfig;
import repositories.NoteRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
public class HibernateTest {

	@Autowired
	SessionFactory sf;

	@Autowired
	DataSource ds;

	@Autowired
	NoteRepository rep;

	Note note = new Note(1, "sample note", "heading four");
	List<Note> notes = new ArrayList<>();

	{
		notes.add(new Note(123, "sample note", "heading one"));
		notes.add(new Note(2345, "sample note", "heading two"));
		notes.add(new Note(3321, "sample note", "heading three"));
	}

	@Test
//	@Ignore
	public void addSingleNoteTest() throws Exception {
		rep.add(note);
		System.out.println(note.getId());
		assertTrue(doesContain(note));
	}
	
	@Test
	@Ignore
	public void updateSingleNoteTest() throws Exception {
		note = rep.lookup(note).get(0);
		rep.add(note);
		assertTrue(doesContain(note));
		note.setContent("sample note");
	}
	
	@Test
	@Ignore
	public void deleteSingleNoteTest() throws Exception {
		rep.delete(note);
		assertFalse(doesContain(note));
	}

	@Test
	@Ignore
	public void addArrayTest() {
		try {
			rep.addAll(notes);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void deleteArrayTest() {
		try {
			List<Note> notes = rep.getAll();
			notes.forEach((note) -> {
				if (note.getHeading().equals("sample note"))
					try {
						rep.delete(note.getId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean doesContain(Note note) {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			List<Note> list = rep.lookup(note);
			session.getTransaction().commit();
			return list.contains(note);
		}
	}

}
