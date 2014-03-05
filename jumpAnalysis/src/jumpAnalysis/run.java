package jumpAnalysis;
import jumpAnalysis.AccelData;
import jumpAnalysis.outputItx;
import jumpAnalysis.MeanShiftSmoothing1D;
import jumpAnalysis.MeanShiftClusterize2D;
import jumpAnalysis.MeanShiftSmoothing3D;

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
        
        MeanShiftSmoothing1D msc1D = new MeanShiftSmoothing1D ();
        msc1D.set(data.getAmp1d().smooth(1), 0.8, 4.0, 1.0);
        msc1D.run();
        outputItx.writeItx(msc1D.getResult(), dir + "rslt.itx", "rslt");

        double dg = 4.0;
        MeanShiftSmoothing3D msc3D = new MeanShiftSmoothing3D ();
        msc3D.set(data, 0.8, dg, dg, dg, 1.0);
        msc3D.run();
        outputItx.writeItx(msc3D.getResult(), dir + "rslt_3.itx", "rslt_3");

//        MeanShiftClusterize1D msc1D_2 = new MeanShiftClusterize1D ();
//        msc1D_2.set(msc1D.getResult1D().smooth(1), 0.8, 2.0, 1.0);
//        msc1D_2.run();
//        outputItx.writeItx(msc1D_2.getResult(), dir + "rslt2.itx", "rslt2");

        MeanShiftClusterize2D msc2D = new MeanShiftClusterize2D ();
        msc2D.set(data.getAmp1d(), 0.3, 20.0, 1.0);
        msc2D.run();
        outputItx.writeItx(msc2D.getResult(), dir + "rslt2.itx", "rslt2");


    }

}
