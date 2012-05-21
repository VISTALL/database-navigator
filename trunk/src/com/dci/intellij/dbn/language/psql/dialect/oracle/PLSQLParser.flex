package com.dci.intellij.dbn.language.psql.dialect.oracle;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.language.common.TokenTypeBundle;

%%

%class OraclePLSQLParserFlexLexer
%implements FlexLexer
%public
%pack
%final
%unicode
%ignorecase
%function advance
%type IElementType
%eof{ return;
%eof}

%{
    private int braceCounter = 0;
    private TokenTypeBundle tt;
    public OraclePLSQLParserFlexLexer(TokenTypeBundle tt) {
        this.tt = tt;
    }
%}

WHITE_SPACE= {white_space_char}|{line_terminator}
line_terminator = \r|\n|\r\n
white_space_char= [\ \n\r\t\f]
ws  = {WHITE_SPACE}+
wso = {WHITE_SPACE}*

comment_tail =([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
BLOCK_COMMENT=("/*"[^]{comment_tail})|"/*"
LINE_COMMENT ={wso} "--" .*
REM_LINE_COMMENT = {wso} "rem" [^a-zA-Z] .*

IDENTIFIER = [:jletter:] ([:jletterdigit:]|"#")*
QUOTED_IDENTIFIER = "\""[^\"]*"\""?

string_simple_quoted      = "'"([^\']|"''"|{WHITE_SPACE})*"'"?
string_alternative_quoted = "q'"[^'][^]*[^']"'"?
STRING = "n"?({string_simple_quoted}|{string_alternative_quoted})

sign = "+"|"-"
digit = [0-9]
INTEGER = {digit}+("e"{sign}?{digit}+)?
NUMBER = {INTEGER}?"."{digit}+(("e"{sign}?{digit}+)|(("f"|"d"){ws}))?

%state DIV
%%

{WHITE_SPACE}+   { return tt.getSharedTokenTypes().getWhiteSpace(); }

{BLOCK_COMMENT}      { return tt.getSharedTokenTypes().getBlockComment(); }
{LINE_COMMENT}       { return tt.getSharedTokenTypes().getLineComment(); }
{REM_LINE_COMMENT}   { return tt.getSharedTokenTypes().getLineComment(); }

{INTEGER}     { return tt.getSharedTokenTypes().getInteger(); }
{NUMBER}      { return tt.getSharedTokenTypes().getNumber(); }
{STRING}      { return tt.getSharedTokenTypes().getString(); }

"("{wso}"+"{wso}")"  {return tt.getTokenType("CHRS_OUTER_JOIN");}

"||" {return tt.getOperatorTokenType(0);}
":=" {return tt.getOperatorTokenType(1);}
".." {return tt.getOperatorTokenType(2);}

"@" {return tt.getCharacterTokenType(0);}
":" {return tt.getCharacterTokenType(1);}
"," {return tt.getCharacterTokenType(2);}
"." {return tt.getCharacterTokenType(3);}
"=" {return tt.getCharacterTokenType(4);}
"!" {return tt.getCharacterTokenType(5);}
">" {return tt.getCharacterTokenType(6);}
"#" {return tt.getCharacterTokenType(7);}
"[" {return tt.getCharacterTokenType(8);}
"{" {return tt.getCharacterTokenType(9);}
"(" {return tt.getCharacterTokenType(10);}
"<" {return tt.getCharacterTokenType(11);}
"-" {return tt.getCharacterTokenType(12);}
"%" {return tt.getCharacterTokenType(13);}
"+" {return tt.getCharacterTokenType(14);}
"]" {return tt.getCharacterTokenType(15);}
"}" {return tt.getCharacterTokenType(16);}
")" {return tt.getCharacterTokenType(17);}
";" {return tt.getCharacterTokenType(18);}
"/" {return tt.getCharacterTokenType(19);}
"*" {return tt.getCharacterTokenType(20);}
"|" {return tt.getCharacterTokenType(21);}




"varchar2" {return tt.getDataTypeTokenType(0);}
"with"{ws}"time"{ws}"zone" {return tt.getDataTypeTokenType(1);}
"with"{ws}"local"{ws}"time"{ws}"zone" {return tt.getDataTypeTokenType(2);}
"varchar" {return tt.getDataTypeTokenType(3);}
"urowid" {return tt.getDataTypeTokenType(4);}
"to"{ws}"second" {return tt.getDataTypeTokenType(5);}
"to"{ws}"month" {return tt.getDataTypeTokenType(6);}
"timestamp" {return tt.getDataTypeTokenType(7);}
"string" {return tt.getDataTypeTokenType(8);}
"smallint" {return tt.getDataTypeTokenType(9);}
"rowid" {return tt.getDataTypeTokenType(10);}
"real" {return tt.getDataTypeTokenType(11);}
"raw" {return tt.getDataTypeTokenType(12);}
"pls_integer" {return tt.getDataTypeTokenType(13);}
"nvarchar2" {return tt.getDataTypeTokenType(14);}
"numeric" {return tt.getDataTypeTokenType(15);}
"number" {return tt.getDataTypeTokenType(16);}
"nclob" {return tt.getDataTypeTokenType(17);}
"nchar"{ws}"varying" {return tt.getDataTypeTokenType(18);}
"nchar" {return tt.getDataTypeTokenType(19);}
"national"{ws}"character"{ws}"varying" {return tt.getDataTypeTokenType(20);}
"national"{ws}"character" {return tt.getDataTypeTokenType(21);}
"national"{ws}"char"{ws}"varying" {return tt.getDataTypeTokenType(22);}
"national"{ws}"char" {return tt.getDataTypeTokenType(23);}
"long"{ws}"varchar" {return tt.getDataTypeTokenType(24);}
"long"{ws}"raw" {return tt.getDataTypeTokenType(25);}
"long" {return tt.getDataTypeTokenType(26);}
"interval"{ws}"year" {return tt.getDataTypeTokenType(27);}
"interval"{ws}"day" {return tt.getDataTypeTokenType(28);}
"integer" {return tt.getDataTypeTokenType(29);}
"int" {return tt.getDataTypeTokenType(30);}
"float" {return tt.getDataTypeTokenType(31);}
"double"{ws}"precision" {return tt.getDataTypeTokenType(32);}
"decimal" {return tt.getDataTypeTokenType(33);}
"date" {return tt.getDataTypeTokenType(34);}
"clob" {return tt.getDataTypeTokenType(35);}
"character"{ws}"varying" {return tt.getDataTypeTokenType(36);}
"character" {return tt.getDataTypeTokenType(37);}
"char" {return tt.getDataTypeTokenType(38);}
"byte" {return tt.getDataTypeTokenType(39);}
"boolean" {return tt.getDataTypeTokenType(40);}
"blob" {return tt.getDataTypeTokenType(41);}
"binary_integer" {return tt.getDataTypeTokenType(42);}
"binary_float" {return tt.getDataTypeTokenType(43);}
"binary_double" {return tt.getDataTypeTokenType(44);}
"bfile" {return tt.getDataTypeTokenType(45);}




"a"{ws}"set" {return tt.getKeywordTokenType(0);}
"all" {return tt.getKeywordTokenType(1);}
"alter" {return tt.getKeywordTokenType(2);}
"and" {return tt.getKeywordTokenType(3);}
"any" {return tt.getKeywordTokenType(4);}
"array" {return tt.getKeywordTokenType(5);}
"as" {return tt.getKeywordTokenType(6);}
"asc" {return tt.getKeywordTokenType(7);}
"at" {return tt.getKeywordTokenType(8);}
"authid" {return tt.getKeywordTokenType(9);}
"automatic" {return tt.getKeywordTokenType(10);}
"autonomous_transaction" {return tt.getKeywordTokenType(11);}
"begin" {return tt.getKeywordTokenType(12);}
"between" {return tt.getKeywordTokenType(13);}
"block" {return tt.getKeywordTokenType(14);}
"body" {return tt.getKeywordTokenType(15);}
"both" {return tt.getKeywordTokenType(16);}
"bulk" {return tt.getKeywordTokenType(17);}
"bulk_exceptions" {return tt.getKeywordTokenType(18);}
"bulk_rowcount" {return tt.getKeywordTokenType(19);}
"by" {return tt.getKeywordTokenType(20);}
"canonical" {return tt.getKeywordTokenType(21);}
"case" {return tt.getKeywordTokenType(22);}
"char_base" {return tt.getKeywordTokenType(23);}
"char_cs" {return tt.getKeywordTokenType(24);}
"check" {return tt.getKeywordTokenType(25);}
"chisq_df" {return tt.getKeywordTokenType(26);}
"chisq_obs" {return tt.getKeywordTokenType(27);}
"chisq_sig" {return tt.getKeywordTokenType(28);}
"close" {return tt.getKeywordTokenType(29);}
"cluster" {return tt.getKeywordTokenType(30);}
"coalesce" {return tt.getKeywordTokenType(31);}
"coefficient" {return tt.getKeywordTokenType(32);}
"cohens_k" {return tt.getKeywordTokenType(33);}
"collect" {return tt.getKeywordTokenType(34);}
"comment" {return tt.getKeywordTokenType(35);}
"commit" {return tt.getKeywordTokenType(36);}
"committed" {return tt.getKeywordTokenType(37);}
"compatibility" {return tt.getKeywordTokenType(38);}
"compress" {return tt.getKeywordTokenType(39);}
"connect" {return tt.getKeywordTokenType(40);}
"constant" {return tt.getKeywordTokenType(41);}
"constraint" {return tt.getKeywordTokenType(42);}
"cont_coefficient" {return tt.getKeywordTokenType(43);}
"count" {return tt.getKeywordTokenType(44);}
"cramers_v" {return tt.getKeywordTokenType(45);}
"create" {return tt.getKeywordTokenType(46);}
"cross" {return tt.getKeywordTokenType(47);}
"cube" {return tt.getKeywordTokenType(48);}
"current" {return tt.getKeywordTokenType(49);}
"current_user" {return tt.getKeywordTokenType(50);}
"currval" {return tt.getKeywordTokenType(51);}
"cursor" {return tt.getKeywordTokenType(52);}
"day" {return tt.getKeywordTokenType(53);}
"declare" {return tt.getKeywordTokenType(54);}
"decrement" {return tt.getKeywordTokenType(55);}
"default" {return tt.getKeywordTokenType(56);}
"definer" {return tt.getKeywordTokenType(57);}
"delete" {return tt.getKeywordTokenType(58);}
"dense_rank" {return tt.getKeywordTokenType(59);}
"desc" {return tt.getKeywordTokenType(60);}
"deterministic" {return tt.getKeywordTokenType(61);}
"df" {return tt.getKeywordTokenType(62);}
"df_between" {return tt.getKeywordTokenType(63);}
"df_den" {return tt.getKeywordTokenType(64);}
"df_num" {return tt.getKeywordTokenType(65);}
"df_within" {return tt.getKeywordTokenType(66);}
"dimension" {return tt.getKeywordTokenType(67);}
"distinct" {return tt.getKeywordTokenType(68);}
"do" {return tt.getKeywordTokenType(69);}
"drop" {return tt.getKeywordTokenType(70);}
"else" {return tt.getKeywordTokenType(71);}
"elsif" {return tt.getKeywordTokenType(72);}
"empty" {return tt.getKeywordTokenType(73);}
"end" {return tt.getKeywordTokenType(74);}
"equals_path" {return tt.getKeywordTokenType(75);}
"error_code" {return tt.getKeywordTokenType(76);}
"error_index" {return tt.getKeywordTokenType(77);}
"escape" {return tt.getKeywordTokenType(78);}
"exact_prob" {return tt.getKeywordTokenType(79);}
"exception" {return tt.getKeywordTokenType(80);}
"exception_init" {return tt.getKeywordTokenType(81);}
"exclusive" {return tt.getKeywordTokenType(82);}
"execute" {return tt.getKeywordTokenType(83);}
"exists" {return tt.getKeywordTokenType(84);}
"exit" {return tt.getKeywordTokenType(85);}
"extend" {return tt.getKeywordTokenType(86);}
"extends" {return tt.getKeywordTokenType(87);}
"f_ratio" {return tt.getKeywordTokenType(88);}
"fetch" {return tt.getKeywordTokenType(89);}
"first" {return tt.getKeywordTokenType(90);}
"following" {return tt.getKeywordTokenType(91);}
"for" {return tt.getKeywordTokenType(92);}
"forall" {return tt.getKeywordTokenType(93);}
"found" {return tt.getKeywordTokenType(94);}
"from" {return tt.getKeywordTokenType(95);}
"full" {return tt.getKeywordTokenType(96);}
"function" {return tt.getKeywordTokenType(97);}
"goto" {return tt.getKeywordTokenType(98);}
"group" {return tt.getKeywordTokenType(99);}
"having" {return tt.getKeywordTokenType(100);}
"heap" {return tt.getKeywordTokenType(101);}
"hour" {return tt.getKeywordTokenType(102);}
"if" {return tt.getKeywordTokenType(103);}
"ignore" {return tt.getKeywordTokenType(104);}
"immediate" {return tt.getKeywordTokenType(105);}
"in" {return tt.getKeywordTokenType(106);}
"in"{ws}"out" {return tt.getKeywordTokenType(107);}
"increment" {return tt.getKeywordTokenType(108);}
"index" {return tt.getKeywordTokenType(109);}
"indicator" {return tt.getKeywordTokenType(110);}
"infinite" {return tt.getKeywordTokenType(111);}
"inner" {return tt.getKeywordTokenType(112);}
"insert" {return tt.getKeywordTokenType(113);}
"interface" {return tt.getKeywordTokenType(114);}
"intersect" {return tt.getKeywordTokenType(115);}
"interval" {return tt.getKeywordTokenType(116);}
"into" {return tt.getKeywordTokenType(117);}
"is" {return tt.getKeywordTokenType(118);}
"isolation" {return tt.getKeywordTokenType(119);}
"isopen" {return tt.getKeywordTokenType(120);}
"iterate" {return tt.getKeywordTokenType(121);}
"java" {return tt.getKeywordTokenType(122);}
"join" {return tt.getKeywordTokenType(123);}
"keep" {return tt.getKeywordTokenType(124);}
"last" {return tt.getKeywordTokenType(125);}
"leading" {return tt.getKeywordTokenType(126);}
"left" {return tt.getKeywordTokenType(127);}
"level" {return tt.getKeywordTokenType(128);}
"like" {return tt.getKeywordTokenType(129);}
"like2" {return tt.getKeywordTokenType(130);}
"like4" {return tt.getKeywordTokenType(131);}
"likec" {return tt.getKeywordTokenType(132);}
"limit" {return tt.getKeywordTokenType(133);}
"limited" {return tt.getKeywordTokenType(134);}
"lock" {return tt.getKeywordTokenType(135);}
"loop" {return tt.getKeywordTokenType(136);}
"main" {return tt.getKeywordTokenType(137);}
"maxvalue" {return tt.getKeywordTokenType(138);}
"mean_squares_between" {return tt.getKeywordTokenType(139);}
"mean_squares_within" {return tt.getKeywordTokenType(140);}
"measures" {return tt.getKeywordTokenType(141);}
"member" {return tt.getKeywordTokenType(142);}
"minus" {return tt.getKeywordTokenType(143);}
"minute" {return tt.getKeywordTokenType(144);}
"minvalue" {return tt.getKeywordTokenType(145);}
"mlslabel" {return tt.getKeywordTokenType(146);}
"mode" {return tt.getKeywordTokenType(147);}
"model" {return tt.getKeywordTokenType(148);}
"month" {return tt.getKeywordTokenType(149);}
"multiset" {return tt.getKeywordTokenType(150);}
"name" {return tt.getKeywordTokenType(151);}
"nan" {return tt.getKeywordTokenType(152);}
"natural" {return tt.getKeywordTokenType(153);}
"naturaln" {return tt.getKeywordTokenType(154);}
"nav" {return tt.getKeywordTokenType(155);}
"nchar_cs" {return tt.getKeywordTokenType(156);}
"new" {return tt.getKeywordTokenType(157);}
"next" {return tt.getKeywordTokenType(158);}
"nextval" {return tt.getKeywordTokenType(159);}
"nocopy" {return tt.getKeywordTokenType(160);}
"nocycle" {return tt.getKeywordTokenType(161);}
"not" {return tt.getKeywordTokenType(162);}
"notfound" {return tt.getKeywordTokenType(163);}
"nowait" {return tt.getKeywordTokenType(164);}
"null" {return tt.getKeywordTokenType(165);}
"nulls" {return tt.getKeywordTokenType(166);}
"number_base" {return tt.getKeywordTokenType(167);}
"ocirowid" {return tt.getKeywordTokenType(168);}
"of" {return tt.getKeywordTokenType(169);}
"on" {return tt.getKeywordTokenType(170);}
"one_sided_prob_or_less" {return tt.getKeywordTokenType(171);}
"one_sided_prob_or_more" {return tt.getKeywordTokenType(172);}
"one_sided_sig" {return tt.getKeywordTokenType(173);}
"only" {return tt.getKeywordTokenType(174);}
"opaque" {return tt.getKeywordTokenType(175);}
"open" {return tt.getKeywordTokenType(176);}
"operator" {return tt.getKeywordTokenType(177);}
"option" {return tt.getKeywordTokenType(178);}
"or" {return tt.getKeywordTokenType(179);}
"order" {return tt.getKeywordTokenType(180);}
"organization" {return tt.getKeywordTokenType(181);}
"others" {return tt.getKeywordTokenType(182);}
"out" {return tt.getKeywordTokenType(183);}
"outer" {return tt.getKeywordTokenType(184);}
"over" {return tt.getKeywordTokenType(185);}
"package" {return tt.getKeywordTokenType(186);}
"parallel_enable" {return tt.getKeywordTokenType(187);}
"partition" {return tt.getKeywordTokenType(188);}
"pctfree" {return tt.getKeywordTokenType(189);}
"phi_coefficient" {return tt.getKeywordTokenType(190);}
"positive" {return tt.getKeywordTokenType(191);}
"positiven" {return tt.getKeywordTokenType(192);}
"pragma" {return tt.getKeywordTokenType(193);}
"preceding" {return tt.getKeywordTokenType(194);}
"present" {return tt.getKeywordTokenType(195);}
"prior" {return tt.getKeywordTokenType(196);}
"private" {return tt.getKeywordTokenType(197);}
"procedure" {return tt.getKeywordTokenType(198);}
"public" {return tt.getKeywordTokenType(199);}
"raise" {return tt.getKeywordTokenType(200);}
"range" {return tt.getKeywordTokenType(201);}
"read" {return tt.getKeywordTokenType(202);}
"record" {return tt.getKeywordTokenType(203);}
"ref" {return tt.getKeywordTokenType(204);}
"reference" {return tt.getKeywordTokenType(205);}
"regexp_like" {return tt.getKeywordTokenType(206);}
"release" {return tt.getKeywordTokenType(207);}
"replace" {return tt.getKeywordTokenType(208);}
"restrict_references" {return tt.getKeywordTokenType(209);}
"return" {return tt.getKeywordTokenType(210);}
"returning" {return tt.getKeywordTokenType(211);}
"reverse" {return tt.getKeywordTokenType(212);}
"right" {return tt.getKeywordTokenType(213);}
"rnds" {return tt.getKeywordTokenType(214);}
"rnps" {return tt.getKeywordTokenType(215);}
"rollback" {return tt.getKeywordTokenType(216);}
"rollup" {return tt.getKeywordTokenType(217);}
"row" {return tt.getKeywordTokenType(218);}
"rowcount" {return tt.getKeywordTokenType(219);}
"rownum" {return tt.getKeywordTokenType(220);}
"rows" {return tt.getKeywordTokenType(221);}
"rowtype" {return tt.getKeywordTokenType(222);}
"rules" {return tt.getKeywordTokenType(223);}
"sample" {return tt.getKeywordTokenType(224);}
"savepoint" {return tt.getKeywordTokenType(225);}
"scn" {return tt.getKeywordTokenType(226);}
"second" {return tt.getKeywordTokenType(227);}
"seed" {return tt.getKeywordTokenType(228);}
"segment" {return tt.getKeywordTokenType(229);}
"select" {return tt.getKeywordTokenType(230);}
"separate" {return tt.getKeywordTokenType(231);}
"sequential" {return tt.getKeywordTokenType(232);}
"serializable" {return tt.getKeywordTokenType(233);}
"serially_reusable" {return tt.getKeywordTokenType(234);}
"set" {return tt.getKeywordTokenType(235);}
"sets" {return tt.getKeywordTokenType(236);}
"share" {return tt.getKeywordTokenType(237);}
"siblings" {return tt.getKeywordTokenType(238);}
"sig" {return tt.getKeywordTokenType(239);}
"single" {return tt.getKeywordTokenType(240);}
"some" {return tt.getKeywordTokenType(241);}
"space" {return tt.getKeywordTokenType(242);}
"sql" {return tt.getKeywordTokenType(243);}
"sqlcode" {return tt.getKeywordTokenType(244);}
"sqlerrm" {return tt.getKeywordTokenType(245);}
"start" {return tt.getKeywordTokenType(246);}
"statistic" {return tt.getKeywordTokenType(247);}
"submultiset" {return tt.getKeywordTokenType(248);}
"subpartition" {return tt.getKeywordTokenType(249);}
"subtype" {return tt.getKeywordTokenType(250);}
"successful" {return tt.getKeywordTokenType(251);}
"sum_squares_between" {return tt.getKeywordTokenType(252);}
"sum_squares_within" {return tt.getKeywordTokenType(253);}
"synonym" {return tt.getKeywordTokenType(254);}
"table" {return tt.getKeywordTokenType(255);}
"then" {return tt.getKeywordTokenType(256);}
"time" {return tt.getKeywordTokenType(257);}
"timezone_abbr" {return tt.getKeywordTokenType(258);}
"timezone_hour" {return tt.getKeywordTokenType(259);}
"timezone_minute" {return tt.getKeywordTokenType(260);}
"timezone_region" {return tt.getKeywordTokenType(261);}
"to" {return tt.getKeywordTokenType(262);}
"trailing" {return tt.getKeywordTokenType(263);}
"transaction" {return tt.getKeywordTokenType(264);}
"trigger" {return tt.getKeywordTokenType(265);}
"trust" {return tt.getKeywordTokenType(266);}
"two_sided_prob" {return tt.getKeywordTokenType(267);}
"two_sided_sig" {return tt.getKeywordTokenType(268);}
"type" {return tt.getKeywordTokenType(269);}
"u_statistic" {return tt.getKeywordTokenType(270);}
"unbounded" {return tt.getKeywordTokenType(271);}
"under_path" {return tt.getKeywordTokenType(272);}
"union" {return tt.getKeywordTokenType(273);}
"unique" {return tt.getKeywordTokenType(274);}
"until" {return tt.getKeywordTokenType(275);}
"update" {return tt.getKeywordTokenType(276);}
"updated" {return tt.getKeywordTokenType(277);}
"upsert" {return tt.getKeywordTokenType(278);}
"use" {return tt.getKeywordTokenType(279);}
"user" {return tt.getKeywordTokenType(280);}
"using" {return tt.getKeywordTokenType(281);}
"validate" {return tt.getKeywordTokenType(282);}
"values" {return tt.getKeywordTokenType(283);}
"varray" {return tt.getKeywordTokenType(284);}
"varying" {return tt.getKeywordTokenType(285);}
"versions" {return tt.getKeywordTokenType(286);}
"view" {return tt.getKeywordTokenType(287);}
"wait" {return tt.getKeywordTokenType(288);}
"when" {return tt.getKeywordTokenType(289);}
"whenever" {return tt.getKeywordTokenType(290);}
"where" {return tt.getKeywordTokenType(291);}
"while" {return tt.getKeywordTokenType(292);}
"with" {return tt.getKeywordTokenType(293);}
"within" {return tt.getKeywordTokenType(294);}
"wnds" {return tt.getKeywordTokenType(295);}
"wnps" {return tt.getKeywordTokenType(296);}
"work" {return tt.getKeywordTokenType(297);}
"write" {return tt.getKeywordTokenType(298);}
"year" {return tt.getKeywordTokenType(299);}
"zone" {return tt.getKeywordTokenType(300);}
"false" {return tt.getKeywordTokenType(301);}
"true" {return tt.getKeywordTokenType(302);}





"abs" {return tt.getFunctionTokenType(0);}
"acos" {return tt.getFunctionTokenType(1);}
"add_months" {return tt.getFunctionTokenType(2);}
"ascii" {return tt.getFunctionTokenType(3);}
"asciistr" {return tt.getFunctionTokenType(4);}
"asin" {return tt.getFunctionTokenType(5);}
"atan" {return tt.getFunctionTokenType(6);}
"atan2" {return tt.getFunctionTokenType(7);}
"avg" {return tt.getFunctionTokenType(8);}
"bfilename" {return tt.getFunctionTokenType(9);}
"bin_to_num" {return tt.getFunctionTokenType(10);}
"bitand" {return tt.getFunctionTokenType(11);}
"cardinality" {return tt.getFunctionTokenType(12);}
"cast" {return tt.getFunctionTokenType(13);}
"ceil" {return tt.getFunctionTokenType(14);}
"chartorowid" {return tt.getFunctionTokenType(15);}
"chr" {return tt.getFunctionTokenType(16);}
"coalesce"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(17);}
"collect"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(18);}
"compose" {return tt.getFunctionTokenType(19);}
"concat" {return tt.getFunctionTokenType(20);}
"convert" {return tt.getFunctionTokenType(21);}
"corr" {return tt.getFunctionTokenType(22);}
"corr_k" {return tt.getFunctionTokenType(23);}
"corr_s" {return tt.getFunctionTokenType(24);}
"cos" {return tt.getFunctionTokenType(25);}
"cosh" {return tt.getFunctionTokenType(26);}
"count"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(27);}
"covar_pop" {return tt.getFunctionTokenType(28);}
"covar_samp" {return tt.getFunctionTokenType(29);}
"cume_dist" {return tt.getFunctionTokenType(30);}
"current_date" {return tt.getFunctionTokenType(31);}
"current_timestamp" {return tt.getFunctionTokenType(32);}
"cv" {return tt.getFunctionTokenType(33);}
"dbtimezone" {return tt.getFunctionTokenType(34);}
"dbtmezone" {return tt.getFunctionTokenType(35);}
"decode" {return tt.getFunctionTokenType(36);}
"decompose" {return tt.getFunctionTokenType(37);}
"depth" {return tt.getFunctionTokenType(38);}
"deref" {return tt.getFunctionTokenType(39);}
"dump"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(40);}
"empty_blob" {return tt.getFunctionTokenType(41);}
"empty_clob" {return tt.getFunctionTokenType(42);}
"existsnode" {return tt.getFunctionTokenType(43);}
"exp" {return tt.getFunctionTokenType(44);}
"extract" {return tt.getFunctionTokenType(45);}
"extractvalue" {return tt.getFunctionTokenType(46);}
"first"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(47);}
"first_value" {return tt.getFunctionTokenType(48);}
"floor" {return tt.getFunctionTokenType(49);}
"from_tz" {return tt.getFunctionTokenType(50);}
"greatest" {return tt.getFunctionTokenType(51);}
"group_id" {return tt.getFunctionTokenType(52);}
"grouping" {return tt.getFunctionTokenType(53);}
"grouping_id" {return tt.getFunctionTokenType(54);}
"hextoraw" {return tt.getFunctionTokenType(55);}
"initcap" {return tt.getFunctionTokenType(56);}
"instr" {return tt.getFunctionTokenType(57);}
"instr2" {return tt.getFunctionTokenType(58);}
"instr4" {return tt.getFunctionTokenType(59);}
"instrb" {return tt.getFunctionTokenType(60);}
"instrc" {return tt.getFunctionTokenType(61);}
"iteration_number" {return tt.getFunctionTokenType(62);}
"lag" {return tt.getFunctionTokenType(63);}
"last"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(64);}
"last_day" {return tt.getFunctionTokenType(65);}
"last_value" {return tt.getFunctionTokenType(66);}
"lead" {return tt.getFunctionTokenType(67);}
"least" {return tt.getFunctionTokenType(68);}
"length" {return tt.getFunctionTokenType(69);}
"length2" {return tt.getFunctionTokenType(70);}
"length4" {return tt.getFunctionTokenType(71);}
"lengthb" {return tt.getFunctionTokenType(72);}
"lengthc" {return tt.getFunctionTokenType(73);}
"ln" {return tt.getFunctionTokenType(74);}
"lnnvl" {return tt.getFunctionTokenType(75);}
"localtimestamp" {return tt.getFunctionTokenType(76);}
"log"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(77);}
"lower" {return tt.getFunctionTokenType(78);}
"lpad" {return tt.getFunctionTokenType(79);}
"ltrim" {return tt.getFunctionTokenType(80);}
"make_ref" {return tt.getFunctionTokenType(81);}
"max" {return tt.getFunctionTokenType(82);}
"median" {return tt.getFunctionTokenType(83);}
"min" {return tt.getFunctionTokenType(84);}
"mod" {return tt.getFunctionTokenType(85);}
"months_between" {return tt.getFunctionTokenType(86);}
"nanvl" {return tt.getFunctionTokenType(87);}
"nchr" {return tt.getFunctionTokenType(88);}
"new_time" {return tt.getFunctionTokenType(89);}
"next_day" {return tt.getFunctionTokenType(90);}
"nls_charset_decl_len" {return tt.getFunctionTokenType(91);}
"nls_charset_id" {return tt.getFunctionTokenType(92);}
"nls_charset_name" {return tt.getFunctionTokenType(93);}
"nls_initcap" {return tt.getFunctionTokenType(94);}
"nls_lower" {return tt.getFunctionTokenType(95);}
"nls_upper" {return tt.getFunctionTokenType(96);}
"nlssort" {return tt.getFunctionTokenType(97);}
"ntile" {return tt.getFunctionTokenType(98);}
"nullif" {return tt.getFunctionTokenType(99);}
"numtodsinterval" {return tt.getFunctionTokenType(100);}
"numtoyminterval" {return tt.getFunctionTokenType(101);}
"nvl" {return tt.getFunctionTokenType(102);}
"nvl2" {return tt.getFunctionTokenType(103);}
"ora_hash" {return tt.getFunctionTokenType(104);}
"path" {return tt.getFunctionTokenType(105);}
"percent_rank" {return tt.getFunctionTokenType(106);}
"percentile_cont" {return tt.getFunctionTokenType(107);}
"percentile_disc" {return tt.getFunctionTokenType(108);}
"power"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(109);}
"powermultiset" {return tt.getFunctionTokenType(110);}
"powermultiset_by_cardinality" {return tt.getFunctionTokenType(111);}
"presentnnv" {return tt.getFunctionTokenType(112);}
"presentv" {return tt.getFunctionTokenType(113);}
"previous" {return tt.getFunctionTokenType(114);}
"rank"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(115);}
"ratio_to_report" {return tt.getFunctionTokenType(116);}
"rawtohex" {return tt.getFunctionTokenType(117);}
"rawtonhex" {return tt.getFunctionTokenType(118);}
"ref"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(119);}
"reftohex" {return tt.getFunctionTokenType(120);}
"regexp_instr" {return tt.getFunctionTokenType(121);}
"regexp_replace" {return tt.getFunctionTokenType(122);}
"regexp_substr" {return tt.getFunctionTokenType(123);}
"regr_avgx" {return tt.getFunctionTokenType(124);}
"regr_avgy" {return tt.getFunctionTokenType(125);}
"regr_count" {return tt.getFunctionTokenType(126);}
"regr_intercept" {return tt.getFunctionTokenType(127);}
"regr_r2" {return tt.getFunctionTokenType(128);}
"regr_slope" {return tt.getFunctionTokenType(129);}
"regr_sxx" {return tt.getFunctionTokenType(130);}
"regr_sxy" {return tt.getFunctionTokenType(131);}
"regr_syy" {return tt.getFunctionTokenType(132);}
"remainder"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(133);}
"replace"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(134);}
"round" {return tt.getFunctionTokenType(135);}
"row_number" {return tt.getFunctionTokenType(136);}
"rowidtochar" {return tt.getFunctionTokenType(137);}
"rowidtonchar" {return tt.getFunctionTokenType(138);}
"rpad" {return tt.getFunctionTokenType(139);}
"rtrim" {return tt.getFunctionTokenType(140);}
"scn_to_timestamp" {return tt.getFunctionTokenType(141);}
"sessiontimezone" {return tt.getFunctionTokenType(142);}
"set"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(143);}
"sign" {return tt.getFunctionTokenType(144);}
"sin" {return tt.getFunctionTokenType(145);}
"sinh" {return tt.getFunctionTokenType(146);}
"soundex" {return tt.getFunctionTokenType(147);}
"sqrt" {return tt.getFunctionTokenType(148);}
"stats_binomial_test" {return tt.getFunctionTokenType(149);}
"stats_crosstab" {return tt.getFunctionTokenType(150);}
"stats_f_test" {return tt.getFunctionTokenType(151);}
"stats_ks_test" {return tt.getFunctionTokenType(152);}
"stats_mode" {return tt.getFunctionTokenType(153);}
"stats_mw_test" {return tt.getFunctionTokenType(154);}
"stats_one_way_anova" {return tt.getFunctionTokenType(155);}
"stats_t_test_indep" {return tt.getFunctionTokenType(156);}
"stats_t_test_indepu" {return tt.getFunctionTokenType(157);}
"stats_t_test_one" {return tt.getFunctionTokenType(158);}
"stats_t_test_paired" {return tt.getFunctionTokenType(159);}
"stats_wsr_test" {return tt.getFunctionTokenType(160);}
"stddev" {return tt.getFunctionTokenType(161);}
"stddev_pop" {return tt.getFunctionTokenType(162);}
"stddev_samp" {return tt.getFunctionTokenType(163);}
"substr" {return tt.getFunctionTokenType(164);}
"substr2" {return tt.getFunctionTokenType(165);}
"substr4" {return tt.getFunctionTokenType(166);}
"substrb" {return tt.getFunctionTokenType(167);}
"substrc" {return tt.getFunctionTokenType(168);}
"sum" {return tt.getFunctionTokenType(169);}
"sys_connect_by_path" {return tt.getFunctionTokenType(170);}
"sys_context" {return tt.getFunctionTokenType(171);}
"sys_dburigen" {return tt.getFunctionTokenType(172);}
"sys_extract_utc" {return tt.getFunctionTokenType(173);}
"sys_guid" {return tt.getFunctionTokenType(174);}
"sys_typeid" {return tt.getFunctionTokenType(175);}
"sys_xmlagg" {return tt.getFunctionTokenType(176);}
"sys_xmlgen" {return tt.getFunctionTokenType(177);}
"sysdate" {return tt.getFunctionTokenType(178);}
"systimestamp" {return tt.getFunctionTokenType(179);}
"tan" {return tt.getFunctionTokenType(180);}
"tanh" {return tt.getFunctionTokenType(181);}
"timestamp_to_scn" {return tt.getFunctionTokenType(182);}
"to_binary_double" {return tt.getFunctionTokenType(183);}
"to_binary_float" {return tt.getFunctionTokenType(184);}
"to_char" {return tt.getFunctionTokenType(185);}
"to_clob" {return tt.getFunctionTokenType(186);}
"to_date" {return tt.getFunctionTokenType(187);}
"to_dsinterval" {return tt.getFunctionTokenType(188);}
"to_lob" {return tt.getFunctionTokenType(189);}
"to_multi_byte" {return tt.getFunctionTokenType(190);}
"to_nchar" {return tt.getFunctionTokenType(191);}
"to_nclob" {return tt.getFunctionTokenType(192);}
"to_number" {return tt.getFunctionTokenType(193);}
"to_single_byte" {return tt.getFunctionTokenType(194);}
"to_timestamp" {return tt.getFunctionTokenType(195);}
"to_timestamp_tz" {return tt.getFunctionTokenType(196);}
"to_yminterval" {return tt.getFunctionTokenType(197);}
"translate" {return tt.getFunctionTokenType(198);}
"treat" {return tt.getFunctionTokenType(199);}
"trim" {return tt.getFunctionTokenType(200);}
"trunc" {return tt.getFunctionTokenType(201);}
"tz_offset" {return tt.getFunctionTokenType(202);}
"uid" {return tt.getFunctionTokenType(203);}
"unistr" {return tt.getFunctionTokenType(204);}
"updatexml" {return tt.getFunctionTokenType(205);}
"upper" {return tt.getFunctionTokenType(206);}
"user"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(207);}
"userenv" {return tt.getFunctionTokenType(208);}
"value"{wso}"(" { yybegin(YYINITIAL); yypushback(1); return tt.getFunctionTokenType(209);}
"var_pop" {return tt.getFunctionTokenType(210);}
"var_samp" {return tt.getFunctionTokenType(211);}
"variance" {return tt.getFunctionTokenType(212);}
"vsize" {return tt.getFunctionTokenType(213);}
"width_bucket" {return tt.getFunctionTokenType(214);}
"xmlagg" {return tt.getFunctionTokenType(215);}
"xmlcolattval" {return tt.getFunctionTokenType(216);}
"xmlconcat" {return tt.getFunctionTokenType(217);}
"xmlforest" {return tt.getFunctionTokenType(218);}
"xmlsequence" {return tt.getFunctionTokenType(219);}
"xmltransform" {return tt.getFunctionTokenType(220);}







