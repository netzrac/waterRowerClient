package waterRowerClient;

import javafx.scene.paint.Color;
import waterRowerClient.DataRecord.DataRecordValueType;

public class MetricHelper {
	
	public static int deltaProzent=30;

	public static int getMidValue(DataRecordValueType vt) {
		switch (vt) {
		case TOTAL_DIST: 
			return 3000;
		case TOTAL_SECS:
			return 1800;
		case CURR500M_SECS:
			return 180;
		case SPM:
			return 25;
		case WATT:
			return 30;
		case CAL_HR:
			return 350;
		case LEVEL:
			return 2;
		}
		return Integer.MAX_VALUE;
	}

	public static Color getColor(DataRecordValueType color_valueType, int colorVal) {
		// GREEN  #008000
		// YELLOW #FFFF00
		// RED    #FF0000
		double red=0.0;
		double green=0.0;
		double blue=0.0;
		double opacity=1.0;
		int midColorValue=MetricHelper.getMidValue(color_valueType);
		int maxDifference=midColorValue*deltaProzent/100;
		if( colorVal>(midColorValue+maxDifference)) {
			colorVal=maxDifference;
		} else if (colorVal<(midColorValue-maxDifference)) {
			colorVal=-maxDifference;
		} else {
			colorVal-=midColorValue;
		}
		double prozent=(double)colorVal/maxDifference;
		// base color: yellow
		if( prozent>0) {
			// calculate green color
			red=1.0-prozent;
			green=1.0-0.5*prozent;
			blue=0.0;
		} else {
			// calculate red color
			red=1.0;
			green=1.0+prozent;
		}
		Color color=new Color( red, green, blue, opacity);
		return color;
	}

	public static int getRadius(DataRecordValueType radius_valueType, int radius_currentValue) {
		// TODO Auto-generated method stub
		return 0;
	}

}
