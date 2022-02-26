package frc.robot.SendableWrapperClasses;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class SendableTalonFx extends TalonFX implements Sendable{
  public SendableTalonFx(int iD){
    super(iD);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Motor Controller");
    builder.setActuator(true);
    builder.setSafeState(() -> super.set(ControlMode.PercentOutput, 0));
    builder.addDoubleProperty("Position", () -> super.getSelectedSensorPosition(), null);
    builder.addDoubleProperty("Speed", () -> super.getSelectedSensorVelocity(), null);
    // builder.addDoubleProperty("Output amperage", () -> super.getStatorCurrent(), null);
    builder.addDoubleProperty("Output Percent", () -> super.getMotorOutputPercent(),
      (double input) ->super.set(ControlMode.PercentOutput, input));
    // builder.addDoubleProperty("Open Loop Ramp rate", () -> super.configGetParameter(ParamEnum.eOpenloopRamp, 0),
    //   (double rate) -> super.configOpenloopRamp(rate));
    builder.update();
  }
}
