package assignment3;

import java.util.ArrayList;

import assignment3.DogShelter.DogNode;

public class DogShelterTester {
	
	private static void test() {
//		
        Dog L = new Dog("Lessie", 3, 70, 9, 25.0);
        Dog R = new Dog("Rex", 8, 100, 5, 50.0);
        Dog S = new Dog("Stella", 5, 50, 2, 250.0);
        Dog P = new Dog("Poldo", 10, 60, 1, 35.0);
        Dog B = new Dog("Bella", 1, 55, 15, 120.0);
        Dog C = new Dog("Cloud", 4, 10, 23, 80.0);
        Dog A = new Dog("Archie", 6, 120, 18, 40.0);
        Dog D = new Dog("Daisy", 7, 15, 12, 35.0);

        DogShelter test = new DogShelter( A );

        DogShelter.DogNode ll = test.new DogNode( L );
        DogShelter.DogNode rr = test.new DogNode( R );
        DogShelter.DogNode ss = test.new DogNode( S );
        DogShelter.DogNode pp = test.new DogNode( P );
        DogShelter.DogNode bb = test.new DogNode( B );
        DogShelter.DogNode cc = test.new DogNode( C );
        DogShelter.DogNode aa = test.new DogNode( A );
        DogShelter.DogNode dd = test.new DogNode( D );

        test.root.younger = ll;
        test.root.younger.parent = test.root;
        test.root.older = rr;
        test.root.older.parent = test.root;

        test.root.younger.younger = bb;
        test.root.younger.younger.parent = ll;
        test.root.younger.older = ss;
        test.root.younger.older.parent = ll;

        test.root.younger.older.younger = cc;
        test.root.younger.older.younger.parent = ss;

        test.root.older.younger = dd;
        test.root.older.younger.parent = rr;
        test.root.older.older = pp;
        test.root.older.older.parent = rr;

        System.out.print( "Testing adopt() #3... " );

        Dog a = test.adopt();

        boolean dogs = test.root.d == R && test.root.younger.d == L && test.root.older.d == P &&
                test.root.younger.younger.d == B && test.root.younger.older.d == S && test.root.younger.older.younger.d == C &&
                test.root.younger.older.older.d == D;

        boolean nulls = test.root.parent == null && test.root.older.younger == null && test.root.older.older == null &&
                test.root.younger.younger.younger == null && test.root.younger.younger.older == null &&
                test.root.younger.older.younger.younger == null && test.root.younger.older.younger.older == null &&
                test.root.younger.older.older.younger == null && test.root.younger.older.older.older == null;

        boolean parents = test.root.younger.parent.d == R &&
                test.root.older.parent.d == R &&
                test.root.younger.younger.parent.d == L &&
                test.root.younger.older.parent.d == L  &&
                test.root.younger.older.younger.parent.d == S &&
                test.root.younger.older.older.parent.d == S;
        boolean ret = a == A;

        if( !( dogs && nulls && parents && ret ) )
        {
            if( !dogs ) System.out.println( "Dogs are not assigned correctly" );
            else if( !nulls ) System.out.println( "Null values are not assigned correctly" );
            else if( !parents ) System.out.println( "Parent pointers are not assigned correctly" );
            else if( !ret ) System.out.println( "The method returned incorrect value" );
        } else {
            System.out.println("Passed.");
        }
    
	}
	
	public static void main(String[] args) {

//		Dog R = new Dog("Rex", 8, 100, 5, 50.0);
//		Dog S = new Dog("Stella", 5, 50, 2, 250.0); 
//		Dog L = new Dog("Lessie", 3, 70, 9, 25.0); 
//		Dog P = new Dog("Poldo", 10, 60, 1, 35.0); 
//		Dog B = new Dog("Bella", 1, 55, 15, 120.0); 
//		Dog C = new Dog("Cloud", 4, 10, 23, 80.0); 
//		Dog A = new Dog("Archie", 6, 120, 18, 40.0); 
//		Dog D = new Dog("Daisy", 7, 15, 12, 35.0);
//		Dog Y = new Dog("Yankee", 6, 100, 2, 10.69);
		
		
	
//		DogShelter daycare = new DogShelter(S);
//		daycare.shelter(P);
//		daycare.shelter(Y);
		
		//daycare.shelter(B); daycare.shelter(C); daycare.shelter(A); daycare.shelter(D);
		//daycare.budgetVetExpenses(80);
		
		//System.out.println(daycare.root.d);
		
 		test();
		
	}

}