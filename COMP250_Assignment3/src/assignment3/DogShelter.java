package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * @Author Jerry Xia
 * McGill ID 260917329
 */

public class DogShelter implements Iterable<Dog> {
	public DogNode root;

	public DogShelter(Dog d) {
		this.root = new DogNode(d);
	}

	private DogShelter(DogNode dNode) {
		this.root = dNode;
	}


	// add a dog to the shelter
	public void shelter(Dog d) {
		if (root == null) 
			root = new DogNode(d);
		else
			root = root.shelter(d);
	}

	// removes the dog who has been at the shelter the longest
	public Dog adopt() {
		if (root == null)
			return null;

		Dog d = root.d;
		root =  root.adopt(d);
		return d;
	}
	
	// overload adopt to remove from the shelter a specific dog
	public void adopt(Dog d) {
		if (root != null)
			root = root.adopt(d);
	}


	// get the oldest dog in the shelter
	public Dog findOldest() {
		if (root == null)
			return null;
		
		return root.findOldest();
	}

	// get the youngest dog in the shelter
	public Dog findYoungest() {
		if (root == null)
			return null;
		
		return root.findYoungest();
	}
	
	// get dog with highest adoption priority with age within the range
	public Dog findDogToAdopt(int minAge, int maxAge) {
		return root.findDogToAdopt(minAge, maxAge);
	}

	// Returns the expected vet cost the shelter has to incur in the next numDays days
	public double budgetVetExpenses(int numDays) {
		if (root == null)
			return 0;
		
		return root.budgetVetExpenses(numDays);
	}
	
	// returns a list of list of Dogs. The dogs in the list at index 0 need to see the vet in the next week. 
	// The dogs in the list at index i need to see the vet in i weeks. 
	public ArrayList<ArrayList<Dog>> getVetSchedule() {
		if (root == null)
			return new ArrayList<ArrayList<Dog>>();
			
		return root.getVetSchedule();
	}


	public Iterator<Dog> iterator() {
		return new DogShelterIterator();
	}


	public class DogNode {
		public Dog d;
		public DogNode younger;
		public DogNode older;
		public DogNode parent;

		public DogNode(Dog d) {
			this.d = d;
			this.younger = null;
			this.older = null;
			this.parent = null;
		}

		public DogNode shelter (Dog d) {
			
			DogNode soFar = this;

			soFar = shelter(d, soFar);

            return soFar;
		}

		private DogNode shelter(Dog d, DogNode root) {	//modified from tutorial slides
			
			if(root == null) return new DogNode(d);
			else if(d.compareTo(root.d) > 0) { // if the dog is older
				root.older = shelter(d, root.older); // in the non base case, we work towards the base case
				
				if(root.older.d.getDaysAtTheShelter() > root.d.getDaysAtTheShelter()) {
					DogNode R = root.older;
					root.older = R.younger;
					if(R.younger!=null) R.younger.parent = root; //
					R.younger = root;
					//R.younger.parent=null;
					R.parent = root.parent; //
					R.younger.parent = R;
					
					return R;
				}
				else root.older.parent = root;
			}else {	   // if the dog is younger (as no two dogs share the same age)
				root.younger = shelter(d, root.younger); // work towards the base case
				
				if(root.younger.d.getDaysAtTheShelter() > root.d.getDaysAtTheShelter()) {
					DogNode L = root.younger;
					root.younger = L.older;
					if(L.older!=null) L.older.parent = root;
					L.older = root;
					//L.older.parent = null;
					L.parent = root.parent;
					L.older.parent = L;
					return L;
				}
				else root.younger.parent = root;
			}
			return root;
		}
		
		public DogNode adopt(Dog d) {

			DogNode ret = adopt(d, this);
            return ret; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
		}

