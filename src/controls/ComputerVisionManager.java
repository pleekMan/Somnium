package controls;

import globals.Main;
import globals.PAppletSingleton;

import java.util.ArrayList;

import processing.core.PVector;
import tsps.*;

// CLASS TO INTERACT WITH "TSPS Computer Vision Tool" or "Community Core Vision" or Other..
// VALUES ARE RECIEVED NORMALIZED

public class ComputerVisionManager {
	Main p5;
	TSPS CVReceiver;
	ArrayList<TSPSPerson> people;
	int maxPeopleCount;

	public ComputerVisionManager() {

		p5 = getP5();

		p5.println("-|| STARTING COMPUTER VISION MANAGER");

		CVReceiver = new TSPS(p5, 12000);
		people = new ArrayList<TSPSPerson>();
		maxPeopleCount = 5;
		p5.println("---------------------------");
	}

	public void update() {
		TSPSPerson[] peopleArray = CVReceiver.getPeopleArray();

		people.clear();

		for (int i = 0; i < peopleArray.length; i++) {
			if (i < maxPeopleCount) {
				people.add(peopleArray[i]);
			} else {
				break;
			}
		}
	}

	public PVector getCentroidFrom(int person) {
		if (person < people.size()) {
			return people.get(person).centroid;
		}
		return new PVector();
	}

	public PVector[] getAllCentroids() {
		PVector[] centroids = new PVector[people.size()];
		for (int i = 0; i < centroids.length; i++) {
			centroids[i] = people.get(i).centroid;
		}
		return centroids;
	}

	public boolean detectsSomething() {
		return CVReceiver.getNumPeople() > 0;
	}

	public int getPeopleCount() {
		return people.size();
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}