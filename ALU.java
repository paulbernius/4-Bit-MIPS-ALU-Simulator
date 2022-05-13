import java.util.Locale;
import java.util.Scanner;

public class ALU {

    /* **************************************

        NOTES:

        1. "Chapter_3-indepth1-ALU-new" Slides: 14, 25, 27, 28, 31, 32, 33, 34


     ************************************** */

    public static void main(String[] args) {

        // Initialize variables
        Scanner scnr = new Scanner(System.in);
        String a;
        String b;
        String operation;
        boolean running = true;


        while (running) {

            // Get operation code
            System.out.print("Input operation (AND, OR, ADD, SUB, SLT), or 'Q' to quit: ");
            operation = scnr.nextLine();
            operation = operation.toUpperCase();
            while (!(operation.equals("AND") || operation.equals("OR") || operation.equals("ADD") || operation.equals("SUB") || operation.equals("SLT") || operation.equals("Q"))) {
                System.out.print("Please enter a valid operation: ");
                operation = scnr.nextLine();
                operation = operation.toUpperCase();
            }

            if (operation.equals("Q")) {
                running = false;
                break;
            }

            // Get variable 'a'
            System.out.print("Input variable 'A' as a 4-bit binary number: ");
            a = scnr.nextLine();

            while (a.length() != 4) {
                System.out.print("Please enter a valid binary number: ");
                a = scnr.nextLine();
            }

            // Get variable 'b'
            System.out.print("Input variable 'B' as a 4-bit binary number: ");
            b = scnr.nextLine();

            while (b.length() != 4) {
                System.out.print("Please enter a valid binary number: ");
                b = scnr.nextLine();
            }

            // Convert string to int array to process the result per bit
            int[] aArray = {0, 0, 0, 0};
            for (int i = 0; i < 4; i++) {
                aArray[i] = Character.getNumericValue(a.charAt(i));
            }

            // Convert string to int array to process the result per bit
            int[] bArray = {0, 0, 0, 0};
            for (int i = 0; i < 4; i++) {
                bArray[i] = Character.getNumericValue(b.charAt(i));
            }

            // Call ALU
            ALU(aArray, bArray, operation);
        }
    }

    public static void ALU(int[] operand1, int[] operand2, String operation) {

        // Initialize variables
        String result = "";
        int carryIn = 0;
        int[] resultArray = {0, 0, 0, 0};
        int[] carryOut = {0, 0, 0, 0};

        // If SUB (110) or SLT (111) call two's complement
        if (operation.equals("SUB") || operation.equals("SLT")) { // TWO's COMPLEMENT
            operand2 = twosComplement(operand2);
        }

        for (int i = 3; i >= 0; i--) { // Loop to execute each operation four times for each bit.

            //operand2[i] = XOR(operand2[i], InvertB(operand2[i])); // To handle the OR gate for 'b' & 'invertedB'.
            // I didn't see how this resulted in an accurate
            // answer so I removed it.

            if (operation.equals("ADD")) { // Execute if operation is ADD
                carryOut[i] = ADD(operand1[i], operand2[i], carryIn); // Call ADD
                if (carryOut[i] == 2) { // If result is 2, then ADD executed 1 + 1, meaning carryOut = 1
                    result = "0" + result;
                    resultArray[i] = 0;
                    carryOut[i] = 1;
                } else if (carryOut[i] == 1) { // If result is 1, then ADD executed 1 + 0, meaning carryOut = 0
                    result = "1" + result;
                    resultArray[i] = 1;
                    carryOut[i] = 0;
                } else { // If result is 0, then ADD executed 0 + 0, meaning carryOut = 0
                    result = "0" + result;
                    resultArray[i] = 0;
                    carryOut[i] = 0;
                }
                carryIn = carryOut[i]; // Set carryIn equal to carryOut from previous operation.


            }
            if (operation.equals("AND")) { // AND operation
                int temp = AND(operand1[i], operand2[i]);
                result = temp + result;
                resultArray[i] = temp;
            }

            if (operation.equals("OR")) { // OR operation
                result = OR(operand1[i], operand2[i]) + result;
            }

            if (operation.equals("SUB")) { // Execute if operation is SUB
                carryOut[i] = ADD(operand1[i], operand2[i], carryIn); // Call ADD
                if (carryOut[i] == 2) { // If result is 2, then ADD executed 1 + 1, meaning carryOut = 1
                    result = "0" + result;
                    resultArray[i] = 0;
                    carryOut[i] = 1;
                } else if (carryOut[i] == 1) { // If result is 1, then ADD executed 1 + 0, meaning carryOut = 0
                    result = "1" + result;
                    resultArray[i] = 1;
                    carryOut[i] = 0;
                } else { // If result is 0, then ADD executed 0 + 0, meaning carryOut = 0
                    result = "0" + result;
                    resultArray[i] = 0;
                    carryOut[i] = 0;
                }
                carryIn = carryOut[i]; // Set carryIn equal to carryOut from previous operation.


            }

            if (operation.equals("SLT")) { // SLT operation
                carryOut[i] = ADD(operand1[i], operand2[i], carryIn); // Call ADD
                if (carryOut[i] == 2) { // If result is 2, then ADD executed 1 + 1, meaning carryOut = 1
                    result = "0" + result;
                    resultArray[i] = 0;
                    carryOut[i] = 1;
                } else if (carryOut[i] == 1) { // If result is 1, then ADD executed 1 + 0, meaning carryOut = 0
                    result = "1" + result;
                    resultArray[i] = 1;
                    carryOut[i] = 0;
                } else { // If result is 0, then ADD executed 0 + 0, meaning carryOut = 0
                    result = "0" + result;
                    resultArray[i] = 0;
                    carryOut[i] = 0;
                }
                carryIn = carryOut[i]; // Set carryIn equal to carryOut from previous operation.
            }
        }

        if (carryOut[0] == 1) {
            result = result.replaceFirst("0", "1");
        }

        int overFlowDetection = Overflow(carryOut); // Call Overflow method to detect overflow
        int zeroDetection = ZeroDetection(resultArray); // Call ZeroDetection method to detect zero
        if (zeroDetection == 0) { // Since the result is inverted, if zeroDetection = 0, then zero was detected
            //System.out.println("Zero detected");
        } // I wasn't completely sure where this belonged in the ALU but wrote the code to show I understand the method

        if (operation.equals("ADD")) { // Code block to determine if any bits were carried
            int carry = 0;
            for (int i = 0; i < 4; i ++) {
                if (carryOut[i] > 0) {
                    carry = 1;
                }
            }
            System.out.println(result + ", " + carry + ", " + overFlowDetection); // Output ADD operator syntax
        } else if (operation.equals("SLT")) {
            System.out.println(result.charAt(0)); // Output SLT operator syntax
        } else {
            System.out.println(result); // Output the rest of the operator syntax
        }

    }

