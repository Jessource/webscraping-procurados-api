package com.webscraping.demo.service;

import java.io.IOException;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.webscraping.demo.model.OrigemDados;
import com.webscraping.demo.model.Procurados;
import com.webscraping.demo.model.interpool.InterpolResponse;
import com.webscraping.demo.model.interpool.Notices;
import com.webscraping.demo.repository.ProcuradosRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ScraperServiceImpl implements ScraperService {

    @Value("#{'${site.urls}'.split(',')}")
    List<String> urls;

    @Autowired
    ProcuradosRepository procuradosRepository;

    @Override
    @PostConstruct
    public void getFBIAndInterpolData() {
        for (String url: urls) {
            if (url.contains("interpol")) {
                extractDataFromInterpol(url);
            } else if (url.contains("fbi")) {
                extractDataFromFbi(url);
            }
        }
    }

    private void extractDataFromInterpol(String url) {
        try {
            WebClient.Builder builder = WebClient.builder();
            InterpolResponse retorno = builder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(InterpolResponse.class)
                .block();
            for (Notices notice : retorno.get_embedded().getNotices()) {
                Procurados response = new Procurados();
                if (notice.get_links().getThumbnail() != null) {
                    response.setImagem(notice.get_links().getThumbnail().getHref());
                }
                if (url.contains("red")) {
                    response.setCrime("Red Notice");
                } else if (url.contains("yellow")) {
                    response.setCrime("Yellow Notice");
                }
                
                response.setNome(notice.getForename() + " " + notice.getName());
                response.setOrigemDados(OrigemDados.INTERPOL);

                procuradosRepository.save(response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void extractDataFromFbi(String url) {
        try {
            String idElement = "query-results-f7f80a1681ac41a08266bd0920c9d9d8";
            Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0").get();

            Element listElement = document.getElementById(idElement);
            Element quantity = listElement.select("div.top-total").first();
            double quantidadeGeral = Double.parseDouble(quantity.text().split(" ")[1]);
            double qtdPaginas = Math.round(quantidadeGeral/40);

            for(int i = 1; i <= qtdPaginas; i++) {
                Document documentPaged = Jsoup.connect(url + "?page=" + i)
                    .userAgent("Mozilla/5.0").get();
                Element listElementPaged = documentPaged.getElementById(idElement);
                Elements listItemElements = listElementPaged
                    .getElementsByTag("ul")
                    .first()
                    .getElementsByTag("li");

                for (Element ads: listItemElements) {
                    Procurados response = new Procurados();
                    Elements elements2 = ads.getElementsByTag("a");
                    String image = elements2.get(0).getElementsByTag("img").attr("src");
                    String crime = elements2.get(1).text();
                    String nome = elements2.get(2).text();

                    response.setImagem(image);
                    response.setCrime(crime);
                    response.setNome(nome);
                    response.setOrigemDados(OrigemDados.FBI);

                    procuradosRepository.save(response);
                }
            }        
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
