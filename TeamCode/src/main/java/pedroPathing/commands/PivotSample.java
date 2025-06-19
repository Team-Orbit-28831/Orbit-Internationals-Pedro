package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadePivot;

public class PivotSample extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadePivot cascadePivotE;


    public PivotSample(CascadePivot subsystem) {
        cascadePivotE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        cascadePivotE.setPivotTarget(-90);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}


