public class MyFA2 {
    private static final String startState = "Start State";
    private static final String state1 = "State1";
    private static final String state2 = "State2";
    private static final String state3 = "State3";
    private static final String state4 = "State4";
    private static final String state5 = "State5";
    private static final String state6 = "State6";
    private static final String state7 = "State7";
    private static final String state8 = "State8";
    private static final String state9 = "State9";
    private static final String state10 = "State10";
    private static final String state11 = "State11";
    private static final String state12 = "State12";
    private static final String state13 = "State13";
    private static final String state14 = "State14";
    private static final String state15 = "State15";
    private static final String state16 = "State16";
    private static String currentState = startState;
    public static String finalState = null;

    public static Boolean matches(String email) {

        String[] parts = email.split("@");
        int ctPart1 = 0;
        int ctPart2 = 0;
        boolean verification = false;

        if(parts.length == 2)
        {
            for (char character : parts[0].toCharArray()) {
                if (currentState.equals(startState)) {
                    if ((character >= 97 && character <= 122) || character == '.' || (character >= 48 && character <= 57)) {
                        currentState = state1;
                        ctPart1++;
                    }
                } else if (currentState.equals(state1)) {
                    if ((character >= 97 && character <= 122) || character == '.' || (character >= 48 && character <= 57))
                        ctPart1++;
                }
            }

            currentState = state2;

            for (char character : parts[1].toCharArray()) {
                if (character == 115 && currentState.equals(state2)) {
                    currentState = state13;
                    ctPart2++;
                } else if (character == 116 && currentState.equals(state13)) {
                    currentState = state14;
                    ctPart2++;
                } else if (character == 117 && currentState.equals(state14)) {
                    currentState = state15;
                    ctPart2++;
                } else if (character == 100 && currentState.equals(state15)) {
                    currentState = state16;
                    ctPart2++;
                } else if (character == 46 && currentState.equals(state16)) {
                    currentState = state2;
                    ctPart2++;
                } else if (character == 117 && currentState.equals(state2)) {
                    currentState = state3;
                    ctPart2++;
                } else if (character == 98 && currentState.equals(state3)) {
                    currentState = state4;
                    ctPart2++;
                } else if (character == 98 && currentState.equals(state4)) {
                    currentState = state5;
                    ctPart2++;
                } else if (character == 99 && currentState.equals(state5)) {
                    currentState = state6;
                    ctPart2++;
                } else if (character == 108 && currentState.equals(state6)) {
                    currentState = state7;
                    ctPart2++;
                } else if (character == 117 && currentState.equals(state7)) {
                    currentState = state8;
                    ctPart2++;
                } else if (character == 106 && currentState.equals(state8)) {
                    currentState = state9;
                    ctPart2++;
                } else if (character == 46 && currentState.equals(state9)) {
                    currentState = state10;
                    ctPart2++;
                } else if (character == 114 && currentState.equals(state10)) {
                    currentState = state11;
                    ctPart2++;
                } else if (character == 111 && currentState.equals(state11)) {
                    currentState = state12;
                    ctPart2++;
                }
            }
        }
        else
            return false;

        finalState = currentState;
        if(ctPart1 == parts[0].length() && ctPart1 != 0 && (ctPart2 == 10 || ctPart2 == 15))
        {
            verification = true;
        }
        return verification;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter an email in the terminal");
        } else {
            if (matches(args[0]))
                System.out.print("VALID email!\n");
            else
                System.out.print("INVALID email!\n");
        }

    }
}

