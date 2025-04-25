package entidades;


import estruturas.RegistroHashExtensivel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


// Par de dois IDs, para representar o relacionamento N:N
//  entre duas entidades.
public class ParIDID implements RegistroHashExtensivel<ParIDID> {
    
    private int id1;   // chave 1
    private int id2;    // chave 2
    private final short TAMANHO = 8;  // tamanho em bytes

    public ParIDID() {
        this.id1 = -1;
        this.id2 = -1;
    }

    public ParIDID(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.id1 + ";" + this.id2+")";
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id1);
        dos.writeLong(this.id2);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id1 = dis.readInt();
        this.id2 = dis.readInt();
    }

}