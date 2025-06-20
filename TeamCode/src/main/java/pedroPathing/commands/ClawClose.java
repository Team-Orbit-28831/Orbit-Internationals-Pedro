package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.Claw;

public class ClawClose extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Claw claw;


    public ClawClose(Claw subsystem) {
        claw = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        claw.closeClaw();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


