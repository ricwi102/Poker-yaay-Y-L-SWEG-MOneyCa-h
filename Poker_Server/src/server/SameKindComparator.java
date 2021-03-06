package server;

import java.util.Comparator;
import java.util.List;

/**
 * This comparator is used to compare different pairs, thee-of-a-kinds, four-of-a-kinds to each other
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class SameKindComparator implements Comparator<List<Card>>
{
    public int compare(List<Card> list1, List<Card> list2){

	if(list1.size() > list2.size()){
	    return -1;
	}else if(list1.size() < list2.size()){
	    return 1;
	}else if(!list1.isEmpty()){
	    if (list1.get(0).getValue() > list2.get(0).getValue()) {
		return -1;
	    } else if (list1.get(0).getValue() < list2.get(0).getValue()) {
		return 1;
	    } else {
		return 0;
	    }
	}
	return 0;
    }
}