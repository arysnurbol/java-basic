package ch02.t04_chaining;

import ch02.check.Check;

public class Task04Check {

    public static void run() {
        Check.task("Тапсырма 04 — Конструкторларды тізбектеу (Pizza)");

        Pizza.resetCreatedCount();

        Pizza def = new Pizza();
        Check.eq("Pizza() -> әдепкі күй",
                "Pizza[size=M, cheese=true, pepperoni=false]", def.toString());

        Pizza large = new Pizza("L");
        Check.eq("Pizza(\"L\") -> өлшемі L, ірімшік бар",
                "Pizza[size=L, cheese=true, pepperoni=false]", large.toString());

        Pizza noCheese = new Pizza("S", false);
        Check.eq("Pizza(\"S\", false) -> ірімшіксіз",
                "Pizza[size=S, cheese=false, pepperoni=false]", noCheese.toString());

        Pizza full = new Pizza("XL", true, true);
        Check.eq("толық конструктор",
                "Pizza[size=XL, cheese=true, pepperoni=true]", full.toString());

        Check.eq("getSize() жұмыс істейді", "XL", full.getSize());

        Check.eq("4 пицца жасалды -> createdCount == 4 (санағыш тек бір жерде өседі)",
                4, Pizza.getCreatedCount());
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
