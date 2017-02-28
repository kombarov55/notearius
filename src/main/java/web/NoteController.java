package web;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.DTO;
import beans.Note;
import repositories.NoteRepository;

@RestController
public class NoteController {
	
	@Autowired
	private NoteRepository rep;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public NoteController() {
	}
	
	public NoteController(NoteRepository rep) {
		this.rep = rep;
	}

	@CrossOrigin
	@GetMapping("/getNotes")
	public List<Note> getNote() throws HibernateException {
		return rep.getAll();
	}
	
	@CrossOrigin
	@GetMapping("/addNote")
	public Note addNote() throws Exception {
		String content;
		do {
			content = String.valueOf(Math.round(Math.random() * Math.pow(10, 16)));
		} while (content.length() != 16);
		Note note = new Note(555, "heading", content);
		rep.add(note);
		return note;
	}
	
	@CrossOrigin
	@PutMapping(value="/updateNote", consumes="application/json")
	public void updateNote(@RequestBody String json) throws Exception {
		Note note;
		synchronized(objectMapper) {
			note = objectMapper.readValue(json, Note.class);
		}
		rep.update(note);
	}
	
	@CrossOrigin
	@PutMapping(value="/deleteNote", consumes="application/json")
	public void deleteNote(@RequestBody String json) throws Exception {
		rep.delete(Integer.valueOf(json));
	}
	
	@CrossOrigin
	@GetMapping("/askUpdate") 
	public DTO getPromise() throws Exception {
		BlockingQueue<DTO> bq = new ArrayBlockingQueue<>(1);
		rep.addCallback(bq);
		return bq.take();
	}
	
}
