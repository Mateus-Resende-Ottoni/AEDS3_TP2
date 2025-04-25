package arquivos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import entidades.Serie;
import entidades.ParNomeID;
import estruturas.HashExtensivel;
import entidades.Ator;
import entidades.ParIDID;
import estruturas.ArvoreBMais;

public class ArquivoSerie extends Arquivo<Serie> {

    private HashExtensivel<ParNomeID> indiceIndiretoNome;

    private ArvoreBMais<ParIDID> indiceBMais_Atores;

    public ArquivoSerie() throws Exception {
        super("series", Serie.class.getConstructor());
        indiceIndiretoNome = new HashExtensivel<>(
            ParNomeID.class.getConstructor(), 
            4, 
            ".\\dados\\series\\indiceNome.d.db",   // diretório
            ".\\dados\\series\\indiceNome.c.db"    // cestos
        );

        Constructor<ParIDID> c = ParIDID.class.getConstructor();
        indiceBMais_Atores = new ArvoreBMais<>(c, 7, ".\\dados\\series\\indiceB+Atores.db");
    }

    @Override
    public int create(Serie c) throws Exception {
        if (read(c.getNome()) != null) {
            System.out.println("Série já cadastrada: " + c.getNome());
            return -1; // Retorna um valor inválido se a série já existir
        }
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeID(c.getNome(), id));
        return id;
    }

    public boolean temSeries() {
        try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
            return raf.length() > 4; // Deve ter pelo menos 4 bytes para armazenar algum dado
        } catch (IOException e) {
            System.out.println("Erro ao acessar o arquivo de séries: " + e.getMessage());
        }
        return false;
    }

    public Serie read(String nome) throws Exception {
        ParNomeID pni = indiceIndiretoNome.read(ParNomeID.hash(nome));
        if (pni == null) return null;
        return read(pni.getId());
    }
    
    // Obter array com ids dos atores relacionadas a série
    public int[] getAtores(int idSerie) throws Exception {
        int[] lista_ids;

        ArrayList<ParIDID> lista = indiceBMais_Atores.read(new ParIDID(idSerie, -1));

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
            //System.out.println("ArquivoSerie delete() IndiceIndireto delete call");
            //return indiceIndiretoNome.delete(ParNomeID.hash(nome));
            
            return delete(pni.getId());
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Serie c = super.read(id);
        if (c != null) {
            if (super.delete(id)) {
                return indiceIndiretoNome.delete(ParNomeID.hash(c.getNome()));
            }
        }
        return false;
    }

    @Override
    public boolean update(Serie novaSerie) throws Exception {
        Serie serieAntiga = read(novaSerie.getNome());
        if (serieAntiga == null) {
            System.out.println("Série não encontrada para atualização: " + novaSerie.getNome());
            return false;
        }
        if (super.update(novaSerie)) {
            if (!novaSerie.getNome().equals(serieAntiga.getNome())) {
                indiceIndiretoNome.delete(ParNomeID.hash(serieAntiga.getNome()));
                indiceIndiretoNome.create(new ParNomeID(novaSerie.getNome(), novaSerie.getId()));
            }
            return true;
        }
        return false;
    }

    // Para associar um ator a uma serie
    public boolean associar_ator(int idSerie, int idAtor) throws Exception {

        Serie serie = read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada: " + idSerie);
            return false;
        }

        ParIDID par = new ParIDID(idSerie, idAtor);
        return indiceBMais_Atores.create(par);
    }

}
