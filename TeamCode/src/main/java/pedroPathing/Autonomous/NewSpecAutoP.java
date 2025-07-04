package pedroPathing.Autonomous;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;
import pedroPathing.commands.ClawDown;
import pedroPathing.commands.ClawOpen;
import pedroPathing.commands.MidClaw;
import pedroPathing.commands.PivotNormal;
import pedroPathing.commands.PivotSpecDone;
import pedroPathing.commands.PivotSpecDrop;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * This is a path-only autonomous that showcases pure movement without subsystem controls.
 * It follows a 0+4 (Specimen + Sample) bucket auto path pattern.
 * All servo and motor subsystem controls have been removed.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "Auto_Chamber_Pedro_PathOnly new lol", group = "Autonomous")
public class NewSpecAutoP extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private SequentialCommandGroup hangCommand;

    private SequentialCommandGroup hang2Command;


    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    private int pathState;

    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(10.1, convertOffset(57.6), Math.toRadians(0));

    private Path scorePreload, park;

    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;

    private Servo clawServo;
    private Servo clawRotServo;
    private Servo clawUDServo;

    public DcMotorEx pivotMotorLeft;   // vertical
    public DcMotorEx pivotMotorRight;

    public static DcMotorEx slideMotor;


    private PathChain aheadAB, grab1Ready, grabPickup1, grab2Ready,grabPickup2,grab3Ready,grabPickup3,firstCollect,collect1,collect2,collect3,collect4,deposit1,deposit2,deposit3,deposit4,deposit5;

    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(new Pose(34.5,  convertOffset(77.7)))));
//        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), new Pose(22, 63, Math.toRadians(180)).getHeading());



        /* Here is an example for Constant Interpolation */
        scorePreload.setConstantHeadingInterpolation(Math.toRadians(0) );

        scorePreload.setPathEndVelocityConstraint(75);
        scorePreload.setZeroPowerAccelerationMultiplier(8.5);


