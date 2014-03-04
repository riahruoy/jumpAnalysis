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
        msc2D.set(data.getAmp1d(), 0.5, 12.0, 1.0);
        msc2D.run();
        outputItx.writeItx(msc2D.getResult(), dir + "rslt.itx", "rslt");

//        outputItx.writeItx(msc2D.getResult1D().smooth(3).getX(), dir + "rslt_smth.itx", "rslt_smth");
        MeanShiftClusterize2D msc2D_2 = new MeanShiftClusterize2D ();
        msc2D_2.set(msc2D.getResult1D(), 0.1, 18.0, 1.0);
        msc2D_2.run();
        outputItx.writeItx(msc2D_2.getResult(), dir + "rslt2.itx", "rslt2");
    }

}
