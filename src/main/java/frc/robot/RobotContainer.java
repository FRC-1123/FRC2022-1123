package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveStraightPos;
import frc.robot.commands.GyroTurn;
import frc.robot.commands.RaiseBallsPos;
import frc.robot.commands.ShootBalls;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RobotContainer {
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());
  private static final MecanumDriveSubsystem drive = new MecanumDriveSubsystem();
  private static final GyroSubsystem gyro = new GyroSubsystem();
  private static final BallSubsystem ballSubysytem = new BallSubsystem();
  private static final ClimbingSubsystem climb = new ClimbingSubsystem();

  private static SendableChooser<Command> autoChooser;

  public static void initialize() {
    JoystickControlSystem.initialize(new Joystick(Constants.joystickChannel), drive, gyro, ballSubysytem, new XboxController(Constants.xBoxChannel));
    DashboardControlSystem.initialize(drive, gyro, ballSubysytem, climb);
    initializeAutonomousChooser();
  }

  public static void testModeDisplayInitialize(){
    DashboardControlSystem.testModeDisplayInitialize(drive, gyro, ballSubysytem);
  }

  public static void testModeDisplayPeriodic(){
    DashboardControlSystem.testModeDisplayInitialize(drive, gyro, ballSubysytem);
  }

  private static void initializeAutonomousChooser(){
    autoChooser = new SendableChooser<>();
    ParallelCommandGroup step2 = new ParallelCommandGroup(new DriveStraightPos(drive, 6, 0.4), new RaiseBallsPos(ballSubysytem, Constants.intakeLifterDownPosition, 1));
    ParallelCommandGroup step5 = new ParallelCommandGroup(new DriveStraightPos(drive, 135, 0.6), new RaiseBallsPos(ballSubysytem, Constants.intakeLifterTopPosition, 1));
    SequentialCommandGroup auto1 = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), step2, new DriveStraightPos(drive, 56, 0.4), new GyroTurn(drive, gyro, 180), step5, new GyroTurn(drive, gyro, 158), new DriveStraightPos(drive, 6, 0.3), new ShootBalls(ballSubysytem));

    autoChooser.setDefaultOption("Auto 1", auto1);
    // autoChooser.addOption("Auto 2", auto2);
    SmartDashboard.putData("Autonomous Chooser", autoChooser);
  }
  public static Command getAutonomousCommand(){
    return autoChooser.getSelected();
  }
}