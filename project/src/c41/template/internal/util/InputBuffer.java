package c41.template.internal.util;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class InputBuffer {

	private final Reader reader;
	
	private final ArrayList<Integer> backs = new ArrayList<>();
	
	public InputBuffer(Reader reader) {
		this.reader = reader;
	}
	
	public int read() throws IOException {
		if(backs.size() > 0) {
			return backs.remove(backs.size() - 1);
		}
		int nread = reader.read();
		if(nread == -1) {
			reader.close();
		}
		return nread;
	}
	
	public void back(int ch) {
		backs.add(ch);
	}
	
}
