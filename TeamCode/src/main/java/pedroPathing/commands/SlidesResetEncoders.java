package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;

public class SlidesResetEncoders extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides cascadeSlidesE;


    public SlidesResetEncoders(CascadeSlides subsystem) {
        cascadeSlidesE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        cascadeSlidesE.resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}




