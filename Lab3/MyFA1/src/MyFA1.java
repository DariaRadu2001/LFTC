public class MyFA1 {

    private static final String startState = "Start State";
    private static final String state1 = "State1";
    private static final String state2 = "State2";
    private static final String state3 = "State3";
    private static final String state4 = "State4";
    private static final String state5 = "State5";
    private static final String state6 = "State6";
    private static final String state7 = "State7";
    private static final String state8 = "State8";
    private static String currentState = startState;
    public static String finalState = null;


    public static boolean correctWord(String word)
    {
        int ct = 0;
        for(char character : word.toCharArray())
        {
            if(character == 'X' && currentState.equals(startState))
            {
                currentState = state1;
                ct++;
            }
            else if (character == 'X' && currentState.equals(state1)) {
                currentState = state2;
                ct++;
            }
            else if (character == 'X' && currentState.equals(state2)) {
                currentState = state3;
                ct++;
            }
            else if (character == 'Y' && currentState.equals(state1)) {
                currentState = state6;
                ct++;
            }
            else if (character == 'Y' && currentState.equals(state2)) {
                currentState = state7;
                ct++;
            }
            else if (character == 'X' && currentState.equals(state3)) {
                currentState = state4;
                ct++;
            }
            else if (character == 'Y' && currentState.equals(state3)) {
                currentState = state8;
                ct++;
            } else if (character == 'Y' && currentState.equals(state5)) {
                currentState = state6;
                ct++;
            } else if (character == 'Y' && currentState.equals(startState)) {
                currentState = state5;
                ct++;
            } else if (character == 'Y' && currentState.equals(state6)) {
                currentState = state7;
                ct++;
            } else if (character == 'Y' && currentState.equals(state7)) {
                currentState = state8;
                ct++;
            }
        }
        finalState = currentState;
        return ct == word.length();
    }

    public static void main(String[] args)
    {
        if(args.length < 1)
            System.out.println("Please enter a word in the terminal");
        else if (args[0].length() >= 2) {
            if(correctWord(args[0]))
            {
                System.out.println("The given word is correct");
            }
            else
            {
                System.out.println("The given word is wrong");
            }
        }
        else
            System.out.println("The word is too short");


    }
}