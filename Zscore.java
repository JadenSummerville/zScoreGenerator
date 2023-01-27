import java.util.Random;
import org.apache.commons.math3.special.Erf;

/**
 * <b>Zscore</B> is an immutable random number genorator from a normally distributed graph.
 * 
 * <p>Each Zscore has a maximum and minimum range for it's values, a standard deviation, and a mean.
 *  All values are normally distributed and within the range of max_value and min_value</p>
 */

public class Zscore {
    public static void main(String[] args) throws Exception{
        
        Zscore b = new Zscore(5,2,20,.5);
        for(int i = 0; i < 10; i++){
        System.out.println(b.generate_value());}
    }
    private final double max_value;
    private final double min_value;
    private final double mean;
    private final double standard_deviation;
    private static final double log_base_2 = Math.log(2);
    Zscore(double mean, double standard_deviation) throws Exception{
        this.mean = mean;
        this.standard_deviation = standard_deviation;
        max_value = (100-mean)/standard_deviation;
        min_value = -(100+mean)/standard_deviation;
        checkRep();
    }
    Zscore(double mean, double standard_deviation, double max_value, double min_value) throws Exception{
        this.mean = mean;
        this.standard_deviation = standard_deviation;
        this.max_value = (max_value-mean)/standard_deviation;
        this.min_value = (min_value-mean)/standard_deviation;
        loops_required(this.max_value, this.min_value);
        checkRep();
    }
    private void checkRep() throws Exception{
        if(max_value <= min_value){
            throw new Exception("Rep invarient failure. Maximum value is not less than minimum value.");
        }
        if(standard_deviation <= 0){
            throw new Exception("Rep invarient failure. Negative or zero standard deviation.");
        }
    }
    public double mean(){
        return mean;
    }
    public double standard_deviation(){
        return standard_deviation;
    }
    public double max_range(){
        return max_value*standard_deviation+mean;
    }
    public double min_range(){
        return min_value*standard_deviation+mean;
    }
    private static double integrate(double value){
        return Erf.erf(value*Math.pow(2,-.5))/2;
    }
    static double generate_zScore() throws Exception{
        return generate_zScore(100,-100);
    }
    static double generate_zScore(double max, double min) throws Exception{
        int loops = loops_required(max, min);
        Random random = new Random();
        for(int i = 0; i < loops; i++){
            if (random.nextDouble()>(integrate(min)-integrate((max+min)/2))/(integrate(min)-integrate(max))){
                min = (max + min)/2;
            }else{
                max = (max + min)/2;
            }
        }
        return (max+min)/2;
    }
    double generate_value() throws Exception{
        return standard_deviation*generate_zScore(max_value, min_value) + mean;
    }
    private static int loops_required(double max_value, double min_value) throws Exception{
        if(max_value == min_value){
            throw new Exception("The maximum value and the minimum value are the same.");
        }
        if(max_value < min_value){
            throw new Exception("The maximum value and the minimum value are backwords.");
        }
        double total_range = max_value - min_value;
        double loops = Math.log(total_range)/log_base_2;
        return (int)loops + 900;
    }
}