package org.clevertec;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@AllArgsConstructor
class Transaction {
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock writeLock = lock.writeLock();
    private final String bankFrom;
    private final String bankTo;
    private final String type;
    private final String counter;
    private final String counterTo;
    private final BigDecimal quantity;
    private final String date;
    private final String time;
    private final String currency;



    @SneakyThrows
    public void writeRow() {
        writeLock.lock();
        Connection connection = new ConnectionBD().connect();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO transactions (bankfrom, bankto, " +
                "type, fromcounter, tocounter, summa, date, time) VALUES (?, ?, ?, ?::uuid, ?::uuid, " +
                "?, ?::date, ?::time)");
        statement.setString(1, bankFrom);
        statement.setString(2, bankTo);
        statement.setString(3, type);
        statement.setString(4, counter);
        statement.setString(5, counterTo);
        statement.setBigDecimal(6, quantity);
        statement.setString(7, date);
        statement.setString(8, time);
        statement.executeUpdate();
        Statement statementGetIdMax = connection.createStatement();
        ResultSet resultSet = statementGetIdMax.executeQuery("select max(id) from transactions");
        resultSet.next();
        String id = Integer.toString(resultSet.getInt(1));
        connection.close();
        writeLock.unlock();
        printCheck(id);

    }
    public void printCheck(String id) {
        System.out.printf("""
                \tBanks check
                Check\t%s
                %s\t%s
                Type\t%s
                Bank from\t%s
                Bank to\t%s
                Counter from\t%s
                Counter to\t%s
                Quantity\t%s %s""", id, date, time, type, bankFrom, bankTo, counter, counterTo, quantity, currency);
        saveCheckAsPdf(id);
    }
    @SneakyThrows
    private void saveCheckAsPdf(String id) {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("check\\" + id + ".pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Banks check", font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(paragraph.ALIGN_CENTER);
        document.add(paragraph);
        document.add(new Chunk("\n" + date + " " + time + "\nType " + type +
                "\nBank from " + bankFrom + "\nBank to " + bankTo + "\nCounter from " + counter + "\nCounter to "
                + counterTo + "\nQuantity " + quantity + " " + currency, font));
        document.close();
    }
}