//error
import~ java.util.Scanner;
//CONSTRUCT 2
public class MainL1{
    //CONSTRUCT 1
    String name;
    int age;
    public MainL1(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {return name;}
    public String toString(){return name + " is " + age;}
    //CONSTRUCT 4
    public static int diffrence(MainL1 person1, MainL1 person2){
        int diffrence;
        diffrence = person1.age - person2.age;
        return diffrence;
    }
    //CONSTRUCT 3
    public static MainL1 choices() {
        System.out.println("Enter age: ");
        Scanner keyboard = new Scanner(System.in);
        int age = keyboard.nextInt();
        System.out.println("Enter name: ");
        Scanner keyboard2 = new Scanner(System.in);
        String name = keyboard2.next();
        return new MainL1(name, age);
    }

    public static void main(String[] args) {
        System.out.println("Who is older program");
        MainL1 person1;
        person1 = choices();
        //CONSTRUCT 6
        while(true) {
            //CONSTRUCT 7
            MainL1 person2;
            person2 = choices();
            int diffrenceAges;
            diffrenceAges = MainL1.diffrence(person1, person2);
            //CONSTRUCT 5
            if(diffrenceAges < 0)
            {
                System.out.println("\nOlder is the second person " + person2.getName());
                person1 = person2;
            }
            else
            {
                System.out.println("\nOlder is the first person " + person1.getName());
            }
            //CONSTRUCT 8
            System.out.println("Press y if you want to close the app ");
            Scanner keyboard = new Scanner(System.in);
            String answere = keyboard.next();
            if (answere.equals("y"))
                break;

        }
    }
}