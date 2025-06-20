package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;

public class SlidesSampleShort extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides cascadeSlidesE;

    public SlidesSampleShort(CascadeSlides subsystem) {
        cascadeSlidesE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        cascadeSlidesE.slideSubShort();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}




