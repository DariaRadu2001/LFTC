public class Product {

    String identifier;
    float price;

    public Product(String identifier, float price) {
        this.identifier = identifier;
        this.price = price;
    }

    public static void main(String[] ARG) {

        int pricelimit = Integer.parseInt(ARG[0]);
        System.out.println("Products below " + pricelimit);

        Product[] prodlist = new Product[3];

        prodlist[0] = new Product("potato", 5);
        prodlist[1] = new Product("wine", 47);
        prodlist[2] = new Product("honey", 22);

        for (Product prod : prodlist) {

            float price = prod.price * 120 / 100;
            if (price < pricelimit) {
                System.out.println(prod.identifier);
            }
        }
    }
}