package menus;


import arquivos.ArquivoAtor;
import arquivos.ArquivoEpisodio;
import arquivos.ArquivoSerie;
import entidades.Serie;
import entidades.Ator;
import entidades.Episodio;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuSeries
{
    
    ArquivoSerie arqSeries;
    ArquivoEpisodio arqEpisodio;
    ArquivoAtor arqAtor;
    private static Scanner console = new Scanner(System.in);

    public MenuSeries() throws Exception 
    {
        arqSeries = new ArquivoSerie();
        arqAtor = new ArquivoAtor();
        arqEpisodio = new ArquivoEpisodio();
    }

    public void menu() 
    {

        int opcao;
        do 
        {

            System.out.println("\nPUCFlix 2.0");
            System.out.println("-----------");
            System.out.println("> Início > Series");
            System.out.println("\n1) Incluir");
            System.out.println("2) Buscar");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("5) Atores associados");
            System.out.println("6) Associar a Ator");
            System.out.println("7) Remover ator associado");
            System.out.println("0) Voltar");

            System.out.print("\nOpçao: ");
            try 
            {
                opcao = Integer.valueOf(console.nextLine());
            } 
            catch(NumberFormatException e) 
            {
                opcao = -1;
            }

            switch (opcao) 
            {
                case 1:
                    incluirSerie();
                    break;
                case 2:
                    buscarSerie();
                    break;
                case 3:
                    alterarSerie();
                    break;
                case 4:
                    excluirSerie();
                    break;
                case 5:
                    listarAtoresAssociados();
                    break;
                case 6:
                    associarAtorASerie();
                    break;
                case 7:
                    removerAtorAssociado();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opçao inválida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void incluirSerie() 
    {
        System.out.println("\nInclusao de Serie");
        String nome = "";
        String sinopse = "";
        int ano = 0;
        String streaming = "";
        boolean dadosCorretos = false;

        do 
        {
            System.out.print("\nNome (vazio para cancelar): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;
        } while(nome.length()<1);

        do 
        {
            System.out.print("sinopse: ");
            sinopse = console.nextLine();
        } while(sinopse.length()==0);

        do 
        {
            dadosCorretos = false;
            System.out.print("Ano: ");
            if (console.hasNextInt()) 
            {
                ano = console.nextInt();
                dadosCorretos = true;
            } 
            else 
            {
                System.err.println("Ano! Por favor, insira um número válido.");
            }
            console.nextLine(); // Limpar o buffer 
        } while(!dadosCorretos);

        do 
        {
            System.out.print("streaming: ");
            streaming = console.nextLine();
        } while(streaming.length()==0);

        System.out.print("\nConfirma a inclusao da Serie? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') 
        {
            try 
            {
                Serie c = new Serie(nome, ano, sinopse, streaming);
                arqSeries.create(c);
                System.out.println("Serie incluída com sucesso.");
            } 
            catch(Exception e) 
            {
                System.out.println("Erro do sistema. Nao foi possível incluir a Serie!");
                System.err.println(e);
            }
        }
    }


    public void buscarSerie() 
    {
        System.out.println("\nBusca de Serie");
        String nome;
        String mostrarEpisodios;

        System.out.print("\nNome da Serie: ");
        nome = console.nextLine();  // Lê o Nome digitado pelo usuário

        if(nome.isEmpty())
        {
            return; 
        }


        System.out.println("Buscando " + nome);
        try 
        {
            Serie serie = arqSeries.read(nome);  // Chama o método de leitura da classe Arquivo
            if (serie != null) 
            {
                mostraSerie(serie);  // Exibe os detalhes da Série encontrada

                System.out.println("1) Mostrar episodios da serie");
                System.out.println("0) Voltar ao menu");
                System.out.print("Opcao: ");
                mostrarEpisodios = console.nextLine();
                if (mostrarEpisodios.charAt(0) == '1') {
                        buscarEpisodiosSerie(serie.getId());
                }
            } 
            else 
            {
                System.out.println("Serie nao encontrada.");
            }
        } 
        catch(Exception e) 
        {
            System.out.println("Erro do sistema. Nao foi possível buscar a Serie!");
            e.printStackTrace();
        }
    }

    public void buscarEpisodiosSerie(int serie_id)
    {
        try {
            ArrayList<Episodio> arrayEpisodios = arqEpisodio.readFromSerie(serie_id);
            int n = arrayEpisodios.size();

            if (n == 0) {
                System.out.println("");
                System.out.println("Serie nao possui episodios");
                System.out.println("");
            } else {

                Episodio [] listaEpisodio = new Episodio[n];
                for (int x = 0; x < n; x++) {
                    listaEpisodio[x] = arrayEpisodios.get(x);
                }
    
                // Organizar array por temporada
                boolean organizado = false;
                while (!organizado) {
                    organizado = true;
                    for (int x = 0; x < n-1; x++) {
                        if (listaEpisodio[x].getTemporada() > listaEpisodio[x+1].getTemporada()) {
                            Episodio temp = listaEpisodio[x];
                            listaEpisodio[x] = listaEpisodio[x+1];
                            listaEpisodio[x+1] = temp;
                            organizado = false;
                        } else if (listaEpisodio[x].getTemporada() == listaEpisodio[x+1].getTemporada()) {
                            // Organização secundária pelo número do episódio
                            if (listaEpisodio[x].getNumero() > listaEpisodio[x+1].getNumero()) {
                                Episodio temp = listaEpisodio[x];
                                listaEpisodio[x] = listaEpisodio[x+1];
                                listaEpisodio[x+1] = temp;
                                organizado = false;
                            }
                        }
                    }
                }
    
                int ultimaTemporada = listaEpisodio[0].getTemporada();
                System.out.printf("\nTemporada %d:\n", ultimaTemporada);
                for (int x = 0; x < n; x++) {
                    if (listaEpisodio[x].getTemporada() != ultimaTemporada) {
                        ultimaTemporada = listaEpisodio[x].getTemporada();
                        System.out.printf("Temporada %d:\n", ultimaTemporada);
                    }
                    System.out.printf("- %s\n", listaEpisodio[x].getTitulo());
                }

            } // fim do else

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterarSerie() 
    {
        System.out.println("\nAlteraçao de Serie");
        String nome;
        boolean nomeValido = false;

        do 
        {
            System.out.print("\nNome: ");
            nome = console.nextLine();  // Lê o nome digitado pelo usuário

            if(nome.isEmpty()) {
                return;
            }
            else {
                nomeValido = true;
            }
        } while (!nomeValido);


        try 
        {
            // Tenta ler o Série com o ID fornecido
            Serie Serie = arqSeries.read(nome);

            if (Serie != null) 
            {
                System.out.println("Serie encontrada:");
                mostraSerie(Serie);  // Exibe os dados do Série para confirmação

                // Alteração de nome
                System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
                String novoNome = console.nextLine();

                if (!novoNome.isEmpty()) 
                {
                    Serie.setNome(novoNome);  // Atualiza o nome se fornecido
                }

                // Alteração de Sinopse
                System.out.print("Nova sinopse (deixe em branco para manter a anterior): ");
                String novaSinopse = console.nextLine();

                if (!novaSinopse.isEmpty()) 
                {
                    Serie.setSinopse(novaSinopse);  // Atualiza a Sinopse se fornecida
                }

                // Alteração de Streaming
                System.out.print("\nNovo Streaming (deixe em branco para manter o anterior): ");
                String novoStreaming = console.nextLine();

                if (!novoStreaming.isEmpty()) 
                {
                    Serie.setStreaming(novoStreaming);  // Atualiza o Streaming se fornecido
                }

                // Alteração de ano
                System.out.print("Novo ano de lançamento (deixe em branco para manter o anterior): ");
                String novoAno = console.nextLine();

                if (!novoAno.isEmpty()) 
                {
                    try 
                    {
                        Serie.setAno(Integer.parseInt(novoAno));  // Atualiza o ano de lançamento se fornecido
                    } 
                    catch (NumberFormatException e) 
                    {
                        System.err.println("Erro. Valor mantido.");
                    }
                }

                // Confirmação da alteração
                System.out.print("\nConfirma as alteraçoes? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = arqSeries.update(Serie);
                    if (alterado) {
                        System.out.println("Serie alterada com sucesso.");
                        
                        nome = console.nextLine(); // Para corrigir um input extra
                    } else {
                        System.out.println("Erro ao alterar o Serie.");
                    }
                } else {
                    System.out.println("Alteraçoes canceladas.");
                }
            } else {
                System.out.println("Serie nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível alterar o Serie!");
            e.printStackTrace();
        }
    }


    public void excluirSerie() {
        System.out.println("\nExclusao de Serie");
        String nome;

        System.out.print("\nNome (Deixar em branco para cancelar): ");
        nome = console.nextLine();  // Lê o nome digitado pelo usuário

        if(nome.isEmpty())
        {
            return; 
        }

        try {
            // Tenta ler o Série com o ID fornecido
            Serie serie = arqSeries.read(nome);
            if (serie != null) {
                System.out.println("Serie encontrado:");
                mostraSerie(serie);  // Exibe os dados do Série para confirmação

                System.out.print("\nConfirma a exclusao do Serie? (S/N) ");
                char resp = console.next().charAt(0);  // Lê a resposta do usuário

                if (resp == 'S' || resp == 's') {

                    // Tentar excluir episódios vinculados à serie
                    boolean episodiosExcluidos = arqEpisodio.deleteFromSerie(serie.getId());
                    if (episodiosExcluidos) {
                        System.out.println("Episodio(s) da Serie excluído(s) com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o(s) episodio(s) da Serie.");
                    }

                    System.out.println("");

                    // Tentar excluir associações entre série e ator
                    int [] lista_id_atores = arqSeries.getAtores(serie.getId());
                    boolean associacaoExcluida = true;
                    for (int x = 0; x < lista_id_atores.length && associacaoExcluida; x++) {
                        associacaoExcluida = arqSeries.deletar_associao_ator(serie.getId(), lista_id_atores[x]);
                    }
                    if (associacaoExcluida) {
                        System.out.println("Associacao(s) com ator(es) excluída(s) com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir a(s) associacao(oes) com ator(es).");
                    }

                    System.out.println("");

                    boolean excluido = arqSeries.delete(nome);  // Chama o método de exclusão no arquivo
                    if (excluido) {
                        System.out.println("Serie excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir a Serie.");
                    }

                    nome = console.nextLine(); // Para corrigir um input extra
                    
                } else {
                    System.out.println("Exclusao cancelada.");
                }
            } else {
                System.out.println("Serie nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível excluir o Serie!");
            e.printStackTrace();
        }
    }


    public void mostraSerie(Serie Serie) {
        if (Serie != null) {
            System.out.println("\nDetalhes do Serie:");
            System.out.println("----------------------");
            System.out.printf("Nome......: %s%n",           Serie.getNome());
            System.out.printf("Sinopse.......: %s%n",       Serie.getSinopse());
            System.out.printf("Ano de lançamento: %s%n",    Serie.getAno());
            System.out.printf("Streaming...: %s%n",         Serie.getStreaming());
            System.out.println("----------------------");
        }
    }

    private void mostraAtor(Ator ator) {
        if (ator != null) {
            System.out.println("\nDetalhes do Ator:");
            System.out.println("----------------------");
            System.out.printf("ID.........: %d%n", ator.getId());
            System.out.printf("Nome.......: %s%n", ator.getNome());
            System.out.println("----------------------");
        }
    }

    // Métodos relacionados a relação Série-Ator
    private void associarAtorASerie() {
        System.out.println("\nAssociar Série a Ator");
        String nome;

        // Obter nome da série
        System.out.print("\nNome da Série: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Serie serie = arqSeries.read(nome);
            if (serie != null) {
                mostraSerie(serie);;

                // Obter nome do ator
                System.out.print("\nNome do Ator:");
                nome = console.nextLine();

                if (nome.isEmpty()) {
                    return;
                }

                
                Ator ator = arqAtor.read(nome);
                if (ator != null) {
                    mostraAtor(ator);

                    // Confirmar associação série-ator
                    System.out.print("\nAssociar Série a Ator? (S/N) ");
                    char resp = console.nextLine().charAt(0);

                    if (resp == 'S' || resp == 's') {
                        // Tentar inclusão da associação na árvore B+ Série_Ator
                        // (o próprio método chamará a associação na árvore B+ Ator_Série)
                        boolean associado = arqSeries.associar_ator(serie.getId(), ator.getId());
                        if (associado) {
                            System.out.println("Série associada a ator com sucesso.");
                        }
                        // Reportar falha da operação
                        else {
                            System.out.println("Erro ao associar série e ator.");
                        }
                    } else {
                        System.out.println("Associaçao cancelada.");
                    }


                } else {
                    System.out.println("Ator nao encontrada.");
                }
                

            } else {
                System.out.println("Série nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema.");
            e.printStackTrace();
        }
    }

    public void listarAtoresAssociados() {
        System.out.println("\nListar Atores associadas a Série");
        String nome;

        System.out.print("\nNome da Série: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Serie serie = arqSeries.read(nome);
            if (serie != null) {
                int [] lista_id_atores = arqSeries.getAtores(serie.getId());

                // Teste para ver lista de ids coletados
                for (int x = 0; x < lista_id_atores.length; x++) {
                    System.out.println(lista_id_atores[x]);
                }

                // Conferir se há associações para listar
                if (lista_id_atores.length <= 0) {
                    System.out.println("Ator nao possui series associadas.");
                    return;
                } else {
                    Ator ator;
                    System.out.println("-------------");
                    for (int x = 0; x < lista_id_atores.length; x++) {
                        ator = arqAtor.read(lista_id_atores[x]);
                        if (serie != null) {
                            System.out.println(ator.getNome());
                        }
                    }
                    System.out.println("-------------");
                }

            } else {
                System.out.println("Serie nao encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível realizar a ação.");
            e.printStackTrace();
        }
    }

    public void removerAtorAssociado() {
        System.out.println("\nRemover Ator associado a Série");
        String nome;

        System.out.print("\nNome da Série: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Serie serie = arqSeries.read(nome);
            if (serie != null) {
                int [] lista_id_atores = arqSeries.getAtores(serie.getId());
                if (lista_id_atores.length == 0) {
                    System.out.println("Ator nao possui series associadas.");
                    return;
                } else {
                    Ator ator;
                    System.out.println("-------------");
                    for (int x = 0; x < lista_id_atores.length; x++) {
                        ator = arqAtor.read(lista_id_atores[x]);
                        if (ator != null) {
                            System.out.println(ator.getNome());
                        }
                    }
                    System.out.println("-------------");
    
                    // Obter nome do ator
                    System.out.print("\nNome do Ator a remover:");   
                    nome = console.nextLine();
    
                    if (nome.isEmpty()) {
                        return;
                    }
    
                    ator = arqAtor.read(nome);
                    boolean remocao = arqSeries.deletar_associao_ator(serie.getId(), ator.getId());
                    if (remocao) {
                        System.out.println("Associacao removida com sucesso.");
                    } else {
                        System.out.println("Erro ao remover associacao.");
                    }
                }


            } else {
                System.out.println("Serie nao encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível realizar a ação.");
            e.printStackTrace();
        }
    }

}