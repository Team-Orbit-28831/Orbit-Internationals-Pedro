//package pedroPathing.TeleOP;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import pedroPathing.SUBSYSTEMS.Drivetrain;
//import pedroPathing.SUBSYSTEMS.CascadeSlides;
//import pedroPathing.SUBSYSTEMS.CascadePivot;
//import pedroPathing.SUBSYSTEMS.Claw;
//import pedroPathing.SUBSYSTEMS.Vision;
//import com.pedropathing.follower.Follower;
//
//@TeleOp(name = "Internationals TeleOp teset do no use", group = "Linear OpMode")
//public class Teleop_Internationals_Test extends LinearOpMode {
//    private Drivetrain drivetrain;
//    private CascadeSlides cascadeSlides;
//    private CascadePivot cascadePivot;
//    private Claw claw;
//    private Vision vision;
//    private Follower follower;
//
//    // Slide positions
//    private static final int SLIDES_POSITION0 = 0;
//    private static final int SLIDES_POSITION1 = 350;
//    private static final int SLIDES_POSITION2 = 700;
//
//    // Claw positions - SAFE STARTING VALUES
//    private static final double CLAW_OPEN = 0.6;
//    private static final double CLAW_CLOSED = 0.3;
//    private static final double CLAW_UP = 0.8;          // Start higher
//    private static final double CLAW_DOWN = 0.2;        // Start lower
//    private static final double CLAW_NEUTRAL = 0.5;     // True middle position
//
//    // variables
//    private Vision.SampleColor currentColor = Vision.SampleColor.RED;
//    private boolean lastButtonState = false;
//    private int colorIndex = 0;
//
//    @Override
//    public void runOpMode() {
//        drivetrain = new Drivetrain();
//        cascadeSlides = new CascadeSlides();
//        cascadePivot = new CascadePivot();
//        claw = new Claw();
//
//        drivetrain.init(hardwareMap);
//        cascadeSlides.init(hardwareMap);
//        cascadePivot.init(hardwareMap);
//        claw.init(hardwareMap);
//        vision = new Vision(hardwareMap, telemetry);
//
//        vision.initializeCamera();
//
//        // Initialize claw to neutral position
//        claw.setServoPosUD(CLAW_NEUTRAL);
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
//        telemetry.addData("Pivot pos", cascadePivot.getCurrentPosition());
//        telemetry.addLine("Vision initialized. Waiting for start...");
//        telemetry.addLine("Vision is init for RED");
//        telemetry.addLine("Claw initialized to neutral position");
//        telemetry.update();
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            // drivetrain
//            drivetrain.drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
//            vision.periodic();
//
//            // claw open/close
//            if (gamepad2.left_trigger > 0.1) {
//                claw.setServoPosOC(CLAW_OPEN);
//            } else if (gamepad2.right_trigger > 0.1) {
//                claw.setServoPosOC(CLAW_CLOSED);
//            }
//
//            // claw rotation
//            if (Math.abs(gamepad2.right_stick_x) > 0.1) {
//                claw.turnClaw(0, gamepad2.right_stick_x);
//            }
//
//            // claw up/down with improved control
//            if (gamepad2.left_bumper) {
//                claw.setServoPosUD(CLAW_UP);
//                telemetry.addData("Claw Command", "UP - Position: " + CLAW_UP);
//            } else if (gamepad2.right_bumper) {
//                claw.setServoPosUD(CLAW_DOWN);
//                telemetry.addData("Claw Command", "DOWN - Position: " + CLAW_DOWN);
//            }
//
//            // Manual claw up/down testing with D-pad
//            if (gamepad2.dpad_up) {
//                claw.setServoPosUD(0.6);
//                telemetry.addData("Manual Claw", "Testing position 0.6");
//            } else if (gamepad2.dpad_down) {
//                claw.setServoPosUD(0.2);
//                telemetry.addData("Manual Claw", "Testing position 0.2");
//            } else if (gamepad2.dpad_left) {
//                claw.setServoPosUD(0.4);
//                telemetry.addData("Manual Claw", "Testing position 0.4");
//            } else if (gamepad2.dpad_right) {
//                claw.setServoPosUD(CLAW_NEUTRAL);
//                telemetry.addData("Manual Claw", "Neutral position");
//            }
//
//            // slides - PIDF
//            if (gamepad2.a) {
//                cascadeSlides.moveSlidesTo(SLIDES_POSITION1);
//            } else if (gamepad2.b) {
//                cascadeSlides.moveSlidesTo(SLIDES_POSITION2);
//            } else if (gamepad2.x) {
//                cascadeSlides.moveSlidesTo(SLIDES_POSITION0);
//            } else if (Math.abs(gamepad2.left_stick_y) > 0.15) {
//                double slidesPower = -gamepad2.left_stick_y * 0.8;
//                cascadeSlides.setPower(slidesPower);
//            } else {
//                cascadeSlides.stop();
//            }
//
//            // pivot
//            if (gamepad1.left_bumper) {
//                cascadePivot.setPower(-0.5);
//            } else if (gamepad1.right_bumper) {
//                cascadePivot.setPower(0.5);
//            } else {
//                cascadePivot.setPower(0);
//            }
//
//            // vision color cycling
//            boolean currentButtonState = gamepad1.a;
//            if (currentButtonState && !lastButtonState) {
//                if (currentColor == Vision.SampleColor.RED) {
//                    currentColor = Vision.SampleColor.BLUE;
//                } else if (currentColor == Vision.SampleColor.BLUE) {
//                    currentColor = Vision.SampleColor.YELLOW;
//                } else if (currentColor == Vision.SampleColor.YELLOW) {
//                    currentColor = Vision.SampleColor.RED;
//                }
//            }
//            lastButtonState = currentButtonState;
//
//            // vision processing and servo rotation
//            if (gamepad1.b) {
//                Double angle = vision.getTurnServoDegree();
//
//                if (angle != null && angle != 0) {
//                    angle = Math.max(0, Math.min(angle, 360));
//                    double servoPos = angle / 360.0;
//                    claw.setServoPosRot(servoPos);
//                }
//
//                vision.setDetectionColor(currentColor);
//            }
//
//            // Enhanced telemetry for debugging
//            telemetry.addData("=== CONTROLS ===", "");
//            telemetry.addData("Left Bumper (UP)", gamepad2.left_bumper ? "PRESSED" : "Released");
//            telemetry.addData("Right Bumper (DOWN)", gamepad2.right_bumper ? "PRESSED" : "Released");
//            telemetry.addData("=== CLAW POSITIONS ===", "");
//            telemetry.addData("CLAW_UP", CLAW_UP);
//            telemetry.addData("CLAW_DOWN", CLAW_DOWN);
//            telemetry.addData("CLAW_NEUTRAL", CLAW_NEUTRAL);
//            telemetry.addData("=== SYSTEM STATUS ===", "");
//            telemetry.addData("Current Color", currentColor.name());
//            telemetry.addData("Current Slides Position", cascadeSlides.getCurrentPosition());
//            telemetry.addData("Current Pivot Position", cascadePivot.getCurrentPosition());
//            telemetry.addData("=== VISION ===", "");
//            telemetry.addData("Vision Target", vision.isTargetVisible());
//            telemetry.addData("Distance", vision.getDistance());
//            telemetry.addData("Angle", vision.getTurnServoDegree());
//            telemetry.addData("=== DEBUG INFO ===", "");
//            telemetry.addData("Use D-pad to test", "Up/Down/Left/Right for different positions");
//            telemetry.update();
//        }
//    }
//}