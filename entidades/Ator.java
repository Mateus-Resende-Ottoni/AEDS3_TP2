package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Ator implements Registro 
{

    // Variáveis
    private int id;             // Chave
    private String nome;        // Nome do ator
    
    /* Constutores */
    public Ator() {
        this(-1, "");
    }

    public Ator(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Ator(String nome) {
        this.nome = nome;
    }
    /* */

    /* Gets */
    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }
    /* */

    /* Sets */
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String novo) {
        this.nome = novo;
    }
    /* */

    @Override
    public int hashCode() {
        return this.id;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
    }

    // Print
    public String toString() {
        return "(" + this.id + ";" + this.nome +")";
    }

}