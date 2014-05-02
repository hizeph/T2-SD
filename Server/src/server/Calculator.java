
package server;

/**
 *
 * @author Hizeph
 */
public class Calculator {
    
    public double sum(double a, double b){
        return a+b;
    }
    
    public double minus(double a, double b){
        return a-b;
    }
    
    public double divide(double a, double b){
        if (b!=0)
            return a/b;
        else
            return 0;
    }
    
    public double multiply(double a, double b){
        return a*b;
    }
    
}
