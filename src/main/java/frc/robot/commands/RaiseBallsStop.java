package frc.robot.commands;

// import src.main.java.frc.subsystems;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmLifterSubsystem;

import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class RaiseBallsStop extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  ArmLifterSubsystem lifter;
  public RaiseBallsStop(ArmLifterSubsystem lifter) {
      this.lifter = lifter;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(lifter);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("got to Raise balls stop motor");
    lifter.stopLifter();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(time > 5){
      return true;
    }
    return false;
  }
}