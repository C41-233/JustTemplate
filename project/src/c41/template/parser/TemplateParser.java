package c41.template.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import c41.template.internal.engine.Template;
import c41.template.internal.util.ArrayUtil;
import c41.template.internal.util.Buffer;
import c41.template.internal.util.InputBuffer;

public class TemplateParser {

	private int commentPrefix = '!';
	private int logicPrefix = '#';
	private int[] templatePrefixs = new int[] {'$'};
	private int openMatch = '{';
	private int closeMatch = '}';
	
	public void setLogicPrefix(char ch) {
		this.logicPrefix = ch;
	}
	
	public void setTemplatePrefix(char... chs) {
		this.templatePrefixs = new int[chs.length];
		for(int i=0; i<chs.length ;i++) {
			this.templatePrefixs[i] = chs[i];
		}
	}

	public void setCommentPrefix(char ch) {
		this.commentPrefix = ch;
	}
	
	public void setOpenMatch(char ch) {
		this.openMatch = ch;
	}
	
	public void setCloseMatch(char ch) {
		this.closeMatch = ch;
	}
	
	private ITemplate parse(InputBuffer reader) throws IOException {
		ParseState state = ParseState.ReadText;
		Buffer buffer = new Buffer();
		Template template = new Template();
		
		int currentTemplatePrefix = 0;

		MainLoop:
		while(true) {
			int ch = reader.read();
			switch (state) {
			
			case ReadText:{
				Runnable endReadText = ()->{
					if(buffer.length() > 0) {
						template.addTextFragment(buffer.toString());
						buffer.clear();
					}
				};
				if(ch == -1) {
					endReadText.run();
					break MainLoop;
				}
				else if(commentPrefix == ch) {
					endReadText.run();
					state = ParseState.WaitCommentOpenMatch;
				}
				else if(ArrayUtil.exist(templatePrefixs, ch)) {
					endReadText.run();
					currentTemplatePrefix = ch;
					state = ParseState.WaitParameterOpenMatch;
				}
				else {
					buffer.append(ch);
				}
				break;
			}

			case ReadComment:{
				if(ch == closeMatch) {
					state = ParseState.ReadText;
				}
				break;
			}
			
			case ReadParameter:{
				if(ch == closeMatch) {
					template.addParameter((char) currentTemplatePrefix, buffer.toString());
					buffer.clear();
					state = ParseState.ReadText;
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case WaitCommentOpenMatch:{
				if(ch == openMatch) {
					state = ParseState.ReadComment;
				}
				else {
					buffer.append(commentPrefix);
					reader.back(ch);
					state = ParseState.ReadText;
				}
				break;
			}
			
			case WaitParameterOpenMatch:{
				if(ch == openMatch) {
					state = ParseState.ReadParameter;
				}
				else {
					buffer.append(currentTemplatePrefix);
					reader.back(ch);
					currentTemplatePrefix = 0;
					state = ParseState.ReadText;
				}
			}
			
			}
		}
		return template;
	}
	
	public ITemplate parse(String template) {
		try {
			StringReader reader = new StringReader(template);
			return parse(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ITemplate parse(Reader reader) throws IOException {
		InputBuffer buffer = new InputBuffer(reader);
		return parse(buffer);
	}
	
	public ITemplate parse(File file) throws IOException {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis, "utf8");
			return parse(reader);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
