package task2810.model;

import task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ru/search/vacancy?text=java+%s&page=%d";
    //private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&enable_snippets=true&clusters=true&area=90&only_with_salary=true&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        try {
            int page = 0;
            boolean hasNext;
            do {
                Document document = getDocument(searchString, page);
                Elements elementsVacancy = document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                for (Element element : elementsVacancy) {
                    Vacancy vacancy = new Vacancy();
                    Element titleTag = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").first();
                    vacancy.setTitle(titleTag.ownText());
                    Element salaryTag = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").first();
                    if (salaryTag != null) vacancy.setSalary(salaryTag.ownText());
                    else vacancy.setSalary("");
                    Element addressTag = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").first();
                    vacancy.setCity(addressTag.ownText());
                    Element employerTag = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").first();
                    vacancy.setCompanyName(employerTag.ownText());
                    String siteName = "http://hh.ru";
                    vacancy.setSiteName(siteName);
                    vacancy.setUrl(titleTag.attr("href"));
                    vacancies.add(vacancy);
                }
                hasNext = document.getElementsByAttributeValue("data-qa", "pager-block").first()
                        .getElementsByAttributeValue("data-qa", "pager-next").first() != null;
                page++;
            } while (hasNext);
        } catch (IOException e) {
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        //String url = "http://javarush.ru/testdata/big28data.html";
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                .referrer("no-referrer-when-downgrade")
                .get();
    }
}
