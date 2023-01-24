import java.util.Scanner;
//CONSTRUCT 2
public class MainS2 {
    //CONSTRUCT 1
    String name;
    int age;
    public MainS2(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {return name;}
    public String toString(){return name + " is " + age;}
    //CONSTRUCT 4
    public static int diffrence(MainS2 person1, MainS2 person2){
        int diffrence;
        diffrence = person1.age - person2.age;
        return diffrence;
    }
    //CONSTRUCT 3
    public static MainS2 choices() {
        System.out.println("Enter age: ");
        Scanner keyboard = new Scanner(System.in);
        int age = keyboard.nextInt();
        System.out.println("Enter name: ");
        Scanner keyboard2 = new Scanner(System.in);
        //error
        String name = keyboard2.next(error);
        return new MainS2(name, age);
    }

    public static void main(String[] args) {
        System.out.println("Who is older program");
        MainS2 person1;
        person1 = choices();
        //CONSTRUCT 6
        while(true) {
            //CONSTRUCT 7
            MainS2 person2;
            person2 = choices();
            int diffrenceAges;
            diffrenceAges = Main.diffrence(person1, person2);
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