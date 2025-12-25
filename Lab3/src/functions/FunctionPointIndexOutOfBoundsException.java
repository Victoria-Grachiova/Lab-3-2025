package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException(){
        super();
    }
    public FunctionPointIndexOutOfBoundsException(String msg){
        super(msg);
    }
    public FunctionPointIndexOutOfBoundsException(int number){
        super("Набор точек выходит за границы: "+ number);
    }
}
