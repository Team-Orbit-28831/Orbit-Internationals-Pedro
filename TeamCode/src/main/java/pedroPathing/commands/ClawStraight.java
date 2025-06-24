package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.Claw;

public class ClawStraight extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Claw claw;


    public ClawStraight(Claw subsystem) {
        claw = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        claw.straightClaw();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


