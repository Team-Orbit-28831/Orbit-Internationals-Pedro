//package pedroPathing.TeleOP;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.BezierLine;
//import com.pedropathing.pathgen.Point;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import pedroPathing.SUBSYSTEMS.Drivetrain;
//import pedroPathing.SUBSYSTEMS.CascadeSlides;
//import pedroPathing.SUBSYSTEMS.CascadePivot;
//import pedroPathing.SUBSYSTEMS.Claw;
//import pedroPathing.SUBSYSTEMS.Vision;
//import pedroPathing.commands.PivotBask;
//import pedroPathing.commands.SlidesHighBask;
//import pedroPathing.commands.SlidesLowBask;
//import pedroPathing.commands.PivotSampleShort;
//import pedroPathing.constants.FConstants;
//import pedroPathing.constants.LConstants;
//
//import com.arcrobotics.ftclib.command.SequentialCommandGroup;
//import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.arcrobotics.ftclib.gamepad.GamepadKeys;
//import com.arcrobotics.ftclib.command.CommandScheduler;
//
//@TeleOp(name = "Internationals TeleOp Merged", group = "Linear OpMode")
//public class Internationals_TeleOP_Pedro extends LinearOpMode {
//
//    // PedroPathing components
//    private Follower follower;
//    private final Pose startPose = new Pose(0, 0, 0);
//
//    // Subsystems
//    private Drivetrain drivetrain;
//    private CascadeSlides cascadeSlides;
//    private CascadePivot cascadePivot;
//    private Claw claw;
//    private Vision vision;
//
//    // Slide positions constants
//    private static final int SLIDES_POSITION0 = 0;
//    private static final int SLIDES_POSITION1 = 350;
//    private static final int SLIDES_POSITION2 = 700;
//
//    // Claw servo positions
//    private static final double CLAW_OPEN = 0.2;
//    private static final double CLAW_CLOSED = 0.8;
//    private static final double CLAW_UP = 0.8;
//    private static final double CLAW_DOWN = 0.2;
//    private static final double CLAW_NEUTRAL = 0.5;
//    private static final double CLAW_FLAT = 0.0;
//    private static final double CLAW_DIA = 0.75;
//
//    // State management for path following
//    private boolean inTeleopMode = true;
//    private boolean pathTriggered = false;
//    private boolean mechanismsActivated = false;
//    private boolean lastButtonBState = false;
//
//    private Vision.SampleColor currentColor = Vision.SampleColor.RED;
//
//    // Command-based gamepad controllers
//    private GamepadEx driver;
//    private GamepadEx operator;
//
//    @Override
//    public void runOpMode() {
//        // Initialize PedroPathing follower
//        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
//        follower.setStartingPose(startPose);
//
//        // Initialize subsystems
////        drivetrain = new Drivetrain();
//        cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
//        cascadePivot = new CascadePivot(hardwareMap, telemetry);
//        claw = new Claw();
//        vision = new Vision(hardwareMap, telemetry);
//
//        // Initialize claw and vision
//        claw.init(hardwareMap);
//        vision.initializeCamera();
//
//        // Initialize command-based gamepads
//        driver = new GamepadEx(gamepad1);
//        operator = new GamepadEx(gamepad2);
//
//        // Set up command bindings
//        setupCommandBindings();
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
//        telemetry.addData("Pivot pos", cascadePivot.getAveragePosition());
//        telemetry.addLine("Vision initialized. Waiting for start...");
//        telemetry.addLine("Vision is init for RED");
//        telemetry.update();
//
//        waitForStart();
//
//        // Start teleop drive
//        follower.startTeleopDrive();
//        inTeleopMode = true;
//
//        while (opModeIsActive()) {
//            // Update vision
//            vision.periodic();
//
//            // Run command scheduler
//            CommandScheduler.getInstance().run();
//
//            // Handle path following state machine
//            if (inTeleopMode) {
//                // Normal teleop control
//                handleTeleopDrive();
//                handleManualControls();
//
//                // Check for path trigger (gamepad1.b)
//                boolean currentButtonB = gamepad1.b;
//                if (currentButtonB && !lastButtonBState) { // Button just pressed
//                    Double angle = vision.getTurnServoDegree();
//                    if (angle != 0) {
//                        startPathFollowing(angle);
//                    }
//                }
//                lastButtonBState = currentButtonB;
//
//            } else {
//                // Path following mode
//                follower.update();
//
//                if (follower.atParametricEnd() && !mechanismsActivated) {
//                    // Path complete - execute mechanisms
//                    follower.breakFollowing();
//                    follower.startTeleopDrive();
//
//                    // Set teleop vectors once to initialize
//                    follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
//                    follower.update();
//
//                    // Execute mechanism sequence
//                    cascadeSlides.setSlideTarget((int) Math.round(vision.getDistance() + 400));
//
//                    cascadePivot.setPivotTarget(-30);
//                    claw.setServoPosOC(CLAW_CLOSED);
//
//                    // Return to teleop mode
//                    inTeleopMode = true;
//                    mechanismsActivated = false;
//                }
//            }
//
//            // Update telemetry
//            updateTelemetry();
//        }
//    }
//
//    private void setupCommandBindings() {
//        // Command-based bindings for preset positions
//        driver.getGamepadButton(GamepadKeys.Button.X).whenPressed(
//                new SequentialCommandGroup(
//                        new PivotSampleShort(cascadePivot),
//                        new SlidesLowBask(cascadeSlides)
//                )
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
//                new SequentialCommandGroup(
//                        new PivotBask(cascadePivot),
//                        new SlidesHighBask(cascadeSlides)
//                )
//        );
//
//        // Additional command bindings can be added here
//        // Note: gamepad1.b is reserved for vision path following
//    }
//
//    private void handleTeleopDrive() {
//        if (inTeleopMode) {
//            follower.setMaxPower(1);
//            follower.setTeleOpMovementVectors(
//                    -gamepad1.left_stick_y,
//                    -gamepad1.left_stick_x,
//                    -gamepad1.right_stick_x,
//                    true
//            );
//            follower.update();
//        }
//    }
//
//    private void handleManualControls() {
//        // Manual claw controls (operator/gamepad2)
//        if (gamepad2.left_trigger > 0.1) {
//            claw.setServoPosOC(CLAW_OPEN);
//        } else if (gamepad2.right_trigger > 0.1) {
//            claw.setServoPosOC(CLAW_CLOSED);
//        }
//
//        // Claw rotation with right stick
//        if (Math.abs(gamepad2.right_stick_x) > 0.1) {
//            double rotPos = (gamepad2.right_stick_x + 1) / 2; // Convert from -1,1 to 0,1
//            claw.setServoPosRot(rotPos);
//        }
//
//        // Claw up/down
//        if (gamepad2.left_bumper) {
//            claw.setServoPosUD(CLAW_UP);
//        } else if (gamepad2.right_bumper) {
//            claw.setServoPosUD(CLAW_DOWN);
//        }
//
//        // Claw rotation presets
//        if (gamepad2.a) {
//            claw.setServoPosRot(CLAW_FLAT);
//        } else if (gamepad2.b) {
//            claw.setServoPosRot(CLAW_DIA);
//        }
//
//        // Manual slide controls (if not using commands)
//        if (gamepad2.dpad_down) {
//            cascadeSlides.setSlideTarget(SLIDES_POSITION0);
//        } else if (gamepad2.dpad_right) {
//            cascadeSlides.setSlideTarget(SLIDES_POSITION1);
//        } else if (gamepad2.dpad_up) {
//            cascadeSlides.setSlideTarget(SLIDES_POSITION2);
//        } else if (Math.abs(gamepad2.left_stick_y) > 0.15) {
//            double slidesPower = -gamepad2.left_stick_y * 0.8;
//            cascadeSlides.setPower(slidesPower);
//        }
//
//        // Manual pivot controls (driver bumpers for fine adjustment)
//        if (gamepad1.left_bumper) {
//            cascadePivot.setPower(-0.5);
//        } else if (gamepad1.right_bumper) {
//            cascadePivot.setPower(0.5);
//        } else if (!gamepad1.left_bumper && !gamepad1.right_bumper) {
//            cascadePivot.setPower(0);
//        }
//    }
//
//    private void startPathFollowing(Double angle) {
//        Pose currentPose = follower.getPose();
//
//        // Clamp angle and calculate perpendicular angle
//        angle = Math.max(0, Math.min(angle, 360));
//        double perpangle = (angle + 90) % 360;
//        double servoPos = perpangle / 360.0;
//
//        // Pre-position mechanisms
//        cascadePivot.setPivotTarget(-20);
//        claw.setServoPosRot(servoPos);
//        claw.setServoPosOC(CLAW_OPEN);
//
//        // Create and start path
//        Point startPoint = new Point(currentPose.getX(), currentPose.getY());
//        Point endPoint = new Point(currentPose.getX(), currentPose.getY() - (vision.getDistance() / 25.4));
//
//        follower.setMaxPower(0.3);
//        follower.followPath(follower.pathBuilder()
//                .addPath(new BezierLine(startPoint, endPoint))
//                .setConstantHeadingInterpolation(currentPose.getHeading())
//                .build());
//
//        // Switch to path following mode
//        inTeleopMode = false;
//        mechanismsActivated = false;
//
//        telemetry.addLine("Path following started!");
//        telemetry.update();
//    }
//
//    private void updateTelemetry() {
//        telemetry.addData("Mode", inTeleopMode ? "Teleop" : "Path Following");
//        telemetry.addData("Follower X", follower.getPose().getX());
//        telemetry.addData("Follower Y", follower.getPose().getY());
//        telemetry.addData("Follower Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
//        telemetry.addData("Current Slides Position", cascadeSlides.getCurrentPosition());
//        telemetry.addData("Current Pivot Position", cascadePivot.getAveragePosition());
//        telemetry.addData("Vision Target Visible", vision.isTargetVisible());
//        telemetry.addData("Vision Distance", vision.getDistance());
//        telemetry.addData("Vision Angle", vision.getTurnServoDegree());
//        telemetry.addData("Current Color", currentColor.name());
//
//        telemetry.addData("Left Stick Y", gamepad2.left_stick_y);
//        telemetry.addData("Left Stick X", gamepad2.left_stick_x);
//        telemetry.addData("Right Stick X", gamepad2.right_stick_x);
//        telemetry.addData("Command X Button", driver.getGamepadButton(GamepadKeys.Button.X).get());
//        telemetry.update();
//    }
//}