import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CompleteScanner {

    static String path;
    static Character[][]typesMatrix;
    static List<Integer> typesMatrixEndzustande;
    static Character[][] keywordsMatrix;
    static List<Integer> keywordsMatrixEndzustande;
    static List<String> types;
    static List<Character> grossBuchstaben;
    static List<Character> kleinBuchstaben;
    static List<Integer> ziffer;
    static List<Integer> zifferOhneNull;
    static HashMap<String, Integer> symbolTable;
    static ArrayList<pifInput> pifTable;
    static ArrayList<Character> ignored;

    public static class pifInput{

//        Example row, keyword “public” in first line of input file:
//        Y,1,13,public
//        Example row, number literal 47 in line 15 of input file:
//        U,15,#,47

        private final String tokenType; //enumul de TokenType
        private final int lineNumber;
        private final String tokenId;
        private final String name;

        public pifInput(String tokenType, int lineNumber, String tokenId, String name) {
            this.tokenType = tokenType;
            this.lineNumber = lineNumber;
            this.tokenId = tokenId;
            this.name = name;
        }

        public pifInput(String tokenType, int lineNumber, String name) {
            this.tokenType = tokenType;
            this.lineNumber = lineNumber;
            this.tokenId = "#";
            this.name = name;
        }
    }

    static void init() {

        types = Arrays.asList("boolean","int","int[]","String","void");

        keywordsMatrixEndzustande = Arrays.asList(5,11,15,18,22,28,31,33,37,40);
        typesMatrixEndzustande = Arrays.asList(3,5,12,16,22);

        typesMatrix = new Character[23][23];
        keywordsMatrix = new Character[41][41];

        //typesMatrix Construction
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 23; j++) {

                if (i == 0 && j == 1) {
                    typesMatrix[i][j] ='i';
                } else if (i == 1 && j == 2) {
                    typesMatrix[i][j] = 'n';
                } else if (i == 2 && j == 3) {
                    typesMatrix[i][j] = 't';
                } else if (i == 3 && j == 4) {
                    typesMatrix[i][j] = '[';
                } else if (i == 4 && j == 5) {
                    typesMatrix[i][j] = ']';
                } else if (i == 0 && j == 6) {
                    typesMatrix[i][j] = 'b';
                } else if (i == 6 && j == 7) {
                    typesMatrix[i][j] = 'o';
                } else if (i == 7 && j == 8) {
                    typesMatrix[i][j] = 'o';
                } else if (i == 8 && j == 9) {
                    typesMatrix[i][j] = 'l';
                } else if (i == 9 && j == 10) {
                    typesMatrix[i][j] = 'e';
                } else if (i == 10 && j == 11) {
                    typesMatrix[i][j] = 'a';
                } else if (i == 11 && j == 12) {
                    typesMatrix[i][j] = 'n';
                } else if (i == 0 && j == 13) {
                    typesMatrix[i][j] = 'v';
                } else if (i == 13 && j == 14) {
                    typesMatrix[i][j] = 'o';
                } else if (i == 14 && j == 15) {
                    typesMatrix[i][j] = 'i';
                } else if (i == 15 && j == 16) {
                    typesMatrix[i][j] = 'd';
                } else if (i == 0 && j == 17) {
                    typesMatrix[i][j] = 'S';
                } else if (i == 17 && j == 18) {
                    typesMatrix[i][j] = 't';
                } else if (i == 18 && j == 19) {
                    typesMatrix[i][j] = 'r';
                } else if (i == 19 && j == 20) {
                    typesMatrix[i][j] = 'i';
                } else if (i == 20 && j == 21) {
                    typesMatrix[i][j] = 'n';
                } else if (i == 21 && j == 22) {
                    typesMatrix[i][j] = 'g';
                } else
                    typesMatrix[i][j] = ' ';
            }
        }

        //keywordsMatrix Construction
        for (int i = 0; i < 41; i++) {
            for (int j = 0; j < 41; j++) {

                if (i == 0 && j == 1) {
                    keywordsMatrix[i][j] ='c';
                } else if (i == 1 && j == 2) {
                    keywordsMatrix[i][j] = 'l';
                } else if (i == 2 && j == 3) {
                    keywordsMatrix[i][j] = 'a';
                } else if (i == 3 && j == 4) {
                    keywordsMatrix[i][j] = 's';
                } else if (i == 4 && j == 5) {
                    keywordsMatrix[i][j] = 's';
                } else if (i == 0 && j == 6) {
                    keywordsMatrix[i][j] = 's';
                } else if (i == 6 && j == 7) {
                    keywordsMatrix[i][j] = 't';
                } else if (i == 7 && j == 8) {
                    keywordsMatrix[i][j] = 'a';
                } else if (i == 8 && j == 9) {
                    keywordsMatrix[i][j] = 't';
                } else if (i == 9 && j == 10) {
                    keywordsMatrix[i][j] = 'i';
                } else if (i == 10 && j == 11) {
                    keywordsMatrix[i][j] = 'c';
                } else if (i == 0 && j == 12) {
                    keywordsMatrix[i][j] = 'e';
                } else if (i == 12 && j == 13) {
                    keywordsMatrix[i][j] = 'l';
                } else if (i == 13 && j == 14) {
                    keywordsMatrix[i][j] = 's';
                } else if (i == 14 && j == 15) {
                    keywordsMatrix[i][j] = 'e';
                } else if (i == 0 && j == 16) {
                    keywordsMatrix[i][j] = 'f';
                } else if (i == 16 && j == 17) {
                    keywordsMatrix[i][j] = 'o';
                } else if (i == 17 && j == 18) {
                    keywordsMatrix[i][j] = 'r';
                } else if (i == 16 && j == 19) {
                    keywordsMatrix[i][j] = 'a';
                } else if (i == 19 && j == 20) {
                    keywordsMatrix[i][j] = 'l';
                } else if (i == 20 && j == 21) {
                    keywordsMatrix[i][j] = 's';
                } else if (i == 21 && j == 22) {
                    keywordsMatrix[i][j] = 'e';
                } else if (i == 0 && j == 23) {
                    keywordsMatrix[i][j] = 'p';
                } else if (i == 23 && j == 24) {
                    keywordsMatrix[i][j] = 'u';
                } else if (i == 24 && j == 25) {
                    keywordsMatrix[i][j] = 'b';
                } else if (i == 25 && j == 26) {
                    keywordsMatrix[i][j] = 'l';
                } else if (i == 26 && j == 27) {
                    keywordsMatrix[i][j] = 'i';
                } else if (i == 27 && j == 28) {
                    keywordsMatrix[i][j] = 'c';
                } else if (i == 0 && j == 29) {
                    keywordsMatrix[i][j] = 'n';
                } else if (i == 29 && j == 30) {
                    keywordsMatrix[i][j] = 'e';
                } else if (i == 30 && j == 31) {
                    keywordsMatrix[i][j] = 'w';
                } else if (i == 0 && j == 32) {
                    keywordsMatrix[i][j] = 'i';
                } else if (i == 32 && j == 33) {
                    keywordsMatrix[i][j] = 'f';
                } else if (i == 0 && j == 34) {
                    keywordsMatrix[i][j] = 't';
                } else if (i == 34 && j == 35) {
                    keywordsMatrix[i][j] = 'h';
                } else if (i == 35 && j == 36) {
                    keywordsMatrix[i][j] = 'i';
                } else if (i == 36 && j == 37) {
                    keywordsMatrix[i][j] = 's';
                } else if (i == 34 && j == 38) {
                    keywordsMatrix[i][j] = 'r';
                } else if (i == 38 && j == 39) {
                    keywordsMatrix[i][j] = 'u';
                } else if (i == 39 && j == 40) {
                    keywordsMatrix[i][j] = 'e';
                } else
                    keywordsMatrix[i][j] = ' ';
            }
        }

        grossBuchstaben =  new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                                                        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));

        kleinBuchstaben =  new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                                                        'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

        ziffer = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
        zifferOhneNull = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

        symbolTable = new HashMap<String, Integer>();
        ignored = new ArrayList<>(Arrays.asList(' ', '/'));
        pifTable = new ArrayList<pifInput>();
    }

    public enum TokenType{
        Keyword("W"),
        Operator("T"),
        Separator("E"),
        Number_Literal("N"),
        String_Literal("S"),
        Identifier("I");

        public final String typeCode;

        TokenType(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public static String getEnumByName(String typeCode) {
            for (int i = 0; i < TokenType.values().length; i++) {
                if (typeCode.equals((TokenType.values()[i].typeCode)))
                    return TokenType.values()[i].typeCode;
            }
            return null;
        }
    }

    public enum Token{

        FALSE("false", "101", TokenType.Keyword.getTypeCode()),
        TRUE("true", "102", TokenType.Keyword.getTypeCode()),
        CLASS("class", "103", TokenType.Keyword.getTypeCode()),
        PUBLIC("public", "104", TokenType.Keyword.getTypeCode()),
        STATIC("static", "105", TokenType.Keyword.getTypeCode()),
        THIS("this", "106", TokenType.Keyword.getTypeCode()),
        IF("if", "107", TokenType.Keyword.getTypeCode()),
        FOR("for", "108", TokenType.Keyword.getTypeCode()),
        NEW("new", "109", TokenType.Keyword.getTypeCode()),
        ELSE("else", "110", TokenType.Keyword.getTypeCode()),
        VOID("void", "111", TokenType.Keyword.getTypeCode()),
        BOOLEAN("boolean", "112", TokenType.Keyword.getTypeCode()),
        INT("int", "113", TokenType.Keyword.getTypeCode()),
        OPEN_ACCOLADE ("{", "201", TokenType.Separator.getTypeCode()),
        CLOSED_ACCOLADE("}", "202", TokenType.Separator.getTypeCode()),
        OPEN_SQUARE("[", "203", TokenType.Separator.getTypeCode()),
        CLOSED_SQUARE("]","204", TokenType.Separator.getTypeCode()),
        OPEN_BRACKET("(", "205", TokenType.Separator.getTypeCode()),
        CLOSED_BRACKET(")","206", TokenType.Separator.getTypeCode()),
        DOT(".", "207", TokenType.Separator.getTypeCode()),
        COMMA(",", "208", TokenType.Separator.getTypeCode()),
        COLON(":", "209", TokenType.Separator.getTypeCode()),
        SEMICOLON(";","210", TokenType.Separator.getTypeCode()),
        NEGATION("!", "301", TokenType.Operator.getTypeCode()),
        CONJUNCTION("&&","302", TokenType.Operator.getTypeCode()),
        SMALLER("<","303", TokenType.Operator.getTypeCode()),
        EQUALS("=","304", TokenType.Operator.getTypeCode()),
        PLUS("+",  "305", TokenType.Operator.getTypeCode()),
        MINUS("-", "306", TokenType.Operator.getTypeCode()),
        STAR("*",  "307", TokenType.Operator.getTypeCode()),
        SLASH("/", "308", TokenType.Operator.getTypeCode());


        public final String token;
        public final String tokenID;
        public final String type;

        Token(String token, String tokenID, String type) {
            this.token = token;
            this.tokenID = tokenID;
            this.type = type;
        }

        public String getToken() {
            return token;
        }
        public String getTokenID() {
            return tokenID;
        }
        public String getType() {
            return type;
        }

        public static Token getEnumByName(String name) {
            for (int i = 0; i < Token.values().length; i++) {
                if (name.equals((Token.values()[i].token)))
                    return Token.values()[i];
            }
            return null;
        }
    }

    public enum State{
        S0, S1, S2, S3, S4, S5, S6, S7, REJECTED
    }

    // the end state here is S3
    public static State identifier(char[] word)
    {
        State currentState = State.S0;
        int ct = 0;
        if(grossBuchstaben.contains(word[0]) || kleinBuchstaben.contains(word[0]))
        {
            currentState = State.S3;
            ct++;
            while(ct < word.length)
            {
                if(grossBuchstaben.contains(word[ct]) || kleinBuchstaben.contains(word[ct]) || ziffer.contains(Integer.parseInt(String.valueOf(word[ct]))))
                {
                    ct++;
                }
                else
                {
                    currentState = State.REJECTED;
                    ct = word.length + 10;
                }
            }
        }

        return currentState;
    }

    // the end state here is S5 or S4 if we have 0
    public static State isIntegerOrZero(char[] word)
    {
        State currentState = State.S0;
        int ct = 0;
        try {
            if (zifferOhneNull.contains(Integer.parseInt(String.valueOf(word[0])))) {
                currentState = State.S5;
                ct++;
                while (ct < word.length) {
                    if (ziffer.contains(Integer.parseInt(String.valueOf(word[ct])))) {
                        ct++;
                    } else {
                        currentState = State.REJECTED;
                        ct = word.length + 10;
                    }
                }
            } else if (Integer.parseInt(String.valueOf(word[0])) == 0 && word.length == 1) {
                currentState = State.S4;
            }
        }
        catch (Exception e)
        {
            currentState = State.REJECTED;
        }

        return currentState;
    }

    // the end state here is S7
    public static State stringLiteral(char[] word)
    {
        State currentState = State.S0;
        int ct = 0;
        if(word[0] == '"')
        {
            currentState = State.S6;
            ct++;
            while(ct < word.length - 1)
            {
                ct++;
            }
            if(word[ct] == '"')
            {
                currentState = State.S7;
            }
            else
            {
                currentState = State.REJECTED;
            }

        }
        else
        {
            currentState = State.REJECTED;
        }

        return currentState;
    }

    public static boolean isType(char[] word){

        int zustand = 0;
        int index = 0;
        int next = 0;
        while (next <= 22) {
            while (typesMatrix[zustand][next] == ' ') {
                next++;
                if (next>22)
                    return false;
            }
            if (typesMatrix[zustand][next] == word[index]) {
                zustand = next;
                next = 0;
                index++;
            }
            else next++;
            if (index == word.length && typesMatrixEndzustande.contains(zustand)){
                return true;
            }
        }
        return false;
    }

    public static boolean isReserved(char[] word){

        int zustand = 0;
        int index = 0;
        int next = 0;
        while (next <= 40) {
            while (keywordsMatrix[zustand][next] == ' ') {
                next++;
                if (next>40)
                    return false;
            }
            if (keywordsMatrix[zustand][next] == word[index]) {
                zustand = next;
                next = zustand+1;
                index++;
            }
            else next++;
            if (index == word.length && keywordsMatrixEndzustande.contains(zustand)){
                return true;
            }
        }
        return false;
    }

    // Symboltabelle sort
    public static TreeMap<String, Integer> sortByKey()
    {
        return new TreeMap<>(symbolTable);
    }

    // to write into PIF.csv and symboltable.csv and if necessary creates
    static void saveResults() throws FileNotFoundException {
        File csvOutputFile = new File("PIF.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pifTable.forEach(p -> pw.println(p.tokenType + "," + p.lineNumber + "," + p.tokenId + "," + p.name));
        }

        File symbolOutputFile = new File("symboltable.csv");
        TreeMap<String, Integer> sortedST = sortByKey();
        try (PrintWriter pw = new PrintWriter(symbolOutputFile)) {
            sortedST.forEach((p1,p2) -> pw.println(p1));
        }
    }

    static HashMap<Integer, StringBuilder> nextToken(int index, char[] line)
    {
        HashMap<Integer, StringBuilder> tokenAtIndex = new HashMap<>();
        StringBuilder token= new StringBuilder();
        //Ignores the spaces
        while (line[index] == ' ')
        {
            index++;
        }

        while (Token.getEnumByName(String.valueOf(line[index])) == null && line[index] != ' ' && index < line.length)
        {
            token.append(line[index]);
            index++;
        }
        tokenAtIndex.put(index, token);

        return  tokenAtIndex;
    }

    static void startScan(String path) throws IOException {

        File file = new File(path);
        Scanner sc = new Scanner(file);
        int lineNumber = 1;
        //line by line
        while (sc.hasNextLine())
        {
            //char by char
            char[] line = sc.nextLine().toCharArray();
            int index = 0;
            System.out.println("Line:   " + "|"+ Arrays.toString(line) +"|");

            while (index < line.length)
            {
                //is mutable
                StringBuilder token= new StringBuilder();
                HashMap<Integer, StringBuilder> tokenAtIndex = nextToken(index, line);
                token = (StringBuilder) tokenAtIndex.values().toArray()[0];
                index = (int) tokenAtIndex.keySet().toArray()[0];

                // if it contains " checks with an FA whether it is a string
                if (token.toString().contains("\"") && !token.toString().endsWith("\""))
                {
                    while (line[index]!='\"' && index < line.length)
                    {
                        token.append(line[index]);
                        index++;
                    }
                    token.append(line[index]);
                }

                if (!token.toString().equals(""))
                {
                    //checks if the token tk is in the TOKEN ENUM
                    Token tk = Token.getEnumByName(token.toString());

                    //if there is in the enum
                    if (tk != null)
                    {
                        // find the typecode
                        String typCode = TokenType.getEnumByName(tk.getType().toString());
                        assert typCode != null;

                        //if there is a type (int, string, etc.) or reserved word (true, new, etc.) it is added in the pif
                        if (isType(token.toString().toCharArray()))
                        {
                            pifTable.add(new pifInput(typCode, lineNumber, tk.getTokenID(), token.toString()));
                        }
                        else if (isReserved(token.toString().toCharArray()))
                        {
                            pifTable.add(new pifInput(typCode, lineNumber, tk.getTokenID(), token.toString()));
                        }

                    }

                    //if there is not in the enum
                    else
                    {
                        //checks whether it is a string / integer / identifier
                        if (stringLiteral(token.toString().toCharArray()) == State.S7)
                        {
                            pifTable.add(new pifInput(TokenType.String_Literal.typeCode, lineNumber, token.toString()));
                            //symbolTable.put(token.toString(), token.toString().hashCode());
                        } else if (isIntegerOrZero(token.toString().toCharArray()) == State.S4 || isIntegerOrZero(token.toString().toCharArray()) == State.S5)
                        {
                            pifTable.add(new pifInput(TokenType.Number_Literal.typeCode, lineNumber, token.toString()));
                            //symbolTable.put(token.toString(), token.toString().hashCode());
                        }
                        else if(pifTable.size() > 1)
                        {
                            //Here we assume that if a token comes after the keyword "class", it is an identifier (ex. class Product)
                        if (pifTable.get(pifTable.size() - 1).name.equals("class") && !token.toString().equals("class"))
                        {
                            if (identifier(token.toString().toCharArray()) == State.S3)
                            {
                                pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));

                                if (!symbolTable.containsKey(token.toString()))
                                    symbolTable.put(token.toString(), token.toString().hashCode());
                            }
                            //here we assume that if a token is placed after the symbol ".", it is an identifier (ex. this.name)
                            else if (pifTable.get(pifTable.size() - 1).name.equals("."))
                            {
                                if (identifier(token.toString().toCharArray()) == State.S3)
                                {
                                    pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));
                                    if (!symbolTable.containsKey(token.toString()))
                                        symbolTable.put(token.toString(), token.toString().hashCode());
                                }
                            }
                        }
                        }
                        //here we assume that if a token has the same hashCode () with an element of the pif
                        //it is an Identifier ex name (other typ Declaration);
                        else
                        {
                            StringBuilder finalToken = token;
                            if (pifTable.stream().anyMatch(element -> element.name.hashCode() == finalToken.toString().hashCode()))
                            {
                                pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));
                                if (!symbolTable.containsKey(token.toString()))
                                    symbolTable.put(token.toString(), token.toString().hashCode());
                            }
                            else if(pifTable.size() > 1)
                            {
                                //here we assume that if a token comes after a type (string, int, etc.), it is an identifier e.g. String name;
                             if (types.contains(pifTable.get(pifTable.size() -1).name))
                            {
                                if (identifier(token.toString().toCharArray()) == State.S3) {
                                    pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));
                                    if (!symbolTable.containsKey(token.toString()))
                                        symbolTable.put(token.toString(), token.toString().hashCode());
                                }
                            }
                             else if(pifTable.size() > 3)
                             {

                                 //Here we assume that if a token comes after an array type (String [], int [], etc.), it is an identifier
                                 //ex. String[] ARG;
                            if (types.contains(pifTable.get(pifTable.size() -3).name) &&
                                     pifTable.get(pifTable.size() -2).name.equals("[") &&
                                     pifTable.get(pifTable.size() -1).name.equals("]")
                             )
                             {
                                 if (identifier(token.toString().toCharArray()) == State.S3)
                                 {
                                     pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));
                                     if (!symbolTable.containsKey(token.toString()))
                                         symbolTable.put(token.toString(), token.toString().hashCode());
                                 }
                             }
                             }
                             else if(pifTable.size() == 0)
                             {
                                 if (identifier(token.toString().toCharArray()) == State.S3)
                                 {
                                     pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));
                                     if (!symbolTable.containsKey(token.toString()))
                                         symbolTable.put(token.toString(), token.toString().hashCode());
                                 }
                             }
                            }

                            //Here we assume that if a token cannot be the previous one, in theory it should be an identifier (class)
                            //ex. if we have System.out.println () we say that System, out and println are class, so they are identifiers
                            else
                            {
                                StringBuilder finalToken1 = token;
                                if (pifTable.stream().noneMatch(e -> e.name.hashCode() == finalToken1.toString().hashCode()) && !token.toString().equals("")
                                        && !token.toString().equals("this"))
                                {
                                    pifTable.add(new pifInput(TokenType.Identifier.typeCode, lineNumber, token.toString()));
                                    if (!symbolTable.containsKey(token.toString()))
                                        symbolTable.put(token.toString(), token.toString().hashCode());
                                }
                            }
                        }
                    }
                }
                //here we add the separators that have remained in the pif
                Token tkSingle = Token.getEnumByName(String.valueOf(line[index]));
                if (tkSingle != null)
                {
                    String typeCode = TokenType.getEnumByName(tkSingle.getType().toString());
                    assert typeCode != null;
                    pifTable.add(new pifInput(typeCode, lineNumber, tkSingle.getTokenID(), tkSingle.getToken()));
                }
                index++;
            }
            lineNumber++;
        }
    }

    public static void main(String[] args) throws IOException {
        path = "C:\\Users\\User\\Desktop\\AN3\\EU\\LFTC\\Labor\\Lab5\\CompleteScanner\\src\\Product.txt";
        //path = args[0];
        init();
        startScan(path);
        saveResults();
    }
}
