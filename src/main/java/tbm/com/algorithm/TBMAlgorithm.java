package tbm.com.algorithm;

import java.util.Arrays;

//WE WSZYSTKIM ZASTOSOWAŁEM ,,NAWIGACJĘ" PIERWSZA WSPÓŁRZĘDNA DOSTAWCA, DRUGA WSPÓŁRZĘDNA ODBIORCA
public class TBMAlgorithm
{
    private static int[][] transportTable;      //tablica z wartościami przejazdów (,,pierwsza tablica")
    private static float[][] optimizationTable;   //tablica ze wskaźnikami optymalności (,,druga tablica")
    private static float[] alpha;                 //tablica z alfami
    private static float[] beta;                  //tablica z betami
    private static float[] tempAlpha;
    private static float[] tempBeta;


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
    public static int[][] compute(int[] provider, int[] recipient, float[][] profits)
    {
        //wykonanie początkowych działań, które robi się tylko raz (odpowiada to punktom 1,2,3 z planu)
        doStartingSetup(provider, recipient, profits);

        while(true)
        {
            alpha = new float[profits.length+1];
            beta = new float[profits[0].length+1];

            //nie jestem pewien czy te dwie pętle są konieczne, czy automatycznie
            // niezapełnione będą NaN, ale tak można mieć chyba pewność
            //(patrz funkcja findAlphaBeta)
            for(int i=0; i< alpha.length; i++)
            {
                alpha[i] = Float.NaN;
            }

            for(int i=0; i< beta.length; i++)
            {
                beta[i] = Float.NaN;
            }

            findAlphaAndBeta(alpha, beta, profits);

            optimizationTable = new float[provider.length+1][recipient.length+1];

            //po wywołaniu tej funkcji mamy tabelę zapełnioną zerami czyli "x" i wartościami
            ConstructOptTable(alpha,beta,profits);


            //tutaj należy sprawdzić czy wszystko jest niedodatnie w tabeli optimization
            if(isOptimal())
            {
                break;
            }

            applyCycle();
        }

        return transportTable;
    }

