package stock;

import java.util.Comparator;

/** Komparator spółek */
public class CompanyComparator implements Comparator<Company> {
    public int compare(Company company1, Company company2) {
        double result = company1.getSharesNumber()*company1.getCurrentQuotation() - company2.getSharesNumber() * company2.getCurrentQuotation();
        return (int)(result*10000) * (-1);
    }
}
