package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;

public class SlidesSample extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides cascadeSlidesE;

    public SlidesSample(CascadeSlides subsystem) {
        cascadeSlidesE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        cascadeSlidesE.slideLowChamber();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}




