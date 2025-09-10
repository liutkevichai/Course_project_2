package ru.realestate.realestate_app.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Сервис для работы с документацией API
 */
@Service
public class ApiDocService {

    /**
     * Читает файл API_ENDPOINTS.md, конвертирует его из Markdown в HTML и возвращает результат
     *
     * @return HTML-строка с документацией API
     */
    public String getApiEndpointsHtml() {
        try {
            ClassPathResource resource = new ClassPathResource("API_ENDPOINTS.md");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            
            StringBuilder markdownContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                markdownContent.append(line).append("\n");
            }
            
            return convertMarkdownToHtml(markdownContent.toString());
        } catch (IOException _) {
            // В случае ошибки возвращаем пустую строку или сообщение об ошибке
            return "<p>Ошибка при загрузке документации API</p>";
        }
    }
    
    /**
     * Простая конвертация Markdown в HTML
     * Поддерживаются только базовые элементы: заголовки, списки, ссылки
     *
     * @param markdown текст в формате Markdown
     * @return текст в формате HTML
     */
    private String convertMarkdownToHtml(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        
        String html = markdown;
        
        // Конвертация заголовков
        html = html.replaceAll("(?m)^### (.*)$", "<h3>$1</h3>");
        html = html.replaceAll("(?m)^## (.*)$", "<h2>$1</h2>");
        html = html.replaceAll("(?m)^# (.*)$", "<h1>$1</h1>");
        
        // Конвертация списков
        html = html.replaceAll("(?m)^\\* (.*)$", "<li>$1</li>");
        html = html.replaceAll("(?s)<li>(.*?)</li>", "<ul>$0</ul>");
        
        // Конвертация ссылок
        html = html.replaceAll("\\[([^\\]]+)\\]\\(([^\\)]+)\\)", "<a href=\"$2\">$1</a>");
        
        // Замена разрывов строк на <br> (кроме тех, что уже в тегах)
        html = html.replaceAll("(?<!>)\n", "<br>\n");
        
        return html;
    }
}