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

public class TrainingStep {

	private int level;
	private int distance;
	private int watt;
	//private int baseTime;
	private int baseDistance;

	public TrainingStep(int level, int distance, int watt) {
		this.level=level;
		this.distance=distance;
		this.watt=watt;
	}
	
	public void startStep(int baseDistance) {
		this.baseDistance=baseDistance;
	}
	
	public String toString() {
		return String.format( "Mit Level %d und %d Watt %d Meter rudern.", level, watt, distance);
	}
	
	public boolean stepFinished( int currDistance) {
		return currDistance>(baseDistance+distance)?true:false;
	}
	
	public boolean conditionFulfilled( int currWatt) {
		return( currWatt>=watt)?true:false;
	}

	public int getRemaining(int currDistance) {
		return baseDistance+distance-currDistance;
	}

}
