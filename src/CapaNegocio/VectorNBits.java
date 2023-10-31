/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaNegocio;

/**
 *
 * @author Usuario
 */
public class VectorNBits {
    //Atributos

    int v[];
    int cantidad;
    int CantBits;

    //constructor   
    public VectorNBits(int NumElementos, int CantBits) {
        int NumBits = NumElementos * CantBits;
        int NumEnteros = NumBits / 32;     //numero de enteros que nesecitamos
        if (NumBits % 32 != 0) {
            NumEnteros++;       //en caso que sobren bits fuera se nesecita uno adicional
        }
        v = new int[NumEnteros];    //crea vectores necesarios para guardar los Numerodebits
        cantidad = NumElementos;
        this.CantBits = CantBits;
    }

    public void insertar(int elemento, int posicion) {
        if (posicion <= cantidad) {
            int ele1 = elemento;
            int mask = (int) (Math.pow(2, CantBits) - 1);      //127=0000000000000000000000001111111
            int NumeroBits = calNumBits(posicion);      //posicion que va a recorrer
            int NumeroEntero = calNumEnteros(posicion); //en que vecctor va a insertar  
            mask = mask << NumeroBits;                  //System.out.println("mask "+mask+ "=" + Integer.toBinaryString(mask)); 
            mask = ~mask;                               //System.out.println("~mask "+mask+ "=" + Integer.toBinaryString(mask));                     
            v[NumeroEntero] = v[NumeroEntero] & mask;   // limpia la v[NumeroEntero] con puros 0   
            elemento = elemento << NumeroBits;
            v[NumeroEntero] = v[NumeroEntero] | elemento;
            if (NumeroBits + CantBits > 32) {
                int mask1 = (int) (Math.pow(2, CantBits) - 1);
                mask1 = mask1 >>> (32 - NumeroBits);
                mask1 = ~mask1;
                v[NumeroEntero + 1] = v[NumeroEntero + 1] & mask1;
                ele1 = ele1 >>> (32 - NumeroBits);
                v[NumeroEntero + 1] = v[NumeroEntero + 1] | ele1;//guarda en forma de bits
            }
        }
    }

    private int calNumBits(int posicion) {
        return (posicion - 1) * CantBits % 32;//posicion del bit
    }

    private int calNumEnteros(int posicion) {
        return (posicion - 1) * CantBits / 32;//posicion del vector      
    }

    public int sacar(int posicion) {
        int mask = (int) (Math.pow(2, CantBits) - 1);  //11111
        int NumeroBits = calNumBits(posicion);
        int NumeroEntero = calNumEnteros(posicion);
        mask = mask << NumeroBits;  //....000111111100000000000000
        mask = mask & v[NumeroEntero];  //devuelve el valor
        mask = mask >>> NumeroBits;

        if ((NumeroBits + CantBits) > 32) {    //saca los bits faltantes que pasen de los 32 bits del vector siguiente
            int mask1 = (int) (Math.pow(2, CantBits) - 1);
            mask1 = mask1 >>> (32 - NumeroBits);
            mask1 = mask1 & v[NumeroEntero + 1];//del vector siguiente donde esta lo faltante
            mask1 = mask1 << (32 - NumeroBits);
            mask = mask | mask1;
        }
        return mask;    //retorna en enteros, no en bits    
    }

    @Override
    public String toString() {
        String s = "V= [";
        for (int i = 1; i <= cantidad; i++) {
            s = s + sacar(i) + ", ";
        }
        return s + "]";
    }
    public String toStringP(){
    String s="[";
    for (int i=1;i<=cantidad;i++){
        int cantidadElementos=Integer.toBinaryString(sacar(i)).length();
        String numerobit=Integer.toBinaryString(sacar(i));
        while (cantidadElementos<CantBits){
            numerobit="0"+numerobit;
            cantidadElementos++;
        }       
        s=s+numerobit+",";
    }
    s=s.substring(0,s.length()-1);
    return s+"]";
}

    public static void main(String[] args) {
        VectorNBits A = new VectorNBits(10, 8);
        A.insertar(10, 3);
        A.insertar(81, 5);
        System.out.println(A.sacar(5));
        System.out.println(A.toString());
    }
}
