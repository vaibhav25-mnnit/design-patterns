public class Main {
    public static void main(String[] args) {
        System.out.println("Hi there we are here learning factory design pattern\nCreating different shapes from shape factory");

        Shape circle = ShapeFactory.getShape(ShapeType.CIRCLE);
        Shape square = ShapeFactory.getShape(ShapeType.SQUARE);
        Shape triangle = ShapeFactory.getShape(ShapeType.TRIANGLE);

        circle.sayMyName();
        square.sayMyName();
        triangle.sayMyName();
    }
}