    public static int[] twosComplement(int[] operand2) { // TWOS COMPLEMENT METHOD
        int addOne = 1; // Bit to be added

        // Flip each bit
        for (int i = 3; i >= 0; i--) {
            if (operand2[i] == 0) {
                operand2[i] = 1;
            } else {
                operand2[i] = 0;
            }
        }

        int index = 3;

        // Find where the additional '1' can go and insert it.
        while (addOne == 1) {
            if (index == 0) {
                System.out.println("Overflow has occured during Two's Complement");
                break;
            }
            if (operand2[index] == 0) {
                operand2[index] = 1;
                addOne--;
            } else {
                operand2[index] = 0;
                index--;
            }
        }
        return operand2;
    }

    public static int ZeroDetection(int[] results) { // ZERO DETECTION METHOD
        // OR gate with four inputs.
        int a = results[0];
        int b = results[1];
        int c = results[2];
        int d = results[3];

        if (a == 1 || b == 1 || c == 1 || d == 1) {
            return 0; // INVERSE
        } else {
            return 1; // INVERSE
        }
    }

    public static int Overflow(int[] carryOut) { // OVERFLOW METHOD
        // XOR gate with last two carryOuts.
        int a = carryOut[0];
        int b = carryOut[1];
        return XOR(a, b);
    }

    public static int AND(int operand1, int operand2) { // AND METHOD (000)
        if (operand1 == 1 && operand2 == 1) {
            return 1;
        }
        return 0;
    }

    public static int OR(int operand1, int operand2) { // OR METHOD (001)
        if (operand1 == 0 && operand2 == 1) {
            return 1;
        } else if (operand1 == 1 && operand2 == 0) {
            return 1;
        } else if (operand1 == 1 && operand2 == 1) {
            return 1;
        }
        return 0;
    }


    public static int ADD(int operand1, int operand2, int carryIn) { // ADD METHOD (010) & USED FOR SUB (110) & SLT (111)
        return operand1 + operand2 + carryIn;
    }

    public static int InvertB(int b) { // INVERT B METHOD
        if (b == 1) {return 0;}
        return 1;
    }

    public static int XOR(int a, int b) { // XOR METHOD
        if (a == 0 && b == 0) {
            return 0;
        } else if (a == 0 && b == 1) {
            return 1;
        } else if (a == 1 && b == 0) {
            return 1;
        }

        return 0;
    }

}
