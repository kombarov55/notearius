package beans;

public class DTO {
	private String status;
	private Note note;
	
	public DTO(String status, Note note) {
		this.status = status;
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}
	
}