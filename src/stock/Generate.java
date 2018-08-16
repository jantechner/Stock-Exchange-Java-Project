package stock;

import java.io.Serializable;
import java.util.*;

public class Generate implements Serializable {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    private static String[] Country = {"", "Albania", "Algeria", "Andorra", "Angola", "Antigua & Deps",
            "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
            "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia Herzegovina",
            "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina", "Burundi", "Cambodia", "Cameroon", "Canada",
            "Cape Verde", "Central African Rep", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo",
            "Congo {Democratic Rep}", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark",
            "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt",
            "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia",
            "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",
            "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland {Republic}",
            "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
            "Korea North", "Korea South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
            "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi",
            "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico",
            "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar, {Burma}",
            "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway",
            "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland",
            "Portugal", "Qatar", "Romania", "Russian Federation", "Rwanda", "St Kitts & Nevis", "St Lucia",
            "Saint Vincent & the Grenadines", "Samoa", "San Marino", "Sao Tome & Principe", "Saudi Arabia",
            "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland",
            "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga",
            "Trinidad & Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine",
            "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu",
            "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"};

    private static String[] Currencies = {"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM",
            "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BOV", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD",
            "CAD", "CDF", "CHE", "CHF", "CHW", "CLF", "CLP", "CNY", "COP", "COU", "CRC", "CUC", "CUP", "CVE", "CZK",
            "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD",
            "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK", "JMD",
            "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD",
            "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MXV",
            "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN",
            "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS",
            "SRD", "SSP", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS",
            "UAH", "UGX", "USD", "USN", "UYI", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF",
            "XPF", "XSU", "XUA", "YER", "ZAR", "ZMW", "ZWL"};

    private static String[] Commodities = {"Corn", "Corn", "Oats", "Rough Rice", "Soybeans", "Rapeseed", "Soybean Meal", "Soybean Oil", "Wheat", "Wheat", "Milk", "Cocoa", "Coffee", "Cotton No.2", "Sugar No.11", "Sugar No.14", "Copper", "Lead", "Zinc", "Tin", "Aluminium", "Aluminium", "Nickel", "Cobalt", "Molybdenum", "Recycled steel", "Gold", "Platinum", "Palladium", "Silver", "PalmOil", "Rubber", "Wool", "Amber", "Lean Hogs", "Live Cattle", "Feeder Cattle", "WTI Crude Oil", "Brent Crude", "Ethanol", "Natural gas", "Heating Oil", "Propane",  };
    private static String[] Units = {"ounce", "kg", "tonne", "lb"};


    private static String[] Cities = {"Hongkong", "Beijin", "Warsaw", "Poznan", "Berlin", "Chicago", "New York", "Istanbul", "Moscow", "London", "Saint", "Berlin", "Madrid", "Kiev", "Rome", "Paris", "Minsk", "Bucharest", "Vienna", "Budapest", "Hamburg", "Barcelona", "Munich", "Kharkiv", "Milan", "Prague", "Sofia", "Nizhny", "Kazan", "Brussels", "Samara", "Belgrade", "Birmingham", "Ufa", "Rostov-on-Don", "Tbilisi", "Cologne", "Perm", "Voronezh", "Volgograd", "Odessa", "Dnipro", "Naples"};