"access_into_null" {return tt.getExceptionTokenType(0);}
"case_not_found" {return tt.getExceptionTokenType(1);}
"collection_is_null" {return tt.getExceptionTokenType(2);}
"cursor_already_open" {return tt.getExceptionTokenType(3);}
"dup_val_on_index" {return tt.getExceptionTokenType(4);}
"invalid_cursor" {return tt.getExceptionTokenType(5);}
"invalid_number" {return tt.getExceptionTokenType(6);}
"login_denied" {return tt.getExceptionTokenType(7);}
"no_data_found" {return tt.getExceptionTokenType(8);}
"not_logged_on" {return tt.getExceptionTokenType(9);}
"program_error" {return tt.getExceptionTokenType(10);}
"rowtype_mismatch" {return tt.getExceptionTokenType(11);}
"self_is_null" {return tt.getExceptionTokenType(12);}
"storage_error" {return tt.getExceptionTokenType(13);}
"subscript_beyond_count" {return tt.getExceptionTokenType(14);}
"subscript_outside_limit" {return tt.getExceptionTokenType(15);}
"sys_invalid_rowid" {return tt.getExceptionTokenType(16);}
"timeout_on_resource" {return tt.getExceptionTokenType(17);}
"too_many_rows" {return tt.getExceptionTokenType(18);}
"value_error" {return tt.getExceptionTokenType(19);}
"zero_divide" {return tt.getExceptionTokenType(20);}



{IDENTIFIER}           { yybegin(YYINITIAL); return tt.getSharedTokenTypes().getIdentifier(); }
{QUOTED_IDENTIFIER}    { yybegin(YYINITIAL); return tt.getSharedTokenTypes().getQuotedIdentifier(); }

<YYINITIAL> {
    .                  { yybegin(YYINITIAL); return tt.getSharedTokenTypes().getIdentifier(); }
}
