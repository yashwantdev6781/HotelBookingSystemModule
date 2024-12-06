package com.ums.Service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ums.entity.Booking;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class PDFService {

    public String generateBookingDetailsPdf(Booking booking) {


        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("E://airbnb-booking//booking-confirmation"+booking.getId()+".pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Booking Details", font);
            document.add(chunk);

            // Create a table with 4 columns
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // Add table headers
            addTableHeader(table);

            // Add booking details row
            addBookingDetails(table, booking);

            document.add(table);
            document.close();

            return "E://airbnb-booking//booking-confirmation"+booking.getId()+".pdf";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell("Guest Name");
        table.addCell("Total Nights");
        table.addCell("Total Price");
        table.addCell("Booking Date");

    }

    private void addBookingDetails(PdfPTable table, Booking booking) {
        table.addCell(booking.getGuestName());
        table.addCell(String.valueOf(booking.getTotalNights()));
        table.addCell(String.valueOf(booking.getTotalPrice()));
        table.addCell(booking.getBookingDate().toString());

    }
}
