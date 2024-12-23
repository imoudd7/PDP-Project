/**
 *
 * Mahmoud Aldaher
 * mahmoud.aldaher@ogr.sakarya.edu.tr
 * 05/04/2024 - 07/04/2024
 * 2-C Grubu - Huseyin Hoca
 */



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






public class EveryThing
{

		public static void main(String[] args) throws IOException, InterruptedException 
		{
			System.setProperty("console.encoding", "UTF-8");
			BufferedReader Oku = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("GitHub depo URL girin Lutfen:   ");
			String URL = Oku.readLine().trim();
			String Direct = Clone(URL);
			
			
			
			if (Direct != null)
			{
				SiniflarAnaliz(Direct);
				EskiDepoFolderSil(Direct);
            } 
			else
			{
            System.out.println("Depo klonlamaadi :( ");
			}
		}
    
    
    
    

    private static String Clone(String URL) throws IOException, InterruptedException
    {
		BufferedReader Oku = new BufferedReader(new InputStreamReader(System.in));
        String Direct;
        System.out.println("\nLutfen klonlamak istediğiniz depoyu indirmek istediğiniz yolu (path) ornek :(C:/Users/Mahmoud/Desktop/Clone) girin :");
		Direct = Oku.readLine().trim();
        
        ProcessBuilder builder = new ProcessBuilder("git", "clone", URL, Direct);
        Process process = builder.start();
        int Cikis = process.waitFor();

        
        if (Cikis == 0) 
        {
            System.out.println("\n\n\n\n");
            System.out.println("|------------------------------|");
            System.out.println("|      Depo klonlandi :)       |");
            System.out.println("|------------------------------|");
            return Direct;
        } 
        else 
        {
            System.out.println("Depo klonlamadi :(");
            BufferedReader OKU = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String Satir;
            while ((Satir = OKU.readLine()) != null) 
            {
                System.out.println(Satir);
            }
            return null;
        }
    }
    
    
    
    
    
    

    private static void SiniflarAnaliz(String Dizi) 
    {
        File folder = new File(Dizi);
        System.out.println("\n\n\n\n-----------------------------------------");
        DiziAnaliz(folder);
    }
    
    
    
    

    private static void DiziAnaliz(File dizin)
    {
        File[] dosyalar = dizin.listFiles();
        if (dosyalar != null)
        {
            for (File dosya : dosyalar) 
            {
                if (dosya.isDirectory()) 
                {
                    DiziAnaliz(dosya);
                } 
                else if (dosya.getName().endsWith(".java") && JavaClassSadece(dosya)) 
                {
                    EveryClassAnaliz(dosya);
                }
            }
        }
    }

    
    
    
    
    
    