    private static String[] FirstNames = {"Dorian", "Glynda", "Garnett", "Reginald", "Valentine", "Tia", "Alec", "Annie", "Kami", "Minna", "Guy", "Shirley", "Jerica", "Rosie", "Treva", "Belle", "Elke", "Mona", "Aracelis", "Tran", "Malisa", "Alysha", "Brunilda", "Shira", "Lang", "Allyson", "Danyelle", "Bethanie", "Letha", "Tien", "Ashanti", "Vernita", "Mina", "Lavinia", "Franchesca", "Nelson", "Leah", "Eusebio", "Deane", "Dawne", "Geri", "Kylee", "Dorethea", "Charissa", "Micheline", "Rolf", "Christene", "Sharen", "Barbar", "Kristine", "Melinda", "Lisha", "Marcellus", "Jacinto", "Edelmira", "Ashton", "Wes", "Leota", "Krystle", "Melony", "Fairy", "Nida", "Roslyn", "Gayla", "Minda", "Oretha", "Fern", "Riva", "Shawnna", "Amee", "Faviola", "Lucio", "Neida", "Theodora", "Arline", "Bruna", "Rosetta", "Kina", "Waldo", "Desmond", "Sonya", "Shanna", "Alene", "Tracee", "Fatima", "Nobuko", "Tula", "Rafaela", "Rasheeda", "Donald", "Pearle", "Cathleen", "Sherell", "Zoraida", "Joelle", "Herlinda", "Filiberto", "Shalon", "Jacqui", "Siobhan"};
    private static String[] SecondNames = {"Fall", "Lazarus", "Fredericksen", "Heatwole", "Olivera", "Steen", "Nakano", "Montanez", "Chichester", "Catledge", "Deleon", "Felder", "Sonnenberg", "Callier", "Faddis", "Madonna", "Drewry", "Gilbert", "Warnick", "Lathan", "Lufkin", "Taing", "Doria", "Grogan", "Basye", "Whiten", "Launer", "Leandro", "Bouldin", "Giunta", "Waldon", "Depuy", "Ulloa", "Tabarez", "Altman", "Caffrey", "Doherty", "Dress", "Albrecht", "Mcgown", "Stoker", "Gorden", "Hodgkins", "Schmidt", "Mendiola", "Bertin", "Mleczko", "Grow", "Schaub", "Alton", "Kadel", "Shehan", "Herriman", "Kimmer", "Ansell", "Swopes", "Fazenbaker", "Vivier", "Heffington", "Plaster", "Schiavo", "Haller", "Powers", "Dudley", "Jacobsen", "Ruhland", "Bombardier", "Sarratt", "Campione", "Gahagan", "Nathan", "Tyson", "Pannell", "Harbuck", "Crowden", "Koonce", "Line", "Roza", "Boese", "Spight", "Milbrandt", "Remley", "Houk", "Boan", "Korb", "Sollars", "Schindler", "Aron", "Chenier", "Feaster", "Levitan", "Mercuri", "Hollenbach", "Muff", "Delamater", "Oleary", "Kluck", "Mcgreevy", "Rainey", "Paulding"};
    private static String[] CompanyNames = {"3i", "Admiral Group", "Anglo American plc", "Antofagasta", "Ashtead Group", "Associated British Foods", "AstraZeneca", "Aviva", "BAE Systems", "Barclays", "Barratt Developments", "Berkeley Group Holdings", "BHP", "BP", "British American Tobacco", "British Land", "BT Group", "Bunzl", "Burberry", "Carnival Corporation & plc", "Centrica", "Coca", "Compass Group", "CRH plc", "Croda International", "DCC plc", "Diageo", "Direct Line Group", "easyJet", "Experian", "Ferguson plc", "Fresnillo plc", "G4S", "GKN", "GlaxoSmithKline", "Glencore", "Halma", "Hammerson", "Hargreaves Lansdown", "HSBC", "Imperial Brands", "Informa", "InterContinental Hotels Group", "International Airlines Group", "Intertek", "ITV plc", "Johnson Matthey", "Just Eat", "Kingfisher plc", "Land Securities", "Legal & General", "Lloyds Banking Group", "London Stock Exchange Group", "Marks & Spencer", "Mediclinic International", "Micro Focus", "Mondi", "Morrisons", "National Grid plc", "Next plc", "NMC Health", "Old Mutual", "Paddy Power Betfair", "Pearson PLC", "Persimmon plc", "Prudential plc", "Randgold Resources", "Reckitt Benckiser", "RELX Group", "Rentokil Initial", "Rio Tinto Group", "Rolls", "The Royal Bank of Scotland Group", "Royal Dutch Shell", "RSA Insurance Group", "Sage Group", "Sainsbury's", "Schroders", "Scottish Mortgage Investment Trust", "Segro", "Severn Trent", "Shire plc", "Sky plc", "Smith & Nephew", "Smith", " D.S.", "Smiths Group", "Smurfit Kappa", "SSE plc", "Standard Chartered", "Standard Life Aberdeen", "St. James's Place plc", "Taylor Wimpey", "Tesco", "TUI Group", "Unilever", "United Utilities", "Vodafone Group", "Whitbread", "Worldpay", "WPP plc"};
    private static String[] FundNames = {"AAMA Income Fund", "AB All Market Real Return Portfolio", "AB Arizona Portfolio", "AB Asia ex-Japan Equity Portfolio", "Aberdeen International Equity Fund", "Aberdeen Select International Equity Fund", "Aberdeen US Mid Cap Equity Fund", "Aberdeen US Multi-Cap Equity Fund", "Aberdeen US Small Cap Equity Fund", "AC Alternatives Disciplined Long Short Fund", "AC Alternatives Income Fund", "Access Flex High Yield Fund", "Adaptive All Cap Fund", "Adaptive Equity Fund", "Adirondack Small Cap Fund", "Alger Capital Appreciation Fund", "Alger Growth & Income Fund", "Alger Health Sciences Fund", "Alger International Growth Fund", "Alger Mid Cap Growth Fund", "Alger Small Cap Focus Fund", "Alger Spectra Fund", "Alight Money Market Fund", "All Cap Growth Fund", "AQR Long-Short Equity Fund", "AQR Multi-Strategy Alternative Fund", "AQR Risk Parity II MV Fund", "AQR Small Cap Momentum Style Fund", "AQR Small Cap Multi-Style Fund", "Archer Balanced Fund", "Archer Stock Fund", "Ariel Fund", "Ariel Global Fund", "Ariel International Fund", "Aspiriant Defensive Allocation Fund", "AT Disciplined Equity Fund", "Avondale Core Investment Fund", "Azzad Ethical Fund", "Azzad Wise Capital Fund", "Balanced Fund", "California High-Yield Municipal Fund", "Cambiar Global Ultra Focus Fund", "Cambiar International Equity Fund", "Cambiar International Small Cap Fund", "Cambiar Opportunity Fund", "Capital Advisors Growth Fund", "Champlain Emerging Markets Fund", "Champlain Mid Cap Fund", "Champlain Small Company Fund", "Tactical Offensive Fixed Income Fund", "Thomson Horstmann & Bryant MicroCap Fund", "US Government Money Market Fund", "US Government Securities Fund", "US Tax Reform Fund", "Westwood SmallCap Fund", "Westwood Strategic Convertibles Fund", "Yorktown Master Allocation Fund", "Yorktown Mid-Cap Fund", "Yorktown Multi-Asset Income Fund"};
    private static String[] Addresses = {"Gib Hill Ln, Rossendale BB4 8FB",
            "119 Wellfield Dr, Burnley BB12 0JD",
            "50 Back Rhoden Rd, Oswaldtwistle, Accrington BB5 3QJ",
            "25 Cowley Cres, Padiham, Burnley BB12 8SX",
            "2 Mount St, Clayton-le-Moors, Accrington BB5 5NP",
            "10 Mary St, Burnley BB10 4AJ",
            "20 Hodgson St, Oswaldtwistle, Accrington BB5 3ND",
            "36 Bedford St, Barrowford, Nelson BB9 6DA",
            "56 St Edmund's St, Great Harwood, Blackburn BB6 7BW",
            "18 Heather Bank, Rossendale BB4 6DZ",
            "333 Willows Ln, Accrington BB5 0NH",
            "Piercy Mount, Rossendale BB4 9JL",
            "The Pennine Bridleway, Worsthorne, Burnley BB10 4RR",
            "1 Hereford Ave, Burnley BB12 6DA",
            "2 Rigby St, Nelson BB9 7AA",
            "Walter St, Accrington BB5 1QX",
            "25 Avenue Rd, Hurst Green, Clitheroe BB7 9QB",
            "137 Waterbarn St, Burnley BB10 1SD",
            "169 Sough Rd, Darwen BB3 2HA",
            "7 Edge End Ln, Nelson BB9 0PR",
            "11 Clifton St, Rishton, Blackburn BB1 4LW",
            "22 Byron Rd, Colne BB8 0RF",
            "23 Bridgefield St, Hapton, Burnley BB12 7JS",
            "17 Pendleton Ave, Accrington BB5 0JW",
            "14 Stanhill Rd, Oswaldtwistle, Accrington BB5 4PP",
            "53 Lindon Park Rd, Haslingden, Rossendale BB4 6LZ",
            "Unnamed Road, Clitheroe BB7 3DG",
            "11 Causey Foot, Nelson BB9 0DR",
            "153 Blackamoor Rd, Guide, Blackburn BB1 2LG",
            "10 Bishopstone Cl, Blackburn BB2 3WD",
            "113 Staghills Rd, Rossendale BB4 7TY",
            "42 Hoddlesden Fold, Hoddlesden, Darwen BB3 3NJ",
            "16 School Ln, Laneshawbridge, Colne BB8 7EQ",
            "2A Aspen Ln, Oswaldtwistle, Accrington BB5 4QA",
            "54-56 Manchester Rd, Burnley BB11 1HW",
            "4 West View, Tockholes, Darwen BB3 0NP",
            "12 Kestrel Ct, Hapton, Burnley BB11 5NA",
            "99 Newchurch Rd, Rossendale BB4 7SU",
            "110 Hallam Rd, Nelson BB9 8DJ",
            "2 The Sycamores, Earby, Barnoldswick BB18 6NH",
            "6 Woodbine Rd, Blackburn BB2 6JZ",
            "9 Todmorden Rd, Briercliffe, Burnley BB10 3QG",
            "9 Spring Terrace, Langho, Blackburn BB6 8DJ",
            "33 Carleton Ave, Simonstone, Burnley BB12 7JA",
            "256-272 A6062, Blackburn BB2 4QL",
            "Reedley Dr, Burnley BB10 2QR",
            "293 Southfield St, Nelson BB9 0LP",
            "10 Salterforth Rd, Earby, Barnoldswick BB18 6NG",
            "1 Fallbarn Rd, Rossendale BB4 7NT",
            "1 Arch St, Burnley BB12 0DR",
            "10 Cambridge St, Haslingden, Rossendale BB4 4BY",
            "31a James St, Blackburn BB1 7JZ",
            "42 Cabin End Row, Blackburn BB1 2DP",
            "9 Woodside Grove, Blackburn BB2 4QP",
            "15 Shackleton St, Burnley BB10 3BJ",
            "341-343 A680, Haslingden, Rossendale BB4 6PT",
            "5 Bank St, Church, Accrington BB5 4HH",
            "Unnamed Road, Clitheroe BB7 4JA",
            "2 Height Side Ln, Rossendale BB4 8TH",
            "49-51 Williams Rd, Burnley BB10 3BT",
            "17A Perth St, Accrington BB5 0QL",
            "9 Goodshaw Fold Rd, Rossendale BB4",
            "5 Quarry Farm Ct, Chatburn, Clitheroe BB7 4GB",
            "9 Comrie Cres, Burnley BB11 5HX",
            "20 Cavendish Pl, Blackburn BB2 2PN",
            "127 Sumner St, Blackburn BB2",
            "44 Sheridan Rd, Colne BB8 7HW",
            "Skipton Rd, Barnoldswick BB18 6JH",
            "1 Woodfields, Stonyhurst, Clitheroe BB7 9PS",
            "5 Brown St, Burnley BB11 1PJ",
            "2 Southern Ct, Burnley BB12 8BF",
            "47 Holland St, Accrington BB5 1RY",
            "17 Carr Ln, Waterfoot, Rossendale BB4 7BY",
            "24 Harcourt Rd, Accrington BB5 2JR",
            "Calf Hall Ln, Barnoldswick BB18 5SA",
            "Jack Walker Way, Blackburn BB2",
            "32 Bamburgh Dr, Burnley BB12 0TE",
            "3 Corporation St, Blackburn BB2 1LB",
            "Back Ln, Burnley BB12",
            "1 Emmott Ct, Laneshawbridge, Colne BB8 7JH",
            "4 Gardeners Row, Sabden, Clitheroe BB7 9EB",
            "20 Abbey St, Accrington BB5 1EB",
            "2 Marlowe Ave, Accrington BB5 2QA",
            "1 Mile End Cl, Foulridge, Colne BB8 7LD",
            "11A Ormerod St, Worsthorne, Burnley BB10 3NU",
            "15-17 Curvin St, Burnley BB11 1BD",
            "6 Perth St, Nelson BB9 8EE",
            "10 Temple St, Colne BB8 9LG",
            "117 Clayton St, Nelson BB9 7EP",
            "Ashton Ln, Darwen BB3 2AY",
            "16-20 Glencoe Ave, Hoddlesden, Darwen BB3 3LW",
            "8 Marsh Gate, Darwen BB3 3SE",
            "291 Colne Rd, Burnley BB10 1EJ",
            "3 Barden Rd, Accrington BB5 0JY",
            "50 Park Ave, Barnoldswick BB18 5AU",
            "9 Whinberry Ave, Rossendale BB4 6AR",
            "20 Stockwood Cl, Blackburn BB2 7QW",
            "2 Mount Trinity, Blackburn BB1 5EP",
            "1 Ashworth St, Rishton, Blackburn BB1 4JW"};

