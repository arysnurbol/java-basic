package ch02.t03_constructors;

import ch02.check.Check;

public class Task03Check {

    public static void run() {
        Check.task("Тапсырма 03 — Конструктор түрлері (Rectangle)");

        Rectangle unit = new Rectangle();
        Check.eq("no-arg konstruktor -> width == 1", 1.0, unit.getWidth(), 1e-9);
        Check.eq("no-arg konstruktor -> height == 1", 1.0, unit.getHeight(), 1e-9);

        Rectangle square = new Rectangle(4);
        Check.eq("Rectangle(4) -> шаршы, area == 16", 16.0, square.area(), 1e-9);
        Check.eq("Rectangle(4) -> perimeter == 16", 16.0, square.perimeter(), 1e-9);

        Rectangle rect = new Rectangle(3, 5);
        Check.eq("Rectangle(3,5) -> area == 15", 15.0, rect.area(), 1e-9);
        Check.eq("Rectangle(3,5) -> perimeter == 16", 16.0, rect.perimeter(), 1e-9);

        Rectangle copy = new Rectangle(rect);
        Check.eq("көшірменің ені бірдей", 3.0, copy.getWidth(), 1e-9);
        Check.eq("көшірменің биіктігі бірдей", 5.0, copy.getHeight(), 1e-9);
        Check.notSame("көшірме — бөлек объект, сол сілтеме емес", rect, copy);
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
