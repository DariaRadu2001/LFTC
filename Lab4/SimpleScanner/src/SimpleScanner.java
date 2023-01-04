import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleScanner {


    enum State {S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20,
        S21, S22, S23, S24, S25, S26, S27, S28, S29, S30, S31, S32, S33, S34, S35, S36, S37, S38, REJECTED}

    public static State isKeyword(String word)
    {
        State currentState = State.S0;
        for(char character : word.toCharArray())
        {
            //int
            if(currentState == State.S0 && character == 'i')
            {
                currentState = State.S1;
            }
            else if(currentState == State.S1 && character == 'n')
            {
                currentState = State.S2;
            }
            else if(currentState == State.S2 && character == 't')
            {
                currentState = State.S3;
            }
            //for
            else if(currentState == State.S0 && character == 'f')
            {
                currentState = State.S4;
            }
            else if(currentState == State.S4 && character == 'o')
            {
                currentState = State.S5;
            }
            else if(currentState == State.S5 && character == 'r')
            {
                currentState = State.S6;
            }
            //double
            else if(currentState == State.S0 && character == 'd')
            {
                currentState = State.S7;
            }
            else if(currentState == State.S7 && character == 'o')
            {
                currentState = State.S8;
            }
            else if(currentState == State.S8 && character == 'u')
            {
                currentState = State.S9;
            }
            else if(currentState == State.S9 && character == 'b')
            {
                currentState = State.S10;
            }
            else if(currentState == State.S10 && character == 'l')
            {
                currentState = State.S11;
            }

            else if(currentState == State.S11 && character == 'e')
            {
                currentState = State.S12;
            }
            //void
            else if(currentState == State.S0 && character == 'v')
            {
                currentState = State.S13;
            }else if(currentState == State.S13 && character == 'o')
            {
                currentState = State.S14;
            }else if(currentState == State.S14 && character == 'i')
            {
                currentState = State.S15;
            }
            else if(currentState == State.S15 && character == 'd')
            {
                currentState = State.S16;
            }
            //public
            else if(currentState == State.S0 && character == 'p')
            {
                currentState = State.S17;
            }
            else if(currentState == State.S17 && character == 'u')
            {
                currentState = State.S18;
            }
            else if(currentState == State.S18 && character == 'b')
            {
                currentState = State.S19;
            }
            else if(currentState == State.S19 && character == 'l')
            {
                currentState = State.S20;
            }
            else if(currentState == State.S20 && character == 'i')
            {
                currentState = State.S21;
            }
            else if(currentState == State.S21 && character == 'c')
            {
                currentState = State.S22;
            }
            //char
            else if(currentState == State.S0 && character == 'c')
            {
                currentState = State.S23;
            }
            else if(currentState == State.S23 && character == 'h')
            {
                currentState = State.S24;
            }
            else if(currentState == State.S24 && character == 'a')
            {
                currentState = State.S25;
            }
            else if(currentState == State.S25 && character == 'r')
            {
                currentState = State.S26;
            }
            //class
            else if(currentState == State.S23 && character == 'l')
            {
                currentState = State.S27;
            }
            else if(currentState == State.S27 && character == 'a')
            {
                currentState = State.S25;
            }
            else if(currentState == State.S25 && character == 's')
            {
                currentState = State.S28;
            }
            else if(currentState == State.S28 && character == 's')
            {
                currentState = State.S29;
            }

        }

        if(currentState == State.S3 || currentState == State.S6 || currentState == State.S12 || currentState == State.S16 ||
                currentState == State.S22 || currentState == State.S26 || currentState == State.S29)
            return currentState;
        else
            return State.REJECTED;
    }

    public static State identifier(String word)
    {
        State currentState = State.S0;
        int ct = 0;
        for(char character : word.toCharArray())
        {
            if(character == ' ')
                return State.REJECTED;
            else
            {
                if(character >= 'A' && character <= 'Z' && currentState == State.S0)
                {
                    currentState = State.S38;
                    ct++;
                }
                else if(character >= 'A' && character <= 'Z' && currentState == State.S38)
                    ct++;
            }

        }
        if(ct == word.length())
        {
            currentState = State.S38;
        }
        else
            currentState = State.REJECTED;

        return currentState;
    }

    public static State integerFloat(String word)
    {
        State currentState = State.S0;
        int ct = 0;
        for(char character : word.toCharArray())
        {
            if(character == '+' && currentState == State.S0)
            {
                currentState = State.S31;
                ct++;
            }
            else if(character == '-' && currentState == State.S0)
            {
                currentState = State.S32;
                ct++;
            }
            else if(Character.isDigit(character) && currentState == State.S31)
            {
                currentState = State.S33;
                ct++;
            }
            else if(Character.isDigit(character) && currentState == State.S32)
            {
                currentState = State.S33;
                ct++;
            }
            else if(Character.isDigit(character) && currentState == State.S33)
            {
                ct++;
            }
            else if((character == '1' || character == '2' || character == '3' || character == '4' || character == '5'
                    || character == '6' || character == '7' || character == '8' || character == '9')
                    && currentState == State.S0)
            {
                currentState = State.S34;
                ct++;
            }
            else if(Character.isDigit(character) && currentState == State.S34)
            {
                ct++;
            }
            else if(character == '.' && currentState == State.S34)
            {
                currentState = State.S33;
                ct++;
            }
            else if(character == '0' && currentState == State.S0)
            {
                currentState = State.S30;
                ct++;
            }

        }
        if(ct == word.length())
            return currentState;
        else
            return State.REJECTED;
    }

    public static State stringLiteral(String word)
    {
        State currentState = State.S0;
        int ct = 0;
        for(char character : word.toCharArray())
        {
            boolean b = (character >= 'A' && character <= 'Z')
                    || (character >= 'a' && character <= 'z') ||
                    character == ' ' || Character.isDigit(character) ;

            if(currentState == State.S0 && character == '"')
            {
                currentState = State.S35;
                ct++;
            }
            else if(currentState == State.S35 && b)
            {
                currentState = State.S36;
                ct++;
            }
            else if(currentState == State.S36 && b)
            {
                ct++;
            }
            else if(currentState == State.S36 && character == '"')
            {
                currentState = State.S37;
                ct++;
            }
        }

        if(ct == word.length())
            return currentState;
        else
            return State.REJECTED;
    }

    public static void main(String[] args) {

        List<State> keywordsGoodStates = new ArrayList<>();
        keywordsGoodStates.add(State.S3);
        keywordsGoodStates.add(State.S6);
        keywordsGoodStates.add(State.S12);
        keywordsGoodStates.add(State.S16);
        keywordsGoodStates.add(State.S22);
        keywordsGoodStates.add(State.S26);
        keywordsGoodStates.add(State.S29);

        List<State> literalsGoodStates = new ArrayList<>();
        literalsGoodStates.add(State.S30);
        literalsGoodStates.add(State.S33);
        literalsGoodStates.add(State.S37);
        
        Scanner keyboard = new Scanner(System.in);

        while(true)
        {
            System.out.println("Enter word: ");
            String word = keyboard.nextLine();

            if(keywordsGoodStates.contains(isKeyword(word)))
            {
                System.out.println("ACCEPTED Keyword");
            }
            else if(literalsGoodStates.contains(integerFloat(word)))
            {
                System.out.println("ACCEPTED Literal");
            }
            else if(stringLiteral(word) == State.S37)
            {
                System.out.println("ACCEPTED Literal");
            }
            else if(identifier(word) == State.S38)
            {
                System.out.println("ACCEPTED Identifier");
            }
            else
                System.out.println("REJECTED");

        }
    }
}
