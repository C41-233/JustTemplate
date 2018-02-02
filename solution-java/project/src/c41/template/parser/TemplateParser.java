package c41.template.parser;

import c41.template.TemplateException;
import c41.template.TemplatePosition;
import c41.template.internal.util.Buffer;
import c41.template.internal.util.ErrorString;
import c41.template.internal.util.InputReader;

public class TemplateParser {

	private final ParseCharacter characters = new ParseCharacter();
	
	public void setLogicPrefix(char ch) {
		characters.setLogicPrefix(ch);
	}
	
	public void setParameterPrefix(char... chs) {
		for(char ch : chs) {
			characters.addParameterPrefix(ch);
		}
	}

	public void setCommentPrefix(char ch) {
		characters.setCommentPrefix(ch);
	}
	
	public void setOpenMatch(char ch) {
		characters.setOpenMatch(ch);
	}
	
	public void setCloseMatch(char ch) {
		characters.setCloseMatch(ch);
	}
	
	private static enum LexicalState{
		Begin, ReadText, WaitForOpenMatch, ReadToken,
	}
	
	public ITemplate parse(String input){
		InputReader reader = new InputReader(input);
		LexicalTable lexical = new LexicalTable(characters);
		
		Buffer buffer = new Buffer();
		
		LexicalState state = LexicalState.Begin;
		
		int startLine = 0;
		int startColumn = 0;
		
		Read: while(true) {
			
			int ch = reader.read();
			
			switch (state) {
			
			case Begin:{
				startLine = reader.getLine();
				startColumn = reader.getColumn();
				if(ch == InputReader.EOF) {
					break Read;
				}
				buffer.append(ch);
				if(characters.isPrefix(ch)) {
					state = LexicalState.WaitForOpenMatch;
				}
				else {
					state = LexicalState.ReadText;
				}
				break;
			}
			
			case ReadText:{
				if(ch == InputReader.EOF) {
					lexical.addText(buffer.take(), new TemplatePosition(startLine, startColumn));
					break Read;
				}
				if(characters.isPrefix(ch)) {
					lexical.addText(buffer.take(), new TemplatePosition(startLine, startColumn));
					startLine = reader.getLine();
					startColumn = reader.getColumn();
					state = LexicalState.WaitForOpenMatch;
				}
				buffer.append(ch);
				break;
			}
				
			case WaitForOpenMatch:{
				if(ch == InputReader.EOF) {
					lexical.addText(buffer.take(), new TemplatePosition(startLine, startColumn));
					break Read;
				}
				buffer.append(ch);
				if(characters.isOpenMatch(ch)) {
					state = LexicalState.ReadToken;
				}
				else {
					state = LexicalState.ReadText;
				}
				break;
			}
			
			case ReadToken:{
				if(ch == InputReader.EOF) {
					throw new TemplateException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				buffer.append(ch);
				if(characters.isCloseMatch(ch)) {
					lexical.addToken(buffer.take(), new TemplatePosition(startLine, startColumn));
					state = LexicalState.ReadText;
				}
				break;
			}
			
			default:
				throw new TemplateException();
			}
		}
		
		return lexical.parse();
	}
	
}
