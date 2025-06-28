//package pedroPathing.SUBSYSTEMS;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.arcrobotics.ftclib.command.Subsystem;
//import com.arcrobotics.ftclib.command.SubsystemBase;
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.LLResultTypes;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//import java.util.List;
//
//import edu.wpi.first.math.MathUtil;
//
//
//public class VisionNeuralDetectors extends SubsystemBase {
//    Limelight3A limelight;
//    int RedYellow = 0;
//    int BlueYellow = 1;
//    int Red = 0; // placeholder
//    int Blue = 1;//Placeholder
//    private LLResult result;
//    List<LLResultTypes.DetectorResult> detections;
//    public static double CAMERA_HEIGHT = 349 - 16;
//    public static double CAMERA_ANGLE = -45.0;
//    public static double TARGET_HEIGHT = 19.05;
//
//
//
//    public static double strafeConversionFactor = 6.6667;
//    public static double cameraStrafeToBot = -13.25;
//
//    public static double sampleToRobotDistance = 145;
//
//    Telemetry telemetry;
//
//    public VisionNeuralDetectors(final HardwareMap hardwareMap, Telemetry telemetry) {
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");
//
//        limelight.pipelineSwitch(RedYellow);
//
//
//
//
//    }
//    public void initializeCamera() {
//        limelight.setPollRateHz(50);
//        limelight.start();
//        limelight.pipelineSwitch(RedYellow);
//    }
//    public double getDistance(){
//        double ty = getTy(0.0);
//        if (MathUtil.isNear(0, ty, 0.01)) {
//            return 0;
//        }
//        double angleToGoalDegrees = CAMERA_ANGLE + ty;
//        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
//        double distanceMM = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(angleToGoalRadians);
//        return Math.abs(distanceMM) - sampleToRobotDistance;
//
//
//    }
//    public double getTx(double defaultValue) {
//        if (result == null) {
//            return defaultValue;
//        }
//        return result.getTx();
//    }
//    public double getStrafeOffset() {
//        double tx = getTx(0);
//        if (tx != 0) {
//            return tx * strafeConversionFactor - cameraStrafeToBot / 25.4;
//        }
//        return 0;
//    }
//    public boolean isTargetVisible() {
//        if (result == null) {
//            return false;
//        }
//        return !MathUtil.isNear(0, result.getTa(), 0.0001);
//    }
//    public boolean getAspectRatio(){
//        return true;
//    }
//
//
//    public double getTy(double defaultValue) {
//        if (result == null) {
//            return defaultValue;
//        }
//        return result.getTy();
//    }
//
//    @Override
//    public void periodic() {
//        LLResult result = limelight.getLatestResult();
//        List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();
//    }
//}
