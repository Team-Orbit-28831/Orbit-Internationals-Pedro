package pedroPathing.commands;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;

public class SampleAutoAlign extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    // Constructor - add your subsystems/parameters here
    private final CascadeSlides cascadeSlidesE;
    private final CascadePivot cascadePivotE;
    private final Vision vision;
    private Follower follower;
    private Pose currentPose;
    private Claw claw;
    private ElapsedTime timer = new ElapsedTime();
    private boolean waitingPhase = false;

    public SampleAutoAlign(CascadeSlides cascadeSlidesE, CascadePivot cascadePivotE, Vision vision, Follower follower, Pose currentPose, Claw claw) {
        // addRequirements(subsystem1, subsystem2);
        this.cascadeSlidesE = cascadeSlidesE;
        this.cascadePivotE = cascadePivotE;
        this.vision = vision;
        this.follower = follower;
        this.currentPose = currentPose;
        this.claw = claw;

        addRequirements(cascadePivotE, cascadeSlidesE, vision);

    }

    @Override
    public void initialize() {
        // Code that runs once when command starts
        if (!waitingPhase) {
            // Do first part of command
            Point startPoint = new Point(currentPose.getX(), currentPose.getY());
            Point endPoint = new Point(currentPose.getX(), currentPose.getY() - (vision.getDistance() / 25.4));

            follower.setMaxPower(0.3);
            follower.followPath(follower.pathBuilder()
                    .addPath(new BezierLine(startPoint, endPoint))
                    .setConstantHeadingInterpolation(currentPose.getHeading())
                    .build());
            while (!follower.atParametricEnd()) {
                follower.update();
            }
            cascadeSlidesE.setSlideTarget((int) Math.round(vision.getDistance() + 400));

            cascadePivotE.setPivotTarget(30);

            // Start waiting
            waitingPhase = true;
            timer.reset();
        } else {

            if (timer.seconds() > .5) {
                // Wait is over, do second part
                claw.setServoPosOC(0.2);
                cascadePivotE.setPivotTarget(0);
                cascadeSlidesE.setSlideTarget(0);
            }


        }
    }

//    @Override
//    public void execute() {
//        // Code that runs repeatedly while command is active
//    }

    @Override
    public boolean isFinished() {
        // Return true when command should end
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        // Code that runs once when command ends
        // interrupted = true if command was cancelled
        follower.breakFollowing();
        follower.startTeleopDrive();

        // Set teleop vectors once to initialize
        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();
        cascadeSlidesE.stop();
        cascadePivotE.stop();



    }
}