		private DogNode adopt(Dog d, DogNode root) {
	        if(root != null) {
	            int compareResult = d.compareTo(root.d);
	            
		        if(compareResult < 0)	// if dog d is younger than root's dog
		        	root.younger = adopt(d, root.younger);
		        else if( compareResult > 0 )
		        	root.older = adopt(d, root.older);
	            else	// if equal
	            {
	            	if(root.younger == null && root.older != null) {	// swaps with older child
	            		DogNode K = root.older;
	            		K.parent = root.parent;
	            		if(root.parent != null && root.parent.younger != null && root.parent.younger.d.compareTo(root.d) == 0) 
	            			root.parent.younger = K;
	            		else if(root.parent != null) root.parent.older = K;
	            		return K;
	            	}
	            	else if(root.older == null && root.younger != null) {	// swaps with younger child
	            		DogNode L = root.younger;
	            		L.parent = root.parent;
	            		if(root.parent!=null && root.parent.younger != null && root.parent.younger.d.compareTo(root.d) == 0) 
	            			root.parent.younger = L;
	            		else if(root.parent!=null) root.parent.older = L;
	            		return L;
	            	}
	            	else if(root.older == null && root.younger == null) {
	            		return null;
	            	}
	            	else {	// if the same
	            		Dog oldD = root.younger.findOldest();	// find the oldest in the left subtree
	            		DogNode oldDNode = root.younger.findOldestNode();
	            		Dog thisDog = root.d;
	            		root.d = oldD;
	            		oldDNode.d =thisDog;	//replace
	            		
	            		
	            		root.adopt(thisDog, root.younger);
	            		trickleDown(root, oldD);
	            		while(root.older != null && root.older.d.getDaysAtTheShelter() 	// cases where the dog has an older child
	            				> root.d.getDaysAtTheShelter()) {
	            			root = rotateWithRightChild(root);
	            		}
	            		while(root.younger != null && root.younger.d.getDaysAtTheShelter()	//cases where the dog still has a younger child
	            				> root.d.getDaysAtTheShelter()) {
	            			root = rotateWithLeftChild(root);
	            		}
	            		while(root.parent != null) {
	            			root = root.parent;
	            		}
	            	}
	            }
	        }
	        return root;
	    }
		
		private void trickleDown(DogNode root, Dog oldD) {	// downheap, essentially, but with rotations
			while((root.younger != null && root.older != null) && (root.younger.d.getDaysAtTheShelter() > root.d.getDaysAtTheShelter() 
					|| root.older.d.getDaysAtTheShelter() > root.d.getDaysAtTheShelter())) {
				
				if(root.younger.d.getDaysAtTheShelter() < root.older.d.getDaysAtTheShelter()) 
					root = rotateWithRightChild(root);
				else if(root.younger.d.getDaysAtTheShelter() > root.older.d.getDaysAtTheShelter()) 
					root = rotateWithLeftChild(root);	
			}
		}	
		
		private DogNode rotateWithLeftChild(DogNode root) {		// assigns parent pointers and rotates child
			DogNode K = root.younger;
			root.younger = K.older;
			if(K.older!=null) K.older.parent = root;
			K.older = root;
			if(root.parent != null && root.parent.younger!=null) root.parent.younger = K;
			K.parent = root.parent;
			K.older.parent = K;
			return root;
		 }

		private DogNode rotateWithRightChild(DogNode root) {	// assigns parent pointers and rotates child
			DogNode K = root.older;
			root.older = K.younger;
			if(K.younger!=null) K.younger.parent = root;
			K.younger = root;
			if(root.parent != null && root.parent.older!=null) root.parent.older = K;
			K.parent = root.parent; //
			K.younger.parent = K;
			return root;
		}
		
		public Dog findOldest() {	// finds oldest dog
			DogNode soFar = this;
			while(soFar.older != null) {
				soFar = soFar.older;
			}
            return soFar.d;
		}
		
		private DogNode findOldestNode() {	// finds oldest dog node for replacement
			DogNode soFar = this;
			while(soFar.older != null) {
				soFar = soFar.older;
			}
            return soFar;
		}
		
