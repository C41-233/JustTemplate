package c41.template.parser;

enum ParseState {

	ReadText,
	ReadComment,
	ReadParameter,
	
	WaitCommentOpenMatch,
	WaitParameterOpenMatch,
	
	
}