    private static Random rand = new Random();

    public static String FirstName() {
        return FirstNames[rand.nextInt(FirstNames.length)];
    }

    public static String Surname() {
        return SecondNames[rand.nextInt(SecondNames.length)];
    }

    public static String Pesel() {
        String result = "";
        result += (rand.nextInt(40) + 60) + "";
        int m = rand.nextInt(12) + 1;
        if (m < 10) result += "0" + m;
        else result += m + "";

        int n = rand.nextInt(29) + 1;
        if (n < 10) result += "0" + n;
        else result += n + "";

        for (int i = 0; i < 5; i++) {
            result += digits.charAt(rand.nextInt(9));
        }
        return result;
    }

    public static Double Margin() {
        return (rand.nextInt(41) + 10) / 10000.0;
    }

    public static double Budget() {
        return (rand.nextInt(1000000) + 100000)/100.0;
    }

    public static double OfferPrice() {
        return (rand.nextInt(35000) + 20000) / 10000.0;
    }

    public static double RoundDouble(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double Spread() {
        return (rand.nextInt(300) + 300) / 100.0;
    }

    public static double CalculatePurchasePrice(double spread, double offerPrice) {
        return Generate.RoundDouble((((100.0 - 0.5 * spread) * offerPrice) / (100.0 + 0.5 * spread)), 4);
    }

    public static Calendar Date() {
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, rand.nextInt(31));
        date.set(Calendar.MONTH, (rand.nextInt(12) + 1));
        date.set(Calendar.YEAR, getYear(today));
        return date;
    }

