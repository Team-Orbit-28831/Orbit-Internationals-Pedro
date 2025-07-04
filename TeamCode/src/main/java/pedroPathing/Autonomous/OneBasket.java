//package pedroPathing.Autonomous;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.arcrobotics.ftclib.command.CommandScheduler;
//import com.arcrobotics.ftclib.command.SequentialCommandGroup;
//import com.arcrobotics.ftclib.command.WaitCommand;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.localization.PoseUpdater;
//import com.pedropathing.pathgen.BezierCurve;
//import com.pedropathing.pathgen.PathBuilder;
//import com.pedropathing.pathgen.PathChain;
//import com.pedropathing.pathgen.Point;
//import com.pedropathing.util.Constants;
//import com.pedropathing.util.DashboardPoseTracker;
//import com.pedropathing.util.Drawing;
//import com.pedropathing.util.Timer;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//import pedroPathing.SUBSYSTEMS.CascadePivot;
//import pedroPathing.SUBSYSTEMS.CascadeSlides;
//import pedroPathing.SUBSYSTEMS.Claw;
//import pedroPathing.commands.ClawClose;
//import pedroPathing.commands.ClawDown;
//import pedroPathing.commands.ClawOpen;
//import pedroPathing.commands.ClawUp;
//import pedroPathing.commands.PivotBask;
//import pedroPathing.commands.PivotZero;
//import pedroPathing.commands.SlideRetract;
//import pedroPathing.commands.SlidesHighBask;
//import pedroPathing.constants.FConstants;
//import pedroPathing.constants.LConstants;
//
//@Autonomous(name = "one sample with zertte", group = "Main")
//public class OneBasket extends OpMode {
//
//    private Follower follower;
//    private Timer pathTimer, opmodeTimer;
//    private int pathState;
//    private PoseUpdater poseUpdater;
//    private DashboardPoseTracker dashboardPoseTracker;
//    private CascadeSlides cascadeSlides;
//    private CascadePivot cascadePivot;
//    private Claw claw;
//    private ElapsedTime clock = new ElapsedTime();
//
//
//    // Dashboard telemetry - using MultipleTelemetry like LocalizationTest
//    private Telemetry telemetryA;
//
//    // Starting pose - matches your generated path start point
//    private final Pose startPos = new Pose(134.615, 57.103, Math.toRadians(0));
//    private final Pose basketPos = new Pose(126.63454433497536, 18.620689655172413, Math.toRadians(-225));
//    private final Pose controlPos = new Pose(104.5, 39.5);
//
//
//
//    // Single path from generated code
//    private PathChain line1;
//
//    // REMOVED CONSTRUCTOR - FTC OpModes should not have custom constructors
//    // The FTC framework creates OpModes automatically
//
//    @Override
//    public void init() {
//        try {
//            pathTimer = new Timer();
//            opmodeTimer = new Timer();
//            opmodeTimer.resetTimer();
//
//
//            // Initialize subsystems here instead of constructor
//            cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
//            cascadePivot = new CascadePivot(hardwareMap, telemetry);
//            claw = new Claw();
//            claw.init(hardwareMap); // Don't forget to initialize the claw
//
//            // Initialize constants and pose updater like LocalizationTest
//            Constants.setConstants(FConstants.class, LConstants.class);
//            poseUpdater = new PoseUpdater(hardwareMap, FConstants.class, LConstants.class);
//            dashboardPoseTracker = new DashboardPoseTracker(poseUpdater);
//
//            // Initialize dashboard telemetry using MultipleTelemetry
//            telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
//
//            follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
//            follower.setStartingPose(startPos);
//            poseUpdater.setStartingPose(startPos);
//
//            buildPaths();
//            CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new ClawClose(claw)));
//            CommandScheduler.getInstance().run();
//
//
//            pathState = 0;
//
//            // Run initialization sequence like in your TeleOp
//
//
//            // Schedule and run the init sequence
//
//
//            // Initial drawing
//            Drawing.drawRobot(poseUpdater.getPose(), "#4CAF50");
//            Drawing.sendPacket();
//
//            telemetryA.addData("Status", "Initialization Complete");
//            telemetryA.update();
//
//        } catch (Exception e) {
//            telemetry.addData("INIT ERROR", e.getMessage());
//            telemetry.update();
//        }
//    }
//
//    public void buildPaths() {
//        // Build the single path from your generated code
//        PathBuilder builder = new PathBuilder();
//        line1 = builder
//                .addPath(
//                        new BezierCurve(
//                                new Point(134.615, 57.103, Point.CARTESIAN),
//                                new Point(105.708, 39.547, Point.CARTESIAN),
//                                new Point(128.05326354679804, 18.620689655172413, Point.CARTESIAN)
//                        )
//                )
//                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-225))
//                .build();
//    }
//
//    public void autonomousPathUpdate() {
//        switch (pathState) {
//            case 0:
//                // Follow the single path
//                CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new ClawClose(claw),new ClawUp(claw),new PivotBask(cascadePivot)) );
//                follower.followPath(line1);
//
//
//                setPathState(1);
//                break;
//            case 1:
//                // Wait for path to complete
//                if(!follower.isBusy()) {
//                    telemetryA.addData("STATUS", "PATH COMPLETE!");
//                    telemetryA.addData("Final X", follower.getPose().getX());
//                    telemetryA.addData("Final Y", follower.getPose().getY());
//                    telemetryA.addData("Final Heading", Math.toDegrees(follower.getPose().getHeading()));
//                    telemetryA.addData("Total Time", opmodeTimer.getElapsedTimeSeconds());
//                    SequentialCommandGroup doBask = new SequentialCommandGroup(
//                            new ClawDown(claw),
//                            new ClawClose(claw),
//                            new PivotBask(cascadePivot),
//
//                            new SlidesHighBask(cascadeSlides),
//                            new WaitCommand(1000),
//                            new ClawUp(claw),
//                            new WaitCommand(300),
//                            new ClawOpen(claw),
//
//                            new ClawDown(claw),
//                            new WaitCommand(100),
//                            new SlideRetract(cascadeSlides),
//                            new PivotZero(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(doBask);
////                    setPathState(2);
//                }
//                break;
////            case 2:
////                // End state - path complete, keep showing completion message
////                //CommandScheduler.getInstance().schedule(new WaitCommand(1000));
////                //follower.followPath(line2);
////                if (!follower.isBusy()) {
//////                    CommandScheduler.getInstance().schedule(new WaitCommand(500));
////                    telemetryA.addData("STATUS", "FINISHED - ROBOT AT DESTINATION");
////                    break;
////                }
//        }
//    }
//
//    public void setPathState(int pState) {
//        pathState = pState;
//        pathTimer.resetTimer();
//        telemetryA.addData("Switching to state", pState);
//    }
//
//    @Override
//    public void loop() {
//        try {
//            // Update pose and dashboard tracking like LocalizationTest
//            poseUpdater.update();
//            dashboardPoseTracker.update();
//
//            // Update Pedro Pathing
//            follower.update();
//
//            // Run autonomous state machine
//            autonomousPathUpdate();
//            // do commands n stuff
//            CommandScheduler.getInstance().run();
//
//            // Get current pose from pose updater (consistent with LocalizationTest)
//            Pose currentPose = poseUpdater.getPose();
//
//            // Telemetry to both Driver Station and Dashboard using MultipleTelemetry
//            telemetryA.addData("=== PATH INFO ===", "");
//            telemetryA.addData("Path State", pathState);
//            telemetryA.addData("Is Busy", follower.isBusy());
//            telemetryA.addData("Path Timer", pathTimer.getElapsedTimeSeconds());
//
//            telemetryA.addData("=== ROBOT POSE ===", "");
//            telemetryA.addData("x", currentPose.getX());
//            telemetryA.addData("y", currentPose.getY());
//            telemetryA.addData("heading", currentPose.getHeading());
//            telemetryA.addData("total heading", poseUpdater.getTotalHeading());
//
//            telemetryA.addData("=== TARGET ===", "");
//            telemetryA.addData("Target X", 126.457);
//            telemetryA.addData("Target Y", 21.990);
//            telemetryA.addData("Target Heading (deg)", -225);
//            telemetryA.update();
//
//            // Drawing for dashboard visualization (same as LocalizationTest)
//            Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
//            Drawing.drawRobot(currentPose, "#4CAF50");
//            Drawing.sendPacket();
//
//        } catch (Exception e) {
//            telemetryA.addData("LOOP ERROR", e.getMessage());
//            telemetryA.update();
//        }
//    }
//
//    @Override
//    public void init_loop() {
//        if (telemetryA != null) {
//            telemetryA.addData("Status", "Initialized - Ready to start");
//            telemetryA.addData("Starting X", startPos.getX());
//            telemetryA.addData("Starting Y", startPos.getY());
//            telemetryA.addData("Starting Heading", Math.toDegrees(startPos.getHeading()));
//            telemetryA.update();
//        }
//    }
//
//    @Override
//    public void start() {
//        opmodeTimer.resetTimer();
//        setPathState(0);
//        telemetryA.addData("Status", "STARTED - Following path...");
//    }
//
//    @Override
//    public void stop() {
//        telemetryA.addData("Status", "STOPPED");
//    }
//}