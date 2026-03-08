public class Tea  extends HotBeverage {

    @Override
    protected void brew() {
        System.out.println("Steeping the tea leaves...");
    }

    @Override
    protected void addCondiments() {
        System.out.println("Adding lemon...");
    }
}