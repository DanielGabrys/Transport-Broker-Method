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

    public static void compute(int[] provider, int[] recipient, float[][] profits)
    {
        //wykonanie początkowych działań, które robi się tylko raz (odpowiada to punktom 1,2,3 z planu)
        doStartingSetup(recipient, provider, profits);
        while(true)
        {
            //computeAlphasAndBetas();
            //constructOptimizationTable();
            //if(haveWeReachedOptimum()) break;
            //applyCycle();
        }
    }

    private static void doStartingSetup(int[] provider, int[] recipient, float[][] profits)
    {
        //ustalenie rozmiarów tablic
        int currentStateProvider[provider.length+1];
        int currentStateRecipient[recipient.length+1];
        transportTable = new int[provider.length+1][recipient.length+1];
        boolean isFilled[provider.length+1][recipient.length+1];
        //obliczenie podaży fikcyjnego dostawcy
        int imaginaryProvider = 0;
        for(int i=0; i<recipient.length; i++) imaginaryProvider += recipient[i];
        //obliczenie popytu fikcyjnego odbiorcy
        int imaginaryRecipient = 0;
        for(int i=0; i<provider.length; i++) imaginaryRecipient += provider[i];
        //uzupełnienie tablic
        System.arraycopy(provider, 0, currentStateProvider, 0, provider.length);
        currentStateProvider[provider.length] = imaginaryProvider;
        System.arraycopy(recipient, 0, currentStateRecipient, 0, recipient.length);
        currentStateRecipient[recipient.length] = imaginaryRecipient;
        Arrays.fill(isFilled, false);

        //zmienne pomocnicze: maksymalna wartość i odpowiadające jej współrzędne w tablicy oraz
        //zmienna służąca sprawdzeniu czy max w danej iteracji był już przypisany
        float maxValue = 0.F;
        int maxValueCoordinates[2];
        boolean wasMaxValueAssigned;
        //TU PRZY SPRAWDZANIU W DŁUGOŚCIACH MOŻNA DAĆ -1 I WTEDY NIE BĘDZIE SPRAWDZAŁ FIKCYJNYCH
        while(!isTableFilled(isFilled))
        {
            //znajdź maksymalny zysk dla komórki nie mającej wypełnienia
            wasMaxValueAssigned = false;
            for(int i=0; i<profits.length; i++)
            {
                for(int j=0; j<profits[0].length; j++)
                {
                    //sprawdzenie czy komórka ma wartość lub została wykreślona
                    if(!isFilled[i][j])
                    {
                        //przypadek dla pierwszego przypisania
                        if(!wasMaxValueAssigned)
                        {
                            maxValue = profits[i][j];
                            maxValueCoordinates[0] = i;
                            maxValueCoordinates[1] = j;
                            wasMaxValueAssigned = true;
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
                for(int i=0; i<isFilled.length; i++) isFilled[i][maxValueCoordinates[1]] = true;
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

}