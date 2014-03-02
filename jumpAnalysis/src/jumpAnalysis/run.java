package jumpAnalysis;
import jumpAnalysis.AccelData;
import jumpAnalysis.outputItx;
public class run{
    public static void main(String[] args) {
        System.out.println("Hello World!");
        AccelData data = new AccelData();
        String dir = "C:\\Users\\Keisuke\\Dropbox\\shared_fujii\\jump_logger\\";
        data.read(dir + "sensor.accelerometer.txt");
        
        outputItx.writeItx(data.getT().clone(), dir + "accel_t.itx", "accel_t");
        outputItx.writeItx(data.getX().clone(), dir + "accel_x.itx", "accel_x");
        outputItx.writeItx(data.getY().clone(), dir + "accel_y.itx", "accel_y");
        
    }

}