		public Dog findYoungest() {	// finds youngest dog
			DogNode soFar = this;
			while(soFar.younger != null) {
				soFar = soFar.younger;
			}
            return soFar.d;
		}

		public Dog findDogToAdopt(int minAge, int maxAge) {	// goes to another method to compute
            DogNode found = findRecursive(minAge, maxAge, this);
            return found.d;
		}
		
		private DogNode findRecursive(int minAge, int maxAge, DogNode root) {	// same as code seen in tutorials, with slight modification
			if(root == null) return null;
			if(root.d.getAge() >= minAge && root.d.getAge() <= maxAge) return root;
			// if the max age is larger than the current age, go right
			if(maxAge > root.d.getAge()) return findRecursive(minAge, maxAge, root.older);
			else return findRecursive(minAge, maxAge, root.younger);	// else, go left
		}
		
		private double budgetVetExpenses(int numDays) {  	//uses iterator and gets total based on input
			DogShelterIterator iter = new DogShelterIterator();
			double total = 0.0;
			//DogNode r = iter.next;
			while(iter.hasNext()) {	// continues to get the cost
				if(iter.next.d.getDaysToNextVetAppointment() < numDays) 
					total += iter.next.d.getExpectedVetCost();
				iter.next();
			}
		    return total;
		}  
		  
		public ArrayList<ArrayList<Dog>> getVetSchedule() {	//gets vet schedule, only goes through tree once
            // ADD YOUR CODE HERE
			ArrayList<ArrayList<Dog>> big = new ArrayList<ArrayList<Dog>>();
			big.add(new ArrayList<Dog>());
			int indicesNow = 0;
			
			DogShelterIterator iter = new DogShelterIterator();
			while(iter.hasNext()) {
				int week = iter.next.d.getDaysToNextVetAppointment()/7;
				if(week > indicesNow) {								// for keeping track and maintaining minimum space complexity
					for(int i = 0; i < week-indicesNow; i++) {
						big.add(new ArrayList<Dog>());
					}
					int tmp = week - indicesNow;
					indicesNow += tmp;
				}
				if(big.get(week) != null) {	// adds to list
					big.get(week).add(iter.next.d);
					iter.next();
				}
			}
            return big;
		}

		public String toString() {
			String result = this.d.toString() + "\n";
			if (this.younger != null) {
				result += "younger than " + this.d.toString() + " :\n";
				result += this.younger.toString();
			}
			if (this.older != null) {
				result += "older than " + this.d.toString() + " :\n";
				result += this.older.toString();
			}
			/*if (this.parent != null) {
				result += "parent of " + this.d.toString() + " :\n";
				result += this.parent.d.toString() +"\n";
			}*/
			return result;
		}
		
	}

	public class DogShelterIterator implements Iterator<Dog> {
		private DogNode next = root;	// points to the root so the iterator knows where to look at the start
	    
	    private DogShelterIterator() {
	    	if(next != null) {	// if the root is not null
	        	while (next.younger != null)	// get the smallest element by going as leftmost as possible
	        		next = next.younger;
	        }
	    }

	    public boolean hasNext() {	// check if next is null
	    	return next != null;
	    }

	    public Dog next() {	// next
	    	if(!hasNext()) throw new NoSuchElementException();
	        DogNode Dog = next;

	        /*
	         * goes through entire tree inorder once, efficient use of parent pointers
	         */
	        if(next.older != null) {	// if right branch exists
	        	next = next.older;
	            while (next.younger != null)
	            	next = next.younger;
	            return Dog.d;
	        }

	        while(true) {	//else enter a while loop (logic pseudocode discussed online but not stolen from others)
	        	if(next.parent == null) {	//checks parent
	            	next = null;
	                return Dog.d;
	            }
	            else if(next.parent.younger == next) {
	            	next = next.parent;
	            	return Dog.d;
	            }
	            else next = next.parent;	// else, if already visited goes back and while loop commences again
	        }
	    }
	}

	
}