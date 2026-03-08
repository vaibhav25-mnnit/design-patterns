public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!\nThis is Template Method Patten\n");

        System.out.println("===== Making Tea =====");
        HotBeverage tea = new Tea();
        tea.prepare();
        System.out.print("===== Done Making Tea =====\n");

        System.out.println("\n===== Making Coffee =====");
        HotBeverage coffee = new Coffee(false);
        coffee.prepare();
        System.out.print("===== Done Making Coffee =====\n");

        System.out.println("\n===== Making Black Coffee =====");
        HotBeverage blackCoffee = new Coffee(true);
        blackCoffee.prepare();
        System.out.print("===== Done Making Black Coffee =====\n");
    }
}