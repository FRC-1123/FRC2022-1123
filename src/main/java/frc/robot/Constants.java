package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int frontLeftDriveMotorCanID = 14;
    public static final int rearLeftDriveMotorCanID = 12;
    public static final int frontRightDriveMotorCanID = 13;
    public static final int rearRightDriveMotorCanID = 11;

    public static final int intakeBallMotorCanID = 21;
    public static final int intakeLifterMotorCanID = 20;

    public static final int climberMotorCanID = 15;

    public static final int massMoverCanID = 17;

    public static final int joystickChannel = 0;
    public static final int xBoxChannel = 1;

    public static final double intakeLifterTopPosition = 0;
    public static final double intakeLifterDownPosition = -17.2;// -17.1 best value distance between top and bottom make sure it is negative
    public static final double intakeLifterScorePosition = intakeLifterTopPosition;
    public static final double intakeLifterWheeliePosition = intakeLifterDownPosition + 1;
    public static final double intakeLifterEndgame10ptUpPosition = intakeLifterTopPosition;
    public static final double intakeLifterEndgamePosition = intakeLifterDownPosition + 0.75;
    public static final double intakeLifterVertical = 5;
    public static final double intakeLifter6ptDone = 2;

    public static final double endgameMassAtWeelie = 90; //TODO: find exact value, this is total guess
    
    public static final double hookDeployed10pt = 15; //TODO: find exact value
    public static final double hookRetracted10pt = 10; //TODO: find exact value
    public static final int FingerCanID = 0; //TODO: find correct value
}