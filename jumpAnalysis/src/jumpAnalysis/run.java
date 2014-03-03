package jumpAnalysis;
import jumpAnalysis.AccelData;
import jumpAnalysis.outputItx;
import jumpAnalysis.MeanShiftClusterize2D;
public class run{
    public static void main(String[] args) {
        System.out.println("Hello World!");
        AccelData data = new AccelData();
        String dir = "C:\\Users\\Keisuke\\Dropbox\\shared_fujii\\jump_logger\\";
        data.read(dir + "sensor.accelerometer.txt");
        
        outputItx.writeItx(data.getT(), dir + "accel_t.itx", "accel_t");
        outputItx.writeItx(data.getX(), dir + "accel_x.itx", "accel_x");
        outputItx.writeItx(data.getY(), dir + "accel_y.itx", "accel_y");
        outputItx.writeItx(data.getamp(), dir + "amp.itx", "amp");
        
        MeanShiftClusterize2D msc2D = new MeanShiftClusterize2D ();
        msc2D.set(data.getT(), data.getamp(), 0.5, 10.0, 1.0);
        msc2D.run();
        outputItx.writeItx(msc2D.getResult(), dir + "rslt.itx", "rslt");
    }

}
