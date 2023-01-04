import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ExpressionParser {

    private static List<Integer> ziffer;
    private static List<Integer> zifferOhneNull;
    private static int pos;
    private static String currentOperator;
    private static int currentToken;
    private static List<String> tokens;

    public static void init(){

        ziffer = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
        zifferOhneNull = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        tokens = new ArrayList<>();
        pos = 0;
    }

    public enum State{
        S0,S1,S2,S3,REJECTED
    }

    public enum TokenType{
        Operator("T"),
        Number_Literal("N");

        public final String typeCode;

        TokenType(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeCode() {
            return typeCode;
        }

    }

    public enum Token{

        PLUS('+',  "305", TokenType.Operator.getTypeCode()),
        MINUS('-', "306", TokenType.Operator.getTypeCode()),
        STAR('*',  "307", TokenType.Operator.getTypeCode()),
        SLASH('/', "308", TokenType.Operator.getTypeCode());

        public final char token;
        public final String tokenID;
        public final String type;

        Token(char token, String tokenID, String type) {
            this.token = token;
            this.tokenID = tokenID;
            this.type = type;
        }

        public char getToken() {
            return token;
        }

        public static Token getEnumByName(char name) {
            for (int i = 0; i < Token.values().length; i++) {
                if (name == Token.values()[i].token)
                    return Token.values()[i];
            }
            return null;
        }
    }

    public static State isIntegerOrZero(State state, char character)
    {
        try {
            if (zifferOhneNull.contains(Integer.parseInt(String.valueOf(character))) && state.equals(State.S0)) {
                return State.S1;
            }
            else if(ziffer.contains(Integer.parseInt(String.valueOf(character))) && state.equals(State.S1)) {
                return State.S1;
            }
            else if(character =='0' && state.equals(State.S0)) {
                return State.S3;
            }
            else
                return State.REJECTED;

        }
        catch (Exception e) {
            return State.REJECTED;
        }
    }

    public static StringBuilder createToken(int begin, int end, char[] list){

        StringBuilder token = new StringBuilder();
        for(int j = begin; j <= end; j++)
        {
            token.append(list[j]);
        }
        return token;
    }

    public static List<String> nextToken(String expression)
    {
        List<String> tokens = new ArrayList<>();

        State currentState = State.S0;
        boolean wasOperator = true;

        char[] list = expression.toCharArray();
        int length = list.length;
        int position = 0;


        while (list[position] == ' ') {
                position++;
        }
        int begin = position;
        int end = position;


        for(int i = position ; i < length; i++)
        {
            if(list[i] == ' ')
            {
                while(list[i] == ' '){
                    i++;
                }

                if(Token.getEnumByName(createToken(begin, end, list).charAt(0)) == null)
                    if(wasOperator){
                    tokens.add(createToken(begin, end, list).toString());
                    wasOperator = false;
                }
                else
                {
                    System.out.println("Number operator number is the right order");
                    System.exit(1);
                }

                begin = i;
                end = i;
                currentState = State.S0;
            }

            if(isIntegerOrZero(currentState, list[i]).equals(State.S1) ||  isIntegerOrZero(currentState, list[i]).equals(State.S3)){
                currentState = isIntegerOrZero(currentState, list[i]);
                end = i;
            }
            else if(Token.getEnumByName(list[i]) != null && currentState.equals(State.S0)){

                begin = i;
                end = i;
                Token operator = Token.getEnumByName(list[i]);
                currentState = State.S2;

                if(!wasOperator){
                    assert operator != null;
                    tokens.add(Character.toString(operator.getToken()));
                    wasOperator = true;
                }
                else {
                    System.out.println("Number operator number is the right order");
                    System.exit(1);
                }

            }
            else{
                currentState = State.S0;
                if(Token.getEnumByName(createToken(begin, end, list).charAt(0)) == null && wasOperator){
                    tokens.add(createToken(begin, end, list).toString());
                    wasOperator = false;
                }
                begin = i;
                end = i;

                if(isIntegerOrZero(currentState, list[i]).equals(State.S1) ||  isIntegerOrZero(currentState, list[i]).equals(State.S3)){
                    currentState = isIntegerOrZero(currentState, list[i]);
                }
                else if(Token.getEnumByName(list[i]) != null){

                    begin = i + 1;
                    Token operator = Token.getEnumByName(list[i]);
                    if(!wasOperator){
                        assert operator != null;
                        tokens.add(Character.toString(operator.getToken()));
                        wasOperator = true;
                    }
                    else {
                        System.out.println("Number operator number is the right order");
                        System.exit(1);
                    }

                }
                else{
                    System.out.println("Not  allowed  character at position: " + i);
                    System.exit(1);
                }
            }

        }
        if(list[length - 1] == ' ')
        {
            tokens.add(createToken(begin, end, list).toString());
        }
        else if(currentState.equals(State.S1) || currentState.equals(State.S3)){
            end = length - 1;

            if(wasOperator)
                tokens.add(createToken(begin, end, list).toString());
            else {
                System.out.println("Number operator number is the right order");
                System.exit(1);
            }
        }
        else {
            System.out.println("Not  allowed  character at position: " + (length - 1));
        }
        return tokens;
    }

    /**
     * add = mul add'
     * add' = ε
     * add' = "+" add
     * add' = "-" add
     */
    public static int add(List<String> tokens){
        int term = mul(tokens);
        if(currentOperator.equals("+")){
            currentOperator = "=";
            int term2 = mul(tokens);
            return term + term2;
        }
        else if(currentOperator.equals("-")){
            currentOperator = "=";
            int term2 = mul(tokens);
            return term - term2;
        }

        return term;
    }

    /**
     * mul = term mul'
     * mul' = ε
     * mul' = "*" mul
     * mul' = "/" mul
     */
    public static int mul(List<String> tokens){
        int term = term(tokens);
        if(currentOperator.equals("*")){
            currentOperator = "=";
            int term2 = mul(tokens);
            return term * term2;
        }
        else if(currentOperator.equals("/")){
            currentOperator = "=";
            int term2 = mul(tokens);
            return term / term2;
        }

        return term;
    }

    public static int term(List<String> tokens){

        int token = currentToken;
        int posNextNumberLiteral = pos + 1;
        currentToken = Integer.parseInt(String.valueOf(tokens.get(posNextNumberLiteral)));
        return Integer.parseInt(String.valueOf(token));
    }

    public static List<String> updateList(int firstPosition, int lastPosition, int calculated, List<String> tokens){
        List<String> updatedList =  new ArrayList<>();
        int position;
        for(position = 0; position < firstPosition; position++)
        {
            updatedList.add(tokens.get(position));
        }

        updatedList.add(Integer.toString(calculated));

        for(position = lastPosition; position < tokens.size(); position++){
            updatedList.add(tokens.get(position));
        }

        return updatedList;
    }

    public static List<String> parser(List<String> tokens){

        int calculated;
        pos = 0;
        while(pos < tokens.size()){
            if(tokens.get(pos).equals("*") || tokens.get(pos).equals("/")){
                currentOperator = tokens.get(pos);
                currentToken = Integer.parseInt(String.valueOf(tokens.get(pos - 1)));
                calculated = mul(tokens);
                tokens = updateList(pos - 1, pos + 2, calculated, tokens);
                pos = pos - 1;
            }
            pos = pos + 1;
        }

        pos = 0;
        while(pos < tokens.size()){
            if(tokens.get(pos).equals("+") || tokens.get(pos).equals("-")){
                currentOperator = tokens.get(pos);
                currentToken = Integer.parseInt(String.valueOf(tokens.get(pos - 1)));
                calculated = add(tokens);
                tokens = updateList(pos - 1, pos + 2, calculated, tokens);
                pos = pos - 1;
            }
            pos = pos + 1;
        }
        return tokens;
    }

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Expression please: ");
        String expression = keyboard.nextLine();
        init();
        tokens = nextToken(expression);
        System.out.println("the result for " + tokens + " is " + parser(tokens).get(0));
    }
}
