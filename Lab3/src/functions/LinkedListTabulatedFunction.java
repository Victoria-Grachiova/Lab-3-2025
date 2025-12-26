package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    // Внутренний класс для узла списка
    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode last;
        FunctionNode next;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }

    private FunctionNode head; // Голова списка
    private FunctionNode lastReadWriteNode; // Последний доступный узел для оптимизации
    private int lastReadWriteIndex; // Индекс последнего доступного узла
    private int size; // Кол-во точек

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек меньше 2");
        }

        initializeList();

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + step * i;
            addNodeToTail(new FunctionPoint(x, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше 2");
        }

        initializeList();

        double step = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < values.length; i++) {
            double x = leftX + step * i;
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }

    // Инициализация пустого списка
    private void initializeList() {
        head = new FunctionNode(null);
        head.last = head;
        head.next = head;
        size = 0;
        lastReadWriteNode = head;
        lastReadWriteIndex = -1;
    }

    // Получение узла по индексу с оптимизацией
    private FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        // Начинаем с последнего доступного узла
        FunctionNode node;
        int startIndex;

        if (lastReadWriteIndex != -1 && Math.abs(index - lastReadWriteIndex) < Math.min(index, size - index)) {
            node = lastReadWriteNode;
            startIndex = lastReadWriteIndex;
        } else {
            node = head.next;
            startIndex = 0;
        }

        // Движение вперед или назад
        if (index > startIndex) {
            for (int i = startIndex; i < index; i++) {
                node = node.next;
            }
        } else if (index < startIndex) {
            for (int i = startIndex; i > index; i--) {
                node = node.last;
            }
        }

        // Сохраняем для следующего доступа
        lastReadWriteNode = node;
        lastReadWriteIndex = index;

        return node;
    }

    // Добавление узла в конец списка
    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);
        FunctionNode tail = head.last;

        newNode.last = tail;
        newNode.next = head;
        tail.next = newNode;
        head.last = newNode;

        size++;
        lastReadWriteNode = newNode;
        lastReadWriteIndex = size - 1;

        return newNode;
    }

    // Добавление узла по индексу
    private FunctionNode addNodeByIndex(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index == size) {
            return addNodeToTail(point);
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode lastNode = nextNode.last;
        FunctionNode newNode = new FunctionNode(point);

        newNode.last = lastNode;
        newNode.next = nextNode;
        lastNode.next = newNode;
        nextNode.last = newNode;

        size++;
        lastReadWriteNode = newNode;
        lastReadWriteIndex = index;

        return newNode;
    }

    // Удаление узла по индексу
    private FunctionNode deleteNodeByIndex(int index)
            throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.last;
        FunctionNode nextNode = nodeToDelete.next;

        prevNode.next = nextNode;
        nextNode.last = prevNode;

        size--;

        // Обновление последнего доступного узла
        if (size == 0) {
            lastReadWriteNode = head;
            lastReadWriteIndex = -1;
        } else if (index == lastReadWriteIndex) {
            lastReadWriteNode = (index == size) ? prevNode : nextNode;
            lastReadWriteIndex = (index == size) ? index - 1 : index;
        } else if (index < lastReadWriteIndex) {
            lastReadWriteIndex--;
        }

        return nodeToDelete;
    }

    //Методы интерфейса TabulatedFunction

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        // Проверка порядка X
        if (index > 0 && point.getter_x() <= node.last.point.getter_x()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }
        if (index < size - 1 && point.getter_x() >= node.next.point.getter_x()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }

        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getter_x();
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        // Проверка порядка X
        if (index > 0 && x <= node.last.point.getter_x()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }
        if (index < size - 1 && x >= node.next.point.getter_x()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }

        node.point.setter_x(x);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getter_y();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        getNodeByIndex(index).point.setter_y(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (size <= 2) {
            throw new IllegalStateException("Нельзя удалить точку, потому что точек меньше 3");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка на дублирование X
        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(current.point.getter_x() - point.getter_x()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с такой координатой х уже существует");
            }
            current = current.next;
        }

        // Поиск позиции для вставки
        int index = 0;
        current = head.next;
        while (current != head && current.point.getter_x() < point.getter_x()) {
            current = current.next;
            index++;
        }

        addNodeByIndex(index, new FunctionPoint(point));
    }

    public double getLeftDomainBorder() {
        if (size == 0) return Double.NaN;
        return head.next.point.getter_x();
    }

    public double getRightDomainBorder() {
        if (size == 0) return Double.NaN;
        return head.last.point.getter_x();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Поиск интервала, в котором есть x
        FunctionNode curr = head.next;
        while (curr.next != head) {
            double x1 = curr.point.getter_x();
            if (Math.abs(x - x1) < 1e-10) {
                return curr.point.getter_y();
            }
            double x2 = curr.next.point.getter_x();

            if (x >= x1 && x <= x2) {
                double y1 = curr.point.getter_y();
                double y2 = curr.next.point.getter_y();

                // Линейная интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            curr = curr.next;
        }

        return Double.NaN;
    }
}
