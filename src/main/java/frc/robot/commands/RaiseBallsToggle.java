package frc.robot.commands;

import frc.robot.Constants;
// import java.util.logging.Logger;
import frc.robot.subsystems.ArmLifterSubsystem;


/**
 * An example command that uses an example subsystem.
 */
public class RaiseBallsToggle extends RaiseBallsPos {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  boolean up = true;
  /**
   * @param position absolute position to go to in inches at the screw from the top
   * @param maxSpeed max speed in percent to run at
   */
  public RaiseBallsToggle(ArmLifterSubsystem lifter, Double position, double maxSpeed) {
    super(lifter, position, maxSpeed);
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
    super.initialize();
  }
}