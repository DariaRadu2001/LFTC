import java.io.*;
import java.util.*;

public class CompleteParser {
    public enum TokenType {

        Keyword('W'),
        Operator('T'),
        Separator('E'),
        NumberLiteral('N'),
        StringLiteral('S'),
        Identifier('I');

        TokenType(char code) {
            this.code = code;
        }

        private final char code;

        public char getValue() {
            return code;
        }
    }

    public enum Token {
        PRIVATE("private", 100, TokenType.Keyword),
        FALSE("false", 101, TokenType.Keyword),
        TRUE("true", 102, TokenType.Keyword),
        CLASS("class", 103, TokenType.Keyword),
        PUBLIC("public", 104, TokenType.Keyword),
        STATIC("static", 105, TokenType.Keyword),
        THIS("this", 106, TokenType.Keyword),
        IF("if", 107, TokenType.Keyword),
        WHILE("while", 108, TokenType.Keyword),
        NEW("new", 109, TokenType.Keyword),
        ELSE("else", 110, TokenType.Keyword),
        VOID("void", 111, TokenType.Keyword),
        BOOLEAN("boolean", 112, TokenType.Keyword),
        INT("int", 113, TokenType.Keyword),
        OPEN_ACCOLADE ("{", 201, TokenType.Separator),
        CLOSED_ACCOLADE("}", 202, TokenType.Separator),
        OPEN_SQUARE("[", 203, TokenType.Separator),
        CLOSED_SQUARE("]",204, TokenType.Separator),
        OPEN_BRACKET("(", 205, TokenType.Separator),
        CLOSED_BRACKET(")",206, TokenType.Separator),
        DOT(".", 207, TokenType.Separator),
        COMMA(",", 208, TokenType.Separator),
        COLON(":", 209, TokenType.Separator),
        SEMICOLON(";",210, TokenType.Separator),
        NEGATION("!", 301, TokenType.Operator),
        CONJUNCTION("&&",302, TokenType.Operator),
        SMALLER("<",303, TokenType.Operator),
        EQUALS("=",304, TokenType.Operator),
        PLUS("+",  305, TokenType.Operator),
        MINUS("-", 306, TokenType.Operator),
        STAR("*",  307, TokenType.Operator),
        SLASH("/", 308, TokenType.Operator);
        Token(String token, int id, TokenType type) {
            this.token = token;
            this.id = id;
            this.type = type;
        }

        private final String token;
        private final int id;
        private final TokenType type;

        public String getValueToken() {
            return token;
        }

        public int getValueId() {
            return id;
        }

        public TokenType getValueType() {
            return type;
        }
    }

    public enum State {
        S0, S1, S2, S3, S4, S5, S6, S7, S8, INVALID
    }

    static class ValidToken {
        String token;
        TokenType type;

        public ValidToken(String token, TokenType type) {
            this.token = token;
            this.type = type;
        }
    }

    static class PIF {
        char typeCode;
        int lineNumber;
        String tokenId;
        String token;

        public PIF(char typeCode, int lineNumber, String tokenId, String token) {
            this.typeCode = typeCode;
            this.lineNumber = lineNumber;
            this.tokenId = tokenId;
            this.token = token;
        }

