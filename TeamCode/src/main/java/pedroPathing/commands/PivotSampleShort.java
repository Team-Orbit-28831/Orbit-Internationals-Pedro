package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadePivot;

public class PivotSampleShort extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadePivot cascadePivotE;


    public PivotSampleShort(CascadePivot subsystem) {
        cascadePivotE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        cascadePivotE.pivotNormal();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


