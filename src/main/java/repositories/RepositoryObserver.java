package repositories;

import java.util.Stack;
import java.util.concurrent.BlockingQueue;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import beans.Note;

@Aspect
public class RepositoryObserver {

	private Stack<BlockingQueue> callbacks = new Stack<>();

	public void addCallback(BlockingQueue bq) {
		callbacks.push(bq);
	}

	private void notifyObserversWithUpdate(Note note) throws Exception {
		while (!callbacks.isEmpty())
			callbacks.pop().put(note);
	}
	
	private void notifyObserversWithDelete(Note note) throws Exception {
		
	}
	
	@After("execution (* repositories.NoteRepository.add(..))")
	private void afterAdd(Note note) {
		System.out.println("afterAdd called, note is " + note);
	}
	
	private void afterUpdate() {
		
	}
	
	private void afterDelete() {
		
	}

}
