public class Coffee  extends HotBeverage {

    private final boolean blackCoffee;

    public Coffee(boolean blackCoffee) {
        this.blackCoffee = blackCoffee;
    }

    @Override
    protected void brew() {
        System.out.println("Dripping coffee through filter...");
    }

    @Override
    protected void addCondiments() {
        System.out.println("Adding milk and sugar...");
    }

    // hook override — black coffee skips condiments entirely
    @Override
    protected boolean customerWantsCondiments() {
        return !blackCoffee;
    }
}