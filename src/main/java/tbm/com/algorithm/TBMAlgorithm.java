package tbm.com.algorithm;

import java.util.ArrayList;
import java.util.List;

//WE WSZYSTKIM ZASTOSOWAŁEM ,,NAWIGACJĘ" PIERWSZA WSPÓŁRZĘDNA DOSTAWCA, DRUGA WSPÓŁRZĘDNA ODBIORCA
public class TBMAlgorithm
{
    private static int[][] transportTable;      //tablica z wartościami przejazdów (,,pierwsza tablica")
    private static int[][] optimizationTable;   //tablica ze wskaźnikami optymalności (,,druga tablica")
    private static int[] alpha;                 //tablica z alfami
    private static int[] beta;                  //tablica z betami

    //pomocnicza metoda sprawdzająca czy tablica transportowa została już wypełniona [NIE SPRAWDZA TRAS FIKCYJNYCH]
    private static boolean isTableFilled(boolean[][] t)
    {
        for(int i=0; i<t.length-1; i++)
        {
            for(int j=0; j<t[0].length-1; j++)
            {
                if(t[i][j]==false) return false;
            }
        }
        return true;
    }

    //profits powinno być chyba zależne od długości providera i recipients
    public static void compute(int[] provider, int[] recipient, float[][] profits)
    {
        //wykonanie początkowych działań, które robi się tylko raz (odpowiada to punktom 1,2,3 z planu)
        doStartingSetup(recipient, provider, profits);

        while(true)
        {
            //computeAlphasAndBetas();


            int[] alpha = new int[profits.length];
            int[] beta = new int[profits[0].length];

            //nie jestem pewien czy te dwie pętle są konieczne, czy automatycznie
            // niezapełnione będą NaN, ale tak można mieć chyba pewność
            //(patrz funkcja findAlphaBeta)
            for(int i=0; i< alpha.length; i++)
            {
                alpha[i] = Number.NaN;
            }

            for(int i=0; i< beta.length; i++)
            {
                beta[i] = Number.NaN;
            }

            findAlphaAndBeta(alpha, beta, profits);

            //constructOptimizationTable();

            int optimizationTable = new int[recipient.length][provider.length];

            //po wywołaniu tej funkcji mamy tabelę zapełnioną zerami czyli "x" i wartościami
            ConstructOptTable(transportTable,alpha,beta,profits, optimizationTable);


            //if(haveWeReachedOptimum()) break;

            //tutaj należy sprawdzić czy wszystko jest niedodatnie w tabeli optimization
            //isOptimal(optimizationTable);

            //applyCycle();

            //petla powtorzenia zmiany tabeli
        }
    }

    private static void doStartingSetup(int[] provider, int[] recipient, float[][] profits)
    {
        //krok 1A

        //ustalenie rozmiarów tablic
        int currentStateProvider[provider.length+1];
        int currentStateRecipient[recipient.length+1];
        transportTable = new int[provider.length+1][recipient.length+1]; //tabela transportów
        boolean isFilled[provider.length+1][recipient.length+1];

        //obliczenie podaży fikcyjnego dostawcy
        int imaginaryProvider = 0;
        for(int i=0; i<recipient.length; i++)
            imaginaryProvider += recipient[i];
        //obliczenie popytu fikcyjnego odbiorcy
        int imaginaryRecipient = 0;
        for(int i=0; i<provider.length; i++)
            imaginaryRecipient += provider[i];

        //uzupełnienie tablic
        System.arraycopy(provider, 0, currentStateProvider, 0, provider.length);
        currentStateProvider[provider.length] = imaginaryProvider;
        System.arraycopy(recipient, 0, currentStateRecipient, 0, recipient.length);
        currentStateRecipient[recipient.length] = imaginaryRecipient;
        Arrays.fill(isFilled, false);

        //krok 1B

        //zmienne pomocnicze: maksymalna wartość i odpowiadające jej współrzędne w tablicy oraz
        //zmienna służąca sprawdzeniu czy max w danej iteracji był już przypisany
        float maxValue = 0.F;
        int maxValueCoordinates[2];
        boolean wasMinValueAssigned;

        //TU PRZY SPRAWDZANIU W DŁUGOŚCIACH MOŻNA DAĆ -1 I WTEDY NIE BĘDZIE SPRAWDZAŁ FIKCYJNYCH
        while(!isTableFilled(isFilled))
        {
            //znajdź maksymalny zysk dla komórki nie mającej wypełnienia
            wasMinValueAssigned = false;
            for(int i=0; i<profits.length; i++)
            {
                for(int j=0; j<profits[0].length; j++)
                {
                    //sprawdzenie czy komórka ma wartość lub została wykreślona
                    if(!isFilled[i][j])
                    {
                        //przypadek dla pierwszego przypisania
                        if(!wasMinValueAssigned)
                        {
                            maxValue = profits[i][j];
                            maxValueCoordinates[0] = i;
                            maxValueCoordinates[1] = j;
                            wasMinValueAssigned = true;
                        }
                        //przypadek dla znalezienia nowego maxa
                        else if(profits[i][j] > maxValue)
                        {
                            maxValue = profits[i][j];
                            maxValueCoordinates[0] = i;
                            maxValueCoordinates[1] = j;
                        }
                    }
                }
            }

            //krok 1C

            //dodaj ile się da i odejmij od aktualnego stanu, a później wykreśl wiersz lub kolumnę
            if(currentStateProvider[maxValueCoordinates[0]] >= currentStateRecipient[maxValueCoordinates[1]])
            {
                //uzupełnienie odpowiedniego pola tablicy transportowej
                transportTable[maxValueCoordinates[0]][maxValueCoordinates[1]] =
                        currentStateRecipient[maxValueCoordinates[1]];
                //zaktualizowanie aktualnego stanu dostawców i odbiorców
                currentStateProvider[maxValueCoordinates[0]] -= currentStateRecipient[maxValueCoordinates[1]];
                currentStateRecipient[maxValueCoordinates[1]] = 0;
                //wykreślenie kolumny
                for(int i=0; i<isFilled.length; i++)
                    isFilled[i][maxValueCoordinates[1]] = true;
            }
            else
            {
                //uzupełnienie odpowiedniego pola tablicy transportowej
                transportTable[maxValueCoordinates[0]][maxValueCoordinates[1]] =
                        currentStateProvider[maxValueCoordinates[0]];
                //zaktualizowanie aktualnego stanu dostawców i odbiorców
                currentStateRecipient[maxValueCoordinates[1]] -= currentStateProvider[maxValueCoordinates[0]];
                currentStateProvider[maxValueCoordinates[0]] = 0;
                //wykreślenie wiersza
                for(int i=0; i<isFilled[0].length; i++) isFilled[maxValueCoordinates[0]][i] = true;
            }
        }

        //krok 1D

        //uzupełnienie wiersza fikcyjnego dostawcy (nie uzupełniamy ostatniego pola, tylko dopiero przy fikcyjnym odbiorcy)
        for(int i=0; i<transportTable[0].length-1; i++)
        {
            if(!isFilled[provider.length][i])
            {
                transportTable[provider.length][i] = currentStateRecipient[i];
                currentStateProvider[provider.length] -= currentStateRecipient[i];
                currentStateRecipient[i] = 0;
                isFilled[provider.length][i] = true;
            }
        }
        //uzupełnienie kolumny fikcyjnego odbiorcy
        for(int i=0; i<transportTable.length; i++)
        {
            if(!isFilled[i][recipent.length])
            {
                transportTable[i][recipent.length] = currentStateProvider[i];
                currentStateRecipient[recipent.length] -= currentStateProvider[i];
                currentStateProvider[i] = 0;
                isFilled[i][recipent.length] = true;
            }
        }
    }

