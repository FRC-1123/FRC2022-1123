package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.MecanumDriveSubsystem;

// import java.util.logging.Logger;



/**
 * An example command that uses an example subsystem.
 */
public class GyroTurn extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  MecanumDriveSubsystem drive;
  double position;
  NetworkTableEntry positionEntry;
  GyroSubsystem gyro;
  public GyroTurn(MecanumDriveSubsystem drive, GyroSubsystem gyro, NetworkTableEntry position) {
      this.drive = drive;
      this.gyro = gyro;
      this.position = position.getDouble(0);
      this.positionEntry = position;
      addRequirements(drive);
    }

    public GyroTurn(MecanumDriveSubsystem drive, GyroSubsystem gyro, double position) {
      this.drive = drive;
      this.gyro = gyro;
      this.position = position;
      addRequirements(drive);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(positionEntry != null){
      position = positionEntry.getDouble(0);
    }
    position = Math.abs(position);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    move();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.driveCartesian(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double delta = Math.abs(Math.abs(rawAngleToAbsolute(gyro.getAngle()) - position));
    if(Math.abs(delta) < 3){
      return true;
    }
    return false;
  }

  private void move(){
    double delta = Math.abs(rawAngleToAbsolute(gyro.getAngle()) - position);
    if(delta > 180){
      delta = 360-delta;
      if(delta > 30)
        drive.driveCartesian(0, 0, -1, 0.1); //0.6
      else if(delta < 10)
        drive.driveCartesian(0, 0, -1, 0.1); //0.3
      else 
        drive.driveCartesian(0, 0, -1, 0.1);
    }
    else {
      if(delta > 30)
        drive.driveCartesian(0, 0, 1, 0.1);
      else if(delta < 10)
        drive.driveCartesian(0, 0, 1, 0.1);
      else 
        drive.driveCartesian(0, 0, 1, 0.1);
    }
  }

  private double rawAngleToAbsolute(double angle){
    if(angle < 0){
      return 360 + (angle % 360);
    }
    return angle % 360;
  }
}