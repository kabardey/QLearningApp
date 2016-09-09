package main;

import java.util.Comparator;

public class pComparator implements Comparator<stateModel> {
	

	@Override
	public int compare(stateModel x, stateModel y) {
		// TODO Auto-generated method stub
		if(x.getp() < y.getp()){
			return 1;
		}else if(x.getp() > y.getp()){
			return -1;
		}
		
		return 0;
	}
}
