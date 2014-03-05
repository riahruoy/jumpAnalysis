package jumpAnalysis;
import jumpAnalysis.AccelData;
import jumpAnalysis.outputItx;
import jumpAnalysis.MeanShiftSmoothing1D;
import jumpAnalysis.MeanShiftClusterize2D;
import jumpAnalysis.MeanShiftSmoothing3D;

public class run{
    public static void main(String[] args) {
    	String dir = "C:\\Users\\Keisuke\\Dropbox\\shared_fujii\\jump_logger\\";
        
    	AccelData accdata = new AccelData();
        accdata.read(dir + "sensor.accelerometer.txt");
        
        outputItx.writeItx(accdata.getT(), dir + "accel_t.itx", "accel_t");
        outputItx.writeItx(accdata.getX(), dir + "accel_x.itx", "accel_x");
        outputItx.writeItx(accdata.getY(), dir + "accel_y.itx", "accel_y");
        outputItx.writeItx(accdata.getZ(), dir + "accel_z.itx", "accel_z");
        outputItx.writeItx(accdata.getamp(), dir + "amp.itx", "amp");

        double dg = 8.0;
        MeanShiftSmoothing3D msc3D = new MeanShiftSmoothing3D ();
        msc3D.set(accdata, 0.5, dg, dg, dg, 1.0);
        msc3D.run();
        outputItx.writeItx(msc3D.getXResult(), dir + "rslt_x.itx", "rslt_x");
        outputItx.writeItx(msc3D.getYResult(), dir + "rslt_y.itx", "rslt_y");
        outputItx.writeItx(msc3D.getZResult(), dir + "rslt_z.itx", "rslt_z");

        GyroData gyrodata = new GyroData ();
        gyrodata.read(dir + "sensor.gyroscope.txt");
        
        outputItx.writeItx(gyrodata.getT(), dir + "gyro_t.itx", "gyro_t");
        outputItx.writeItx(gyrodata.getX(), dir + "gyro_x.itx", "gyro_x");
        outputItx.writeItx(gyrodata.getY(), dir + "gyro_y.itx", "gyro_y");
        outputItx.writeItx(gyrodata.getZ(), dir + "gyro_z.itx", "gyro_z");
        outputItx.writeItx(gyrodata.getamp(), dir + "gyro_amp.itx", "gyro_amp");

        double dgyro = 4.0;
        MeanShiftSmoothing3D msc3D2 = new MeanShiftSmoothing3D ();
        msc3D2.set(gyrodata, 0.5, dgyro, dgyro, dgyro, 1.0);
        msc3D2.run();
        outputItx.writeItx(msc3D2.getXResult(), dir + "gyroRslt_x.itx", "gyroRslt_x");
        outputItx.writeItx(msc3D2.getYResult(), dir + "gyroRslt_y.itx", "gyroRslt_y");
        outputItx.writeItx(msc3D2.getZResult(), dir + "gyroRslt_z.itx", "gyroRslt_z");

        

    }

}
