import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("** Тест ArrayTabulatedFunction **");
        testArrayFunction();

        System.out.println("\n** Тест LinkedListTabulatedFunction **");
        testLinkedListFunction();

        System.out.println("\n** Тестирование исключений **");
        testExceptions();
    }

    private static void testArrayFunction() {
        try {
            // Обычное создание функции
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            System.out.println("Array функция создана успешно");

            // Тестируем методы
            for (int i = 0; i < func.getPointsCount(); i++) {
                double x = func.getPointX(i);
                func.setPointY(i, Math.sin(x));
            }

            printPoints(func);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void testLinkedListFunction() {
        try {
            // Обычное создание функции
            TabulatedFunction func = new LinkedListTabulatedFunction(-5, 5, 6);
            System.out.println("LinkedList функция создана успешно");

            // Тестируем методы
            for (int i = 0; i < func.getPointsCount(); i++) {
                double x = func.getPointX(i);
                func.setPointY(i, Math.cos(x));
            }

            printPoints(func);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void testExceptions() {
        System.out.println("\n** Тест исключений конструкторов:");

        try {
            // Неправильные границы
            TabulatedFunction func1 = new ArrayTabulatedFunction(10, 0, 5);
            System.out.println("Должно быть исключение IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            // Мало точек
            TabulatedFunction func2 = new LinkedListTabulatedFunction(0, 10, 1);
            System.out.println("Должно быть исключение IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n** Тестирование исключений доступа по индексу:");

        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            // Выход за границы
            func.getPoint(10);
            System.out.println("Должно быть исключение FunctionPointIndexOutOfBoundsException");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getClass().getName() + " - " + e.getMessage());
        }

        System.out.println("\n** Тестирование исключений при изменении точек:");

        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, 5);
            // Нарушение порядка X
            func.setPointX(2, 100); // Должно быть между соседними точками
            System.out.println("Должно быть исключение InappropriateFunctionPointException");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n** Тестирование исключений при добавлении точек:");

        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            // Добавляем точку с существующей X координатой
            func.addPoint(new FunctionPoint(5, 100));
            System.out.println("Должно быть исключение InappropriateFunctionPointException");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n** Тестирование исключений при удалении точек:");

        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, 2); // Всего 2 точки
            func.deletePoint(0); // Пытаемся удалить
            System.out.println("Должно быть исключение IllegalStateException");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n** Тестирование полиморфизма:");

        // Объявляем как TabulatedFunction, но создаем разные реализации
        TabulatedFunction[] functions = new TabulatedFunction[2];
        functions[0] = new ArrayTabulatedFunction(0, 5, 3);
        functions[1] = new LinkedListTabulatedFunction(0, 5, 3);

        for (int i = 0; i < functions.length; i++) {
            System.out.println("Функция " + (i+1) + " типа: " + functions[i].getClass().getSimpleName());
            System.out.println("Количество точек: " + functions[i].getPointsCount());
            System.out.println("Границы: [" + functions[i].getLeftDomainBorder() +
                    ", " + functions[i].getRightDomainBorder() + "]");
        }
    }

    static void printPoints(TabulatedFunction func) {
        System.out.print("Точки функции: ");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.printf("(%.2f, %.2f) ", func.getPointX(i), func.getPointY(i));
        }
        System.out.println();
    }
}