package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveForwardTime;
import frc.robot.commands.DriveReverseTime;
import frc.robot.commands.DriveStraightPos;
import frc.robot.commands.GyroTurn;
import frc.robot.commands.RaiseBallsPos;
import frc.robot.commands.ShootBalls;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RobotContainer {
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());
  private static final MecanumDriveSubsystem drive = new MecanumDriveSubsystem();
  private static final GyroSubsystem gyro = new GyroSubsystem();
  private static final BallSubsystem ballSubysytem = new BallSubsystem();
  private static final ArmLifterSubsystem lifter = new ArmLifterSubsystem();
  private static final ClimbingSubsystem climb = new ClimbingSubsystem();
  private static final MassMoverSubsystem massMover = new MassMoverSubsystem();

  private static SendableChooser<Command> autoChooser;

  public static void initialize() {
    JoystickControlSystem.initialize(new Joystick(Constants.joystickChannel), drive, gyro, ballSubysytem, new XboxController(Constants.xBoxChannel), lifter);
    DashboardControlSystem.initialize(drive, gyro, ballSubysytem, climb, lifter, massMover);
    initializeAutonomousChooser();
  }

  private static void initializeAutonomousChooser(){
    autoChooser = new SendableChooser<>();
    ParallelCommandGroup basicAutoStep1 = new ParallelCommandGroup(new DriveForwardTime(drive, 1.5, 0.1), new ShootBalls(ballSubysytem, 3));
    // SequentialCommandGroup basicAuto = new SequentialCommandGroup(basicAutoStep1, new DriveReverseTime(drive, 10, 0.1));
    autoChooser.setDefaultOption("basic auto", basicAutoStep1);

    ParallelCommandGroup step2Right = new ParallelCommandGroup(new DriveStraightPos(drive, 6, 0.4), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1));
    ParallelCommandGroup step5Right = new ParallelCommandGroup(new DriveStraightPos(drive, 100, 0.6), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));//slowed down cause unnessasry speed also keeps center of gravity lower for longer
    ParallelCommandGroup lastStep = new ParallelCommandGroup(new DriveForwardTime(drive, 0.5, 0.3), new ShootBalls(ballSubysytem, 0.7));
    SequentialCommandGroup complexAutoRight = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), step2Right, new DriveStraightPos(drive, 50, 0.4),
     new GyroTurn(drive, gyro, 177), step5Right, new GyroTurn(drive, gyro, 202), lastStep);

    ParallelCommandGroup step2Left = new ParallelCommandGroup(new DriveStraightPos(drive, 6, 0.4), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1));
    ParallelCommandGroup step5Left = new ParallelCommandGroup(new DriveStraightPos(drive, 55, 0.6), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));//slowed down cause unnessasry speed also keeps center of gravity lower for longer
    ParallelCommandGroup secondToLastStepLeft = new ParallelCommandGroup(new DriveStraightPos(drive, 45, 0.4));
    ParallelCommandGroup lastStepLeft = new ParallelCommandGroup(new DriveForwardTime(drive, 1, 0.3), new ShootBalls(ballSubysytem, 1.5));
    SequentialCommandGroup complexAutoLeft = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), step2Left, new DriveStraightPos(drive, 50, 0.4),
      new GyroTurn(drive, gyro, 180), step5Left, new GyroTurn(drive, gyro, 202), secondToLastStepLeft, lastStepLeft);

    autoChooser.addOption("Complex auto right", complexAutoRight);
    autoChooser.addOption("Complex auto left", complexAutoLeft);
    autoChooser.addOption("do nothing", null);
    SmartDashboard.putData("Autonomous Chooser", autoChooser);
  }
  public static Command getAutonomousCommand(){
    SequentialCommandGroup auto = new SequentialCommandGroup(new ShootBalls(ballSubysytem, 4), new DriveReverseTime(drive, 7, 0.23), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 0.7));
    return auto;
  }

  public static void setTeleTime(double time){
    DashboardControlSystem.setTeleTime(time);
  }
}