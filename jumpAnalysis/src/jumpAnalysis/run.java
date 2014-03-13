package jumpAnalysis;
import jumpAnalysis.AccelData;
import jumpAnalysis.outputItx;
import jumpAnalysis.GpsData;
import jumpAnalysis.MeanShiftSmoothing1D;
import jumpAnalysis.MeanShiftClusterize2D;
import jumpAnalysis.MeanShiftSmoothing3D;

public class run{
    public static void main(String[] args) {
    	String dir = "data/cccc/2014-03-09 15%3A34%3A15/";
        
    	
    	GpsData gpsdata = new GpsData();
    	gpsdata.read(dir + "location.gps.txt");
        outputItx.writeItx(gpsdata.getT(), dir + "gps_t.itx", "gps_t");
        outputItx.writeItx(gpsdata.getX(), dir + "gps_x.itx", "gps_x");
        outputItx.writeItx(gpsdata.getY(), dir + "gps_y.itx", "gps_y");
        outputItx.writeItx(gpsdata.getZ(), dir + "gps_z.itx", "gps_z");
    	gpsdata.getSpeed(3.0, 1.0);
    	outputItx.writeItx(gpsdata.getXbar(), dir + "gps_xbar.itx", "gps_xbar");
        outputItx.writeItx(gpsdata.getYbar(), dir + "gps_ybar.itx", "gps_ybar");
        outputItx.writeItx(gpsdata.getZbar(), dir + "gps_zbar.itx", "gps_zbar");
    	outputItx.writeItx(gpsdata.getVx(), dir + "gps_vx.itx", "gps_vx");
        outputItx.writeItx(gpsdata.getVy(), dir + "gps_vy.itx", "gps_vy");
        outputItx.writeItx(gpsdata.getVz(), dir + "gps_vz.itx", "gps_vz");
        outputItx.writeItx(gpsdata.getVamp(), dir + "gps_vamp.itx", "gps_vamp");
    	
    	AccelData accdata = new AccelData();
        accdata.read(dir + "sensor.accelerometer.txt");
        accdata.detectJump(0.1 ,15.0, 8.0, 0.5);
    	outputItx.writeItx(accdata.getX(), dir + "acc_x.itx", "acc_x");
    	outputItx.writeItx(accdata.getY(), dir + "acc_y.itx", "acc_y");
    	outputItx.writeItx(accdata.getZ(), dir + "acc_z.itx", "acc_z");
    	outputItx.writeItx(accdata.getXsmth(), dir + "acc_x_smth.itx", "acc_x_smth");
    	outputItx.writeItx(accdata.getYsmth(), dir + "acc_y_smth.itx", "acc_y_smth");
    	outputItx.writeItx(accdata.getZsmth(), dir + "acc_z_smth.itx", "acc_z_smth");
    	outputItx.writeItx(accdata.getamp(), dir + "acc_amp.itx", "acc_amp");
    	outputItx.writeItx(accdata.getT(), dir + "acc_t.itx", "acc_t");
        outputItx.writeItx(accdata.getDataSmth().getamp(), dir + "rslt_amp.itx", "rslt_amp");
        outputItx.writeItx(accdata.getJump_t_start(), dir + "jump_t_start.itx", "jump_t_start");
        outputItx.writeItx(accdata.getJump_t_duration(), dir + "Jump_t_duration.itx", "Jump_t_duration");
        outputItx.writeItx(accdata.getJumpDistance(gpsdata.getVamp1d()), dir + "Jump_distance.itx", "Jump_distance");
/*
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
*/
        

    }

}
