
// ============================================================
// ABSTRACT CLASS — defines the skeleton
// templateMethod() is final — algorithm order never changes
// ============================================================

public abstract class HotBeverage {

    // TEMPLATE METHOD — final, skeleton fixed, subclasses cannot change order
    public final void prepare() {
        boilWater();          // fixed step
        brew();               // varies — abstract
        pourInCup();          // fixed step
        if (customerWantsCondiments()) { // hook controls whether to add
            addCondiments();  // varies — hook
        }
    }

    // fixed steps — same for all beverages
    private void boilWater() {
        System.out.println("Boiling water...");
    }

    private void pourInCup() {
        System.out.println("Pouring into cup...");
    }

    // abstract — subclass MUST define how to brew
    protected abstract void brew();

    // HOOK — optional override, default is empty
    // subclass overrides only if it wants condiments
    protected void addCondiments() {}

    // HOOK — optional override, controls whether condiments step runs at all
    // default is true — subclass can override to return false
    protected boolean customerWantsCondiments() { return true; }
}
