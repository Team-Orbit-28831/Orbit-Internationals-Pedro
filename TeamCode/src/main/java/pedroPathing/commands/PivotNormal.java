package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;

public class PivotNormal extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final CascadePivot pivot;


    public PivotNormal(CascadePivot subsystem2) {
        pivot = subsystem2;

        addRequirements(subsystem2);
    }

    @Override
    public void initialize() {
        //turn outtake on
        pivot.pivotNormal();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


