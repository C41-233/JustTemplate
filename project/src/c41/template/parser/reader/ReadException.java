package c41.template.parser.reader;

public class ReadException extends RuntimeException{

	private static final long serialVersionUID = -6085230775736663255L;

	public ReadException(Exception e) {
		super(e);
	}
	
}
