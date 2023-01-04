import java.util.Scanner;
//CONSTRUCT 2
public class Person {
    //CONSTRUCT 1
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    //CONSTRUCT 4
    static int diffrence(Person person1, Person person2){return person1.age - person2.age; }
    public String toString(){
        return name + " is " + age + " old.";
    }
    //CONSTRUCT 3
    public static Person choices() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter name: ");
        String name = keyboard.next();
        System.out.println("Enter age: ");
        int age = keyboard.nextInt();
        return new Person(name, age);
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Who is older program");
        Person person1 = choices();
        //CONSTRUCT 6
        while (true) {
            //CONSTRUCT 7
            Person person2 = choices();
            int diffrenceAges = Person.diffrence(person1, person2);
            //CONSTRUCT 5
            if(diffrenceAges < 0)
            {
                System.out.println(person2.getName() + " is older!");
                person1 = person2;
            }
            else
            {
                System.out.println(person1.getName() + " is older!");
            }
            //CONSTRUCT 8
            System.out.println("Do you want to close the application? y/n");
            if (keyboard.next().equals("y")) System.exit(1);
        }
    }
}