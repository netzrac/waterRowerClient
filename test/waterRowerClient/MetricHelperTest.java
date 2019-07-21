package waterRowerClient;

import org.junit.Test;

import javafx.scene.paint.Color;
import waterRowerClient.DataRecord.DataRecordValueType;

public class MetricHelperTest {

	
	@Test
	public void testGetColor() {
		
		Color c1=MetricHelper.getColor( DataRecordValueType.WATT, 2, 15);
		Color c2=MetricHelper.getColor( DataRecordValueType.WATT, 2, 24);
		Color c3=MetricHelper.getColor( DataRecordValueType.WATT, 2, 30);
		Color c4=MetricHelper.getColor( DataRecordValueType.WATT, 2, 36);
		Color c5=MetricHelper.getColor( DataRecordValueType.WATT, 2, 50);
		System.out.println( c1);
		System.out.println( c2);
		System.out.println( c3);
		System.out.println( c4);
		System.out.println( c5);
			
		
	}	
	
	@Test
	public void testGetRadiusMultiplier() {
		
		double c1=MetricHelper.getRadiusMultiplier( DataRecordValueType.WATT, 2, 15);
		double c2=MetricHelper.getRadiusMultiplier( DataRecordValueType.WATT, 2, 24);
		double c3=MetricHelper.getRadiusMultiplier( DataRecordValueType.WATT, 2, 30);
		double c4=MetricHelper.getRadiusMultiplier( DataRecordValueType.WATT, 2, 36);
		double c5=MetricHelper.getRadiusMultiplier( DataRecordValueType.WATT, 2, 50);
		System.out.println( c1);
		System.out.println( c2);
		System.out.println( c3);
		System.out.println( c4);
		System.out.println( c5);
		
	}
}