    public static int getYear(Calendar today) {
        int a = 0;
        do {
            a = rand.nextInt(today.get(Calendar.YEAR));
        } while (a < 1970);
        return a;
    }

    public static double Quotation() {
        return rand.nextInt(9) / 10.0 + rand.nextInt(9) / 100.0 + (double) rand.nextInt(9) + 1;
    }

    public static double Capital() {
        return rand.nextInt(9) / 10.0 + rand.nextInt(9) / 100.0 + (double) rand.nextInt(100000) + 5000.0;
    }


    private static ArrayList<String> currencies = new ArrayList<>() {{
        for (String s: Currencies){
            add(s);
        }
    }};
    private static ArrayList<String> funds = new ArrayList<>() {{
        for (String s: FundNames){
            add(s);
        }
    }};
    public static ArrayList<String> commodities = new ArrayList<>() {{
        for (String s: Commodities){
            add(s);
        }
    }};
    public static ArrayList<String> addresses = new ArrayList<>() {{
        for (String s: Addresses){
            add(s);
        }
    }};
    public static ArrayList<String> companies = new ArrayList<>() {{
        for (String s: CompanyNames){
            add(s);
        }
    }};

    public static String CurrencyName() {
        return currencies.remove(rand.nextInt(currencies.size()));
    }
    public static String FundName() {
        return funds.remove(rand.nextInt(funds.size()));
    }
    public static String CommodityName() {
        if (commodities.size() != 0)
            return commodities.remove(rand.nextInt(commodities.size()));
        else
            return commodityName();
    }
    private static String commodityName() {
        StringBuilder result = new StringBuilder();
        result.append(upper.charAt(rand.nextInt(upper.length())));
        for (int i=0; i<rand.nextInt(7)+3; i++){
            result.append(lower.charAt(rand.nextInt(lower.length())));
        }
        return result.toString();
    }
    public static String CompanyName() {
        if (companies.size() != 0)
            return companies.remove(rand.nextInt(companies.size()));
        else
            return Generate.FirstName();
    }


