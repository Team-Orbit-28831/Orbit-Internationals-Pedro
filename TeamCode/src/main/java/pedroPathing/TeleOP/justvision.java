package pedroPathing.TeleOP;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * This is an example teleop that showcases movement and robot-centric driving with vision integration.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 12/30/2024
 */

@TeleOp(name = "just vision i need this to work", group = "Examples")
public class justvision extends OpMode {
    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);

    // Vision subsystem
    private Vision vision;

    // State management for path following
    private boolean inTeleopMode = true;
    private boolean mechanismsActivated = false;
    private boolean lastButtonBState = false;

    private Vision.SampleColor currentColor = Vision.SampleColor.RED;
    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;

    /** This method is call once when init is played, it initializes the follower **/
    @Override
    public void init() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        // Initialize vision
        vision = new Vision(hardwareMap, telemetry);
        vision.initializeCamera();

        telemetry.addLine("Vision initialized. Waiting for start...");
        telemetry.addLine("Vision is init for RED");
        telemetry.update();
    }

    /** This method is called continuously after Init while waiting to be started. **/
    @Override
    public void init_loop() {
    }

    /** This method is called once at the start of the OpMode. **/
    @Override
    public void start() {
        follower.startTeleopDrive();
        inTeleopMode = true;
    }

    /** This is the main loop of the opmode and runs continuously after play **/
    @Override
    public void loop() {
        // Update vision
        vision.periodic();


        // Handle path following state machine
        if (inTeleopMode) {
            // Normal teleop control
            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            follower.update();

            // Check for path trigger (gamepad1.b)
            boolean currentButtonB = gamepad1.b;
            if (currentButtonB && !lastButtonBState) { // Button just pressed
                Double angle = vision.getTurnServoDegree();
                if (angle != 0) {
                    startPathFollowing(angle);
                }
            }
            lastButtonBState = currentButtonB;

        } else {
            // Path following mode
            follower.update();

            if (follower.atParametricEnd() && !mechanismsActivated) {
                // Path complete - execute mechanisms
                follower.breakFollowing();
                follower.startTeleopDrive();

                // Set teleop vectors once to initialize
                follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
                follower.update();
                cascadePivot.setPivotTarget(-500);
                claw.setServoPosOC(0.8);
                cascadeSlides.setSlideTarget((int) Math.round(vision.getDistance() + 400));
                cascadePivot.setPivotTarget(-10);
                claw.setServoPosOC(0.2);

                // Return to teleop mode
                inTeleopMode = true;
                mechanismsActivated = false;

                telemetry.addLine("Vision path complete!");
            }
        }

        /* Telemetry Outputs */
        telemetry.addData("Mode", inTeleopMode ? "Teleop" : "Path Following");
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

        // Vision telemetry
        telemetry.addData("Vision Target Visible", vision.isTargetVisible());
        telemetry.addData("Vision Distance", vision.getDistance());
        telemetry.addData("Vision Angle", vision.getTurnServoDegree());
        telemetry.addData("Current Color", currentColor.name());
        telemetry.addLine("Press B to start vision path following");

        /* Update Telemetry to the Driver Hub */
        telemetry.update();
    }

    private void startPathFollowing(Double angle) {
        Pose currentPose = follower.getPose();

        // Clamp angle and calculate perpendicular angle
        angle = Math.max(0, Math.min(angle, 360));
        double perpangle = (angle + 90) % 360;
        double servoPos = perpangle / 360.0;

        // Pre-position mechanisms (add your subsystem calls here)
        // Example: cascadePivot.setPivotTarget(-20);
        // Example: claw.setServoPosRot(servoPos);
        // Example: claw.setServoPosOC(CLAW_OPEN);

        // Create and start path
        Point startPoint = new Point(currentPose.getX(), currentPose.getY());
        Point endPoint = new Point(currentPose.getX(), currentPose.getY() - (vision.getDistance() / 25.4));

        follower.setMaxPower(0.3);
        follower.followPath(follower.pathBuilder()
                .addPath(new BezierLine(startPoint, endPoint))
                .setConstantHeadingInterpolation(currentPose.getHeading())
                .build());

        // Switch to path following mode
        inTeleopMode = false;
        mechanismsActivated = false;

        telemetry.addLine("Vision path following started!");
        telemetry.update();
    }

    /** We do not use this because everything automatically should disable **/
    @Override
    public void stop() {
    }
}