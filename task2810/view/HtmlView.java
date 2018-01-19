package task2810.view;

import task2810.Controller;
import task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class HtmlView implements View {
    private final String filePath = this.getClass().getClassLoader().getResource("").getPath().replaceFirst("/","")+this.getClass().getPackage().toString().split(" ")[1].replaceAll("\\.", "/") + "/vacancies.html";
    private Controller controller;

    @Override
    public void update(List<Vacancy> vacancies) {
        String updatedFileContent = getUpdatedFileContent(vacancies);
        updateFile(updatedFileContent);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Томск");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document document = getDocument();

            Element vacancyTemplate = document.getElementsByClass("template").first();

            for (Element elementVacancyOld : document.getElementsByAttributeValue("class", "vacancy")) {
                elementVacancyOld.remove();
            }

            for (Vacancy vacancy : vacancies) {
                Element elementVacancy = vacancyTemplate.clone();
                elementVacancy.removeClass("template")
                        .removeAttr("style");
                elementVacancy.getElementsByClass("title").first()
                        .getElementsByAttribute("href").first()
                        .attr("href", vacancy.getUrl())
                        .text(vacancy.getTitle());
                elementVacancy.getElementsByClass("city").first()
                        .text(vacancy.getCity());
                elementVacancy.getElementsByClass("companyName").first()
                        .text(vacancy.getCompanyName());
                elementVacancy.getElementsByClass("salary")
                        .first().text(vacancy.getSalary());

                vacancyTemplate.before(elementVacancy);
            }

            return document.outerHtml();
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
    }

    private void updateFile(String htmlText) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(htmlText);
        } catch (IOException e) {
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(Paths.get(filePath).toFile(), "UTF-8");
    }
}
