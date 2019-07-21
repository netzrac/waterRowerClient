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
