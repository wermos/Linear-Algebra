import java.util.*;
/**
 * Performs Gauss-Jordan Elimination on the matrix input by the user.
 * Returns the rref (reduced row echelon form) of the matrix.
 *
 * @author Tirthankar Mazumder
 * @date June 5, 2020 -- June 6th, 2020 (updates done on 8th August, 2020)
 * @version 2.0
 */
public class GaussJordan {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        
        System.out.print("Would you like to find the rref of \n1) a system of linear");
        System.out.println(" equations, or \n2) a matrix?");
        
        System.out.println();
        
        System.out.print("Please enter an option here: ");
        
        int choice = s.nextInt();
        
        System.out.println();
        
        if (choice == 1) {
            System.out.print("Enter the number of equations: ");
            int a = s.nextInt();
            
            s.nextLine();
            
            System.out.print("Enter the number of variables: ");
            int b = s.nextInt();
    
            Matrix mat = new Matrix(a, b + 1);
            
            System.out.println();
            
            if (Parser.takeInput(mat)) {
                System.out.println("Original Matrix:");
                System.out.println(mat.toString());
                
                mat = gaussJordanEliminate(mat, true, true);
                
                System.out.println("Reduced Row Echelon Form:");
                System.out.println(mat.toString());
                
                System.out.println("Reduced Row Echelon Form, represented as an augmented matrix:");
                System.out.println(mat.augmentedMatrixToString());
            }
        } else if (choice == 2) {
            System.out.print("Enter the number of rows: ");
            int r = s.nextInt();
            
            s.nextLine();
            
            System.out.print("Enter the number of columns: ");
            int c = s.nextInt();
    
            Matrix mat = new Matrix(r, c);
            
            System.out.println();

            if (Parser.takeInput(mat)) {
                System.out.println("Original Matrix:");
                System.out.println(mat.toString());
                
                mat = gaussJordanEliminate(mat, true, false);
                
                System.out.println("Reduced Row Echelon Form:");
                System.out.println(mat.toString());
            }
        } else {
            System.out.println("Invalid choice. Exiting...");    
        }
    }

    /**
     * Performs Gauss-Jordan Elimination on the matrix.
     */
    public static Matrix gaussJordanEliminate(Matrix m, boolean reduce, boolean is_system) {
        m = Gauss.gaussEliminate(m, false);
        
        //To get from ref to rref, we need to traverse back up the matrix and
        //make all the other non-leading entries zeroes
        for (int i = m.rows() - 1; i >= 0; i--) {
            if (!m.zeroRow(i)) {
                Fraction f = new Fraction();
                
                int c;
                
                if (is_system)
                    c = m.columns() - 2; //the last column stores the value
                else
                    c = m.columns() - 1; //it's just a normal matrix

                //Here, we're finding the last nonzero entry in the row.
                for (; c > 0; c--) {
                    if (!m.get(i, c).equals(Fraction.ZERO)) {
                        f = m.get(i, c);
                        break;
                    }
                    //We don't need to check if the element at row i and column 0 is a zero
                    //or not because we know that the entire row is not filled with zeroes (we
                    //checked for that with the zeroRow method). Therefore, if we got all the
                    //way to c = 0 (by iterating past c = 1), then c = 0 has to have the lone
                    //non zero element in the row.
                }

                //Now, we use that entry to make all the entries in the c'th column
                //zero by doing a row combination.
                for (int j = i - 1; j >= 0; j--) {
                    Fraction factor = m.get(j, c).div(f).mul(Fraction.NEG_ONE);
                    m.comb(j, i, factor);
                }
            }
        }
        
        //If reduce is true, then we clear the rows of any common factors.
        //We made it an option for performance reasons
        if (reduce) {
            for (int r = 0; r < m.rows(); r++) {
                m.mult(Fraction.inverse(m.rowGCD(r, m.columns() - 2)), r);
                //Keep in mind that the arrays are zero-indexed, so the index of the second-
                //last (heh) column is m.columns() - 2
            }
        }
        return m;
    }
}