    private static boolean JavaClassSadece(File dosya)
    {
        List<String> Satirlar = null;
        try 
        {
            Satirlar = Files.readAllLines(dosya.toPath());
            for (String Satir : Satirlar) {
                if (Satir.matches(".*\\bclass\\b.*"))
                {
                    return true;
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    
    
    
    
    
    private static void EveryClassAnaliz(File dosya) 
    {
        List<String> Satirlar = null;
        
        
        try 
        {
            Satirlar = Files.readAllLines(dosya.toPath());

            
            
            short Java_Doc_Satir_Sayisi = JavadocSayac(Satirlar);
            int Diger_Yorum_Satir_Sayisi = digerYorumSatirlariniSayac(Satirlar);
            short  Kod_Satir_Sayis = kodSatirlarSayac(Satirlar);
            int loc = Satirlar.size();
            short Fonk_Sayisi = FonkSayac(Satirlar);
            double yorumSapmaYuzdesi = yorumSapmaYuzdesiniHesapla(Java_Doc_Satir_Sayisi, Diger_Yorum_Satir_Sayisi,Fonk_Sayisi, Kod_Satir_Sayis);
            String sinif_Adi = dosya.getName().substring(0, dosya.getName().lastIndexOf('.'));
            
            
            Print(sinif_Adi,Java_Doc_Satir_Sayisi,Diger_Yorum_Satir_Sayisi,Kod_Satir_Sayis,loc,Fonk_Sayisi,yorumSapmaYuzdesi);
        
        }
        
        
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    	
    private static void Print(String SA, short JDSS, int DYSS, short KSS, int LOC, short FS, double YSY)
    {
        System.out.println("Sinif: " + SA + ".java");
        System.out.println("Javadoc Satir Sayisi: " + JDSS);
        System.out.println("Yorum Satir Sayisi: " + DYSS);
        System.out.println("Kod Satir Sayisi: " + KSS);
        System.out.println("LOC: " + LOC);
        System.out.println("Fonksiyon Sayisi: " + FS);
        System.out.println("Yorum Sapma Yüzdesi: %" + YSY);
        System.out.println("-----------------------------------------");
    }
    
    
    
    
    
    private static short JavadocSayac(List<String> Satirlar)
    {
        short Cntr = 0;
        boolean Baslangic = false;

        
        for (String Satir : Satirlar)
        {
            Satir = Satir.trim();
            if (Satir.startsWith("/**")) 
            {
                if (!Baslangic) 
                {
                    Baslangic = true;
                    continue;
                }
            }
            
            if (Baslangic)
            {
                Cntr++;
                
                if (Satir.contains("*/")) 
                {
                    Cntr--;
                    Baslangic = false;
                }
            }
        }
        return Cntr;
    }
    
    
    
    
    
    
    
    
    

    private static int TekLineComment(List<String> Satirlar)
    {
        int Cntr = 0;
        
        
        for (String Satir : Satirlar) 
        {
            Satir = Satir.trim();

            
            if (Satir.contains("//")) 
            {
                Cntr++;
            }
        }
        return Cntr;
    }
    
    
    
    
    
    

    private static int digerYorumSatirlariniSayac(List<String> Satirlar) 
    {
        int Cntr = 0;
        boolean MultiLine = false;

        for (String Satir : Satirlar)
        {
            Satir = Satir.trim();

            
            
            if (Satir.startsWith("/*") && !Satir.startsWith("/**")) 
            {
                MultiLine = true;
                if (Satir.endsWith("*/")) 
                {
                    Cntr++;
                    MultiLine = false;
                }
                continue;
            }

            
            if (MultiLine) {
                if (Satir.endsWith("*/"))
                {
                    Cntr++;
                    MultiLine = false;
                }
                continue;
            }

            
            
            if (Satir.contains("//")) 
            {
                Cntr++;
            }
        }

        return Cntr;
    }
    
    
    
    

    private static short kodSatirlarSayac(List<String> Satirlar)
    {
        short Cntr = 0;
        
        
        for (String Satir : Satirlar) 
        {
            Satir = Satir.trim();
            
            if (!Satir.isEmpty() && !Satir.startsWith("//") && !Satir.startsWith("/*") && !Satir.endsWith("*/") && !Satir.startsWith("*")) 
            {
                Cntr++;
            }
        }
        
        
        return Cntr;
    }
    
    
    
    
    
    

    private static double yorumSapmaYuzdesiniHesapla(int Java_Doc_Satir_Sayisi, int Diger_Yorum_Satir_Sayisi, int fonkSayisi,int Kod_Satir_Sayis) 
    {
        double YG = ((Java_Doc_Satir_Sayisi + Diger_Yorum_Satir_Sayisi) * 0.8) / fonkSayisi;
        double YH = (Kod_Satir_Sayis / (double) fonkSayisi) * 0.3;
        return ((100 * YG) / YH) - 100;
    }
    
  
    

    private static short FonkSayac(List<String> Satirlar)
    {
        short Cntr = 0;
        Pattern desen = Pattern.compile(".*\\b\\w+\\s*\\(.*\\)\\s*\\{?\\s*");
        
        
        for (String Satir : Satirlar) 
        {
            if (desen.matcher(Satir).matches()) 
            {
                Cntr++;
            }
        }
        
        
        return Cntr;
    }
    
    
    
    
    

    private static void EskiDepoFolderSil(String Dizi) 
    {
        File X = new File(Dizi);
        if (X.exists()) 
        {
            Sil(X);
        }
    }
    
    
    
    

    private static void Sil(File dosya)
    {
        if (dosya.isDirectory()) 
        {
            File[] dosyalar = dosya.listFiles();
            if (dosyalar != null) 
            {
                for (File f : dosyalar) 
                {
                    Sil(f);
                }
            }
        }
        dosya.delete();
    }
}