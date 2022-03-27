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
    double delta = 0;
    if(gyro.getAngle() > 0)
      delta = Math.abs(gyro.getAngle()%360 - position);
    else{
      delta = Math.abs(Math.abs(360 + gyro.getAngle()%360) - position);
    }
    // logger.info("delta " + delta);

    if(Math.abs(delta) < 3){
      return true;
    }
    return false;
  }

  private void move(){
    double gyroAngle = gyro.getAngle();
    double delta = gyroAngle%360 - position;
    if(Math.abs(delta) > 360){
      delta = delta%360;
    }
    // logger.info("input angle " + gyroAngle + " ouput angle " + delta);
    if(delta < 0){
      delta = Math.abs(delta);
      if(delta < 180){
        driving(delta, 1);
      }
      else{
        delta = 360 - delta;
        driving(delta, -1);
      }
    }
    else{
      if(delta < 180){
        driving(delta, -1);
      }
      else{
        delta = 360 - delta;
        driving(delta, 1);
      }
    }
  }

  private void driving(double angle, int direction){ 
    angle = Math.abs(angle);
    if(angle > 30)
          drive.driveCartesian(0, 0, direction, 0.3);
        else if(angle < 10)
          drive.driveCartesian(0, 0, direction, 0.1);
        else 
          drive.driveCartesian(0, 0, direction, 0.2);
  }

  // private double rawAngleToAbsolute(double angle){
  //   // if(angle < 0){
  //   //   return 360 + (angle % 360);
  //   // }
  //   // return angle % 360;
  //   double delta = (angle %= 360) >= 0 ? angle : (angle + 360);
  //   logger.info("input angle " + angle + " ouput angle " + delta);
  //   return delta;
  // }
}