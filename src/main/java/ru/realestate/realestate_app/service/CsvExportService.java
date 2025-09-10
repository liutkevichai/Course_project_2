package ru.realestate.realestate_app.service;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Сервис для экспорта данных в формат CSV
 */
@Service
public class CsvExportService {

    /**
     * Преобразует список объектов в CSV-файл
     *
     * @param data список объектов для экспорта
     * @param clazz класс объектов в списке
     * @param <T> тип объектов в списке
     * @return массив байтов CSV-файла
     * @throws IOException если возникает ошибка при записи в поток
     * @throws CsvDataTypeMismatchException если типы данных не совпадают
     * @throws CsvRequiredFieldEmptyException если обязательное поле пустое
     */
    public <T> byte[] exportToCsv(List<T> data, Class<T> clazz) 
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Добавляем BOM для правильного отображения кириллицы в Excel
        outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(writer,
                     ';', // Используем точку с запятой как разделитель для Excel
                     ICSVWriter.DEFAULT_QUOTE_CHARACTER, // Используем кавычки для полей
                    ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    ICSVWriter.DEFAULT_LINE_END)) {
            
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withQuotechar(ICSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .build();
            
            beanToCsv.write(data);
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Форматирует BigDecimal в строку с запятой в качестве десятичного разделителя
     * 
     * @param value значение для форматирования
     * @return отформатированная строка
     */
    public String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "";
        }
        
        // Создаем форматтер с русской локалью, где десятичный разделитель - запятая
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("ru-RU"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        
        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);
        return formatter.format(value);
    }
}