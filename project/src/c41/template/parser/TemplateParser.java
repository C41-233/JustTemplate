package c41.template.parser;

import c41.template.internal.util.ArrayUtil;
import c41.template.internal.util.Buffer;
import c41.template.parser.reader.ITemplateReader;
import c41.template.resolver.ResolveException;

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
	
	public ITemplate parse(ITemplateReader reader){
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
						template.addTextFragment(buffer.take());
					}
				};
				if(ch == -1) {
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
					template.addParameter((char) currentTemplatePrefix, buffer.take());
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
					reader.push(ch);
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
					reader.push(ch);
					currentTemplatePrefix = 0;
					state = ParseState.ReadText;
				}
				break;
			}
			
			case WaitLogicOpenMatch:{
				if(ch == openMatch) {
					state = ParseState.WaitLogicWord;
				}
				else {
					buffer.append(logicPrefix);
					reader.push(ch);
					state = ParseState.ReadText;
				}
				break;
			}
			
			case WaitLogicWord:{
				if(ch == 'i') {
					state = ParseState.WaitLogicWord_I_F;
				}
				else if(ch == 'e') {
					state = ParseState.WaitLogicWord_E_NDIF;
				}
				else {
					throw new ResolveException("unrecognized logic word %c", ch);
				}
				break;
			}
			
			case WaitLogicWord_I_F:{
				if(ch == 'f') {
					state = ParseState.EndLogicWord_IF;
				}
				else {
					throw new ResolveException("unrecognized logic word %c", ch);
				}
				break;
			}
			
			case EndLogicWord_IF:{
				if(Character.isWhitespace(ch)) {
					state = ParseState.ReadLogicWord_IF_Whitespace;
					reader.push(ch);
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
					template.addIfFragment(buffer.take());
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case WaitLogicWord_E_NDIF:{
				if(ch == 'n') {
					state = ParseState.WaitLogicWord_EN_DIF;
				}
				else {
					throw new ResolveException();
				}
				break;
			}
			
			case WaitLogicWord_EN_DIF:{
				if(ch == 'd') {
					state = ParseState.WaitLogicWord_END_IF;
				}
				else {
					throw new ResolveException();
				}
				break;
			}
			
			case WaitLogicWord_END_IF:{
				if(ch == 'i') {
					state = ParseState.WaitLogicWord_ENDI_F;
				}
				else {
					throw new ResolveException();
				}
				break;
			}
			
			case WaitLogicWord_ENDI_F:{
				if(ch == 'f') {
					state = ParseState.WaitLogicWordCloseMatch;
				}
				else {
					throw new ResolveException();
				}
				break;
			}
			
			case WaitLogicWordCloseMatch:{
				if(ch == '}') {
					state = ParseState.ReadText;
					template.endIfFragment();
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
