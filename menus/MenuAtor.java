package menus;

import arquivos.ArquivoAtor;
import entidades.Ator;

import java.util.Scanner;

public class MenuAtor {
    
    private ArquivoAtor arqAtor;
    private static Scanner console = new Scanner(System.in);

    public MenuAtor() throws Exception {
        arqAtor = new ArquivoAtor();
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Atores");
            System.out.println("\n1) Incluir");
            System.out.println("2) Buscar");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("0) Voltar");

            System.out.print("\nOpção: ");
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
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private void incluirAtor() {
        System.out.println("\nInclusão de Ator");
        String nome;

        do {
            System.out.print("\nNome (vazio para cancelar): ");
            nome = console.nextLine();
            if (nome.isEmpty()) {
                return;
            }
        } while (nome.length() == 0);

        System.out.print("\nConfirma a inclusão do Ator? (S/N) ");
        char resp = console.nextLine().charAt(0);

        if (resp == 'S' || resp == 's') {
            try {
                Ator novoAtor = new Ator(nome);
                arqAtor.create(novoAtor);
                System.out.println("Ator incluído com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o ator!");
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
                System.out.println("Ator não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o ator!");
            e.printStackTrace();
        }
    }

    private void alterarAtor() {
        System.out.println("\nAlteração de Ator");
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
                    System.out.println("Alteração cancelada.");
                }
            } else {
                System.out.println("Ator não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o ator!");
            e.printStackTrace();
        }
    }

    private void excluirAtor() {
        System.out.println("\nExclusão de Ator");
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

                System.out.print("\nConfirma a exclusão do ator? (S/N) ");
                char resp = console.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {
                    boolean excluido = arqAtor.delete(nome);
                    if (excluido) {
                        System.out.println("Ator excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o ator.");
                    }
                } else {
                    System.out.println("Exclusão cancelada.");
                }
            } else {
                System.out.println("Ator não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o ator!");
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
}
