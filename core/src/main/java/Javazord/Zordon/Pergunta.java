package Javazord.Zordon;

public class Pergunta
{
    private String alternativaA,alternativaB,alternativaC,alternativaD,enunciado;
    private char alternativaCerta;

    public Pergunta (String enunciado,String alternativaA,String alternativaB,String alternativaC,
    String alternativaD,char alternativaCerta)
    {
        this.enunciado = enunciado;
        this.alternativaA = alternativaA;
        this.alternativaB = alternativaB;
        this.alternativaC = alternativaC;
        this.alternativaD = alternativaD;
        this.alternativaCerta = alternativaCerta;
    }
    
    public void mostraPergunta()
    {
        System.out.println(enunciado);
        System.out.println("A) " + alternativaA);
        System.out.println("B) " + alternativaB);
        System.out.println("C) " + alternativaC);
        System.out.println("D) " + alternativaD);
    }
    public boolean conferePergunta (char escolhaUsuario)
    {
        if (escolhaUsuario == alternativaCerta)
        {
            return true;  
        }
        else
        {
            return false;
        }
    }

    public String getEnunciado()
    {
        return enunciado;
    }
    
    public String getAlternativaA()
    {
        return alternativaA;
    }
    
    public String getAlternativaB()
    {
        return alternativaB;
    }
    
    public String getAlternativaC()
    {
        return alternativaC;
    }
    
    public String getAlternativaD()
    {
        return alternativaD;
    }

    public char getAlternativaCerta()
    {
        return alternativaCerta;
    }
}
