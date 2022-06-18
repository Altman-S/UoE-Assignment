grammar Minibase;

query
    : head ':-' body
    ;

head
    : ID_UPPER '(' ')'
    | ID_UPPER '(' sumagg ')'
    | ID_UPPER '(' avgagg ')'
    | ID_UPPER '(' variable (',' variable)*  ')'
    | ID_UPPER '(' variable (',' variable)* ',' sumagg ')'
    | ID_UPPER '(' variable (',' variable)* ',' avgagg ')'
    ;

sumagg
    : 'SUM' '(' variable ')'
    ;

avgagg
    : 'AVG' '(' variable ')'
    ;

body
    : atom (',' atom)*
    ;

atom
    : relationalAtom
    | comparisonAtom
    ;

relationalAtom
    : ID_UPPER '(' term (',' term)* ')'
    ;

comparisonAtom
    : term cmpOp term
    ;

term
    : variable
    | constant
    ;

variable
    : ID_LOWER
    ;

constant
    : INT
    | STRING
    ;

cmpOp
    : '=' | '!=' | '<' | '<=' | '>' | '>='
    ;

INT : [0-9]+ ;

STRING : '\'' [a-zA-Z \t]* '\'' ;

ID_UPPER : [A-Z]+ ;

ID_LOWER : [a-z]+ ;

WS : [ \t\r\n]+ -> skip ;
