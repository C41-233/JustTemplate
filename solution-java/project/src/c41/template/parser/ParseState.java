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
	
	EndLogicWord_For,
	ReadLogicWord_For_Whitespace1,
	ReadLogicWord_For_Parameter1,
	ReadLogicWord_For_Whitespace2,
	ReadLogicWord_For_In,
	ReadLogicWord_For_Whitespace3,
	ReadLogicWord_For_Parameter2,
	ReadLogicWord_For_Whitespace4,
	ReadLogicWord_For_By,
	ReadLogicWord_For_Whitespace5,
	ReadLogicWord_For_Parameter3,
	
}
