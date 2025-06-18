package pedroPathing.TeleOP;

import static android.os.SystemClock.sleep;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@TeleOp(name = "Internationals TeleOp PedroPathing", group = "Examples")
public class Internationals_TeleOP_Pedro extends OpMode {

    private Follower follower;
    private final Pose startPose = new Pose(0, 0, 0);

    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;
    private Vision vision;

    // Slide positions constants
    private static final int SLIDES_POSITION0 = 0;
    private static final int SLIDES_POSITION1 = 350;
    private static final int SLIDES_POSITION2 = 700;

    // Claw servo positions
    private static final double CLAW_OPEN = 0.2;
    private static final double CLAW_CLOSED = 0.8;
    private static final double CLAW_UP = 0.9;
    private static final double CLAW_DOWN = 0.1;
    private boolean atPoint = false;


    private Vision.SampleColor currentColor = Vision.SampleColor.RED;
    private boolean lastButtonState = false;

    @Override
    public void init() {
        // Initialize follower with constants and starting pose
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        // Initialize subsystems
        cascadeSlides = new CascadeSlides();
        cascadePivot = new CascadePivot();
        claw = new Claw();
        vision = new Vision(hardwareMap, telemetry);

        cascadeSlides.init(hardwareMap);
        cascadePivot.init(hardwareMap);
        claw.init(hardwareMap);
        vision.initializeCamera();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
        telemetry.addData("Pivot pos", cascadePivot.getCurrentPosition());
        telemetry.addLine("Vision initialized. Waiting for start...");
        telemetry.addLine("Vision is init for RED");
        telemetry.update();
    }

    @Override
    public void start() {
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        // PedroPathing follower drive control (robot-centric)
        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();

        // Vision periodic update
        vision.periodic();

        // Claw controls (gamepad2)
        if (gamepad2.left_bumper) {
            claw.setServoPosOC(CLAW_OPEN);
        } else if (gamepad2.right_bumper) {
            claw.setServoPosOC(CLAW_CLOSED);
        }

        if (gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1 ) {
            claw.turnClaw(gamepad2.left_trigger,gamepad2.right_trigger);
        }

        if (gamepad2.left_bumper) {
            claw.setServoPosUD(CLAW_UP);
        } else if (gamepad2.right_bumper) {
            claw.setServoPosUD(CLAW_DOWN);
        }

        // Slides manual power control (gamepad2)
        if (gamepad2.y) {
            cascadeSlides.setPower(0.8);
        } else if (gamepad2.a) {
            cascadeSlides.setPower(-0.8);
        }

        // Slides PIDF position control (gamepad2)
        if (gamepad2.a) {
            cascadeSlides.moveSlidesTo(SLIDES_POSITION1);
        } else if (gamepad2.b) {
            cascadeSlides.moveSlidesTo(SLIDES_POSITION2);
        } else if (gamepad2.x) {
            cascadeSlides.moveSlidesTo(SLIDES_POSITION0);
        } else if (Math.abs(gamepad2.left_stick_y) > 0.15) {
            double slidesPower = -gamepad2.left_stick_y * 0.8;
            cascadeSlides.setPower(slidesPower);
        } else {
            cascadeSlides.stop();
        }

        // Pivot controls (gamepad1 bumpers)
        if (gamepad1.left_bumper) {
            cascadePivot.setPower(-0.5);
        } else if (gamepad1.right_bumper) {
            cascadePivot.setPower(0.5);
        } else {
            cascadePivot.setPower(0);
        }

        // Vision color cycling on gamepad1.a button press


        // Vision servo rotation on gamepad1.b
        if (gamepad1.b) {
//            Double angle = vision.getTurnServoDegree(); // Should be 0 to 360
//
//            if (angle != null && angle != 0) {
//                angle = Math.max(0, Math.min(angle, 360));
//                double servoPos = angle / 360.0;
//                claw.setServoPosRot(servoPos);
//            }
            Pose currentPose = follower.getPose();

            Double angle = vision.getTurnServoDegree(); // Should be 0 to 360

            if (angle != 0) {
                // Clamp just in case
                //0.98562628 = ticks per mm
                angle = Math.max(0, Math.min(angle, 360));
                double perpangle = (angle + 90) % 360;

                // Map 0-360 degrees to 0.0-1.0 servo range
                double servoPos = perpangle / 360.0;
                cascadePivot.setPos(-150);
                claw.setServoPosRot(servoPos);
                claw.setServoPosOC(CLAW_OPEN);





                Point startPoint = new Point(currentPose.getX(), currentPose.getY());
                Point endPoint = new Point(currentPose.getX() , currentPose.getY()- (vision.getStrafeOffset() / 25.4));
                follower.setMaxPower(0.3);
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(startPoint, endPoint))
                        .setConstantHeadingInterpolation(currentPose.getHeading()) // Keeps current heading
                        .build());

                    while (!follower.atPoint(endPoint, 0.3, 0.3)) {
                        follower.update();



                    }

                    follower.breakFollowing();
                    follower.startTeleopDrive();
                    cascadeSlides.moveSlidesTo((int) Math.round(vision.getDistance() + 170));

                    cascadePivot.setPos(30);
                    while (!(cascadePivot.getCurrentPosition() > 20 && cascadePivot.getCurrentPosition() <40 )){

                    }
                    claw.setServoPosOC(CLAW_CLOSED);


            }



        }
        if (gamepad1.x ) {
            follower.breakFollowing();
            follower.startTeleopDrive();
            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            follower.update();
            telemetry.addLine("Path following stopped manually!");
        }

        // Telemetry updates
        telemetry.addData("Follower X", follower.getPose().getX());
        telemetry.addData("Follower Y", follower.getPose().getY());
        telemetry.addData("Follower Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));

        telemetry.addData("Slides Pos", cascadeSlides.getCurrentPosition());
        telemetry.addData("Pivot Pos", cascadePivot.getCurrentPosition());
        telemetry.addData("Vision Target Visible", vision.isTargetVisible());
        telemetry.addData("Vision Distance", vision.getDistance());
        telemetry.addData("Vision Angle", vision.getTurnServoDegree());
        telemetry.addData("Current Color", currentColor.name());

        telemetry.update();
    }

    @Override
    public void stop() {
        // Optional cleanup if needed
    }
}
