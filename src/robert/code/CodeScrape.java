package robert.code;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CodeScrape {

    public static String url;
    public static PrintWriter out;

    public static String StringError = "error";
    public static int counter;

    //Set the Date stuff
    static Date date = new Date();
    static LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    static int month = localDate.getMonthValue();
    static int year = localDate.getYear();
    static int day = localDate.getDayOfMonth();

    static Calendar calendar;


    //test date printout
    public static String testDate = month + "/" + day + "/" + year;

    //Magic html magic
    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public static void main(String[] args) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //1 day in the future
        cal.add(Calendar.DAY_OF_YEAR, 1); // <--
        Date testDatetemp = cal.getTime();
        String testDate2 = month + "/" + testDatetemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() + "/" + year;

        //2 days in the future
        cal.add(Calendar.DAY_OF_YEAR, 1); // <--
        Date testDatetemp2 = cal.getTime();
        String testDate3 = month + "/" + testDatetemp2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() + "/" + year;

        //3 days in the future
        cal.add(Calendar.DAY_OF_YEAR, 1); // <--
        Date testDatetemp3 = cal.getTime();
        String testDate4 = month + "/" + testDatetemp3.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() + "/" + year;
        //4 days in the future
        cal.add(Calendar.DAY_OF_YEAR, 1); // <--
        Date testDatetemp4 = cal.getTime();
        String testDate5 = month + "/" + testDatetemp4.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() + "/" + year;

        System.out.println(testDate);
        System.out.println(testDate2);
        System.out.println(testDate3);
        System.out.println(testDate4);
        System.out.println(testDate5);

        double minClassCode = 3000;
        double maxClassCode = 6500;
        double totalCodes = maxClassCode-minClassCode;
        double codesTested = 0;

        url = "http://anchor.marinette.k12.wi.us/rti/section_info.php?sectionID=";
        out = new PrintWriter("ClassCodes.txt");

        String appendText;

        double classCode = minClassCode;

        while (classCode < maxClassCode) {
            appendText = "";
            classCode++;
            counter = 0;
            codesTested ++;

            //Percentage output (lets ya know the code is running ;) )
            if(classCode % 50 == 0){
                System.out.println((codesTested/totalCodes)*100 + "%");
            }

            String html = getHTML(url + classCode);

            //checks if requested page is an error page
            if (!html.toLowerCase().contains(StringError.toLowerCase()) && ((html.contains(testDate)
                    || html.contains(testDate2)
                    || html.contains(testDate3)
                    || html.contains(testDate4)
                    || html.contains(testDate5)))) {

                while (counter < html.length() - 2) {
                    counter++;
                    String testChar = Character.toString(html.charAt(counter));
                    String testChar2;
                    testChar2 = Character.toString(html.charAt(counter + 1));

                    //parses for a prefix such as Mr or MS
                    if ((testChar.equals("M") || testChar.equals("m")) && (testChar2.equals("r") || testChar2.equals("R") || testChar2.equals("s") || testChar2.equals("S"))) {

                        Boolean runTheWhile = true;

                        //Appends characters to the end of a string until a "<" is detected. This gets a teachers name.
                        while (runTheWhile) {
                            testChar = Character.toString(html.charAt(counter));

                            if (!testChar.equals("<")) {
                                appendText = appendText + testChar;
                            } else {

                                //list the correct date for an entry
                                //TODO: get rid of all these damned if's
                                if (html.contains(testDate)) {
                                    saveText(classCode, appendText, testDate);
                                } else if (html.contains(testDate2)) {
                                    saveText(classCode, appendText, testDate2);
                                } else if (html.contains(testDate3)) {
                                    saveText(classCode, appendText, testDate3);
                                } else if (html.contains(testDate4)) {
                                    saveText(classCode, appendText, testDate4);
                                } else if (html.contains(testDate5)) {
                                    saveText(classCode, appendText, testDate4);
                                } else {
                                    System.out.println("Date missing from website");
                                }

                                counter = html.length() + 2;
                                runTheWhile = false;
                            }
                            counter++;
                        }

                    }
                }
            }
        }
        out.close();
    }

    //sign up for class based on teacher name
    //so i can auto signup for teachers
    public static void Signup(int classid) {
        //TODO: add webrequest to signup for class by id
        //
    }

    public static void saveText(double classCode, String appendText, String theDate) {
        out.println(classCode + " " + appendText + "  " + theDate);
    }

}
