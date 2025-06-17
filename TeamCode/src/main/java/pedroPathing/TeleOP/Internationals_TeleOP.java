package pedroPathing.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.SUBSYSTEMS.Drivetrain;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;
import com.pedropathing.follower.Follower;

@TeleOp(name = "Internationals TeleOp", group = "Linear OpMode")
public class Internationals_TeleOP extends LinearOpMode {
    private Drivetrain drivetrain;
    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;
    private Vision vision;
    private Follower follower;
    // Slide positions
    private static final int SLIDES_POSITION0 = 0;
    private static final int SLIDES_POSITION1 = 350;
    private static final int SLIDES_POSITION2 = 700;

    // Claw positions
    private static final double CLAW_OPEN = 0.8;
    private static final double CLAW_CLOSED = 0.2;
    private static final double CLAW_UP = 0.9;
    private static final double CLAW_DOWN = 0.1;
    private Vision.SampleColor currentColor = Vision.SampleColor.RED;
    private boolean lastButtonState = false;
    private int colorIndex = 0;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain();
        cascadeSlides = new CascadeSlides();
        cascadePivot = new CascadePivot();
        claw = new Claw();

        drivetrain.init(hardwareMap);
        cascadeSlides.init(hardwareMap);
        cascadePivot.init(hardwareMap);
        claw.init(hardwareMap);
        vision = new Vision(hardwareMap, telemetry);


        vision.initializeCamera();  // Start the Limelight camera

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
        telemetry.addData("Pivot pos", cascadePivot.getCurrentPosition());
        telemetry.addLine("Vision initialized. Waiting for start...");
        telemetry.addLine("Vision is init for RED");

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // drivetrain
            drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            vision.periodic();

            // claw
            if (gamepad2.left_trigger > 0.1) {
                claw.setServoPosOC(CLAW_OPEN);
            } else if (gamepad2.right_trigger > 0.1) {
                claw.setServoPosOC(CLAW_CLOSED);
            }

            if (Math.abs(gamepad2.right_stick_x) > 0.1) {
                claw.turnClaw(0, gamepad2.right_stick_x);
            }

            if (gamepad2.left_bumper) {
                claw.setServoPosUD(CLAW_UP);
            } else if (gamepad2.right_bumper) {
                claw.setServoPosUD(CLAW_DOWN);
            }

            // slides
            if (gamepad2.y) {
                cascadeSlides.setPower(0.8);
            } else if (gamepad2.a) {
                cascadeSlides.setPower(-0.8);
            }

            // slides - PIDF
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

            // pivot
            if (gamepad1.left_bumper) {
                cascadePivot.setPower(-0.5);
            } else if (gamepad1.right_bumper) {
                cascadePivot.setPower(0.5);
            }
            else{
                cascadePivot.setPower(0);
            }
            // vision
            boolean currentButtonState = gamepad1.a;

//            if (currentButtonState && !lastButtonState) {
//                // Cycle colors using if statements
//                if (currentColor == Vision.SampleColor.RED) {
//                    currentColor = Vision.SampleColor.BLUE;
//                } else if (currentColor == Vision.SampleColor.BLUE) {
//                    currentColor = Vision.SampleColor.YELLOW;
//                } else if (currentColor == Vision.SampleColor.YELLOW) {
//                    currentColor = Vision.SampleColor.RED;
//                }
            }
            if (gamepad1.b) {
                Double angle = vision.getTurnServoDegree(); // Should be 0 to 360

                if (angle != 0) {
                    // Clamp just in case
                    angle = Math.max(0, Math.min(angle, 360));

                    // Map 0-360 degrees to 0.0-1.0 servo range
                    double servoPos = angle / 360.0;

                    claw.setServoPosRot(servoPos);

                }

                // Set the detection color in your vision subsystem
                vision.setDetectionColor(currentColor);

                // Telemetry for feedback
                telemetry.addData("Current Color", currentColor.name());

                // Vision color cycling

// Optional: keep track of selected color in telemetry

                // telemetry
                telemetry.addData("Current Slides Position", cascadeSlides.getCurrentPosition());
                telemetry.addData("Current Pivot Position", cascadePivot.getCurrentPosition());
                telemetry.addData("Left Stick Y", gamepad2.left_stick_y);
                telemetry.addData("Left Stick X", gamepad2.left_stick_x);
                telemetry.addData("Right Stick X", gamepad2.right_stick_x);
                telemetry.addData("controlscounter", "temp");
                telemetry.addData("vision", vision.isTargetVisible());
                telemetry.addData("distance", vision.getDistance());
                telemetry.addData("angle", vision.getTurnServoDegree());


                telemetry.update();
            }
        }
    }

