package waterRowerClient;

import java.io.IOException;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import waterRowerClient.DataRecord.DataRecordValueType;

public class CircleMeter  implements DataNotifier {

	private Circle c;
	int duration=ClientConfig.getIntOptionValue("animationDuration", 2000);
	private DataRecordValueType radiusX_valueType;
	private DataRecordValueType radiusY_valueType;
	private DataRecordValueType color_valueType;
	private StdVBox left;
	private StdVBox right;
	private StdHBox top;

	/**
	public CircleMeter ( Circle c) {
		this.c=c;
		cmInit();
	}
	**/
	
	public CircleMeter(Circle c, StdVBox leftBox, StdHBox topBox, StdVBox rightBox) {
		this.left=leftBox;
		this.right=rightBox;
		this.top=topBox;
		this.c=c;
		cmInit();
	}

	private void cmInit() {
		setRadiusX_DataRecordValueType(DataRecordValueType.SPM);
		setRadiusY_DataRecordValueType(DataRecordValueType.CURR500M_SECS);
		setColor_DataRecordValueType(DataRecordValueType.WATT);
		
		fill = new FillTransition(Duration.millis(duration)); 
		fill.setToValue(color_currentValue); 

		scale = new ScaleTransition(Duration.millis(duration)); 
		scale.setToX( radiusX_currentMultiplier); 
		scale.setToY( radiusY_currentMultiplier); 

		transition = new ParallelTransition(c, fill, scale); 
	}

	private Color color_currentValue=Color.YELLOW;
	private double radiusX_currentMultiplier=1.0;
	private double radiusY_currentMultiplier=1.0;
	private FillTransition fill;
	private ParallelTransition transition;
	private ScaleTransition scale;

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setRadiusX_DataRecordValueType(DataRecordValueType vt) {
		this.radiusX_valueType = vt;
		left.setLabel(MetricHelper.getLabel(vt));
	}
	public void setRadiusY_DataRecordValueType(DataRecordValueType vt) {
		this.radiusY_valueType = vt;
		top.setLabel(MetricHelper.getLabel(vt));
	}
	public DataRecordValueType getRadiusX_DataRecordValueType() {
		return radiusX_valueType;
	}
	public DataRecordValueType getRadiusY_DataRecordValueType() {
		return radiusY_valueType;
	}
	public double getRadiusXMultiplier() {
		return radiusX_currentMultiplier;
	}
	public void setRadiusXMultiplier(double multiplier) {
		this.radiusX_currentMultiplier= multiplier;
	}
	public double getRadiusYMultiplier() {
		return radiusY_currentMultiplier;
	}
	public void setRadiusYMultiplier(double multiplier) {
		this.radiusY_currentMultiplier= multiplier;
	}

	public void setColor_DataRecordValueType(DataRecordValueType vt) {
		this.color_valueType = vt;
		right.setLabel(MetricHelper.getLabel(vt));
	}
	public DataRecordValueType getColor_DataRecordValueType() {
		return color_valueType;
	}
	public Color getColor_currentValue() {
		return color_currentValue;
	}
	public void setColor_currentValue(Color color_currentValue) {
		this.color_currentValue = color_currentValue;
	}

	public void startTransition() {

		System.out.println( "Color: "+color_currentValue+", Radius: "
				+radiusX_currentMultiplier+"/"+radiusY_currentMultiplier);
		
		transition.stop();
		
		fill.setToValue(color_currentValue); 

		scale.setToX( radiusX_currentMultiplier); 
		scale.setToY( radiusY_currentMultiplier); 

		transition.play();

	}

	@Override
	public void readEvent(String rawData) throws IOException {
		
		if( !DataRecord.isDataRecord(rawData)) {
			return;
		}
		
		DataRecord dr=null;
		try {
			dr=DataRecord.getDataRecord(rawData);
		} catch (DataRecordException e) {
			// No data record
			return;
		}
		

		int colorVal=dr.getValue( color_valueType);
		right.setCurrValue(dr.getStringValue(color_valueType));
		right.setMidValue(MetricHelper.getMidValueString(color_valueType, dr.getLevel()));
		setColor_currentValue(MetricHelper.getColor( color_valueType, dr.getLevel(), colorVal));

		int radiusYVal=dr.getValue( radiusY_valueType);
		top.setCurrValue(dr.getStringValue(radiusY_valueType));
		top.setMidValue(MetricHelper.getMidValueString(radiusY_valueType, dr.getLevel()));
		setRadiusYMultiplier(MetricHelper.getRadiusMultiplier( radiusY_valueType, dr.getLevel(), radiusYVal));
		
		int radiusXVal=dr.getValue( radiusX_valueType);
		left.setCurrValue(dr.getStringValue(radiusX_valueType));
		left.setMidValue(MetricHelper.getMidValueString(radiusX_valueType, dr.getLevel()));
		setRadiusXMultiplier(MetricHelper.getRadiusMultiplier( radiusX_valueType, dr.getLevel(), radiusXVal));
		
		System.out.println( "Color: "+colorVal+", Radius: "+radiusXVal+"/"+radiusYVal);

		startTransition();
	}

}
