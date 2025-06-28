package pedroPathing.SUBSYSTEMS;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import lombok.Getter;

public class Vision extends SubsystemBase {
    private final Limelight3A limelight;
    private LLResult result;
    @Getter
    private List<LLResultTypes.DetectorResult> detections;
    private List<List<Double>> corners;

    public Vision(final HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0); // RedYellow pipeline
    }

    public void initializeCamera() {
        limelight.setPollRateHz(50);
        limelight.start();
        limelight.pipelineSwitch(0);
    }

    public double getDistance() {
        double ty = getTy(0.0);
        if (ty == 0) return 0;
        double angleToGoal = CAMERA_ANGLE + ty;
        double distanceMM = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(Math.toRadians(angleToGoal));
        return Math.abs(distanceMM) - sampleToRobotDistance;
    }

    public double getTx(double defaultValue) {
        return (result != null) ? result.getTx() : defaultValue;
    }

    public double getTy(double defaultValue) {
        return (result != null) ? result.getTy() : defaultValue;
    }

    public double getStrafeOffset() {
        double tx = getTx(0);
        return (tx != 0)
                ? tx * strafeConversionFactor - cameraStrafeToBot / 25.4
                : 0;
    }

    public boolean isTargetVisible() {
        return result != null && result.isValid() && !detections.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public List<LLResultTypes.DetectorResult> getDetections() {
        return detections;
    }

    @Override
    public void periodic() {
        result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            detections = result.getDetectorResults();
            if (detections != null && !detections.isEmpty()) {
                corners = detections.get(0).getTargetCorners();
            } else {
                corners = null;
            }
        } else {
            corners = null;
        }
    }

    /**
     * Returns orientation flag:
     * 1 if contour is horizontal (width ≥ 1.25 × height), else 0.
     */
    public int getOrientationFlag() {
        if (corners == null || corners.size() != 4) {
            return 0;
        }
        List<Double> tl = corners.get(0);
        List<Double> tr = corners.get(1);
        List<Double> br = corners.get(2);
        List<Double> bl = corners.get(3);

        if (tl == null || tr == null || br == null || bl == null) {
            return 0;
        }

        double top = Math.hypot(tr.get(0) - tl.get(0), tr.get(1) - tl.get(1));
        double bottom = Math.hypot(br.get(0) - bl.get(0), br.get(1) - bl.get(1));
        double left = Math.hypot(bl.get(0) - tl.get(0), bl.get(1) - tl.get(1));
        double right = Math.hypot(br.get(0) - tr.get(0), br.get(1) - tr.get(1));

        double width = (top + bottom) / 2.0;
        double height = (left + right) / 2.0;

        return (width >= 1.25 * height) ? 1 : 0;
    }

    // Constants
    public static final double CAMERA_HEIGHT = 279;
    public static final double CAMERA_ANGLE = -30.0;
    public static final double TARGET_HEIGHT = 19.05;
    public static final double strafeConversionFactor = 6.6667;
    public static final double cameraStrafeToBot = 13.25;
    public static final double sampleToRobotDistance = 145;
}
