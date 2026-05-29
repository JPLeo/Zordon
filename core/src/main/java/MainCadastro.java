import java.util.Scanner;
import java.util.ArrayList;

import Javazord.Zordon.Usuario;

public class MainCadastro
{

    public static void mostraMenu()
    {
        System.out.println("#==========PLATAFORMA DE JOGOS==========# ");
        System.out.println("1 - Cadastrar usuário");
        System.out.println("2 - Listar usuários");
        System.out.println("3 - Mostrar partida");
        System.out.println("4 - Mostrar pontuação");
        System.out.println("0 - SAIR");
        System.out.println("=========================================");
    }
    public static void main(String[] args)
    {
        Scanner teclado = new Scanner(System.in);
        ArrayList<Usuario>usuarios = new ArrayList<>();
        ArrayList<Partida>partidas = new ArrayList<>();
        int idIncremento = 1;

        String nome;
        int opcao;
        do
        {
            mostraMenu();
            opcao = teclado.nextInt();
            switch (opcao)
            {
                case 1:
                {
                    teclado.nextLine();

                    System.out.print("Nome de usuário: ");
                    nome = teclado.nextLine();

                    Usuario usuario = new Usuario(idIncremento, nome);
                    usuarios.add(usuario);
                    idIncremento++;

                    System.out.println("Usuário cadastrado com sucesso");
                    break;
                }

                case 2:
                {
                    if (usuarios.isEmpty())
                    {
                        System.out.println("Nenhum usuário cadastrado");
                    }
                    else
                    {
                        for (Usuario usuario : usuarios)
                        {
                            System.out.println("ID: " + usuario.getIdUsuario());
                            System.out.println("Nome: " + usuario.getNome());
                            System.out.println("Pontuação: " + usuario.getPontuacaoTotal());
                        }
                    }
                    break;
                }
                case 3:
                {
                    System.out.println("Integrar sistema partidas posteriormente");
                    break;
                }

                case 4:
                {
                    int IdDeBusca;
                    boolean usuarioEncontrado;

                    if(usuarios.isEmpty())
                    {
                        System.out.println("Nenhum usuário cadastrado");
                    }
                    else
                    {
                        usuarioEncontrado = false;
                        System.out.println("Digite o ID do usuário: ");
                        IdDeBusca = teclado.nextInt();

                        for (Usuario usuario : usuarios)
                        {
                            if (usuario.getIdUsuario() == IdDeBusca)
                            {
                                System.out.println("Nome: " + usuario.getNome());
                                System.out.println("Pontuação total: " + usuario.getPontuacaoTotal());
                                usuarioEncontrado = true;
                            }
                        }
                        if (!usuarioEncontrado)
                        {
                            System.out.println("Usuário não encontrado");
                        }
                    }
                    break;
                }

                case 0:
                {
                    break;
                }

                default:
                {
                    System.out.println("Opção inválida");
                }
                    break;

            }
        } while (opcao != 0);
        teclado.close();

    }

}
