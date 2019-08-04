/**
 *
 * Copyright 2019 Carsten Pratsch
 *
 * This file is part of waterRowerClient.
 *
 * waterRowerClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * waterRowerClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with waterRowerClient.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package waterRowerClient;

import javafx.scene.paint.Color;
import waterRowerClient.DataRecord.DataRecordValueType;

public class MetricHelper {
	
	public static double deltaProzent=0.9;

	static int midCurr500MSecs[] = { 230, 180, 150, 120};
	static int midWatt[] = { 25, 50, 75, 100};
	static int midCalHr[] = { 400, 500, 600, 700};

	public static int getMidValue(DataRecordValueType vt, int level) {
		switch (vt) {
		case TOTAL_DIST: 
			return 4500;
		case TOTAL_SECS:
			return 1800;
		case CURR500M_SECS:
			return midCurr500MSecs[ level-1];
		case SPM:
			return 27;
		case WATT:
			return midWatt[ level-1];
		case CAL_HR:
			return midCalHr[ level-1];
		case LEVEL:
			return 2;
		default:
			break;
		}
		return Integer.MAX_VALUE;
	}
	

	public static String getMidValueString(DataRecordValueType vt, int level) {
		switch (vt) {
		case TOTAL_DIST: 
		case WATT:
		case SPM:
		case CAL_HR:
		case LEVEL:
			return String.valueOf(getMidValue(vt, level));
		case TOTAL_SECS:
		case CURR500M_SECS:
			return String.format("%02d:%02d", getMidValue(vt, level)/60, getMidValue(vt, level)%60);
		default:
			break;
		}

		return "UNKNOWN";	
	}

	public static double getDoubleMidValue(DataRecordValueType vt, int level) {
		return (double) getMidValue(vt, level);
	}
	
	public static String getLabel( DataRecordValueType vt) {
		switch (vt) {
		case TOTAL_DIST: 
			return "TOTAL DIST";
		case TOTAL_SECS:
			return "TOTOL TIME";
		case CURR500M_SECS:
			return "500 M TIME";
		case SPM:
			return "SPM";
		case WATT:
			return "WATT";
		case CAL_HR:
			return "CAL/HR";
		case LEVEL:
			return "LEVEL";
		default:
			break;
		}
		return "UNKNOWN";		
	}
	
	public static Color getColor(DataRecordValueType color_valueType, int level, int colorVal) {
		// GREEN  #008000
		// YELLOW #FFFF00
		// RED    #FF0000
		double red=0.0;
		double green=0.0;
		double blue=0.0;
		double opacity=1.0;
		int midColorValue=MetricHelper.getMidValue(color_valueType, level);
		int maxDifference=(int) (midColorValue*deltaProzent);
		if( colorVal>(midColorValue+maxDifference)) {
			colorVal=maxDifference;
		} else if (colorVal<(midColorValue-maxDifference)) {
			colorVal=-maxDifference;
		} else {
			colorVal-=midColorValue;
		}
		double prozent=(double)colorVal/maxDifference;
		switch( color_valueType) {
		case WATT:
		case TOTAL_DIST:
		case CAL_HR:
		case SPM:
			break;
		default:
			prozent=-1.0*prozent;
			break;
		}
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
			blue=0.0;
		}
		Color color=new Color( red, green, blue, opacity);
		return color;
	}

	public static double getRadiusMultiplier(DataRecordValueType radius_valueType, int level, int radius_currentValue) {
		double multiplier=(getDoubleMidValue( radius_valueType, level)
				-(getDoubleMidValue( radius_valueType, level)-radius_currentValue))
				/getDoubleMidValue( radius_valueType, level);
		//
		switch( radius_valueType) {
		case WATT:
		case TOTAL_DIST:
		case CAL_HR:
		case SPM:
			break;
		default:
			if( multiplier<1.0) {
				// 0.9 --> 1.1: 1+1-.9=2-0.9=1.1
				multiplier=1.0+(1.0-multiplier);
			} else {
				// 1.1 --> 0.9: 1-(1.1-1)=1 -0.9
				multiplier=1.0-(multiplier-1.0);
			};
			break;
		}
		//
		if( multiplier<(1.0-deltaProzent)) {
			return (1.0-deltaProzent);
		} else if( multiplier>(1.0+deltaProzent)) {
			return (1.0+deltaProzent);
		} 
		return multiplier;
	}

}