    public static String makeCountryList(Currency currency) {
        StringBuilder result = new StringBuilder();
        result.append(currency.getCountryList().get(0).getName());
        for (Country c : currency.getCountryList()){
            if (!c.equals(currency.getCountryList().get(0)))
                result.append(", " + c.getName());
        }
        return result.toString();
    }

    public static double CommodityValue() {
        return ( rand.nextInt(800000) + 200000) / 10000.0;
    }

    public static String PickUnit() {
        return Units[rand.nextInt(Units.length)];
    }

    public static Currency PickCurrency() {
        try {
            if (Currency.getList().size() == 0) {
                throw new EmptyStackException();
            }
            int a = rand.nextInt(Currency.getList().size());
            return Currency.get(a);
        } catch (EmptyStackException e) {
            System.out.println(e + " W bazie nie ma żadnej waluty!");
        }
        return null;
    }

    public static ArrayList<Country> countries = new ArrayList<>() {{
        for (String c: Country){
            add(new Country(c));
        }
    }};
    public static void PickCountry(Currency currency) {
        int number = rand.nextInt(3) + 1;
        for (int i = 0; i < number; i++) {
            currency.addCountry(selectCountry());
        }
    }

    public static Country selectCountry(){
        if (countries.size()!=0)
            return countries.remove(rand.nextInt(countries.size()));
        else
            return new Country(CountryName());
    }

