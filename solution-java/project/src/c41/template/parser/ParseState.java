package c41.template.parser;

enum ParseState {

	ReadText,
	ReadComment,
	ReadParameter,
	
	WaitCommentOpenMatch,
	WaitParameterOpenMatch,
	WaitLogicOpenMatch,
	
	ReadLogicWord,
	WaitLogicWordCloseMatch,
	
	EndLogicWord_IF,
	ReadLogicWord_IF_Whitespace,
	ReadLogicParamter_IF,
	
	EndLogicWord_ElseIf,
	ReadLogicWord_ElseIf_Whitespace,
	ReadLogicParameter_ElseIf,
}
