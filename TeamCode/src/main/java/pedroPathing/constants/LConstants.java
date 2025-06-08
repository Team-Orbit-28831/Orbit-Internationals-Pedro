package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LConstants {
    static {
        TwoWheelConstants.forwardTicksToInches = .002;
        TwoWheelConstants.strafeTicksToInches = .539;
        TwoWheelConstants.forwardY = 2.8;
        TwoWheelConstants.strafeX = -3.1;
        TwoWheelConstants.forwardEncoder_HardwareMapName = "front_right_drive";
        TwoWheelConstants.strafeEncoder_HardwareMapName = "front_left_drive";
        TwoWheelConstants.forwardEncoderDirection = Encoder.FORWARD;
        TwoWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
        TwoWheelConstants.IMU_HardwareMapName = "imu";
        TwoWheelConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);
    }
}
