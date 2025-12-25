package functions;

public class FunctionPoint{

    private double x;
    private double y;

    //Конструкторы из задания 2
    public FunctionPoint(double x,double y){
        this.x=x;
        this.y=y;
    }
    public FunctionPoint(FunctionPoint point){
        this.x=point.x;
        this.y=point.y;
    }
    public FunctionPoint(){
        this(0.0, 0.0);
    }

    //Геттеры и сеттеры
    public double getter_x(){ return x;}
    public double getter_y(){ return y;}
    public void setter_x(double x){ this.x=x;}
    public void setter_y(double y){ this.y=y;}
}
