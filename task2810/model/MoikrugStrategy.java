package task2810.model;

import task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        try {
            int page = 0;
            //boolean hasNext;
            String siteName = "https://moikrug.ru";
            do {
                Document document = getDocument(searchString, page);
                Elements elementsVacancy = document.getElementsByClass("job");

                if (elementsVacancy.size() == 0) break;
                for (Element element : elementsVacancy) {
                    Vacancy vacancy = new Vacancy();

                    Element titleTag = element.getElementsByClass("title").first();
                    vacancy.setTitle(titleTag.attr("title"));
                    vacancy.setUrl(siteName + titleTag.getElementsByAttribute("href").first().attr("href"));

                    Element salaryTag = element.getElementsByClass("salary").first()
                            .getElementsByClass("count").first();
                    vacancy.setSalary(salaryTag != null ? salaryTag.text() : "");

                    Element addressTag = element.getElementsByClass("location").first();
                    vacancy.setCity(addressTag != null ? addressTag.text() : "");

                    Element employerTag = element.getElementsByClass("company_name").first();
                    vacancy.setCompanyName(employerTag.text());
                    vacancy.setSiteName(siteName);

                    vacancies.add(vacancy);
                }

                //hasNext = document.getElementsByClass("next_page").first().hasAttr("href");
                page++;
            } while (true);//hasNext);
        } catch (IOException e) {
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        //String url = "http://javarush.ru/testdata/big28data2.html";
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                .referrer("no-referrer-when-downgrade")
                .get();
    }
}