    private static void doStartingSetup(int[] provider, int[] recipient, float[][] profits)
    {
        //krok 1A

        //ustalenie rozmiarów tablic
        int currentStateProvider[] = new int[provider.length+1];
        int currentStateRecipient[] = new int[recipient.length+1];
        transportTable = new int[provider.length+1][recipient.length+1]; //tabela transportów
        boolean isFilled[][] = new boolean[provider.length+1][recipient.length+1];

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
//!!     //Arrays.fill(isFilled, false);

        //krok 1B

        //zmienne pomocnicze: maksymalna wartość i odpowiadające jej współrzędne w tablicy oraz
        //zmienna służąca sprawdzeniu czy max w danej iteracji był już przypisany
        float maxValue = 0.F;
        int maxValueCoordinates[] = new int[2];
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
            if(!isFilled[i][recipient.length])
            {
                transportTable[i][recipient.length] = currentStateProvider[i];
                currentStateRecipient[recipient.length] -= currentStateProvider[i];
                currentStateProvider[i] = 0;
                isFilled[i][recipient.length] = true;
            }
        }
    }

    private static void findAlphaAndBeta(float[] alpha, float[] beta, float[][] profits)
    {
        //zmienna pomocnicza do sprawdzania czy pole ma wartosc ktora mozna obliczyc alfę albo betę
        boolean isValue = true;


        tempAlpha = Arrays.copyOf(alpha, alpha.length);
        tempBeta = Arrays.copyOf(beta, beta.length);

        //sprawdzenie czy liczba jest "liczbą"
        for(int i=0; i< alpha.length; i++)
        {
            if(Float.isNaN(alpha[i]))
                isValue = false;
        }

        for(int i=0; i< beta.length; i++)
        {
            if(Float.isNaN(beta[i]))
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
            for(int j=0; j<beta.length; j++)
            {
                if(transportTable[i][j]>0)
                {
                    //jesli beta nie jest numerem, a alfa jest
                    if(Float.isNaN(beta[j]) && !Float.isNaN(alpha[i]))
                    {
                        if(j>=profits[0].length || i>=profits.length) beta[j] = 0.F - alpha[i];
                        else beta[j] = profits[i][j] - alpha[i];
                    }
                    //odwrotna sytuacja
                    else if(!Float.isNaN(beta[j]) && Float.isNaN(alpha[i]))
                    {
                        if(i>=profits.length || j>=profits[0].length) alpha[i] = 0.F - beta[j];
                        else alpha[i] = profits[i][j] - beta[j];
                    }
                }
            }
        }

        if(Arrays.equals(tempAlpha, alpha) && Arrays.equals(tempBeta, beta))
        {
            for(int i=0; i< alpha.length; i++)
            {
                if(Float.isNaN(alpha[i]))
                {
                    alpha[i] = 0;
                    break;
                }
            }
        }

        //samowywołanie się funkcji
        //findAlphaAndBeta(tempAlpha, tempBeta);
        findAlphaAndBeta(alpha, beta, profits);
    }

    private static void ConstructOptTable(float[] alpha, float[] beta, float[][] profits)
    {
        for(int i=0; i< transportTable.length; i++)
        {
            for(int j=0; j<transportTable[0].length; j++)
            {
                //jeśli w tabeli transportów jest wartość, ustawia zero czyli "x"
                //if(!Float.isNaN(transportTable[i][j]))
                if(transportTable[i][j]!=0)
                {
                    optimizationTable[i][j] = Float.NaN;
                }

                else
                {
                    optimizationTable[i][j] = transportTable[i][j] - alpha[i]- beta[j];
                }
            }
        }
    }

    private static boolean isOptimal()
    {
        //sprawdzenie czy wszystkie elementy tablicy są <= 0 [czy tablica jest optymalna]
        for(int i=0; i<optimizationTable.length; i++)
            for(int j=0; j<optimizationTable[0].length; j++)
                if(optimizationTable[i][j] > 0) return false;
        return true;
    }

    private static void applyCycle()
    {
        float maxVal = 0.F;
        int optimalPathCoordinates[][] = new int[4][2];

        //znajdź największy element i jego współrzędne
        for (int i = 0; i < optimizationTable.length; i++)
        {
            for(int j = 0; j< optimizationTable[0].length; j++)
            {
                if(!Float.isNaN(optimizationTable[i][j]) && optimizationTable[i][j] > maxVal)
                {
                    maxVal = optimizationTable[i][j];
                    optimalPathCoordinates[0][0] = i;
                    optimalPathCoordinates[0][1] = j;
                }
            }
        }

        //znajdowanie cyklu optymalizacyjnego w tabeli
        //zmienna pomocnicza
        boolean wasPathAssigned = false;
        //ustalenie współrzędnych, które zawsze muszą być takie same
        optimalPathCoordinates[1][0] = optimalPathCoordinates[0][0];
        optimalPathCoordinates[3][1] = optimalPathCoordinates[0][1];
        //iteracja po ,,pierwszym wymiarze"
        for(int i=0; i<optimizationTable[0].length; i++)
        {
            if(i!=optimalPathCoordinates[0][1])
            {
                //znaleźliśmy x w tej linii
                if(Float.isNaN(optimizationTable[optimalPathCoordinates[0][0]][i]))
                {
                    //iteracja po ,,drugim wymiarze"
                    for(int j=0; j<optimizationTable.length; j++)
                    {
                        if(j!=optimalPathCoordinates[0][0])
                        {
                            //znaleźliśmy x w tej linii ; jeśli znaleźliśmy 2 x do cyklu, to trzeci musi mieć
                            // już ustalone miejsce (jeśli go tam nie ma, to nie ma tam też cyklu)
                            if(Float.isNaN(optimizationTable[j][i]) &&
                                    Float.isNaN(optimizationTable[j][optimalPathCoordinates[0][1]]))
                            {
                                //ustalenie współrzędnych
                                optimalPathCoordinates[1][1] = i;
                                optimalPathCoordinates[2][0] = j;
                                optimalPathCoordinates[2][1] = i;
                                optimalPathCoordinates[3][0] = j;
                                //przerwanie obu pętli
                                wasPathAssigned = true;
                                break;
                            }
                        }
                    }
                }
            }
            if(wasPathAssigned) break;
        }

        //zastosowanie cyklu
        //znajdź min w minusach cyklu
        int changeValue = Math.min(transportTable[optimalPathCoordinates[1][0]][optimalPathCoordinates[1][1]]
                , transportTable[optimalPathCoordinates[3][0]][optimalPathCoordinates[3][1]]);
        //odejmij od minusów i dodaj do plusów
        transportTable[optimalPathCoordinates[0][0]][optimalPathCoordinates[0][1]] += changeValue;
        transportTable[optimalPathCoordinates[1][0]][optimalPathCoordinates[1][1]] -= changeValue;
        transportTable[optimalPathCoordinates[2][0]][optimalPathCoordinates[2][1]] += changeValue;
        transportTable[optimalPathCoordinates[3][0]][optimalPathCoordinates[3][1]] -= changeValue;
    }
}