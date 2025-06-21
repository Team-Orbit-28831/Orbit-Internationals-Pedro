package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Vision;

public class VisionSlides extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides cascadeSlidesE;

    private final Vision vision;
    public VisionSlides(CascadeSlides subsystem, Vision subsystem1) {
        cascadeSlidesE = subsystem;
        vision = subsystem1;


        addRequirements(subsystem,subsystem1);
    }

    @Override
    public void initialize() {
        double distance = (vision.getDistance()/25.4)+400;
        cascadeSlidesE.setSlideTarget(distance);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}




