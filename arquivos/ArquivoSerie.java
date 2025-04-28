package arquivos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import entidades.Serie;
import entidades.ParNomeID;
import entidades.ParIDID;
//import entidades.Ator;
import estruturas.HashExtensivel;
import estruturas.ArvoreBMais;

public class ArquivoSerie extends Arquivo<Serie> {

    private HashExtensivel<ParNomeID> indiceIndiretoNome;
    private ArvoreBMais<ParIDID> indiceBMais_Atores;

    public ArquivoSerie() throws Exception {
        super("series", Serie.class.getConstructor());
        indiceIndiretoNome = new HashExtensivel<>(
            ParNomeID.class.getConstructor(),
            4,
            ".\\dados\\series\\indiceNome.d.db",
            ".\\dados\\series\\indiceNome.c.db"
        );

        Constructor<ParIDID> c = ParIDID.class.getConstructor();
        indiceBMais_Atores = new ArvoreBMais<>(c, 7, ".\\dados\\series\\indiceB+Atores.db");
    }

    @Override
    public int create(Serie c) throws Exception {
        if (read(c.getNome()) != null) {
            System.out.println("Série já cadastrada: " + c.getNome());
            return -1;
        }
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeID(c.getNome(), id));
        return id;
    }

    public boolean temSeries() {
        try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
            return raf.length() > 4;
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

    // Método público para associar Ator a Série (e vice-versa)
    public boolean associar_ator(int idSerie, int idAtor) throws Exception {
        Serie serie = read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada: " + idSerie);
            return false;
        }

        ParIDID par = new ParIDID(idSerie, idAtor);
        boolean associado = indiceBMais_Atores.create(par);

        if (associado) {
            ArquivoAtor arqAtor = new ArquivoAtor();
            arqAtor.associar_serie_sem_retorno(idAtor, idSerie);
        }

        return associado;
    }

    // Método protegido para associação silenciosa
    protected boolean associar_ator_sem_retorno(int idSerie, int idAtor) throws Exception {
        ParIDID par = new ParIDID(idSerie, idAtor);
        return indiceBMais_Atores.create(par);
    }
}
