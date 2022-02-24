package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.BallSubsystem;
import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class RaiseBallsToggle extends RaiseBallsPos {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  boolean up = true;
  public RaiseBallsToggle(BallSubsystem balls, Double position) {
    super(balls, position);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(up){
      super.setPositionGoal(Constants.intakeLifterDownPosition);
    }
    else{
      super.setPositionGoal(Constants.intakeLifterTopPosition);
    }
    if(up){
      up = false;
    }
    else{
      up = true;
    }
    SmartDashboard.putBoolean("arm toggle", up);
    super.initialize();
  }
}