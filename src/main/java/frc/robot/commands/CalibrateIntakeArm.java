package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmLifterSubsystem;

import java.util.logging.Logger;

/**
 * An example command that uses an example subsystem.
 */
public class CalibrateIntakeArm extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  ArmLifterSubsystem lifter;
  public CalibrateIntakeArm(ArmLifterSubsystem lifter) {
    this.lifter = lifter;
  // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(lifter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Calibrate intake arm");
    time = 0;
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    lifter.driveLifter(-0.15);
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    lifter.stopLifter();
    lifter.resetPos();
    lifter.setStartPosition(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if(balls.getLifterCurrent() > 0.8){
    //   return true;
    // }
    if(time > 10 && Math.abs(lifter.getLiftMotorVelocity()) < 200){
      System.out.println("ended " + lifter.getLiftMotorVelocity());
      return true;
    }
    return false;
  }

  // private double inchToClicks(double inches){
  //   return inches * 385843;
  // }
}