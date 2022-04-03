package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveSubsystem;

import java.util.logging.Logger;

/**
 * An example command that uses an example subsystem.
 */
public class DriveStraightPos extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  MecanumDriveSubsystem drive;
  double position;
  NetworkTableEntry positionEntry;
  double driveSpeed;
  NetworkTableEntry driveSpeedEntry;
  double frontLeftStartPosition;
  double frontRightStartPosition;
  double rearLeftStartPosition;
  double rearRightStartPosition;
  public DriveStraightPos(MecanumDriveSubsystem drive, NetworkTableEntry position, NetworkTableEntry driveSpeed) {
    this.drive = drive;
    this.position = position.getDouble(0);
    positionEntry = position;
    this.driveSpeed = driveSpeed.getDouble(0.1);
    this.driveSpeedEntry = driveSpeed;
    addRequirements(drive);
  }

  public DriveStraightPos(MecanumDriveSubsystem drive, double position, NetworkTableEntry driveSpeed) {
    this.drive = drive;
    this.position = position;
    this.driveSpeed = driveSpeed.getDouble(0.1);
    this.driveSpeedEntry = driveSpeed;
  // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  public DriveStraightPos(MecanumDriveSubsystem drive, double position, double driveSpeed) {
    this.drive = drive;
    this.position = position;
    this.driveSpeed = driveSpeed;
  // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Drive Forward activate");
    drive.resetMotorPosition();
    if(positionEntry != null){
      position = positionEntry.getDouble(0);
    }
    if(driveSpeedEntry != null){
      driveSpeed = driveSpeedEntry.getDouble(0.1);
    }
    drive();
    time = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive();
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.driveCartesian(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs(getDistanceTravel() - position) < 0.5){
      return true;
    }
    if(time > 20 && drive.getAverageSpeed() < 2.5){
      return true;
    }
    return false;
  }

  // private double inchToClicks(double inches){
  //   return inches * 1087; 
  //   //inches / (3.14 * 6) * 10 * 2048
  // }
  private double clicksToInches(double clicks){
    return clicks / 1087; 
  }

  private void drive(){
    double delta = getDistanceTravel() - position;
    // logger.info("target position " + position + " delta " +  + delta);
    // logger.info("delta " + delta);
    if(delta > 0){
      if(Math.abs(delta)> 7){
        drive.driveCartesian(0, 1, 0, driveSpeed);
      }
      else if(Math.abs(delta) < 0.6){
        drive.driveCartesian(0, 1, 0, 0.1);
      }
      else {
        drive.driveCartesian(0, 1, 0, driveSpeed/3);
      }
    }
    else {
      if(Math.abs(delta)> 7){
        drive.driveCartesian(0, -1, 0, driveSpeed);
      }
      else if(Math.abs(delta) < 0.6){
        drive.driveCartesian(0, -1, 0, 0.1); 
      }
      else {
        drive.driveCartesian(0, -1, 0, driveSpeed/3);
      }
    }
  }

  private double getDistanceTravel(){
    double output = clicksToInches(drive.getAverageDistance() * 0.97);
    // logger.info("distance travel " + output);
    return output;
  }
}