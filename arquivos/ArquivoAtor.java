package arquivos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import entidades.Ator;
import entidades.ParNomeID;
import entidades.ParIDID;
import estruturas.ArvoreBMais;
import estruturas.HashExtensivel;

public class ArquivoAtor extends Arquivo<Ator> {

    private HashExtensivel<ParNomeID> indiceIndiretoNome;
    private ArvoreBMais<ParIDID> indiceBMais_Series;
    private ArquivoSerie arqSerie;

    public ArquivoAtor() throws Exception {
        super("atores", Ator.class.getConstructor());
        indiceIndiretoNome = new HashExtensivel<>(
            ParNomeID.class.getConstructor(),
            4,
            ".\\dados\\atores\\indiceNome.d.db",
            ".\\dados\\atores\\indiceNome.c.db"
        );

        Constructor<ParIDID> c = ParIDID.class.getConstructor();
        indiceBMais_Series = new ArvoreBMais<>(c, 7, ".\\dados\\atores\\indiceB+Series.db");
    }

    @Override
    public int create(Ator c) throws Exception {
        if (read(c.getNome()) != null) {
            System.out.println("Ator já cadastrado: " + c.getNome());
            return -1;
        }
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeID(c.getNome(), id));
        return id;
    }

    public boolean temAtores() {
        try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
            return raf.length() > 4;
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

    public boolean temSerie(int idAtor) throws Exception {
        boolean resultado = false;
        ArrayList<ParIDID> lista = indiceBMais_Series.read(new ParIDID(idAtor, -1));

        if (lista != null) {
            resultado = true;
        }

        return resultado;
    }

    public int[] getSeries(int idAtor) throws Exception {
        int[] lista_ids;
        ArrayList<ParIDID> lista = indiceBMais_Series.read(new ParIDID(idAtor, -1));

        if (lista == null) {
            lista_ids = new int[0];
        } else {
            lista_ids = new int[lista.size()];
            for (int i = 0; i < lista.size(); i++) {
            
                // Teste do par lido
                //System.out.println("ID Ator: " + lista.get(i).getId1() + " ID Serie: " + lista.get(i).getId2());

                lista_ids[i] = lista.get(i).getId2();
            }
        }

        indiceBMais_Series.print();

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
            System.out.println("Ator não encontrado para atualização: " + novoAtor.getNome());
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

    // Método público para associar Série a Ator (e vice-versa)
    public boolean associar_serie(int idAtor, int idSerie) throws Exception {
        Ator ator = read(idAtor);
        if (ator == null) {
            System.out.println("Ator não encontrado: " + idAtor);
            return false;
        }

        ParIDID par = new ParIDID(idAtor, idSerie);
        
        //System.out.println("Associar_serie, IdAtor(" + idAtor +") e IdSerie(" + idSerie + ")");
        //System.out.println("Criando em Arvore B+ Ator_Serie par: " + par.getId1() + " " + par.getId2());
        boolean associado = indiceBMais_Series.create(par);

        //mostrar_arvore();

        if (associado) {
            //System.out.printf("\n\n");
            //System.out.println("Criando em Arvore B+ Serie_Ator par: " + par.getId2() + " " + par.getId1());

            arqSerie = new ArquivoSerie();

            arqSerie.associar_ator_sem_retorno(idSerie, idAtor);

            //arqSerie.mostrar_arvore();
        }

        return associado;
    }

    // Método protegido para associação silenciosa
    protected boolean associar_serie_sem_retorno(int idAtor, int idSerie) throws Exception {
        ParIDID par = new ParIDID(idAtor, idSerie);
        return indiceBMais_Series.create(par);
    }

    // Deletar relação entre Ator e Série
    public boolean deletar_associao_serie(int idAtor, int idSerie) throws Exception {
        Ator ator = read(idAtor);
        if (ator == null) {
            System.out.println("Ator não encontrado: " + idAtor);
            return false;
        }

        ParIDID par = new ParIDID(idAtor, idSerie);
        boolean deletado = indiceBMais_Series.delete(par);

        if (deletado) {

            arqSerie = new ArquivoSerie();

            arqSerie.deletar_associacao_ator_sem_retorno(idSerie, idAtor);
        }

        return deletado;
    }

    // Método protegido para deletar associação silenciosamente
    protected boolean deletar_associacao_serie_sem_retorno(int idAtor, int idSerie) throws Exception {
        ParIDID par = new ParIDID(idAtor, idSerie);
        return indiceBMais_Series.delete(par);
    }

    public void mostrar_arvore() throws Exception {
        indiceBMais_Series.print();
        System.out.printf("\n\n");
    }

}
