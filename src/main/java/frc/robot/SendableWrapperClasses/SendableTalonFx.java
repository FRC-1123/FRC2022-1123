package frc.robot.SendableWrapperClasses;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.util.function.BooleanConsumer;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class SendableTalonFx extends TalonFX implements Sendable{
  public SendableTalonFx(int iD){
    super(iD);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    // TODO Auto-generated method stub
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
