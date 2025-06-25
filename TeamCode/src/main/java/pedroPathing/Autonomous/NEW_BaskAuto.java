package pedroPathing.Autonomous;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.commands.ClawClose;
import pedroPathing.commands.ClawDown;
import pedroPathing.commands.ClawOpen;
import pedroPathing.commands.ClawUp;
import pedroPathing.commands.PivotBask;
import pedroPathing.commands.PivotNormal;
import pedroPathing.commands.SlideRetract;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

import pedroPathing.commands.SlidesHighBask;

import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Drivetrain;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;

@Autonomous(name = "Basket Auto - RED", group = "Autonomous")
public class NEW_BaskAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private CascadePivot pivot;
    private CascadeSlides slides;
    private Claw claw;

    // Starting pose
    private final Pose startPose = new Pose(134.37433155080214, 48.1283422459893, Math.toRadians(0));

    // Score Pose
    private final Pose scorePose = new Pose(127.05882352941177, 19.25133689839572, Math.toRadians(-45));

    private final Pose pickup1Pose = new Pose(97.79679144385028, 37.540106951871664, Math.toRadians(270));

    private final Pose parkPose = new Pose(131.10160427807486, 130.9090909090909, Math.toRadians(21));

    // Paths
    private Path scorePreload, park;

    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3, parkPath;

//    private PathChain pathToSample, pathToBasket, pathToNextSample, etc;

    @Override
    public void init() {
        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        Constants.setConstants(FConstants.class, LConstants.class);

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        pivot = new CascadePivot(hardwareMap, telemetry);
        slides = new CascadeSlides(hardwareMap, telemetry);
        claw = new Claw();

        claw.init(hardwareMap);

        buildPaths();
        pathState = 0;
    }

    public void buildPaths() {

        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .setPathEndVelocityConstraint(10)
                .build();

        parkPath = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(parkPose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), parkPose.getHeading())
                .setPathEndVelocityConstraint(10)
                .build();
    }


//        pathToSample = follower.pathBuilder()
//                .addPath(new BezierLine(
//                        new Point(9.757, 84.983, Point.CARTESIAN),
//                        new Point(10.203, 125.519, Point.CARTESIAN)
//                ))
//                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(280))
//                .build();
//
//        pathToBasket = follower.pathBuilder()
//                .addPath(new BezierLine(
//                        new Point(10.203, 125.519, Point.CARTESIAN),
//                        new Point(33.112, 23.679, Point.CARTESIAN)
//                ))
//                .setLinearHeadingInterpolation(Math.toRadians(280), Math.toRadians(180))
//                .build();
//    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload);
                setPathState(1);
                break;
//            case 1:
//
//                if(!follower.isBusy()) {
//                    follower.followPath(grabPickup1,true);
//                    setPathState(2);
//                }
//                break;
//            case 2:
//                if(!follower.isBusy()) {
//                    follower.followPath(scorePreload);
//                    new SequentialCommandGroup(
//                            new ClawClose(claw),
//                            new WaitCommand(300),
//                            new PivotBask(pivot),
//                            new ClawUp(claw),
//                            new SlidesHighBask(slides),
//                            new ClawDown(claw),
//                            new ClawOpen(claw),
//                            new WaitCommand(300),
//                            new ClawClose(claw),
//                            new ClawUp(claw),
//                            new SlideRetract(slides),
//                            new PivotNormal(pivot)
//
////                        new PivotSampleShort(cascadePivot),
////                        new PivotNormal(cascadePivot),
//                    );
//                }
//                setPathState(3);
//                break;
//            case 3:
//                if(!follower.isBusy()) {
//                    follower.followPath(parkPath, true);
//                    setPathState(4);
//                }
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        telemetry.addData("Switching to state", pState);
    }

    @Override
    public void loop() {
        // Update Pedro Pathing
        follower.update();

        // Run autonomous state machine
        autonomousPathUpdate();

        // Telemetry
        telemetry.addData("Path State", pathState);
        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.addData("Path Timer", pathTimer.getElapsedTimeSeconds());

        // Add subsystem telemetry
        telemetry.addData("Pivot Position", pivot.getAveragePosition());
        telemetry.addData("Slides Position", slides.getCurrentPosition());
        telemetry.addData("Claw State", claw.getCurrentOCPos());
        telemetry.addData("Claw Pivot State", claw.getCurrentUDPos());
        telemetry.addData("Claw Rotation state", claw.getCurrentROTPos());

        telemetry.update();
    }

    @Override
    public void init_loop(){}

    @Override

    public void start (){
        opmodeTimer.resetTimer();
        new ClawClose(claw);
        setPathState(0);
    }

    @Override
    public void stop() {
        // Stop all subsystems when autonomous ends
        pivot.stop();
        slides.stop();
//        claw.stop();
    }
}