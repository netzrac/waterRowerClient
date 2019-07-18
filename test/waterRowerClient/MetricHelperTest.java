package waterRowerClient;

import org.junit.Test;

import javafx.scene.paint.Color;
import waterRowerClient.DataRecord.DataRecordValueType;

public class MetricHelperTest {

	
	@Test
	public void testGetColor() {
		
		Color c1=MetricHelper.getColor( DataRecordValueType.WATT, 15);
		Color c2=MetricHelper.getColor( DataRecordValueType.WATT, 24);
		Color c3=MetricHelper.getColor( DataRecordValueType.WATT, 30);
		Color c4=MetricHelper.getColor( DataRecordValueType.WATT, 36);
		Color c5=MetricHelper.getColor( DataRecordValueType.WATT, 50);
		
		
	}
}
