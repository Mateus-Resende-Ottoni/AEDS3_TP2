package arquivos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import entidades.Ator;
import entidades.ParNomeID;
import estruturas.HashExtensivel;

import entidades.ParIDID;
import estruturas.ArvoreBMais;

public class ArquivoAtor extends Arquivo<Ator> {

    private HashExtensivel<ParNomeID> indiceIndiretoNome;

    private ArvoreBMais<ParIDID> indiceBMais_Series;

    public ArquivoAtor() throws Exception {
        super("series", Ator.class.getConstructor());
        indiceIndiretoNome = new HashExtensivel<>(
            ParNomeID.class.getConstructor(), 
            4, 
            ".\\dados\\atores\\indiceNome.d.db",   // diretório
            ".\\dados\\atores\\indiceNome.c.db"    // cestos
        );

        Constructor<ParIDID> c = ParIDID.class.getConstructor();
        indiceBMais_Series = new ArvoreBMais<>(c, 7, ".\\dados\\atores\\indiceB+Series.db");
    }

    @Override
    public int create(Ator c) throws Exception {
        if (read(c.getNome()) != null) {
            System.out.println("Ator já cadastrado: " + c.getNome());
            return -1; // Retorna um valor inválido se o ator já existir
        }
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeID(c.getNome(), id));
        return id;
    }

    public boolean temAtores() {
        try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
            return raf.length() > 4; // Deve ter pelo menos 4 bytes para armazenar algum dado
        } catch (IOException e) {
            System.out.println("Erro ao acessar o arquivo de atores: " + e.getMessage());
        }
        return false;
    }

    public Ator read(String nome) throws Exception {
        ParNomeID pni = indiceIndiretoNome.read(ParNomeID.hash(nome));
        if (pni == null) return null;
        return read(pni.getId());
    }

    // Obter array com ids das séries relacionadas ao ator
    public int[] getSeries(int idAtor) throws Exception {
        int[] lista_ids;

        ArrayList<ParIDID> lista = indiceBMais_Series.read(new ParIDID(idAtor, -1));

        if (lista == null) {
            lista_ids = new int[0];
        } else {
            lista_ids = new int[lista.size()];
            for (int i = 0; i < lista.size(); i++) {
                lista_ids[i] = lista.get(i).getId2();
            }
        }

        return lista_ids;
    }
    
    public boolean delete(String nome) throws Exception {
        ParNomeID pni = indiceIndiretoNome.read(ParNomeID.hash(nome));
        if (pni != null) {
            //System.out.println("ArquivoAtor delete() IndiceIndireto delete call");
            //return indiceIndiretoNome.delete(ParNomeID.hash(nome));
            
            return delete(pni.getId());
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Ator c = super.read(id);
        if (c != null) {
            if (super.delete(id)) {
                return indiceIndiretoNome.delete(ParNomeID.hash(c.getNome()));
            }
        }
        return false;
    }

    @Override
    public boolean update(Ator novoAtor) throws Exception {
        Ator atorAntigo = read(novoAtor.getNome());
        if (atorAntigo == null) {
            System.out.println("Série não encontrada para atualização: " + novoAtor.getNome());
            return false;
        }
        if (super.update(novoAtor)) {
            if (!novoAtor.getNome().equals(atorAntigo.getNome())) {
                indiceIndiretoNome.delete(ParNomeID.hash(atorAntigo.getNome()));
                indiceIndiretoNome.create(new ParNomeID(novoAtor.getNome(), novoAtor.getId()));
            }
            return true;
        }
        return false;
    }

    // Para associar uma serie a um ator
    public boolean associar_serie(int idAtor, int idSerie) throws Exception {
        Ator ator = read(idAtor);
        if (ator == null) {
            System.out.println("Ator não encontrado: " + idAtor);
            return false;
        }
        ParIDID par = new ParIDID(idAtor, idSerie);
        return indiceBMais_Series.create(par);
    }

}