    public static String CountryName(){
        StringBuilder result = new StringBuilder();
        result.append(upper.charAt(rand.nextInt(upper.length())));
        for (int i=0; i<rand.nextInt(7)+3; i++){
            result.append(lower.charAt(rand.nextInt(lower.length())));
        }
        return result.toString();
    }

    public static ArrayList<String> cities = new ArrayList<>() {{
        for (String c: Cities){
            add(c);
        }
    }};
    public static String City() {
        if (cities.size()!=0)
            return cities.remove(rand.nextInt(cities.size()));
        else
            return CountryName();
    }


    public static String Address(){
        return addresses.remove(rand.nextInt(addresses.size()));
    }


    public static void NewCompanies(Index i, int n){
        for (int j = 0; j < n; j++) {
            Company c = new Company();
            c.setStockExchange(i.getBaseStock());
            i.getBaseStock().getMainIndex().addCompany(c);
            i.getBaseStock().getSortedCompanies().add(c);
            i.addCompany(c);
            new Thread(c).start();
        }
    }
    public static void Companies(Index i) {
        int size = i.getBaseStock().getMainIndex().getCompanyList().size();
        int third = (int)(size * 0.3);
        int r = rand.nextInt(size - third) + third;
        i.setCompanyNumber(r);
        int choice;
        for (int j = 0; j < r; j++) {
            choice = rand.nextInt(i.getBaseStock().getMainIndex().getCompanyList().size());

            while(i.getCompanyList().contains(i.getBaseStock().getMainIndex().getCompanyList().get(choice))){
                choice = rand.nextInt(i.getBaseStock().getMainIndex().getCompanyList().size());
            }
            i.addCompany(i.getBaseStock().getMainIndex().getCompanyList().get(choice));
        }
    }

    public static void Buyers(String asset) { Generate.Buyers(asset, 0);}
    public static void Buyers(String asset, int sharesNumber) {
        switch(asset) {
            case "currency":
                if (Currency.getList().size() != 0)
                    addBuyers(0, 1);
                break;
            case "commodity":
                if (Commodity.getList().size() != 0)
                    addBuyers(1,0);
                break;
            case "company":
                if (Company.getList().size() != 0)
                    addBuyers(1, 0);
                break;
            default: break;
        }
    }
    private static synchronized void addBuyers(int investors, int investmentFunds) {
        for (int i = 0; i < investors; i++)
            new Thread(new Investor()).start();

        for (int i = 0; i < investmentFunds; i++)
            new Thread(new InvestmentFund()).start();
    }

}