//        grab1Ready = follower.pathBuilder()
//                .addPath(new BezierLine(new Point((new Pose(36.3, convertOffset(66.3)))), new Point(new Pose(13.9, convertOffset(31.4)))))
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .setPathEndVelocityConstraint(75)
//                .setZeroPowerAccelerationMultiplier(8.5)
//                .build();
        grab1Ready = follower.pathBuilder()
                .addPath(new BezierCurve(new Point((new Pose(30, convertOffset(77.7)))), new Point(new Pose(5.4, convertOffset(20.9))), new Point(new Pose(69.7, convertOffset(39.3))), new Point(new Pose(52.5, convertOffset(27.3)))))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point((new Pose(52.5, convertOffset(27.3)))), new Point(new Pose(13.7, convertOffset(27.6)))))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();

        grab2Ready = follower.pathBuilder()
                .addPath(new BezierLine(new Point((new Pose(6.5, convertOffset(27.6)))), new Point(new Pose(63.4, convertOffset(25.7)))))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        grabPickup2 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(63.4, convertOffset(25.7), Point.CARTESIAN),
                                new Point(80.9, convertOffset(14.4), Point.CARTESIAN),
                                new Point(14.4, convertOffset(15.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)

                .build();
        grab3Ready = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(14.416, convertOffset(15.563), Point.CARTESIAN),
                                new Point(63.918, convertOffset(10.235), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        grabPickup3 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(63.918, convertOffset(10.235), Point.CARTESIAN),
                                new Point(67.659, convertOffset(10), Point.CARTESIAN),
                                new Point(13.925, convertOffset(9.3), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        firstCollect = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(13.925, convertOffset(8.6), Point.CARTESIAN),

                                new Point(11.5, convertOffset(23.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        deposit1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(11.500, convertOffset(23.5), Point.CARTESIAN),
                                new Point(36.8, convertOffset(66.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();

        collect1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(36.8, convertOffset(66.5), Point.CARTESIAN),
                                new Point(11.500, convertOffset(23.5), Point.CARTESIAN)

                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        deposit2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(11.500, convertOffset(24.5), Point.CARTESIAN),
                                new Point(36.8, convertOffset(67.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        collect2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(36.8, convertOffset(67.5), Point.CARTESIAN),
                                new Point(11.500, convertOffset(24.5), Point.CARTESIAN)

                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();

        deposit3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(11.500, convertOffset(24.5), Point.CARTESIAN),
                                new Point(36.8, convertOffset(68.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        collect3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(36.8, convertOffset(68.5), Point.CARTESIAN),
                                new Point(11.500, convertOffset(24.5), Point.CARTESIAN)

                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        deposit4 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(11.500, convertOffset(24.5), Point.CARTESIAN),
                                new Point(36.8, convertOffset(69.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();
        deposit5 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(11.500, convertOffset(24.5), Point.CARTESIAN),
                                new Point(36.8, convertOffset(70.5), Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(75)
                .setZeroPowerAccelerationMultiplier(8.5)
                .build();




        /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        grabPickup2 = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(new Pose(22, 70, Math.toRadians(180))), new Point(new Pose(67, 84, Math.toRadians(180))), new Point(new Pose(67, 70, Math.toRadians(180))), new Point(new Pose(55, 65, Math.toRadians(180)))))
//                .setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
//                .setPathEndVelocityConstraint(10)
//                .build();
//
//        /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        scorePickup2 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(new Pose(55, 68, Math.toRadians(180))), new Point(new Pose(18, 65, Math.toRadians(180)))))
//                .setLinearHeadingInterpolation(new Pose(63, 68, Math.toRadians(180)).getHeading(), new Pose(22, 68, Math.toRadians(180)).getHeading())
//                .setPathEndVelocityConstraint(10)
//                .build();

//        grabPickup2p5 = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(new Pose(22, 72, Math.toRadians(180))), new Point(new Pose(67, 60, Math.toRadians(180))), new Point(new Pose(67, 70, Math.toRadians(180))), new Point(new Pose(63, 60, Math.toRadians(180)))))
//                .setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
//                .setPathEndVelocityConstraint(10)
//                .build();
//
//        /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        grabPickup3 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(new Pose(63, 60, Math.toRadians(180))), new Point(new Pose(18, 62, Math.toRadians(180)))))
//                .setLinearHeadingInterpolation(new Pose(20, 43, Math.toRadians(180)).getHeading(), new Pose(41, 80, Math.toRadians(180)).getHeading())
//                .setZeroPowerAccelerationMultiplier(10)
//                .build();
//
//        grabPickup3p5 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(new Pose(22, 72, Math.toRadians(180))), new Point(new Pose(41, 80, Math.toRadians(180)))))
//                .setLinearHeadingInterpolation(new Pose(20, 43, Math.toRadians(180)).getHeading(), new Pose(41, 80, Math.toRadians(180)).getHeading())
//                .setPathEndVelocityConstraint(10)
//                .build();
//
//        /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        scorePickup3 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(new Pose(follower.getPose().getX(), follower.getPose().getY(), Math.toRadians(180))), new Point(new Pose(8, 68, Math.toRadians(180)))))
//                .setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
//                .setZeroPowerAccelerationMultiplier(1)
//                .build();

        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
        park = new Path(new BezierCurve(new Point(new Pose(10, 80, Math.toRadians(180))), new Point(new Pose(10, 130, Math.toRadians(180))), /* Control Point */ new Point(new Pose(30, 130, Math.toRadians(180)))));
        park.setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading());
    }

    /** This switch is called continuously and runs the pathing.
     * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
     * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
    public void autonomousPathUpdate() {

//        CommandScheduler.getInstance().run();


        switch (pathState) {
            case 0:
                follower.followPath(scorePreload, true);
                setPathState(2);
                break;

//            case 1:
//                if (!follower.isBusy()) {
//                    if (hang2Command == null) {
//                        hang2Command = new SequentialCommandGroup(
//                                new ClawDown(claw),
//                                new WaitCommand(2000),
//                                new PivotSpecDone(cascadePivot),
//                                new WaitCommand(2000),
//                                new ClawOpen(claw)
//                        );
//                        CommandScheduler.getInstance().schedule(hang2Command);
//                    }
//
//                    // Wait until command finishes
//                    if (!CommandScheduler.getInstance().isScheduled(hang2Command)) {
//                        setPathState(2);
//                    }
//                    break;
//                }

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(grab1Ready, true);
                    setPathState(3);
                }
                break;
            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup1, true);
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(grab2Ready, true);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()){
                    follower.followPath(grabPickup2);
                    setPathState(100);
                }
                break;
            case 6:
                if (!follower.isBusy()){
                    follower.followPath(grab3Ready);
                    setPathState(7);
                }
                break;
            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup3);
                    setPathState(8);
                }
                break;
            case 8:
                if (!follower.isBusy()){
                    follower.followPath(firstCollect);
                    setPathState(9);
                }
                break;
            case 9:
                if (!follower.isBusy()) {
                    follower.followPath(collect1);
                    setPathState(10);
                }
                break;
            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(deposit1);
                    setPathState(11);
                }
                break;
            case 11:
                if (!follower.isBusy()) {
                    follower.followPath(collect2);
                    setPathState(12);
                }
                break;
            case 12:
                if (!follower.isBusy()) {
                    follower.followPath(deposit2);
                    setPathState(13);
                }
                break;
            case 13:
                if (!follower.isBusy()) {
                    follower.followPath(collect3);
                    setPathState(14);
                }
                break;
            case 14:
                if (!follower.isBusy()) {
                    follower.followPath(deposit3);
                    setPathState(15);
                }
                break;
            case 15:
                if (!follower.isBusy()) {
                    follower.followPath(collect4);
                    setPathState(16);
                }
                break;
            case 16:
                if (!follower.isBusy()) {
                    follower.followPath(deposit4);
                    setPathState(17);
                }
                break;

            case 17:
                if (!follower.isBusy()) {
                    follower.followPath(deposit5);
                    setPathState(100);
                }
                break;

//            case 3:
//                if (!follower.isBusy()) {
//                    follower.followPath(scorePickup1, true);
//                    setPathState(4);
//                }
//                break;
//
//            case 4:
//                if (!follower.isBusy()) {
//                    follower.followPath(grabPickup2, true);
//                    setPathState(5);
//                }
//                break;
//
//            case 5:
//                if (!follower.isBusy()) {
//                    follower.followPath(scorePickup2, true);
//                    setPathState(6);
//                }
//                break;
//
//            case 6:
//                if (!follower.isBusy()) {
//                    follower.followPath(grabPickup2p5, true);
//                    setPathState(7);
//                }
//                break;
//
//            case 7:
//                if (!follower.isBusy()) {
//                    follower.followPath(grabPickup3, true);
//                    setPathState(8);
//                }
//                break;
//
//            case 8:
//                if (!follower.isBusy()) {
//                    follower.followPath(grabPickup3p5, true);
//                    setPathState(9);
//                }
//                break;
//
//            case 9:
//                if (!follower.isBusy()) {
//                    follower.followPath(scorePickup3, true);
//                    setPathState(10);
//                }
//                break;
//
//            case 10:
//                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 1) {
//                    follower.followPath(park, true);
//                    setPathState(11);
//                }
//                break;

            case 100:
                // Auto complete - robot is parked
                break;
        }
    }

    /** These change the states of the paths
     * It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {
//         These loop the movements of the robot

        follower.update();
        follower.setMaxPower(1);
        autonomousPathUpdate();

        CommandScheduler.getInstance().run();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        Constants.setConstants(FConstants.class, LConstants.class);

        cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
        cascadePivot = new CascadePivot(hardwareMap, telemetry);
        claw = new Claw(hardwareMap);
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawRotServo = hardwareMap.get(Servo.class, "clawRot");
        clawUDServo = hardwareMap.get(Servo.class, "clawUD");

        slideMotor = hardwareMap.get(DcMotorEx.class, "cascade");
        slideMotor.setDirection(DcMotorEx.Direction.REVERSE);
//        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        pivotMotorLeft = hardwareMap.get(DcMotorEx.class, "pivotLeft");
        pivotMotorRight = hardwareMap.get(DcMotorEx.class, "pivotRight");

        pivotMotorLeft.setDirection(DcMotorEx.Direction.REVERSE);
        pivotMotorRight.setDirection(DcMotorEx.Direction.REVERSE);

//        pivotMotorLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        pivotMotorRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);


        pivotMotorLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        pivotMotorRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    public double convertOffset(double y) {
        return (y/24)*16;
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {

    }
}