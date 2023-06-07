grammar Rule;

parse
 : expr EOF
 ;

expr
 : '(' expr ')' #PAREN
 | '!'<assoc=right> expr #NOT
 | expr '&&' expr #AND
 | expr '||' expr #OR
 | expr '?' expr ':' expr #IF
 | expr '?' expr #SEMI_IF
 | expr '->' expr #SER
 | expr '=>' expr #PAR
 | LIMIT LP CONST ',' CONST ',' arguments RP #LIMIT
 | ID #ID
 ;

arguments
 : expr (',' expr )*
 ;

LP:'(';
RP:')';
LIMIT: 'limit';
STRING:'"'(ESC|.)*?':';
fragment ESC:'\\'[btnr"\\];

CONST: '\'' [-]?DIGIT+ '\'';

ID: '∅'|(ID_LETTER|DIGIT)+;
fragment ID_LETTER:'a'..'z'|'A'..'Z'|'_';
fragment DIGIT:'0'..'9';

WS: [ \t\r\n]+ -> skip;









