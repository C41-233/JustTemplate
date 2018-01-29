package c41.template.parser;

enum ParseState {

	ReadText,
	ReadComment,
	ReadParameter,
	
	WaitCommentOpenMatch,
	WaitParameterOpenMatch,
	WaitLogicOpenMatch,
	
	WaitLogicWord,
	
	WaitLogicWord_I_F,
	EndLogicWord_IF,
	ReadLogicWord_IF_Whitespace,
	ReadLogicParamter_IF,
	
	WaitLogicWord_E_NDIF,
	WaitLogicWord_EN_DIF,
	WaitLogicWord_END_IF,
	WaitLogicWord_ENDI_F,
	WaitLogicWordCloseMatch,
	
	UnrecognizedLogicWord,
}
