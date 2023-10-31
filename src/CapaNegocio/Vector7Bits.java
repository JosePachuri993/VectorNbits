package CapaNegocio;

public class Vector7Bits {

    //Atributos
    int v[];
    int cantidad;

    //constructor
    public Vector7Bits(int NumElementos, int CantBits) {
        int NumBits = NumElementos * CantBits;
        int NumEnteros = NumBits / 32;
        if (NumBits % 32 != 0) {
            NumEnteros++;
        }
    }

    public Vector7Bits(int NumElementos) {
        int NumBits = NumElementos * 7;
        int NumEnteros = NumBits / 32;     //numero de enteros que nesecitamos
        if (NumBits % 32 != 0) {
            NumEnteros++;       //en caso que sobren bits fuera se nesecita uno adicional
        }        
        v = new int[NumEnteros];    //crea vectores necesarios para guardar los Numerodebits
        cantidad = NumElementos;
    }

    public void insertar(int elemento, int posicion) {
        if (posicion <= cantidad) {
            int ele1 = elemento;
            int mask = (int) (Math.pow(2, 7) - 1);      //127=0000000000000000000000001111111
            int NumeroBits = calNumBits(posicion);      //posicion que va a recorrer
            int NumeroEntero = calNumEnteros(posicion); //en que vecctor va a insertar            
            System.out.println("mask " + mask + "=" + Integer.toBinaryString(mask));

            mask = mask << NumeroBits;                  //System.out.println("mask "+mask+ "=" + Integer.toBinaryString(mask)); 
            mask = ~mask;                               //System.out.println("~mask "+mask+ "=" + Integer.toBinaryString(mask));                     
            v[NumeroEntero] = v[NumeroEntero] & mask;   // limpia la v[NumeroEntero] con puros 0   
            System.out.println("elemnto " + Integer.toBinaryString(elemento));
            elemento = elemento << NumeroBits;
            System.out.println("elemnto " + Integer.toBinaryString(elemento));
            v[NumeroEntero] = v[NumeroEntero] | elemento;
            System.out.println(Integer.toBinaryString((v[0])));
            if (NumeroBits + 7 > 32) {
                int mask1 = (int) (Math.pow(2, 7) - 1);
                mask1 = mask1 >>> (32 - NumeroBits);
                mask1 = ~mask1;
                v[NumeroEntero + 1] = v[NumeroEntero + 1] & mask1;
                ele1 = ele1 >>> (32 - NumeroBits);
                v[NumeroEntero + 1] = v[NumeroEntero + 1] | ele1;//guarda en forma de bits
                System.out.println(Integer.toBinaryString((v[NumeroEntero + 1])));
            }
        }
    }

    private int calNumBits(int posicion) {
        return (posicion - 1) * 7 % 32;//posicion del bit
    }

    private int calNumEnteros(int posicion) {
        return (posicion - 1) * 7 / 32;//posicion del vector      
    }

    public int sacar(int posicion) {
        int mask = (int) (Math.pow(2, 7) - 1);  //11111
        int NumeroBits = calNumBits(posicion);
        int NumeroEntero = calNumEnteros(posicion);
        mask = mask << NumeroBits;  //....000111111100000000000000
        mask=mask&v[NumeroEntero];  //devuelve el valor
        mask=mask>>>NumeroBits;
        
        if((NumeroBits+7)>32){    //saca los bits faltantes que pasen de los 32 bits del vector siguiente
            int mask1=(int) (Math.pow(2, 7)-1);
            mask1=mask1>>>(32-NumeroBits);
            mask1=mask1&v[NumeroEntero+1];//del vector siguiente donde esta lo faltante
            mask1=mask1<<(32-NumeroBits);
            mask=mask|mask1;
        }
        return mask;    //retorna en enteros, no en bits    
    }

    @Override
    public String toString() {
        String s="V= [";
        for (int i = 1; i <= cantidad; i++) {
            s=s+sacar(i)+", ";
        }
        return s+"]";
    }
    
    public static void main(String[] args) {
        Vector7Bits A = new Vector7Bits(10);
        A.insertar(10, 3);
        System.out.println(A.sacar(3));
        A.insertar(81, 5);
        System.out.println(A.sacar(5));
        System.out.println(A.toString());
    }
}
