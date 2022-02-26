package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.RunCommand;
// import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.RaiseBallsPos;
import frc.robot.commands.RaiseBallsToggle;
// import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class JoystickControlSystem {
  public static Joystick driverJoystick;
  private static XboxController xboxController;

  public static void initialize(Joystick driverJoystickInput, MecanumDriveSubsystem driveSubsystemInput, GyroSubsystem Gyro, BallSubsystem ballSubsystem, XboxController xboxControllerInput) {
    Logger logger = Logger.getLogger(frc.robot.JoystickControlSystem.class.getName());

    driverJoystick = driverJoystickInput;
    xboxController = xboxControllerInput;
    MecanumDriveSubsystem driveSubsystem = driveSubsystemInput;

    logger.info("Mecanum drive subsystem defaulting to driveCartesian.");
    driveSubsystem.setDefaultCommand(new RunCommand(() -> driveSubsystem.driveCartesian(-driverJoystick.getX(),
        driverJoystick.getY(), driverJoystick.getZ()/2, (1 - driverJoystick.getThrottle()) / 2), driveSubsystem));
    // ballSubsystem.setDefaultCommand(new RunCommand(() -> ballSubsystem.spinIntake(
    //   Math.abs(driverJoystick.getY()*((1 - driverJoystick.getThrottle()) / 4))+0.1), ballSubsystem));
    
    ballSubsystem.setDefaultCommand(new RunCommand(() -> ballSubsystem.spinIntake(0.4), ballSubsystem));

    // logger.info("Driver button 10 bound to pivotCartesian");
    // JoystickButton driveModeButton = new JoystickButton(driverJoystick, 10);
    // driveModeButton.whenHeld(new RunCommand(() -> driveSubsystem.pivotCartesian(-driverJoystick.getX(),
    //     driverJoystick.getY(), driverJoystick.getZ(), (1 - driverJoystick.getThrottle()) / 2), driveSubsystem));

    // JoystickButton fieldOrientedButton = new JoystickButton(driverJoystick, 3);
    // fieldOrientedButton.whenHeld(new RunCommand(() -> driveSubsystem.fieldCartesian(-driverJoystick.getX(),
    //   driverJoystick.getY(), driverJoystick.getZ(), (1 - driverJoystick.getThrottle()) / 2, Gyro.getAngle()), driveSubsystem));
    
    JoystickButton shootBall = new JoystickButton(driverJoystick, 1);
    shootBall.whenHeld(new RunCommand(() -> ballSubsystem.spinIntake(-1), ballSubsystem));
    
    RaiseBallsToggle controllerRaiseBallsToggle = new RaiseBallsToggle(ballSubsystem, Constants.intakeLifterTopPosition, 1);
    JoystickButton toggleBallPosition = new JoystickButton(driverJoystick, 2);
    toggleBallPosition.whenPressed(controllerRaiseBallsToggle);
    JoystickButton xboxToggleBallPosition = new JoystickButton(xboxController, 1);
    xboxToggleBallPosition.whenPressed(controllerRaiseBallsToggle);

    JoystickButton putBallsDown = new JoystickButton(driverJoystick, 3);
    putBallsDown.whenPressed(new RaiseBallsPos(ballSubsystem, Constants.intakeLifterDownPosition, 1));
    JoystickButton xboxPutBallsDown = new JoystickButton(xboxController, 3);
    xboxPutBallsDown.whenPressed(new RaiseBallsPos(ballSubsystem, Constants.intakeLifterDownPosition, 1));

    JoystickButton putBallsUp = new JoystickButton(driverJoystick, 4);
    putBallsUp.whenPressed(new RaiseBallsPos(ballSubsystem, Constants.intakeLifterTopPosition, 1));
    JoystickButton xboxPutBallsUp = new JoystickButton(xboxController, 2);
    xboxPutBallsUp.whenPressed(new RaiseBallsPos(ballSubsystem, Constants.intakeLifterTopPosition, 1));
  }

  public static double getThrottle() {
    return driverJoystick.getThrottle();
  }
}