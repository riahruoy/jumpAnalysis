package jumpAnalysis;
import jumpAnalysis.threeDdata;

public class run{
    public static void main(String[] args) {
        System.out.println("Hello World!");
        threeDdata data = new threeDdata();
        data.read("C:\\Users\\Keisuke\\Dropbox\\shared_fujii\\jump_logger\\sensor.accelerometer.txt");
        
        System.out.println("index(50.0) = " + data.getFloorIndex(50.0));
        System.out.println("x[2] = " + data.getX(data.getT(2)));
        System.out.println("x(50.0) = " + data.getX(50.0));
        System.out.println("y(50.0) = " + data.getY(50.0));
        System.out.println("z(50.0) = " + data.getZ(50.0));
    }

}
