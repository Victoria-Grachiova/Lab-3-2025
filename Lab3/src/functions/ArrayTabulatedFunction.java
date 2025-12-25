package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {

    private FunctionPoint[] points; //массив типа FunctionPoint
    private int pointsCount;

    public static boolean Comparison(double a, double b) {  //функция сравнения чисел c плавающей точкой
        final double EPSILON = 1E-10f;
        return Math.abs(a - b) < EPSILON;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {
            if (leftX >= rightX) {
                throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
            }
            if (pointsCount < 2) {
                throw new IllegalArgumentException("Количество точек меньше 2");
            }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 10];

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше 2");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount + 10]; // делаем с запасом для 6 задания с методом добавления точек

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    public double getLeftDomainBorder(){
        return points[0].getter_x();
    }
    public double getRightDomainBorder(){
        return points[pointsCount-1].getter_x();
    }

    public double getFunctionValue(double x){

        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Поиск интервала, в который попадает x
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getter_x();
            double x2 = points[i + 1].getter_x();

            // Если x совпадает с одной из точек
            if (Comparison(x, x1)) {
                return points[i].getter_y();
            }
            if (Comparison(x, x2)) {
                return points[i + 1].getter_y();
            }

            // Если x лежит между точками i и i+1
            if (x > x1 && x < x2) {
                double y1 = points[i].getter_y();
                double y2 = points[i + 1].getter_y();

                // Линейная интерполяция: y = y1 + (y2 - y1) * (x - x1) / (x2 - x1)
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }
    public int getPointsCount() {
        return pointsCount;
    }
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index<0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        // проверяем границы для новой точки
        if (index > 0 && (point.getter_x() < points[index - 1].getter_x()
                ||Comparison(point.getter_x(),points[index - 1].getter_x()))) {
            throw new InappropriateFunctionPointException("X лежит вне определенного интервала");
        }
        if (index < pointsCount - 1 && (point.getter_x() > points[index + 1].getter_x())
                ||Comparison(point.getter_x(),points[index + 1].getter_x())) {
            throw new InappropriateFunctionPointException("X лежит вне определенного интервала");
        }

        // копия точки
        points[index] = new FunctionPoint(point);
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index<0 || index>= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getter_x();
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index<0 || index>= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (index > 0 && (x < points[index - 1].getter_x()||Comparison(x, points[index - 1].getter_x()))) {
            throw new InappropriateFunctionPointException("X лежит вне определенного интервала");
        }
        if (index < pointsCount - 1 && (x > points[index + 1].getter_x()||Comparison(x, points[index + 1].getter_x()))) {
            throw new InappropriateFunctionPointException("X лежит вне определенного интервала");
        }
        points[index].setter_x(x);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getter_y();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || index>= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        points[index].setter_y(y);
    }

    //методы изменения количества точек
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if(pointsCount<3){
            throw new IllegalStateException("Количество точек меньше 3");
        }
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // позиция для вставки индекса
        int index_insert = 0;
        while (index_insert < pointsCount && points[index_insert].getter_x() < point.getter_x()) {
            index_insert++;
        }

        // если точка с такой координатой x уже есть
        if (index_insert < pointsCount && Comparison(points[index_insert].getter_x(), point.getter_x())) {
            throw new InappropriateFunctionPointException("Точка с такой координатой x уже есть");
        }

        // проверка, есть ли место в массиве
        if (pointsCount == points.length) {
            // если не хватает, то увеличим место в 2 раза
            FunctionPoint[] newArray = new FunctionPoint[(int)(points.length * 2) + 1];
            System.arraycopy(points, 0, newArray, 0, pointsCount);
            points = newArray;
        }

        // двигаем точки вправо чтобы освободить место
        if (index_insert < pointsCount) {
            System.arraycopy(points, index_insert, points, index_insert + 1, pointsCount - index_insert);
        }

        // вставляем копию новой точки
        points[index_insert] = new FunctionPoint(point);
        pointsCount++;
    }
}
