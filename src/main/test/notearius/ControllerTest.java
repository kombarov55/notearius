package notearius;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import beans.DTO;
import beans.Note;
import configuration.RootConfig;
import repositories.NoteRepository;
import web.NoteController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
public class ControllerTest {

	@Autowired
	private NoteRepository rep;
	
	@Autowired
	SessionFactory sf;
	
	private Note note = new Note();
	
	@Autowired
	private DataSource ds;
	
	@Test
	@Ignore
	public void basicCallbackTest() throws Exception {
		BlockingQueue<Note> bq = new ArrayBlockingQueue<>(1);
		rep.addCallback(bq);
		rep.add(note);
		assertEquals(bq.take(), note);
	}

	@Test
	@Ignore
	public void controllerCallbackTest() throws Exception {
		NoteController controller = new NoteController(rep);
		FutureTask<DTO> task = new FutureTask<>(() -> controller.getPromise());
		new Thread(task).start();
		String response = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		do {
			System.out.println("add new element?(y/n)");
			response = reader.readLine();
		} while (!response.equals("y"));
		rep.add(note);
		assertEquals(task.get().getNote(), note);
	}
}