    private static void findAlphaAndBeta(int[] alpha, int[] beta, int [][] profits)
    {
        //zmienna pomocnicza do sprawdzania czy pole ma wartosc ktora mozna obliczyc alfę albo betę
        public static boolean isValue = true;


        public static int[] tempAlpha = Arrays.copyOf(alpha, alpha.length);
        public static int[] tempBeta = Arrays.copyOf(beta, beta.length);

        //sprawdzenie czy liczba jest "liczbą"
        for(int i=0; i< alpha.length; i++)
        {
        if(Number.isNaN(alpha[i]))
            isValue = false;
        }

        for(int i=0; i< beta.length; i++)
        {
            if(Number.isNaN(beta[i]))
                isValue = false;
        }

        //jesli nie ma wartosci to funkcja sie zamyka
        if(isValue)
        {
            return;
        }

        //obliczenie wartosci
        for(int i=0; i< alpha.length; i++)
        {
            for(j=0; j<beta.length; j++)
            {
                if(profits[i][j]>0)
                {
                    //jesli beta nie jest numerem, a alfa jest
                    if(Number.isNaN(beta[j]) && !Number.isNaN(alpha[i]))
                    {
                        beta[j] = profits[i][j] - alpha[i];
                    }
                    //odwrotna sytuacja
                    else if(!Number.isNaN(beta[j]) && Number.isNaN(alpha[i]))
                    {
                        alpha[i] = profits[i][j] - beta[j];
                    }
                }
            }
        }

        if(Arrays.equals(tempAlpha, alpha) && Arrays.equals(tempBeta, beta))
        {
            for(int i=0; i< alpha.length; i++)
            {
                if(Number.isNaN(alpha[i]))
                {
                    alpha[i] = 0;
                    break;
                }
            }
        }

    //samowywołanie się funkcji
    findAlphaAndBeta(tempAlpha, tempBeta, profits);
    }

    private static void ConstructOptTable(int[][] transportTable, int[] alpha, int[] beta, int[][] profits, int[][] optimizationTable)
    {
        for(int i=0; i< transportTable.length; i++)
        {
            for(int j=0; j<transportTable[0].length; j++)
            {
                //jeśli w tabeli transportów jest wartość, ustawia zero czyli "x"
                if(!Number.isNaN(transportTable[i][i]))
                {
                    optimizationTable[i][j] = 0;
                }

                else
                {
                    optimizationTable[i][j] = profits[i][j] - alpha[i]- beta[j];
                }
            }
        }
    }
}

private static void isOptimal(int[][] optimizationTable)
{
    for (int i = 0; i < optimizationTable.length; i++)
    {
        for(int j = 0; j< optimizationTable[0].length; j++)
        {
            if(optimizationTable[i][j] >0)
            {
                //do nothing for now
            }
        }
    }

    break;
}

}