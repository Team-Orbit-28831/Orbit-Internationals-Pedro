package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;

public class CascadeSpecDep extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides slides;


    public CascadeSpecDep(CascadeSlides subsystem) {
        slides = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        slides.specDep();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


