package entidades;


import estruturas.RegistroArvoreBMais;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


// Par de dois IDs, para representar o relacionamento N:N
//  entre duas entidades.
public class ParIDID implements RegistroArvoreBMais<ParIDID> {
    
    private int id1;    // chave 1
    private int id2;    // chave 2

    public ParIDID() {
        this.id1 = -1;
        this.id2 = -1;
    }

    public ParIDID(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public int getId1() {
        return this.id1;
    }

    public int getId2() {
        return this.id2;
    }

    public short size() {
        return (short) (Integer.BYTES * 2); // 2 inteiros: 8 bytes
    }

    public String toString() {
        return "("+this.id1 + ";" + this.id2+")";
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id1);
        dos.writeInt(this.id2);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        //System.out.println("ParIDID fromByteArray: " + ba);

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id1 = dis.readInt();
        this.id2 = dis.readInt();
    }

    // Método de comparação entre chaves
    @Override
    public int compareTo(ParIDID obj) {
        if (this.id1 != obj.id1) {
            return Integer.compare(this.id1, obj.id1);
        }
        // Se for -1, faz parte da busca em lista, então é necessário retornar 0
        else if (this.id2 == -1) {
            return 0;
        } else {
            return Integer.compare(this.id2, obj.id2);
        }
    }

    @Override
    public ParIDID clone() {
        return new ParIDID(this.id1, this.id2);
    }

}