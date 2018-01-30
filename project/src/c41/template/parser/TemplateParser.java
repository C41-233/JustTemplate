package c41.template.parser;

import c41.template.internal.util.ArrayUtil;
import c41.template.internal.util.Buffer;
import c41.template.internal.util.ErrorString;
import c41.template.internal.util.InputReader;
import c41.template.resolver.ResolveException;

public class TemplateParser {

	private int CommentPrefix = '!';
	private int LogicPrefix = '#';
	private int[] TemplatePrefixs = new int[] {'$'};
	private int OpenMatch = '{';
	private int CloseMatch = '}';
	
	public void setLogicPrefix(char ch) {
		this.LogicPrefix = ch;
	}
	
	public void setTemplatePrefix(char... chs) {
		this.TemplatePrefixs = new int[chs.length];
		for(int i=0; i<chs.length ;i++) {
			this.TemplatePrefixs[i] = chs[i];
		}
	}

	public void setCommentPrefix(char ch) {
		this.CommentPrefix = ch;
	}
	
	public void setOpenMatch(char ch) {
		this.OpenMatch = ch;
	}
	
	public void setCloseMatch(char ch) {
		this.CloseMatch = ch;
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
				if(ch == InputReader.EOF) {
					template.onText(buffer.take());
					break MainLoop;
				}
				else if(ch == CommentPrefix) {
					template.onText(buffer.take());
					state = ParseState.WaitCommentOpenMatch;
				}
				else if(ch == LogicPrefix) {
					template.onText(buffer.take());
					state = ParseState.WaitLogicOpenMatch;
				}
				else if(ArrayUtil.exist(TemplatePrefixs, ch)) {
					template.onText(buffer.take());
					currentTemplatePrefix = ch;
					state = ParseState.WaitParameterOpenMatch;
				}
				else {
					buffer.append(ch);
				}
				break;
			}

			case ReadComment:{
				if(ch == CloseMatch) {
					state = ParseState.ReadText;
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				break;
			}
			
			case ReadParameter:{
				if(ch == CloseMatch) {
					String word = buffer.take();
					template.onParameter((char) currentTemplatePrefix, word, reader.getLine(), reader.getColumn()-word.length());
					state = ParseState.ReadText;
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case WaitCommentOpenMatch:{
				if(ch == OpenMatch) {
					state = ParseState.ReadComment;
				}
				else {
					buffer.append(CommentPrefix);
					reader.pushBack();
					state = ParseState.ReadText;
				}
				break;
			}
			
			case WaitParameterOpenMatch:{
				if(ch == OpenMatch) {
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
				if(ch == OpenMatch) {
					state = ParseState.ReadLogicWord;
					currentLogicWordPos = reader.getColumn() + 1;
				}
				else {
					buffer.append(LogicPrefix);
					reader.pushBack();
					state = ParseState.ReadText;
				}
				break;
			}
			
			case ReadLogicWord:{
				if(Character.isWhitespace(ch) || ch==CloseMatch) {
					if(buffer.length() == 0) {
						throw new ResolveException(ErrorString.emptyLogicWord(reader.getLine(), reader.getColumn()-1));
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
						break;
					case "elseif":
						state = ParseState.EndLogicWord_ElseIf;
						break;
					default:
						throw new ResolveException(ErrorString.unrecognizedLogicWord(word, reader.getLine(), reader.getColumn() - word.length()));
					}
					reader.pushBack();
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
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
					throw new ResolveException(ErrorString.unexpectedCharacterAfter(ch, "if", reader.getLine(), reader.getColumn()));
				}
				break;
			}

			case ReadLogicWord_IF_Whitespace:{
				if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				else if(!Character.isWhitespace(ch)) {
					buffer.append(ch);
					state = ParseState.ReadLogicParamter_IF;
				}
				break;
			}
			
			case ReadLogicParamter_IF:{
				if(ch == CloseMatch) {
					state = ParseState.ReadText;
					String word = buffer.take();
					template.onIf(word, reader.getLine(), currentLogicWordPos, reader.getLine(), reader.getColumn()-word.length());
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
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
					throw new ResolveException(ErrorString.unexpectedCharacterAfter(ch, "elseif", reader.getLine(), reader.getColumn()));
				}
				break;
			}
			
			case ReadLogicWord_ElseIf_Whitespace:{
				if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				else if(!Character.isWhitespace(ch)) {
					buffer.append(ch);
					state = ParseState.ReadLogicParameter_ElseIf;
				}
				break;
			}

			case ReadLogicParameter_ElseIf:{
				if(ch == CloseMatch) {
					state = ParseState.ReadText;
					String word = buffer.take();
					template.onElseIf(word, reader.getLine(), currentLogicWordPos, reader.getLine(), reader.getColumn()-word.length());
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case WaitLogicWordCloseMatch:{
				if(ch == CloseMatch) {
					state = ParseState.ReadText;
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEOF(reader.getLine()));
				}
				else {
					throw new ResolveException();
				}
				break;
			}
			
			default:
				throw new ResolveException();
			}
		}
		
		template.end();
		return template;
	}
	
}
