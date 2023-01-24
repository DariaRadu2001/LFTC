import java.util.*;
%%
%public
%class SymTabs
%standalone
%line

S = []
L = [\n]
LETTER = [a-zA-Z]
DIGIT = [0-9]
DIGITWZERO = [1-9]
NUMBER = 0|{DIGITWZERO}{DIGIT}*
IDENTIFIER = [a-zA-Z][a-zA-Z0-9]*
STRING = \"([^\\\"]|\\.)*\"
KEYWORDS = "import"|"if"|"else"|"public"|"class"|"static"|"void"|"int"|"while"|"new"|"private"|"return"|"true"|"this"|"break"
OPERATORS = ("+"|"-"|"*"|"/"|"="|"<"|">")
SEPARATORS = ("["|"]"|"("|")"|"{"|"}"|";"|","|"."|":")
COMMENT = ("//".*)

%{
    List<String> numbers = new ArrayList<>();
    List<String> literals = new ArrayList<>();
    List<String> identifiers = new ArrayList<>();
    List<String> keywords = new ArrayList<>();
    int ctTokens = 0;
    int ctIdentifiers = 0;
    int ctKeywords = 0;
    int ctLiterals = 0;
    int ctOperators = 0;
    int ctSeparators = 0;
    int lineNumber = 1;
%}

%eof{
        System.out.println(" Tokens < " + ctTokens + " >");
        System.out.println(" Identifiers < " + ctIdentifiers + " >");
        System.out.println(" Literals < " + ctLiterals + " >");
        System.out.println(" Keywords < " + ctKeywords + " >");
        System.out.println(" Operators < "  + ctOperators + " >");
        System.out.println(" Separators < " + ctSeparators + " >");
        System.out.println("Identifiers:");
        Set<String> set = new HashSet<>(identifiers);
        identifiers.clear();
        identifiers.addAll(set);
        Collections.sort(identifiers);
        for (String identifier : identifiers)
            System.out.println("\t" + identifier);
        System.out.println("Literals:");
        set = new HashSet<>(literals);
        literals.clear();
        literals.addAll(set);
        Collections.sort(literals);
        for (String literal : literals)
                System.out.println("\t" + literal);
%eof}

%%
{COMMENT} {}
{STRING} {ctLiterals++; ctTokens++; literals.add(yytext() + " (String)");}
{NUMBER} {ctLiterals++; ctTokens++; literals.add(yytext() + " (Integer)");}
{KEYWORDS} {ctKeywords++; ctTokens++;}
{IDENTIFIER} {ctIdentifiers++; ctTokens++; identifiers.add(yytext());}
{OPERATORS} {ctOperators++; ctTokens++;}
{SEPARATORS} {ctSeparators++; ctTokens++;}
[\n\s]* {}
. {System.out.println("\n\nError: Not allowed character " + yytext() + " in line " + String.valueOf(yyline + 1) + ", column " + yycolumn);}
