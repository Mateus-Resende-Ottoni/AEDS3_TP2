package menus;

import arquivos.ArquivoAtor;
import arquivos.ArquivoSerie;
import entidades.Ator;
import entidades.Serie;

import java.util.Scanner;

public class MenuAtor {
    
    private ArquivoAtor arqAtor;
    private ArquivoSerie arqSerie;
    private static Scanner console = new Scanner(System.in);

    public MenuAtor() throws Exception {
        arqAtor = new ArquivoAtor();
        arqSerie = new ArquivoSerie();
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\nPUCFlix 2.0");
            System.out.println("-----------");
            System.out.println("> Início > Atores");
            System.out.println("\n1) Incluir");
            System.out.println("2) Buscar");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("5) Séries Associadas");
            System.out.println("6) Associar a Série");
            System.out.println("7) Remover série associada");
            System.out.println("0) Voltar");

            System.out.print("\nOpçao: ");
            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluirAtor();
                    break;
                case 2:
                    buscarAtor();
                    break;
                case 3:
                    alterarAtor();
                    break;
                case 4:
                    excluirAtor();
                    break;
                case 5:
                    listarSeriesAssociadas();
                    break;
                case 6:
                    associarAtorASerie();
                    break;
                case 7:
                    removerSerieAssociada();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opçao inválida!");
            }

        } while (opcao != 0);
    }

    private void incluirAtor() {
        System.out.println("\nInclusao de Ator");
        String nome;

        do {
            System.out.print("\nNome (vazio para cancelar): ");
            nome = console.nextLine();
            if (nome.isEmpty()) {
                return;
            }
        } while (nome.length() == 0);

        System.out.print("\nConfirma a inclusao do Ator? (S/N) ");
        char resp = console.nextLine().charAt(0);

        if (resp == 'S' || resp == 's') {
            try {
                Ator novoAtor = new Ator(nome);
                arqAtor.create(novoAtor);
                System.out.println("Ator incluído com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Nao foi possível incluir o ator!");
                e.printStackTrace();
            }
        }
    }

    private void buscarAtor() {
        System.out.println("\nBusca de Ator");
        String nome;

        System.out.print("\nNome do Ator: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Ator ator = arqAtor.read(nome);
            if (ator != null) {
                mostraAtor(ator);
            } else {
                System.out.println("Ator nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível buscar o ator!");
            e.printStackTrace();
        }
    }

    private void alterarAtor() {
        System.out.println("\nAlteraçao de Ator");
        String nome;

        System.out.print("\nNome do Ator: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Ator ator = arqAtor.read(nome);

            if (ator != null) {
                System.out.println("Ator encontrado:");
                mostraAtor(ator);

                System.out.print("\nNovo nome (deixe vazio para manter o atual): ");
                String novoNome = console.nextLine();

                if (!novoNome.isEmpty()) {
                    ator.setNome(novoNome);
                }

                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {
                    boolean alterado = arqAtor.update(ator);
                    if (alterado) {
                        System.out.println("Ator alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o ator.");
                    }
                } else {
                    System.out.println("Alteraçao cancelada.");
                }
            } else {
                System.out.println("Ator nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível alterar o ator!");
            e.printStackTrace();
        }
    }

    private void excluirAtor() {
        System.out.println("\nExclusao de Ator");
        String nome;

        System.out.print("\nNome do Ator (vazio para cancelar): ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Ator ator = arqAtor.read(nome);

            if (ator != null) {
                System.out.println("Ator encontrado:");
                mostraAtor(ator);

                System.out.print("\nConfirma a exclusao do ator? (S/N) ");
                char resp = console.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {

                    // Confirmar se há associações com séries
                    int [] lista_id_series = arqAtor.getSeries(ator.getId());
                    if (lista_id_series.length > 0) {
                        System.out.println("Ator associado a série(s). Exclusao nao permitida.");
                    }
                    else {
                        boolean excluido = arqAtor.delete(nome);
                        if (excluido) {
                            System.out.println("Ator excluído com sucesso.");
                        } else {
                            System.out.println("Erro ao excluir o ator.");
                        }

                    }
                } else {
                    System.out.println("Exclusao cancelada.");
                }
            } else {
                System.out.println("Ator nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível excluir o ator!");
            e.printStackTrace();
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

    // Métodos relacionados a relação Ator-Série
    private void associarAtorASerie() {
        System.out.println("\nAssociar Ator a Série");
        String nome;

        // Obter nome do ator
        System.out.print("\nNome do Ator: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Ator ator = arqAtor.read(nome);
            if (ator != null) {
                mostraAtor(ator);

                // Obter nome da série
                System.out.print("\nNome da Série:");
                nome = console.nextLine();

                if (nome.isEmpty()) {
                    return;
                }

                
                Serie serie = arqSerie.read(nome);
                if (serie != null) {
                    mostraSerie(serie);

                    // Confirmar associação ator-série
                    System.out.print("\nAssociar Ator a Série? (S/N) ");
                    char resp = console.nextLine().charAt(0);

                    if (resp == 'S' || resp == 's') {
                        // Tentar inclusão da associação na árvore B+ Ator_Série
                        // (o próprio método chamará a associação na árvore B+ Série_Ator)
                        boolean associado = arqAtor.associar_serie(ator.getId(), serie.getId());
                        if (associado) {
                            System.out.println("Ator associado a série com sucesso.");
                        }
                        // Reportar falha da operação
                        else {
                            System.out.println("Erro ao associar ator e série.");
                        }
                    } else {
                        System.out.println("Associaçao cancelada.");
                    }


                } else {
                    System.out.println("Série nao encontrada.");
                }
                

            } else {
                System.out.println("Ator nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema.");
            e.printStackTrace();
        }
    }

    public void listarSeriesAssociadas() {
        System.out.println("\nListar Séries associadas a Ator");
        String nome;

        System.out.print("\nNome do Ator: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Ator ator = arqAtor.read(nome);
            if (ator != null) {
                int [] lista_id_series = arqAtor.getSeries(ator.getId());

                // Teste para ver lista de ids coletados
                for (int x = 0; x < lista_id_series.length; x++) {
                    System.out.println(lista_id_series[x]);
                }

                // Conferir se há associações para listar
                if (lista_id_series.length <= 0) {
                    System.out.println("Nenhuma série associada ao ator.");
                    return;
                } else {

                    Serie serie;
                    System.out.println("-------------");
                    for (int x = 0; x < lista_id_series.length; x++) {
                        serie = arqSerie.read(lista_id_series[x]);
                        if (serie != null) {
                            System.out.println(serie.getNome() + " - " + serie.getAno());
                        }
                    }
                    System.out.println("-------------");

                }

            } else {
                System.out.println("Ator nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível realizar a ação.");
            e.printStackTrace();
        }
    }

    public void removerSerieAssociada() {
        System.out.println("\nRemover Série associada a Ator");
        String nome;

        System.out.print("\nNome do Ator: ");
        nome = console.nextLine();

        if (nome.isEmpty()) {
            return;
        }

        try {
            Ator ator = arqAtor.read(nome);
            if (ator != null) {
                int [] lista_id_series = arqAtor.getSeries(ator.getId());

                // Conferir se há associações para remover
                if (lista_id_series.length == 0) {
                    System.out.println("Nenhuma série associada ao ator.");
                    return;
                } else {
                    Serie serie;
                    System.out.println("-------------");
                    for (int x = 0; x < lista_id_series.length; x++) {
                        serie = arqSerie.read(lista_id_series[x]);
                        if (serie != null) {
                            System.out.println(serie.getNome() + " - " + serie.getAno());
                        }
                    }
                    System.out.println("-------------");
    
                    // Obter nome da série
                    System.out.print("\nNome da Série a remover:");   
                    nome = console.nextLine();
    
                    if (nome.isEmpty()) {
                        return;
                    }
    
                    serie = arqSerie.read(nome);
                    boolean remocao = arqAtor.deletar_associao_serie(ator.getId(), serie.getId());
                    if (remocao) {
                        System.out.println("Associacao removida com sucesso.");
                    } else {
                        System.out.println("Erro ao remover associacao.");
                    }
                }

            } else {
                System.out.println("Ator nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possível realizar a ação.");
            e.printStackTrace();
        }
    }

}
