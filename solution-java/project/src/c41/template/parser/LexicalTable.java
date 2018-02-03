package c41.template.parser;

import java.util.ArrayList;
import java.util.List;

import c41.template.TemplateException;
import c41.template.TemplatePosition;
import c41.template.internal.util.Buffer;
import c41.template.internal.util.ErrorString;
import c41.template.internal.util.InputReader;

class LexicalTable {

	private final ParseCharacter characters;
	private final Template template = new Template();
	
	public LexicalTable(ParseCharacter characters) {
		this.characters = characters;
	}
	
	public void addText(String text, TemplatePosition position) {
		if(text.length() == 0) {
			return;
		}
		template.onText(text, position);
	}
	
	public void addToken(String text, TemplatePosition position) {
		char mark = text.charAt(0);
		if(characters.isComment(mark)) {
			return;
		}
		
		String body = text.substring(2, text.length()-1);
		TemplatePosition bodyPosition = position.offset(1, 3);
		if(characters.isParameterPrefix(mark)) {
			processParameter(mark, body, bodyPosition);
			return;
		}
		
		if(characters.isLogicPrefix(mark)) {
			processLogic(body, bodyPosition);
			return;
		}
		
		throw new TemplateException(text);
	}
	
	private void processParameter(char mark, String body, TemplatePosition position) {
		if(body.length() == 0) {
			throw new TemplateException(ErrorString.unexpectedCharacterAfter(
				characters.getCloseMatch(), 
				String.valueOf(characters.getOpenMatch()), 
				position.line, 
				position.column
			));
		}
		template.onParameter(mark, body, position);
	}
	
	private void processLogic(String body, TemplatePosition position) {
		List<Token> tokens = new ArrayList<>();
		InputReader reader = new InputReader(body);
		TokenState state = TokenState.Begin;
		
		Buffer buffer = new Buffer();
		int startLine = 0;
		int startColumn = 0;
		
		Read: while(true) {
			
			int ch = reader.read();
			
			switch (state) {
			
			case Begin:{
				if(ch == InputReader.EOF){
					break Read;
				}
				if(Character.isWhitespace(ch)){
					state = TokenState.ReadWhitespace;
				}
				else {
					buffer.append(ch);
					state = TokenState.ReadToken;
					startLine = reader.getLine();
					startColumn = reader.getColumn();
				}
				break;
			}
			case ReadWhitespace:{
				if(ch == InputReader.EOF) {
					break Read;
				}
				if(!Character.isWhitespace(ch)) {
					buffer.append(ch);
					state = TokenState.ReadToken;
					startLine = reader.getLine();
					startColumn = reader.getColumn();
				}
				break;
			}
			
			case ReadToken:{
				if(ch == InputReader.EOF) {
					tokens.add(new Token(buffer.take(), position.offset(startLine, startColumn)));
					break Read;
				}
				if(Character.isWhitespace(ch)) {
					tokens.add(new Token(buffer.take(), position.offset(startLine, startColumn)));
					state = TokenState.ReadWhitespace;
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			default:
				throw new TemplateException("state: %s", state);
			}
		}
		
		if(tokens.size() == 0) {
			throw new TemplateException(ErrorString.unexpectedCharacterAfter(
				characters.getCloseMatch(), 
				String.valueOf(characters.getOpenMatch()), 
				position.line, 
				position.column
			));
		}
		
		Token word = tokens.get(0);
		switch (word.text) {
		case "if":{
			if(tokens.size() == 1) {
				TemplatePosition errorPosition = position.offset(reader.getLine(), reader.getColumn() + 1);
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					word.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			Token name = tokens.get(1);
			if(tokens.size() > 2) {
				TemplatePosition errorPosition = tokens.get(2).position;
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					name.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			template.onIf(name.text, word.position.line, word.position.column, name.position.line, name.position.column);
			break;
		}
		case "endif":{
			if(tokens.size() > 1) {
				TemplatePosition errorPosition = tokens.get(1).position;
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					word.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			template.onEndIf(word.position.line, word.position.column);
			break;
		}
		
		case "else":{
			if(tokens.size() > 1) {
				TemplatePosition errorPosition = tokens.get(1).position;
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					word.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			template.onElse(word.position.line, word.position.column);
			break;
		}
		case "elseif":{
			if(tokens.size() == 1) {
				TemplatePosition errorPosition = position.offset(reader.getLine(), reader.getColumn() + 1);
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					word.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			Token name = tokens.get(1);
			if(tokens.size() > 2) {
				TemplatePosition errorPosition = position.offset(reader.getLine(), reader.getColumn() + 1);
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					name.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			template.onElseIf(name.text, word.position.line, word.position.column, name.position.line, name.position.column);
			break;
		}
		case "for":{
			if(tokens.size() == 1) {
				TemplatePosition errorPosition = position.offset(reader.getLine(), reader.getColumn() + 1);
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					word.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			if(tokens.size() == 2) {
				Token name = tokens.get(1);
				template.onFor(name.text, position.line, position.column, name.position.line, name.position.column);
			}
			break;
		}
		case "endfor":{
			if(tokens.size() > 1) {
				TemplatePosition errorPosition = tokens.get(1).position;
				throw new TemplateException(ErrorString.unexpectedCharacterAfter(
					characters.getCloseMatch(), 
					word.text, 
					errorPosition.line, 
					errorPosition.column
				));
			}
			template.onEndFor(word.position.line, word.position.column);
			break;
		}
		
		default:
			throw new TemplateException(ErrorString.unrecognizedLogicWord(word.text, word.position.line, word.position.column));
		}
	}
	
	private static enum TokenState{
		Begin, ReadWhitespace, ReadToken
	}
	
	public Template parse() {
		return template;
	}

	private static class Token{
		public final String text;
		public final TemplatePosition position;
		
		public Token(String text, TemplatePosition position) {
			this.text = text;
			this.position = position;
		}
		
	}
	
}