        @Override
        public String toString() {
            return "PIF{" +
                    "typeCode=" + typeCode +
                    ", lineNumber=" + lineNumber +
                    ", tokenId='" + tokenId + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    static class Entry {
        String name;
        String kind;
        String type;
        String lineOfDeclaration;
        String accessModifier;
        String scope;

        public Entry(String name, String kind, String type, String lineOfDeclaration, String accessModifier, String scope) {
            this.name = name;
            this.kind = kind;
            this.type = type;
            this.lineOfDeclaration = lineOfDeclaration;
            this.accessModifier = accessModifier;
            this.scope = scope;
        }
    }

    static State state;
    static List<Integer> digits = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    static List<Character> letters = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    static List<String> keywords = new ArrayList<>(Arrays.asList("class", "public", "private", "static", "int", "boolean", "void", "if", "else", "while", "true", "false", "this", "new", "import", "return", "break"));
    static List<ValidToken> tokens = new ArrayList<>();
    static int position = -1;
    static List<Character> specialCharacters = new ArrayList<>(Arrays.asList('{', '}', '[', ']', '(', ')', '.', ',', ':', ';', '!', '&', '<', '=', '+', '-', '*', '/', '!', '>'));
    static List<Character> otherCharsInString = new ArrayList<>(Arrays.asList(':', ',', '%', '-', '\\', '_'));
    static int counter = -1;
    static List<PIF> pifTable = new ArrayList<>();
    static int invalidLine = -1;
    static PIF invalidToken;
    static int nextLine = 0;
    static List<Entry> entries = new ArrayList<>();
    static String dataType;
    static String classScope;
    static String methodScope = "";

    public static void transitionFromS0(char character) {
        if (character == '=' | character == '<' | character == '+' | character == '-' | character == '*' | character == '/' | character == '!' | character == '>')
            state = State.S1;
        else if (character == '&')
            state = State.S2;
        else if (character == '{' | character == '}' | character == ';' | character == '(' | character == ')' | character == '[' | character == ']' | character == ',' | character == ':' | character == '.' | character == '@')
            state = State.S3;
        else if (character == '0')
            state = State.S4;
        else if (digits.contains(Character.getNumericValue(character)))
            state = State.S5;
        else if (character == '"')
            state = State.S6;
        else if (letters.contains(character))
            state = State.S8;
        else
            state = State.INVALID;
    }

    public static void transitionFromS2(char character) {
        if (character == '&')
            state = State.S1;
        else
            state = State.INVALID;
    }

    public static void transitionFromS5(char character) {
        if (digits.contains(Character.getNumericValue(character)))
            state = State.S5;
        else
            state = State.INVALID;
    }

    public static void transitionFromS6(char character) {
        if (letters.contains(character) | digits.contains(Character.getNumericValue(character)) | character == ' ' | otherCharsInString.contains(character))
            state = State.S6;
        else if (character == '"')
            state = State.S7;
        else
            state = State.INVALID;
    }

    public static void transitionFromS8(char character) {
        if (letters.contains(character) | digits.contains(Character.getNumericValue(character)) | character == '_')
            state = State.S8;
        else
            state = State.INVALID;
    }

    public static boolean checkInvalidChar(char c) {
        return !digits.contains(Character.getNumericValue(c)) & !letters.contains(c) & !specialCharacters.contains(c) & !otherCharsInString.contains(c) & c != ' ' & c != '\"';
    }

    public static void nextToken(String word) throws Exception {
        int i = 0;
        state = State.S0;
        int spaces = 0;
        if (word.length() > 1)
            if (word.charAt(0) == ' ') {
                while (word.charAt(spaces) == ' ')
                    spaces++;
                position = position + spaces;
            }
        word = word.trim();
        word = word + " ";
        String formedTokenWord = "";
        while (i < word.length()) {
            if (state == State.S0) {
                transitionFromS0(word.charAt(i));
                formedTokenWord = formedTokenWord + word.charAt(i);
            } else if (state == State.S2) {
                transitionFromS2(word.charAt(i));
                formedTokenWord = formedTokenWord + word.charAt(i);
            } else if (state == State.S5) {
                transitionFromS5(word.charAt(i));
                formedTokenWord = formedTokenWord + word.charAt(i);
            } else if (state == State.S6) {
                i--;
                position--;
                while (state == State.S6) {
                    transitionFromS6(word.charAt(i));
                    if (state == State.S6) {
                        formedTokenWord = formedTokenWord + word.charAt(i);
                        i++;
                        position++;
                    } else if (state == State.S7)
                        formedTokenWord = formedTokenWord + word.charAt(i);
                }
            } else if (state == State.S8) {
                transitionFromS8(word.charAt(i));
                formedTokenWord = formedTokenWord + word.charAt(i);
            }
            i++;
            position++;

            //end states
            if (state == State.S1) {
                tokens.add(new ValidToken(formedTokenWord, TokenType.Operator));
                nextToken(word.substring(i));
            } else if (state == State.S3) {
                tokens.add(new ValidToken(formedTokenWord, TokenType.Separator));
                nextToken(word.substring(i));
            } else if (state == State.S4) {
                tokens.add(new ValidToken(formedTokenWord, TokenType.NumberLiteral));
                nextToken(word.substring(i));
            } else if (state == State.S5) {
                int charsRead = 0;
                while (state == State.S5 && i < word.length()) {
                    transitionFromS5(word.charAt(i));
                    if (state != State.INVALID) {
                        formedTokenWord = formedTokenWord + word.charAt(i);
                        i++;
                        position++;
                        charsRead++;
                    } else if (word.charAt(i) == ' ') {
                        i++;
                        position++;
                    }
                }
                tokens.add(new ValidToken(formedTokenWord, TokenType.NumberLiteral));
                nextToken(word.substring(i));
                i = i + charsRead;
                position = position + charsRead;
            } else if (state == State.S7) {
                tokens.add(new ValidToken(formedTokenWord, TokenType.StringLiteral));
                nextToken(word.substring(i));
            } else if (state == State.S8) {
                int charsRead = 0;
                while (state == State.S8 && i < word.length()) {
                    transitionFromS8(word.charAt(i));
                    if (state != State.INVALID) {
                        formedTokenWord = formedTokenWord + word.charAt(i);
                        i++;
                        position++;
                        charsRead++;
                    } else if (word.charAt(i) == ' ') {
                        i++;
                        position++;
                    }
                }
                if (keywords.contains(formedTokenWord))
                    tokens.add(new ValidToken(formedTokenWord, TokenType.Keyword));
                else
                    tokens.add(new ValidToken(formedTokenWord, TokenType.Identifier));
                nextToken(word.substring(i));
                i = i + charsRead;
                position = position + charsRead;
            } else if (checkInvalidChar(formedTokenWord.charAt(formedTokenWord.length() - 1)))
                throw new Exception();

            i++;
            position++;
        }
    }

    public static void scanner(String file) throws Exception {
        BufferedReader reader;
        int lineNumber = nextLine;
        try {
            reader = new BufferedReader(new FileReader(".\\" + file));
            String line = "";
            for (int i = 0; i <= lineNumber; i++)
                line = reader.readLine();
            lineNumber++;
            while (line != null) {
                while (line.trim().startsWith("//")) {
                    line = reader.readLine();
                    lineNumber++;
                }
                if (line.trim().startsWith("/*")) {
                    while (!line.trim().endsWith("*/")) {
                        line = reader.readLine();
                        lineNumber++;
                    }
                    line = reader.readLine();
                    lineNumber++;
                }
                try {
                    nextLine = lineNumber;
                    nextToken(line);
                    for (ValidToken token : tokens) {
                        boolean found = true;
                        for (Token tk : Token.values())
                            if (tk.getValueToken().equals(token.token)) {
                                pifTable.add(new PIF(tk.getValueType().getValue(), lineNumber, String.valueOf(tk.getValueId()), tk.getValueToken()));
                                //System.out.println(new PIF(tk.getValueType().getValue(), lineNumber, String.valueOf(tk.getValueId()), tk.getValueToken()));
                                found = true;
                                break;
                            } else
                                found = false;
                        if (!found){
                            pifTable.add(new PIF(token.type.code, lineNumber, "#", token.token));
                            //System.out.println(new PIF(token.type.code, lineNumber, "#", token.token));
                        }

                    }
                    line = reader.readLine();
                    lineNumber++;
                    tokens.clear();
                    position = -1;
                } catch (Exception e) {
                    throw new Exception("Invalid character in line " + lineNumber + ", column " + position);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PIF getToken() {
        counter++;
        return pifTable.get(counter);
    }

    public static PIF getNextToken() {
        int next = 0;
        if (counter + 1 < pifTable.size())
            next = counter + 1;
        return pifTable.get(next);
    }

    public static PIF getSecondNextToken() {
        int next = 0;
        if (counter + 2 < pifTable.size())
            next = counter + 2;
        return pifTable.get(next);
    }

    public static PIF getThirdNextToken() {
        int next = 0;
        if (counter + 3 < pifTable.size())
            next = counter + 3;
        return pifTable.get(next);
    }

    //IMPORT="import",IDENTIFIER,".",IDENTIFIER,".",IDENTIFIER,";";
    public static boolean importFunction() {
        if (Objects.equals(getToken().token, "import"))
            if (getToken().typeCode == 'I')
                if (Objects.equals(getToken().token, "."))
                    if (getToken().typeCode == 'I')
                        if (Objects.equals(getToken().token, "."))
                            if (getToken().typeCode == 'I')
                                return Objects.equals(getToken().token, ";");
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //PROGRAM={IMPORT},CLASS;
    public static boolean program() {
        while (Objects.equals(getNextToken().token, "import"))
            if (!importFunction())
                return false;
        if (classFunction())
            return true;
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //CLASS=["public"],["static"],"class",IDENTIFIER,CLASS-BLOCK;
    public static boolean classFunction() {
        String accessModifier = "default";
        if (Objects.equals(getNextToken().token, "public")) {
            getToken();
            accessModifier = "public";
        }
        if (Objects.equals(getNextToken().token, "static"))
            getToken();
        if (Objects.equals(getToken().token, "class"))
            if (getNextToken().typeCode == 'I') {
                PIF newEntry = getToken();
                classScope = newEntry.token;
                entries.add(new Entry(newEntry.token, "class", "--", String.valueOf(newEntry.lineNumber), accessModifier, "global"));
                if (Objects.equals(getNextToken().token, "{"))
                    return classBlock();
            } else
                getToken();
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //DECLARATION=DATA-TYPE,IDENTIFIER,{",",IDENTIFIER},";";
    public static boolean declaration() {
        if (dataType())
            if (getNextToken().typeCode == 'I') {
                PIF newEntry = getToken();
                entries.add(new Entry(newEntry.token, "variable", dataType, String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                if (Objects.equals(getToken().token, ";"))
                    return true;
            } else
                getToken();
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //DATA-TYPE="int"|"void"|IDENTIFIER,["[","]"];
    public static boolean dataType() {
        if (Objects.equals(getNextToken().token, "int") | Objects.equals(getNextToken().token, "void") | getNextToken().typeCode == 'I') {
            dataType = getToken().token;
            if (Objects.equals(getNextToken().token, "[")) {
                dataType = dataType + getToken().token;
                if (Objects.equals(getNextToken().token, "]")) {
                    dataType = dataType + getToken().token;
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    //PARAMETER=[DATA-TYPE,IDENTIFIER,{",",DATA-TYPE,IDENTIFIER}];
    public static boolean parameter() {
        if (dataType())
            if (getNextToken().typeCode == 'I') {
                PIF newEntry = getToken();
                entries.add(new Entry(newEntry.token, "parameter", dataType, String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                while (Objects.equals(getNextToken().token, ",")) {
                    getToken();
                    if (dataType()) {
                        if (getNextToken().typeCode != 'I') {
                            if (invalidLine == -1) {
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        }
                        newEntry = getToken();
                        entries.add(new Entry(newEntry.token, "parameter", dataType, String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                    } else {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                }
            } else {
                if (invalidLine == -1) {
                    invalidToken = getToken();
                    invalidLine = invalidToken.lineNumber;
                }
                return false;
            }
        return true;
    }

    //METHOD-CALL=IDENTIFIER,"(",")";
    public static boolean methodCall() {
        if (getToken().typeCode == 'I')
            if (Objects.equals(getToken().token, "("))
                if (!Objects.equals(getToken().token, ")")) {
                    if (invalidLine == -1) {
                        counter--;
                        invalidToken = getToken();
                        invalidLine = invalidToken.lineNumber;
                    }
                    return false;
                } else return true;
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //OUTPUT=STRING|IDENTIFIER,{"+",STRING|(IDENTIFIER,[".",METHOD-CALL])};
    public static boolean output() {
        if (getNextToken().typeCode == 'S') {
            PIF newEntry = getToken();
            entries.add(new Entry(newEntry.token, "literal", "String", String.valueOf(newEntry.lineNumber), "--", "global"));
            while (Objects.equals(getNextToken().token, "+")) {
                getToken();
                if (getNextToken().typeCode == 'S') {
                    newEntry = getToken();
                    entries.add(new Entry(newEntry.token, "literal", "String", String.valueOf(newEntry.lineNumber), "--", "global"));
                } else if (getNextToken().typeCode == 'I') {
                    getToken();
                    if (Objects.equals(getNextToken().token, ".")) {
                        getToken();
                        if (!methodCall()) {
                            if (invalidLine == -1) {
                                counter--;
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        }
                    }
                } else {
                    if (invalidLine == -1) {
                        counter--;
                        invalidToken = getToken();
                        invalidLine = invalidToken.lineNumber;
                    }
                    return false;
                }
            }
            return true;
        } else if (getNextToken().typeCode == 'I') {
            getToken();
            while (Objects.equals(getNextToken().token, "+")) {
                getToken();
                if (getNextToken().typeCode == 'S') {
                    PIF newEntry = getToken();
                    entries.add(new Entry(newEntry.token, "literal", "String", String.valueOf(newEntry.lineNumber), "--", "global"));
                } else if (getNextToken().typeCode == 'I') {
                    getToken();
                    if (Objects.equals(getNextToken().token, ".")) {
                        getToken();
                        if (!methodCall()) {
                            if (invalidLine == -1) {
                                counter--;
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        }
                    }
                } else {
                    if (invalidLine == -1) {
                        counter--;
                        invalidToken = getToken();
                        invalidLine = invalidToken.lineNumber;
                    }
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //NEW_OBJECT = "new", DATA-TYPE,"(", IDENTIFIER "," IDENTIFIER, ")";
    public static boolean newObject(){

        PIF newEntry;
        if (Objects.equals(getNextToken().token, "new")){
            getToken();
            if (dataType())
                if (Objects.equals(getToken().token, "("))
                    if (getNextToken().typeCode == 'I'){
                        newEntry = getToken();
                        entries.add(new Entry(newEntry.token, "new_parameter", "--", String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                        if(Objects.equals(getToken().token, ",")){
                            if (getNextToken().typeCode == 'I'){
                                newEntry = getToken();
                                entries.add(new Entry(newEntry.token, "new_parameter", "--", String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                                if (Objects.equals(getToken().token, ")"))
                                    return true;
                            }
                            else
                                return false;
                        }
                        else
                            return false;
                    }
        }
        return false;

    }

    //RETURN="return",(OUTPUT|NEW_OBJECT),";";
    public static boolean returnFunction() {
        if (Objects.equals(getToken().token, "return"))
            if (output() || newObject())
                if (!Objects.equals(getToken().token, ";")) {
                    if (invalidLine == -1) {
                        counter--;
                        invalidToken = getToken();
                        invalidLine = invalidToken.lineNumber;
                    }
                    return false;
                } else
                    return true;
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //EXPRESSION=IDENTIFIER|NUMBER,{OPERATOR,IDENTIFIER|NUMBER};
    public static boolean expression() {
        if(getToken().typeCode == 'I' ){
            if(Objects.equals(getNextToken().token, ";"))
                return true;
            else if(Objects.equals(getNextToken().token, ".")) {
                getToken();
                if (getToken().typeCode == 'I')
                    if (getNextToken().typeCode == 'T'){
                        getToken();
                        if (getToken().typeCode == 'I')
                            if (Objects.equals(getToken().token, "."))
                                if (getToken().typeCode == 'I')
                                    return true;
                    }
                else if(Objects.equals(getToken().token, "(")){
                        if (getNextToken().typeCode == 'I') {
                            PIF newEntry = getToken();
                            entries.add(new Entry(newEntry.token, "parameter", "Main", String.valueOf(newEntry.lineNumber), "--", "global"));
                            if (Objects.equals(getToken().token, ","))
                                if (getNextToken().typeCode == 'I') {
                                    newEntry = getToken();
                                    entries.add(new Entry(newEntry.token, "parameter", "Main", String.valueOf(newEntry.lineNumber), "--", "global"));
                                }
                            if (Objects.equals(getToken().token, ")"))
                                return true;
                        }
                    }

            }
            else if (getToken().typeCode == 'E')
                    if (getToken().typeCode == 'E')
                        return true;

        }

        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //ASSIGNMENT=IDENTIFIER,"=",EXPRESSION,";";
    public static boolean assignment() {
        if (getToken().typeCode == 'I')
            if (Objects.equals(getToken().token, "="))
                if (expression())
                    if (!Objects.equals(getToken().token, ";")) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    } else
                        return true;
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //LOOP="while","(","true",")",BLOCK;
    public static boolean loop() {
        if (Objects.equals(getToken().token, "while")){
            if (Objects.equals(getToken().token, "("))
                if (Objects.equals(getToken().token, "true"))
                    if (Objects.equals(getToken().token, ")"))
                        if (!block()) {
                            if (invalidLine == -1) {
                                counter--;
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        } else
                            return true;
        } else
            getToken();
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //PRINT="System",".","out",".","println","(",OUTPUT,")",";";
    public static boolean print() {
        if (Objects.equals(getToken().token, "System")) {
            boolean exist = false;
            for (Entry entry : entries)
                if (Objects.equals(entry.name, "System")) {
                    exist = true;
                    break;
                }
            if (!exist)
                entries.add(new Entry("System", "class", "--", "--", "public", "global"));
            else
                exist = false;
            if (Objects.equals(getToken().token, "."))
                if (Objects.equals(getToken().token, "out")) {
                    for (Entry entry : entries)
                        if (Objects.equals(entry.name, "out")) {
                            exist = true;
                            break;
                        }
                    if (!exist)
                        entries.add(new Entry("out", "variable", "PrintStream", "--", "public", "System"));
                    else
                        exist = false;
                    if (Objects.equals(getToken().token, "."))
                        if (Objects.equals(getToken().token, "println")) {
                            for (Entry entry : entries)
                                if (Objects.equals(entry.name, "println")) {
                                    exist = true;
                                    break;
                                }
                            if (!exist)
                                entries.add(new Entry("println", "method", "void", "--", "public", "System"));
                            if (Objects.equals(getToken().token, "(")){
                                if (output())
                                    if (Objects.equals(getToken().token, ")"))
                                        if (!Objects.equals(getToken().token, ";")) {
                                            if (invalidLine == -1) {
                                                counter--;
                                                invalidToken = getToken();
                                                invalidLine = invalidToken.lineNumber;
                                            }
                                            return false;
                                        } else
                                            return true;
                            }

                        }
                }
        }
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //INPUT="Scanner",IDENTIFIER,"=","new","Scanner","(","System",".","in",")",";",DATA-TYPE,IDENTIFIER,"=",IDENTIFIER,".",("nextInt"|"next"),"(",")",";";
    public static boolean input() {
        if (Objects.equals(getToken().token, "Scanner"))
            if (getNextToken().typeCode == 'I') {
                PIF newEntry = getToken();
                entries.add(new Entry(newEntry.token, "variable", "Scanner", String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                if (Objects.equals(getToken().token, "="))
                    if (Objects.equals(getToken().token, "new"))
                        if (Objects.equals(getToken().token, "Scanner")) {
                            boolean exist = false;
                            for (Entry entry : entries)
                                if (Objects.equals(entry.name, "Scanner")) {
                                    exist = true;
                                    break;
                                }
                            if (!exist)
                                entries.add(new Entry("Scanner", "constructor", "--", "--", "public", "Scanner"));
                            else
                                exist = false;
                            if (Objects.equals(getToken().token, "("))
                                if (Objects.equals(getToken().token, "System")) {
                                    for (Entry entry : entries)
                                        if (Objects.equals(entry.name, "System")) {
                                            exist = true;
                                            break;
                                        }
                                    if (!exist)
                                        entries.add(new Entry("System", "class", "--", "--", "public", "global"));
                                    else
                                        exist = false;
                                    if (Objects.equals(getToken().token, "."))
                                        if (Objects.equals(getToken().token, "in")) {
                                            for (Entry entry : entries)
                                                if (Objects.equals(entry.name, "in")) {
                                                    exist = true;
                                                    break;
                                                }
                                            if (!exist)
                                                entries.add(new Entry("in", "variable", "InputStream", "--", "public", "System"));
                                            else
                                                exist = false;
                                            if (Objects.equals(getToken().token, ")"))
                                                if (Objects.equals(getToken().token, ";"))
                                                    if (dataType())
                                                        if (getNextToken().typeCode == 'I') {
                                                            newEntry = getToken();
                                                            entries.add(new Entry(newEntry.token, "variable", dataType, String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                                                            if (Objects.equals(getToken().token, "="))
                                                                if (getToken().typeCode == 'I')
                                                                    if (Objects.equals(getToken().token, ".")){
                                                                        Object o = getToken().token;
                                                                        if (Objects.equals(o, "nextInt")) {
                                                                            for (Entry entry : entries)
                                                                                if (Objects.equals(entry.name, "nextInt")) {
                                                                                    exist = true;
                                                                                    break;
                                                                                }
                                                                            if (!exist)
                                                                                entries.add(new Entry("nextInt", "method", "int", "--", "public", "Scanner"));
                                                                            if (Objects.equals(getToken().token, "("))
                                                                                if (Objects.equals(getToken().token, ")"))
                                                                                    if (!Objects.equals(getToken().token, ";")) {
                                                                                        if (invalidLine == -1) {
                                                                                            counter--;
                                                                                            invalidToken = getToken();
                                                                                            invalidLine = invalidToken.lineNumber;
                                                                                        }
                                                                                        return false;
                                                                                    } else
                                                                                        return true;
                                                                        }
                                                                        else if(Objects.equals(o, "next")) {
                                                                            for (Entry entry : entries)
                                                                                if (Objects.equals(entry.name, "next")) {
                                                                                    exist = true;
                                                                                    break;
                                                                                }
                                                                            if (!exist)
                                                                                entries.add(new Entry("next", "method", "int", "--", "public", "Scanner"));
                                                                            if (Objects.equals(getToken().token, "("))
                                                                                if (Objects.equals(getToken().token, ")"))
                                                                                    if (!Objects.equals(getToken().token, ";")) {
                                                                                        if (invalidLine == -1) {
                                                                                            counter--;
                                                                                            invalidToken = getToken();
                                                                                            invalidLine = invalidToken.lineNumber;
                                                                                        }
                                                                                        return false;
                                                                                    } else
                                                                                        return true;
                                                                        }
                                                                    }
                                                        } else
                                                            getToken();
                                        }
                                }
                        }
            } else
                getToken();
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //CONDITION="if","(",(IDENTIFIER),COMPARATOR,NUMBER,")",BLOCK,"else", BLOCK;
    public static boolean elseFunction(){
        if (Objects.equals(getToken().token, "else")) {
            if (!block()) {
                if (invalidLine == -1) {
                    counter--;
                    invalidToken = getToken();
                    invalidLine = invalidToken.lineNumber;
                }
                return false;
            }
            return true;
        }

        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //CONDITION="if","(",(IDENTIFIER),COMPARATOR,NUMBER,")",BLOCK;
    public static boolean condition() {
        if (Objects.equals(getToken().token, "if"))
            if (Objects.equals(getToken().token, "("))
                if (getToken().typeCode == 'I')
                    if (getNextToken().typeCode == 'T'){
                        getToken();
                        if (getNextToken().typeCode == 'N') {
                            PIF newEntry = getToken();
                            entries.add(new Entry(newEntry.token, "literal", "int", String.valueOf(newEntry.lineNumber), "--", "global"));
                            if (Objects.equals(getToken().token, ")")) {
                                if (!block()) {
                                    if (invalidLine == -1) {
                                        counter--;
                                        invalidToken = getToken();
                                        invalidLine = invalidToken.lineNumber;
                                    }
                                    return false;
                                } else
                                    return true;
                            }
                            else
                                return false;
                        }
                    }
                    else if(Objects.equals(getToken().token, "."))
                        if (getToken().typeCode == 'I')
                            if(Objects.equals(getToken().token, "("))
                                if(getToken().typeCode == 'S')
                                    if(Objects.equals(getToken().token, ")"))
                                        if(Objects.equals(getToken().token, ")"))
                                            return true;

        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //BREAK="break",";";
    public static boolean breakFunction() {
        if (Objects.equals(getToken().token, "break"))
            if (!Objects.equals(getToken().token, ";")) {
                if (invalidLine == -1) {
                    counter--;
                    invalidToken = getToken();
                    invalidLine = invalidToken.lineNumber;
                }
                return false;
            } else
                return true;
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //OBJECT=DATA-TYPE,IDENTIFIER,"=","new",DATA-TYPE,"(",STRING|NUMBER,{",",STRING|NUMBER},")",";";
    public static boolean object() {
        if (dataType())
            if (getNextToken().typeCode == 'I') {
                PIF newEntry = getToken();
                entries.add(new Entry(newEntry.token, "variable", dataType, String.valueOf(newEntry.lineNumber), "--", classScope + methodScope));
                if (Objects.equals(getToken().token, "="))
                    if (Objects.equals(getToken().token, "new"))
                        if (dataType())
                            if (Objects.equals(getToken().token, "("))
                                if (getNextToken().typeCode == 'S' | getNextToken().typeCode == 'N') {
                                    String type;
                                    if (getNextToken().typeCode == 'S')
                                        type = "String";
                                    else
                                        type = "int";
                                    newEntry = getToken();
                                    entries.add(new Entry(newEntry.token, "literal", type, String.valueOf(newEntry.lineNumber), "--", "global"));
                                    while (Objects.equals(getNextToken().token, ",")) {
                                        getToken();
                                        if (getNextToken().typeCode == 'S' | getNextToken().typeCode == 'N') {
                                            if (getNextToken().typeCode == 'S')
                                                type = "String";
                                            else
                                                type = "int";
                                            newEntry = getToken();
                                            entries.add(new Entry(newEntry.token, "literal", type, String.valueOf(newEntry.lineNumber), "--", "global"));
                                        } else {
                                            if (invalidLine == -1) {
                                                invalidToken = getToken();
                                                invalidLine = invalidToken.lineNumber;
                                            }
                                            return false;
                                        }
                                    }
                                    if (Objects.equals(getToken().token, ")"))
                                        if (!Objects.equals(getToken().token, ";")) {
                                            if (invalidLine == -1) {
                                                counter--;
                                                invalidToken = getToken();
                                                invalidLine = invalidToken.lineNumber;
                                            }
                                            return false;
                                        } else
                                            return true;
                                }
            } else
                getToken();
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //BLOCK="{",{DECLARATION|RETURN|ASSIGNMENT|OBJECT|LOOP|PRINT|INPUT|CONDITION|BREAK|ELSE},"}";
    public static boolean block() {
        if (Objects.equals(getToken().token, "{")) {
            while (Objects.equals(getNextToken().token, "int") || Objects.equals(getNextToken().token, "else")|| Objects.equals(getNextToken().token, "void") || getNextToken().typeCode == 'I' || Objects.equals(getNextToken().token, "return") || Objects.equals(getNextToken().token, "while") || Objects.equals(getNextToken().token, "System") || Objects.equals(getNextToken().token, "Scanner") || Objects.equals(getNextToken().token, "if") || Objects.equals(getNextToken().token, "break"))
                if (Objects.equals(getNextToken().token, "int") || Objects.equals(getNextToken().token, "void")) {
                    if (!declaration()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                } else if (Objects.equals(getNextToken().token, "System")) {
                    if (!print()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                } else if (Objects.equals(getNextToken().token, "Scanner")) {
                    if (!input()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                } else if (Objects.equals(getNextToken().token, "if")) {
                    if (!condition()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                }else if (Objects.equals(getNextToken().token, "else")) {
                    if (!elseFunction()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                }
                else if (Objects.equals(getNextToken().token, "break")) {
                    if (!breakFunction()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                } else if (getNextToken().typeCode == 'I') {
                    if (getSecondNextToken().typeCode == 'I') {
                        if (Objects.equals(getThirdNextToken().token, "=")) {
                            if (!object()) {
                                if (invalidLine == -1) {
                                    counter--;
                                    invalidToken = getToken();
                                    invalidLine = invalidToken.lineNumber;
                                }
                                return false;
                            }
                        } else if (Objects.equals(getThirdNextToken().token, ";") | Objects.equals(getThirdNextToken().token, ","))
                            if (!declaration()) {
                                if (invalidLine == -1) {
                                    counter--;
                                    invalidToken = getToken();
                                    invalidLine = invalidToken.lineNumber;
                                }
                                return false;
                            }
                    } else if (Objects.equals(getSecondNextToken().token, "="))
                        if (!assignment()) {
                            if (invalidLine == -1) {
                                counter--;
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        }
                } else if (Objects.equals(getNextToken().token, "return")) {
                    if (!returnFunction()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                } else if (Objects.equals(getNextToken().token, "while")) {
                    if (!loop()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                }

            if (!Objects.equals(getToken().token, "}")) {
                if (invalidLine == -1) {
                    counter--;
                    invalidToken = getToken();
                    invalidLine = invalidToken.lineNumber;
                }
                return false;
            } else
                return true;
        }
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //METHOD = "public",["static"],DATA-TYPE,IDENTIFIER,"(",PARAMETER,")",BLOCK;
    public static boolean method() {

        if (Objects.equals(getToken().token, "public")) {
            if (Objects.equals(getNextToken().token, "static"))
                getToken();
            if (dataType())
                if (getNextToken().typeCode == 'I') {
                    PIF newEntry = getToken();
                    methodScope = "." + newEntry.token;
                    entries.add(new Entry(newEntry.token, "method", dataType, String.valueOf(newEntry.lineNumber), "public", classScope));
                    if (Objects.equals(getToken().token, "("))
                        if (parameter())
                            if (Objects.equals(getToken().token, ")"))
                                if (!block()) {
                                    if (invalidLine == -1) {
                                        counter--;
                                        invalidToken = getToken();
                                        invalidLine = invalidToken.lineNumber;
                                    }
                                    return false;
                                } else
                                    return true;
                } else
                    getNextToken();
        }
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //CONSTRUCTOR="public",IDENTIFIER,"(",PARAMETER,")","{",{"this",".",IDENTIFIER,"=",IDENTIFIER,";"},"}";
    public static boolean constructor() {
        if (Objects.equals(getToken().token, "public"))
            if (getNextToken().typeCode == 'I') {
                PIF newEntry = getToken();
                methodScope = "." + newEntry.token;
                entries.add(new Entry(newEntry.token, "constructor", "--", String.valueOf(newEntry.lineNumber), "public", classScope));
                if (Objects.equals(getToken().token, "("))
                    if (parameter())
                        if (Objects.equals(getToken().token, ")"))
                            if (Objects.equals(getToken().token, "{")) {
                                while (Objects.equals(getNextToken().token, "this")) {
                                    getToken();
                                    if (Objects.equals(getToken().token, "."))
                                        if (getToken().typeCode == 'I')
                                            if (Objects.equals(getToken().token, "="))
                                                if (getToken().typeCode == 'I') {
                                                    if (!Objects.equals(getToken().token, ";")) {
                                                        if (invalidLine == -1) {
                                                            counter--;
                                                            invalidToken = getToken();
                                                            invalidLine = invalidToken.lineNumber;
                                                        }
                                                        return false;
                                                    }
                                                } else {
                                                    if (invalidLine == -1) {
                                                        counter--;
                                                        invalidToken = getToken();
                                                        invalidLine = invalidToken.lineNumber;
                                                    }
                                                    return false;
                                                }
                                            else {
                                                if (invalidLine == -1) {
                                                    counter--;
                                                    invalidToken = getToken();
                                                    invalidLine = invalidToken.lineNumber;
                                                }
                                                return false;
                                            }
                                        else {
                                            if (invalidLine == -1) {
                                                counter--;
                                                invalidToken = getToken();
                                                invalidLine = invalidToken.lineNumber;
                                            }
                                            return false;
                                        }
                                    else {
                                        if (invalidLine == -1) {
                                            counter--;
                                            invalidToken = getToken();
                                            invalidLine = invalidToken.lineNumber;
                                        }
                                        return false;
                                    }
                                }
                                if (!Objects.equals(getToken().token, "}")) {
                                    if (invalidLine == -1) {
                                        counter--;
                                        invalidToken = getToken();
                                        invalidLine = invalidToken.lineNumber;
                                    }
                                    return false;
                                } else
                                    return true;
                            }
            } else
                getToken();
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    //CLASS-BLOCK="{",{CLASS|DECLARATION|CONSTRUCTOR|METHOD},"}";
    public static boolean classBlock() {
        if (Objects.equals(getToken().token, "{")) {
            while (Objects.equals(getNextToken().token, "public") | Objects.equals(getNextToken().token, "static") | Objects.equals(getNextToken().token, "class") | Objects.equals(getNextToken().token, "int") | Objects.equals(getNextToken().token, "void") | getNextToken().typeCode == 'I')
                if (Objects.equals(getNextToken().token, "public")) {
                    if (Objects.equals(getSecondNextToken().token, "static")) {
                        if (Objects.equals(getThirdNextToken().token, "class")) {
                            if (!classFunction()) {
                                if (invalidLine == -1) {
                                    counter--;
                                    invalidToken = getToken();
                                    invalidLine = invalidToken.lineNumber;
                                }
                                return false;
                            }
                        } else if (Objects.equals(getThirdNextToken().token, "int") | Objects.equals(getThirdNextToken().token, "void") | getThirdNextToken().typeCode == 'I')
                            if (!method()) {
                                if (invalidLine == -1) {
                                    counter--;
                                    invalidToken = getToken();
                                    invalidLine = invalidToken.lineNumber;
                                }
                                return false;
                            }
                    } else if (Objects.equals(getSecondNextToken().token, "class")) {
                        if (!classFunction()) {
                            if (invalidLine == -1) {
                                counter--;
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        }
                    } else if (Objects.equals(getSecondNextToken().token, "int") | Objects.equals(getSecondNextToken().token, "void")) {
                        if (!method()) {
                            if (invalidLine == -1) {
                                counter--;
                                invalidToken = getToken();
                                invalidLine = invalidToken.lineNumber;
                            }
                            return false;
                        }
                    } else if (getSecondNextToken().typeCode == 'I')
                        if (Objects.equals(getThirdNextToken().token, "(")) {
                            if (!constructor()) {
                                if (invalidLine == -1) {
                                    counter--;
                                    invalidToken = getToken();
                                    invalidLine = invalidToken.lineNumber;
                                }
                                return false;
                            }
                        } else if (getThirdNextToken().typeCode == 'I')
                            if (!method()) {
                                if (invalidLine == -1) {
                                    counter--;
                                    invalidToken = getToken();
                                    invalidLine = invalidToken.lineNumber;
                                }
                                return false;
                            }
                } else if (Objects.equals(getNextToken().token, "static") | Objects.equals(getNextToken().token, "class")) {
                    if (!classFunction()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                } else if (Objects.equals(getNextToken().token, "int") | Objects.equals(getNextToken().token, "void") | getNextToken().typeCode == 'I') {
                    if (!declaration()) {
                        if (invalidLine == -1) {
                            counter--;
                            invalidToken = getToken();
                            invalidLine = invalidToken.lineNumber;
                        }
                        return false;
                    }
                }

            if (!Objects.equals(getToken().token, "}")) {
                if (invalidLine == -1) {
                    counter--;
                    invalidToken = getToken();
                    invalidLine = invalidToken.lineNumber;
                }
                return false;
            } else {
                Entry entryClass = null;
                for (Entry e : entries)
                    if (Objects.equals(e.name, classScope)) {
                        entryClass = e;
                        break;
                    }
                for (int i = entries.indexOf(entryClass) - 1; i >= 0; i--)
                    if (Objects.equals(entries.get(i).kind, "class")) {
                        classScope = entries.get(i).name;
                        break;
                    }
                return true;
            }
        }
        if (invalidLine == -1) {
            counter--;
            invalidToken = getToken();
            invalidLine = invalidToken.lineNumber;
        }
        return false;
    }

    public static void read(String file) {
        try {
            scanner(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void parse(String file) throws Exception {
        if (program() & counter == pifTable.size() - 1) {
            writeSymbolTable();
            System.out.println("Symbol table done");
        }
        if (invalidLine != -1) {
            writeSymbolTable();
            throw new Exception("Invalid token " + invalidToken.token + " in line " + invalidLine);
        }
    }

    public static void writeSymbolTable() {
        String eol = System.getProperty("line.separator");
        try (Writer writer = new FileWriter("symboltable.csv")) {
            for (Entry entry : entries)
                writer.append(entry.name)
                        .append(',')
                        .append(entry.kind)
                        .append(',')
                        .append(entry.type)
                        .append(',')
                        .append(entry.lineOfDeclaration)
                        .append(',')
                        .append(entry.accessModifier)
                        .append(',')
                        .append(entry.scope)
                        .append(eol);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        System.out.println("\nResult for " + args[0]);
        //read("src/MainS2");
        read(args[0]);
        try {
            //parse("src/MainS2");
            parse(args[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
