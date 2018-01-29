package c41.template.parser;

import c41.template.internal.util.ArrayUtil;
import c41.template.internal.util.Buffer;
import c41.template.internal.util.InputReader;
import c41.template.resolver.ResolveException;
import javafx.scene.shape.Line;

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
	
	public ITemplate parse(String input){
		InputReader reader = new InputReader(input);
		Template template = new Template();

		ParseState state = ParseState.ReadText;
		Buffer buffer = new Buffer();
		
		int currentTemplatePrefix = 0;
		int currentLogicWordPos = 0;

		MainLoop:
		while(true) {
			int ch = reader.read();
			
			switch (state) {
			
			case ReadText:{
				Runnable endReadText = ()->{
					if(buffer.length() > 0) {
						template.onText(buffer.take());
					}
				};
				if(ch == InputReader.EOF) {
					endReadText.run();
					break MainLoop;
				}
				else if(ch == commentPrefix) {
					endReadText.run();
					state = ParseState.WaitCommentOpenMatch;
				}
				else if(ch == logicPrefix) {
					endReadText.run();
					state = ParseState.WaitLogicOpenMatch;
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
					template.onParameter((char) currentTemplatePrefix, buffer.take());
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
					reader.pushBack();
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
					reader.pushBack();
					currentTemplatePrefix = 0;
					state = ParseState.ReadText;
				}
				break;
			}
			
			case WaitLogicOpenMatch:{
				if(ch == openMatch) {
					state = ParseState.ReadLogicWord;
					currentLogicWordPos = reader.getColumn() + 1;
				}
				else {
					buffer.append(logicPrefix);
					reader.pushBack();
					state = ParseState.ReadText;
				}
				break;
			}
			
			case ReadLogicWord:{
				if(Character.isWhitespace(ch) || ch=='}') {
					if(buffer.length() == 0) {
						throw new ResolveException("empty logic block in line %d column %d", reader.getLine(), reader.getColumn()-1);
					}
					String word = buffer.take();
					switch (word) {
					case "if":
						state = ParseState.EndLogicWord_IF;
						break;
					case "endif":
						state = ParseState.WaitLogicWordCloseMatch;
						template.onEndif(reader.getLine(), currentLogicWordPos);
						break;
					case "else":
						state = ParseState.WaitLogicWordCloseMatch;
						template.onElse(reader.getLine(), currentLogicWordPos);
					case "elseif":
						state = ParseState.EndLogicWord_ElseIf;
						break;
					default:
						throw new ResolveException("unrecognized logic word '%s' in line %d column %d", word, reader.getLine(), reader.getColumn() - word.length());
					}
					reader.pushBack();
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case EndLogicWord_IF:{
				if(Character.isWhitespace(ch)) {
					state = ParseState.ReadLogicWord_IF_Whitespace;
					reader.pushBack();
				}
				else {
					throw new ResolveException("unexpected character '%c' after 'if' in line %d column %d", ch, reader.getLine(), reader.getColumn());
				}
				break;
			}

			case ReadLogicWord_IF_Whitespace:{
				if(!Character.isWhitespace(ch)) {
					buffer.append(ch);
					state = ParseState.ReadLogicParamter_IF;
				}
				break;
			}
			
			case ReadLogicParamter_IF:{
				if(ch == '}') {
					state = ParseState.ReadText;
					template.onIf(buffer.take(), reader.getLine(), currentLogicWordPos);
				}
				else {
					buffer.append(ch);
				}
				break;
			}

			case EndLogicWord_ElseIf:{
				if(Character.isWhitespace(ch)) {
					state = ParseState.ReadLogicWord_ElseIf_Whitespace;
					reader.pushBack();
				}
				else {
					throw new ResolveException("unexpected character '%c' after 'if' in line %d column %d", ch, reader.getLine(), reader.getColumn());
				}
				break;
			}
			
			case ReadLogicWord_ElseIf_Whitespace:{
				if(!Character.isWhitespace(ch)) {
					buffer.append(ch);
					state = ParseState.ReadLogicParameter_ElseIf;
				}
				break;
			}

			case ReadLogicParameter_ElseIf:{
				if(ch == '}') {
					state = ParseState.ReadText;
					template.OnElseIf(buffer.take(), reader.getLine(), currentLogicWordPos);
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case WaitLogicWordCloseMatch:{
				if(ch == '}') {
					state = ParseState.ReadText;
				}
				else {
					throw new ResolveException();
				}
				break;
			}
			
			default:
				throw new ResolveException("state %s", state);
			}
		}
		
		template.end();
		return template;
	}
	
}
